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
package com.jagrosh.interactions.receive;

import com.jagrosh.interactions.command.ApplicationCommandOption;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class CommandInteractionDataOption
{
    private final String name;
    private final ApplicationCommandOption.Type type;
    private final String stringValue;
    private final Double doubleValue;
    private final Integer intValue;
    private final List<CommandInteractionDataOption> options;
    private final boolean focused;
    
    public CommandInteractionDataOption(JSONObject json)
    {
        this.name = json.getString("name");
        this.type = ApplicationCommandOption.Type.of(json.getInt("type"));
        switch(type)
        {
            case STRING:
                this.stringValue = json.getString("value");
                this.doubleValue = null;
                this.intValue = null;
                break;
            case INTEGER:
                this.stringValue = null;
                this.doubleValue = null;
                this.intValue = json.getInt("value");
                break;
            case NUMBER:
                this.stringValue = null;
                this.doubleValue = json.getDouble("value");
                this.intValue = null;
                break;
            default:
                this.stringValue = json.optString("value");
                this.doubleValue = null;
                this.intValue = null;
        }
        this.options = null;
        this.focused = json.optBoolean("focused");
    }
    
    public String getName()
    {
        return this.name;
    }

    public List<CommandInteractionDataOption> getOptions()
    {
        return options;
    }
    
    public String getStringValue()
    {
        return this.stringValue;
    }
    
    public double getDoubleValue()
    {
        return this.doubleValue;
    }

    public int getIntValue()
    {
        return this.intValue;
    }
}
