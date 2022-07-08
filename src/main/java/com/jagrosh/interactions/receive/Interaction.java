/*
 * Copyright 2020 John Grosh (john.a.grosh@gmail.com).
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
package com.jagrosh.interactions.receive;

import com.jagrosh.interactions.InteractionsClient;
import com.jagrosh.interactions.entities.*;
import com.jagrosh.interactions.interfaces.ISnowflake;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class Interaction implements ISnowflake
{
    private final InteractionsClient client;
    private final long id, applicationId;
    private final Interaction.Type type;
    private final CommandInteractionData cmdData;
    private final ComponentInteractionData compData;
    private final long guildId, channelId;
    private final GuildMember member;
    private final User user;
    private final String token;
    private final int version;
    private final ReceivedMessage message;
    private final WebLocale locale, guildLocale;
    private final long appPermissions;
    
    
    public Interaction(JSONObject json, InteractionsClient client)
    {
        this.client = client;
        this.id = json.getLong("id");
        this.applicationId = json.getLong("application_id");
        this.type = Interaction.Type.of(json.getInt("type"));
        if(json.has("data"))
            switch(this.type)
            {
                case APPLICATION_COMMAND:
                case APPLICATION_COMMAND_AUTOCOMPLETE:
                    this.cmdData = new CommandInteractionData(json.getJSONObject("data"));
                    this.compData = null;
                    break;
                case MESSAGE_COMPONENT:
                case MODAL_SUBMIT:
                    this.cmdData = null;
                    this.compData = new ComponentInteractionData(json.getJSONObject("data"));
                    break;
                case PING:
                case UNKNOWN:
                default:
                    this.cmdData = null;
                    this.compData = null;
            }
        else
        {
            this.cmdData = null;
            this.compData = null;
        }
        this.guildId = json.optLong("guild_id");
        this.channelId = json.optLong("channel_id");
        this.member = json.has("member") ? new GuildMember(json.getJSONObject("member")) : null;
        this.user = json.has("user") ? new User(json.getJSONObject("user")) : this.member == null ? null : member.getUser();
        this.token = json.getString("token");
        this.version = json.getInt("version");
        this.message = json.has("message") ? new ReceivedMessage(json.getJSONObject("message")) : null;
        this.locale = WebLocale.of(json.optString("locale"));
        this.guildLocale = WebLocale.of(json.optString("guild_locale"));
        this.appPermissions = json.optLong("app_permissions");
    }
    
    public InteractionsClient getClient()
    {
        return client;
    }

    @Override
    public long getIdLong()
    {
        return id;
    }
    
    public Interaction.Type getType()
    {
        return type;
    }
    
    public CommandInteractionData getCommandData()
    {
        return cmdData;
    }
    
    public ComponentInteractionData getComponentData()
    {
        return compData;
    }

    public ReceivedMessage getMessage()
    {
        return message;
    }

    public long getGuildId()
    {
        return guildId;
    }

    public long getChannelId()
    {
        return channelId;
    }

    public GuildMember getMember()
    {
        return member;
    }
    
    public User getUser()
    {
        return user;
    }

    public String getToken()
    {
        return token;
    }
    
    public WebLocale getUserLocale()
    {
        return locale;
    }
    
    public WebLocale getGuildLocale()
    {
        return locale;
    }
    
    public WebLocale getEffectiveLocale()
    {
        return locale == WebLocale.UNKNOWN ? guildLocale : locale;
    }
    
    public boolean appHasPermission(Permission p)
    {
        return (this.appPermissions & p.getValue()) > 0;
    }
    
    public enum Type 
    { 
        UNKNOWN(0), PING(1), APPLICATION_COMMAND(2), MESSAGE_COMPONENT(3), APPLICATION_COMMAND_AUTOCOMPLETE(4), MODAL_SUBMIT(5);

        private final int value;

        private Type(int value)
        {
            this.value = value;
        }

        public static Type of(int value)
        {
            for(Type type: Type.values())
                if(type.value == value)
                    return type;
            return UNKNOWN;
        }
    }
}
