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

import com.jagrosh.interactions.receive.Interaction;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class Interactions
{
    public static void start(InteractionsClient client, InteractionsConfig config) throws InvalidKeyException, NoSuchAlgorithmException
    {
        final Logger log = LoggerFactory.getLogger("Interactions");
        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
        EdDSAPublicKeySpec pubKey = new EdDSAPublicKeySpec(Utils.hexToBytes(config.publicKey), spec);
        EdDSAEngine sgr = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));
        sgr.initVerify(new EdDSAPublicKey(pubKey));
        
        Javalin.create(conf -> 
        {
            // configure https
            if(config.keystore != null && config.keystorePass != null)
            {
                conf.server(() -> 
                {
                    Server server = new Server();
                    SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
                    sslContextFactory.setKeyStorePath(config.keystore);
                    sslContextFactory.setKeyStorePassword(config.keystorePass);
                    ServerConnector sslConnector = new ServerConnector(server, sslContextFactory);
                    sslConnector.setPort(config.port);
                    server.setConnectors(new Connector[]{sslConnector});
                    return server;
                });
            }
            
            // configure access manager
            conf.accessManager((handler, ctx, routeRoles) -> 
            {
                // verify that discord is sending the request
                sgr.update((ctx.header("x-signature-timestamp") + ctx.body()).getBytes(Charset.forName("UTF-8")));
                boolean verified = false;
                try
                {
                    verified = sgr.verify(Utils.hexToBytes(ctx.header("x-signature-ed25519")));
                }
                catch (SignatureException | NullPointerException ex) {}
                if(verified)
                {
                    handler.handle(ctx);
                }
                else
                {
                    log.warn(String.format("Unverified request from %s (%s | %s)", ctx.host(), ctx.matchedPath(), ctx.header("x-signature-ed25519")));
                    ctx.status(401);
                }
            });
        }).post(config.path, ctx ->
        {
            log.debug(String.format("Received interaction: %s", ctx.body()));

            // construct an interaction object
            Interaction interaction = new Interaction(new JSONObject(ctx.body()));
            String response = client.handle(interaction).toJson().toString();

            log.debug(String.format("Replying with: %s", response));
            ctx.header("Content-Type", "Application/Json").result(response);
        }).exception(Exception.class, (ex, ctx) -> ex.printStackTrace()).start();
    }
    
    public static class InteractionsConfig
    {
        public String publicKey, path = "/", keystore = null, keystorePass = null;
        public int port = 8443;
    }
}
