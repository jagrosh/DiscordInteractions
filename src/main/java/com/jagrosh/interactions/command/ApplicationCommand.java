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
package com.jagrosh.interactions.command;

import com.jagrosh.interactions.interfaces.IJson;
import com.jagrosh.interactions.interfaces.ISnowflake;
import com.jagrosh.interactions.util.JsonUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class ApplicationCommand implements ISnowflake, IJson
{
    private final long id;
    private final ApplicationCommand.Type type;
    private final long applicationId, guildId;
    private final String name, description;
    private final List<ApplicationCommandOption> options;
    private final boolean defaultPermission;
    private final long version;

    public ApplicationCommand(ApplicationCommand.Type type, String name, String description, List<ApplicationCommandOption> options, boolean defaultPermission)
    {
        this.type = type;
        this.name = name;
        this.description = description;
        this.options = options;
        this.defaultPermission = defaultPermission;
        
        this.id = 0L;
        this.applicationId = 0L;
        this.guildId = 0L;
        this.version = 0;
    }
    
    public ApplicationCommand(JSONObject json)
    {
        this.id = json.getLong("id");
        this.type = ApplicationCommand.Type.of(json.optInt("type"));
        this.applicationId = json.getLong("application_id");
        this.guildId = json.optLong("guild_id");
        this.name = json.getString("name");
        this.description = json.getString("description");
        this.options = JsonUtil.optArray(json, "options", ApplicationCommandOption::new);
        this.defaultPermission = json.optBoolean("default_permission", true);
        this.version = json.getLong("version");
    }
    
    @Override
    public long getIdLong()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }

    public Type getType()
    {
        return type;
    }
    
    @Override
    public JSONObject toJson()
    {
        return new JSONObject()
                //.put("id", id)
                .put("type", type.getValue())
                .put("name", name)
                .put("description", description)
                .putOpt("options", options.isEmpty() ? null : JsonUtil.buildArray(options))
                .putOpt("default_permission", defaultPermission);
    }
    
    public enum Type
    {
        UNKNOWN(0), CHAT_INPUT(1), USER(2), MESSAGE(3);

        private final int value;

        private Type(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }

        public static Type of(int val)
        {
            for(Type t: Type.values())
                if(t.value == val)
                    return t;
            return UNKNOWN;
        }
    }
    
    public static class Builder
    {
        private final List<ApplicationCommandOption> options = new ArrayList<>();
        
        private ApplicationCommand.Type type;
        private String name = "", description = "";
        private boolean defaultPermission = true;
        
        public Builder setType(ApplicationCommand.Type type)
        {
            this.type = type;
            return this;
        }
        
        public Builder setName(String name)
        {
            this.name = name;
            return this;
        }
        
        public Builder setDescription(String description)
        {
            this.description = description;
            return this;
        }
        
        public Builder setDefaultPermission(boolean defaultPermission)
        {
            this.defaultPermission = defaultPermission;
            return this;
        }
        
        public Builder addOptions(ApplicationCommandOption... options)
        {
            for(ApplicationCommandOption aco: options)
                this.options.add(aco);
            return this;
        }
        
        public ApplicationCommand build()
        {
            return new ApplicationCommand(type, name, description, options, defaultPermission);
        }
    }
}
