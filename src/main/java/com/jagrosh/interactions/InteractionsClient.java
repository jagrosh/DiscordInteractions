/*
 * Copyright 2022 John Grosh (john.a.grosh@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jagrosh.interactions;

import com.jagrosh.interactions.command.ApplicationCommand;
import com.jagrosh.interactions.command.Command;
import com.jagrosh.interactions.receive.Interaction;
import com.jagrosh.interactions.requests.RestClient;
import com.jagrosh.interactions.requests.RestClient.RestResponse;
import com.jagrosh.interactions.requests.Route;
import com.jagrosh.interactions.responses.*;
import io.javalin.Javalin;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import org.eclipse.jetty.io.EofException;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class InteractionsClient
{
    private final Logger log = LoggerFactory.getLogger(InteractionsClient.class);
    private final Map<String,Long> metrics = new HashMap<>();
    
    private final RestClient rest;
    private final long appId;
    private final String publicKey, path, keystore, keystorePass;
    private final int port, threads;
    private final List<Command> commands;
    private final InteractionsListener listener;
    
    private Javalin javalin;
    
    protected InteractionsClient(long appId, String publicKey, String path, String keystore, String keystorePass, int port, int threads, RestClient rest, List<Command> commands, InteractionsListener listener)
    {
        this.appId = appId;
        this.publicKey = publicKey;
        this.path = path == null ? "/" : path;
        this.keystore = keystore;
        this.keystorePass = keystorePass;
        this.port = port <= 0 ? 8443 : port;
        this.threads = threads  <= 0 ? 250 : threads;
        this.rest = rest;
        this.commands = commands;
        this.listener = listener == null ? new InteractionsListener(){} : listener;
    }
    
    public void start() throws InvalidKeyException, NoSuchAlgorithmException
    {
        if(javalin != null)
            throw new IllegalStateException("Interactions Client has already started!");
        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
        EdDSAPublicKeySpec pubKey = new EdDSAPublicKeySpec(Utils.hexToBytes(publicKey), spec);
        EdDSAEngine sgr = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));
        sgr.initVerify(new EdDSAPublicKey(pubKey));
        
        javalin = Javalin.create(conf -> 
        {
            // configure https
            if(keystore != null && keystorePass != null)
            {
                conf.server(() -> 
                {
                    QueuedThreadPool pool = new QueuedThreadPool(threads, 8, 60_000);
                    pool.setName("JettyServerThreadPool");
                    Server server = new Server(pool);
                    SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
                    sslContextFactory.setKeyStorePath(keystore);
                    sslContextFactory.setKeyStorePassword(keystorePass);
                    ServerConnector sslConnector = new ServerConnector(server, sslContextFactory);
                    sslConnector.setPort(port);
                    server.setConnectors(new Connector[]{sslConnector});
                    StatisticsHandler statsHandler = new StatisticsHandler();
                    statsHandler.setGracefulShutdownWaitsForRequests(true);
                    server.setHandler(statsHandler);
                    server.setStopTimeout(5000);
                    return server;
                });
            }
        }).post(path, ctx ->
        {
            long startTime = System.nanoTime();
            
            // verify that discord is sending the request
            sgr.update((ctx.header("x-signature-timestamp") + ctx.body()).getBytes(Charset.forName("UTF-8")));
            boolean verified = false;
            try
            {
                verified = sgr.verify(Utils.hexToBytes(ctx.header("x-signature-ed25519")));
            }
            catch (SignatureException | NullPointerException ex) {}
            
            long verifyTime = System.nanoTime();
            long parseTime = System.nanoTime();
            long processTime = System.nanoTime();
            
            if(verified)
            {
                log.debug(String.format("Received interaction: %s", ctx.body()));

                // construct an interaction object
                Interaction interaction = new Interaction(new JSONObject(ctx.body()), this);
                parseTime = System.nanoTime();
                String response = handle(interaction).toJson().toString();
                processTime = System.nanoTime();

                log.debug(String.format("Replying with: %s", response));
                increaseMetricValue("Valid", 1);
                increaseMetricValue("Type:" + interaction.getType().toString(), 1);
                ctx.status(200).header("Content-Type", "Application/Json").result(response);
            }
            else
            {
                log.debug(String.format("Unverified request from %s (%s | %s)", ctx.host(), ctx.matchedPath(), ctx.header("x-signature-ed25519")));
                increaseMetricValue("Unverified", 1);
                ctx.status(401);
            }
            
            long finishTime = System.nanoTime();
            
            increaseMetricValue("TotalRequests", 1);
            increaseMetricValue("TotalTime", finishTime - startTime);
            increaseMetricValue("ResponseTime", finishTime - processTime);
            increaseMetricValue("ProcessTime", processTime - parseTime);
            increaseMetricValue("ParseTime", parseTime - verifyTime);
            increaseMetricValue("VerifyTime", verifyTime - startTime);
        }).exception(Exception.class, (ex, ctx) -> 
        {
            if(ex instanceof EofException)
                increaseMetricValue("EofException", 1);
            else
                log.error("Exception", ex);
        }).start();
    }
    
    public void shutdown()
    {
        try
        {
            javalin.stop();
        }
        catch(Exception ex)
        {
            log.warn("Javalin may not have shut down: ", ex);
        }
    }
    
    private InteractionResponse handle(Interaction interaction)
    {
        switch(interaction.getType())
        {
            // always reply to ping with a pong
            case PING:
                return new PongCallback();

            // check commands for response to a command
            case APPLICATION_COMMAND:
                return commands.stream()
                        .filter(c -> interaction.getCommandData().getName().equalsIgnoreCase(c.getApplicationCommand().getName()))
                        .findFirst()
                        .map(c -> c.execute(interaction))
                        .orElse(listener.onCommand(interaction));
            
            case APPLICATION_COMMAND_AUTOCOMPLETE:
                return listener.onAutocomplete(interaction);
                
            case MESSAGE_COMPONENT:
                return listener.onMessageComponent(interaction);
                
            case MODAL_SUBMIT:
                return listener.onModalSubmit(interaction);
                
            case UNKNOWN:
            default:
                return listener.onUnknownInteraction(interaction);
                    
        }
    }
    
    public void updateGlobalCommands()
    {
        log.info(String.format("Attempting to update %d comamnds", commands.size()));
        JSONArray arr = new JSONArray();
        commands.forEach(c -> arr.put(c.getApplicationCommand().toJson()));
        try
        {
            log.info(String.format("Sending commands: %s", arr));
            RestResponse res = rest.request(Route.PUT_COMMANDS.format(appId), arr).get();
            log.info(String.format("Commands updated with status '%d'", res.getStatus()));
            if(!res.isSuccess())
                log.error(String.format("Error updating commands: %s", res.getBody()));
        }
        catch(InterruptedException | ExecutionException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void updateGuildCommands(long guildId, ApplicationCommand... cmds)
    {
        log.info(String.format("Attempting to update %d guild commands in %d", cmds.length, guildId));
        for(ApplicationCommand cmd: cmds)
        {
            try
            {
                RestResponse res = rest.request(Route.POST_GUILD_COMMANDS.format(appId, guildId), cmd.toJson()).get();
                log.info(String.format("Command '%s' updated with status '%d'", cmd.getName(), res.getStatus()));
                if(!res.isSuccess())
                    log.error(String.format("Error updating command: %s", res.getBody()));
            }
            catch(InterruptedException | ExecutionException ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
    public void updateGuildCommands(long guildId, Command... cmds)
    {
        log.info(String.format("Attempting to update %d guild commands in %d", cmds.length, guildId));
        for(Command cmd: cmds)
        {
            try
            {
                RestResponse res = rest.request(Route.POST_GUILD_COMMANDS.format(appId, guildId), cmd.getApplicationCommand().toJson()).get();
                log.info(String.format("Command '%s' updated with status '%d'", cmd.getApplicationCommand().getName(), res.getStatus()));
                if(!res.isSuccess())
                    log.error(String.format("Error updating command: %s", res.getBody()));
            }
            catch(InterruptedException | ExecutionException ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
    public void deleteGuildCommands(long guildId, long... cmds)
    {
        log.info(String.format("Attempting to delete %d guild commands in %d", cmds.length, guildId));
        for(long cmd: cmds)
        {
            try
            {
                RestResponse res = rest.request(Route.DELETE_GUILD_COMMAND.format(appId, guildId, cmd), "").get();
                log.info(String.format("Deleted command '%d' with status '%d'", cmd, res.getStatus()));
                if(!res.isSuccess())
                    log.error(String.format("Error updating command: %s", res.getBody()));
            }
            catch(InterruptedException | ExecutionException ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
    private synchronized long increaseMetricValue(String key, long count)
    {
        long val = metrics.getOrDefault(key, 0L);
        val += count;
        metrics.put(key, val);
        return val;
    }
    
    public Map<String,Long> getMetrics()
    {
        return Collections.unmodifiableMap(metrics);
    }
    
    public static class Builder
    {
        private final List<Command> commands = new ArrayList<>();
        private String publicKey, path, keystore, keystorePass;
        private int port, threads;
        private long appId;
        private RestClient rest;
        private InteractionsListener listener;
        
        public Builder setAppId(long appId)
        {
            this.appId = appId;
            return this;
        }
        
        public Builder setPublicKey(String publicKey)
        {
            this.publicKey = publicKey;
            return this;
        }
        
        public Builder setPath(String path)
        {
            this.path = path;
            return this;
        }
        
        public Builder setKeystore(String keystore)
        {
            this.keystore = keystore;
            return this;
        }
        
        public Builder setKeystorePass(String keystorePass)
        {
            this.keystorePass = keystorePass;
            return this;
        }
        
        public Builder setPort(int port)
        {
            this.port = port;
            return this;
        }
        
        public Builder setThreads(int threads)
        {
            this.threads = threads;
            return this;
        }
        
        public Builder setRestClient(RestClient rest)
        {
            this.rest = rest;
            return this;
        }
        
        public Builder addCommands(Command... commands)
        {
            for(Command cmd: commands)
                this.commands.add(cmd);
            return this;
        }
        
        public Builder setListener(InteractionsListener listener)
        {
            this.listener = listener;
            return this;
        }
        
        public InteractionsClient build()
        {
            return new InteractionsClient(appId, publicKey, path, keystore, keystorePass, port, threads, rest, commands, listener);
        }
    }
}
