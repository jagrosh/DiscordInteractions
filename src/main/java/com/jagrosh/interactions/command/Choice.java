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
package com.jagrosh.interactions.command;

import com.jagrosh.interactions.interfaces.IJson;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 * @param <T> type of choice
 */
public class Choice<T> implements IJson
{
    private final String name;
    private final T value;
    
    public Choice(String name, T value)
    {
        this.name = name;
        this.value = value;
    }

    @Override
    public JSONObject toJson()
    {
        return new JSONObject()
                .put("name", name)
                .put("value", value);
    }
    
    public static class StringChoice extends Choice<String>
    {
        public StringChoice(JSONObject json)
        {
            super(json.getString("name"), json.getString("value"));
        }
    }
    
    public static class IntegerChoice extends Choice<Integer>
    {
        public IntegerChoice(JSONObject json)
        {
            super(json.getString("name"), json.getInt("value"));
        }
    }
    
    public static class NumberChoice extends Choice<Double>
    {
        public NumberChoice(JSONObject json)
        {
            super(json.getString("name"), json.getDouble("value"));
        }
    }
}
