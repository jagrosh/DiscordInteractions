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

import com.jagrosh.interactions.entities.ChannelType;
import com.jagrosh.interactions.interfaces.IJson;
import com.jagrosh.interactions.util.JsonUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class ApplicationCommandOption implements IJson
{
    private final ApplicationCommandOption.Type type;
    private final String name, description;
    private final boolean required;
    private final List<Choice<?>> choices;
    private final List<ApplicationCommandOption> options;
    private final List<ChannelType> channelTypes;
    private final Double minValue, maxValue;
    private final Boolean autocomplete;

    public ApplicationCommandOption(Type type, String name, String description, boolean required)
    {
        this(type, name, description, required, null, null, null);
    }
    
    public ApplicationCommandOption(Type type, String name, String description, boolean required, int minValue, int maxValue, Boolean autocomplete)
    {
        this(type, name, description, required, (double) minValue, (double) maxValue, autocomplete);
    }
    
    public ApplicationCommandOption(Type type, String name, String description, boolean required, Double minValue, Double maxValue, Boolean autocomplete)
    {
        this.type = type;
        this.name = name;
        this.description = description;
        this.required = required;
        this.choices = new ArrayList<>();
        this.options = new ArrayList<>();
        this.channelTypes = new ArrayList<>();
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.autocomplete = autocomplete;
    }
    
    public ApplicationCommandOption(JSONObject json)
    {
        this.type = Type.of(json.getInt("type"));
        this.name = json.getString("name");
        this.description = json.getString("description");
        this.required = json.optBoolean("required", false);
        switch(type)
        {
            case STRING:
                this.choices = JsonUtil.optArray(json, "choices", Choice.StringChoice::new);
                break;
            case INTEGER:
                this.choices = JsonUtil.optArray(json, "choices", Choice.IntegerChoice::new);
                break;
            case NUMBER:
                this.choices = JsonUtil.optArray(json, "choices", Choice.NumberChoice::new);
                break;
            default:
                this.choices = Collections.emptyList();
        }
        this.options = JsonUtil.optArray(json, "options", ApplicationCommandOption::new);
        this.channelTypes = JsonUtil.optIntArray(json, "channel_types", ChannelType::of);
        this.minValue = type == ApplicationCommandOption.Type.INTEGER ? json.optInt("min_value") : json.optDouble("min_value");
        this.maxValue = type == ApplicationCommandOption.Type.INTEGER ? json.optInt("max_value") : json.optDouble("max_value");
        this.autocomplete = json.optBoolean("autocomplete", false);
    }
    
    public void addChoices(Choice<?>... choices)
    {
        for(Choice<?> choice: choices)
            this.choices.add(choice);
    }
    
    public void addOptions(ApplicationCommandOption... options)
    {
        for(ApplicationCommandOption option: options)
            this.options.add(option);
    }

    public void setChannelTypes(ChannelType... types)
    {
        this.channelTypes.clear();
        this.channelTypes.addAll(Arrays.asList(types));
    }
    
    public List<ChannelType> getChannelTypes()
    {
        return channelTypes;
    }

    @Override
    public JSONObject toJson()
    {
        JSONObject obj = new JSONObject()
                .put("type", type.getValue())
                .put("name", name)
                .put("description", description)
                .put("required", required)
                .putOpt("choices", JsonUtil.buildArray(choices))
                .putOpt("options", JsonUtil.buildArray(options))
                .putOpt("channel_types", JsonUtil.buildArray(channelTypes, (c,a) -> a.put(c.getValue())))
                .putOpt("autocomplete", autocomplete);
        switch(type)
        {
            case INTEGER:
                return obj
                        .putOpt("min_value", minValue == null ? null : (int)(double) minValue)
                        .putOpt("max_value", maxValue == null ? null : (int)(double) maxValue);
            case NUMBER:
                return obj
                        .putOpt("min_value", minValue)
                        .putOpt("max_value", maxValue);
            default:
                return obj;
        }
    }
    
    public enum Type
    {
        UNKNOWN(0),
        SUB_COMMAND(1),
        SUB_COMMAND_GROUP(2),
        STRING(3),
        INTEGER(4),
        BOOLEAN(5),
        USER(6),
        CHANNEL(7),
        ROLE(8),
        MENTIONABLE(9),
        NUMBER(10);

        private final int value;

        private Type(int value)
        {
            this.value = value;
        }
        
        public int getValue()
        {
            return value;
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
