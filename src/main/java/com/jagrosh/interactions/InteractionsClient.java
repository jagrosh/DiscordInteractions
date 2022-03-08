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
import com.jagrosh.interactions.receive.CommandInteractionData;
import com.jagrosh.interactions.receive.Interaction;
import com.jagrosh.interactions.requests.RestClient;
import com.jagrosh.interactions.requests.RestClient.RestResponse;
import com.jagrosh.interactions.requests.Route;
import com.jagrosh.interactions.responses.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class InteractionsClient
{
    private final Logger log = LoggerFactory.getLogger(InteractionsClient.class);
    private final RestClient rest;
    private final long appId;
    private final String botToken;
    private final List<Command> commands;
    private final InteractionsListener listener;
    
    protected InteractionsClient(long appId, String botToken, List<Command> commands, InteractionsListener listener)
    {
        this.appId = appId;
        this.botToken = botToken;
        this.commands = commands;
        this.listener = listener == null ? new InteractionsListener(){} : listener;
        this.rest = new RestClient(botToken);
    }
    
    public String getBotToken()
    {
        return botToken;
    }
    
    public List<Command> getCommands()
    {
        return commands;
    }
    
    public InteractionResponse handle(Interaction interaction)
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
        for(Command cmd: commands)
        {
            try
            {
                RestResponse res = rest.request(Route.POST_COMMANDS.format(appId), cmd.getApplicationCommand().toJson()).get();
                log.info(String.format("Command '%s' updated with status '%d'", cmd.getApplicationCommand().getName(), res.getStatus()));
            }
            catch(InterruptedException | ExecutionException ex)
            {
                ex.printStackTrace();
            }
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
            }
            catch(InterruptedException | ExecutionException ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
    public static class Builder
    {
        private final List<Command> commands = new ArrayList<>();
        private long appId;
        private String botToken;
        private InteractionsListener listener;
        
        public Builder setAppId(long appId)
        {
            this.appId = appId;
            return this;
        }
        
        public Builder setBotToken(String botToken)
        {
            this.botToken = botToken;
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
            return new InteractionsClient(appId, botToken, commands, listener);
        }
    }
}
