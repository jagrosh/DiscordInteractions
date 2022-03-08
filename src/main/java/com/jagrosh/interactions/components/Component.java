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
package com.jagrosh.interactions.components;

import com.jagrosh.interactions.interfaces.IJson;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public abstract class Component implements IJson
{
    public abstract Type getType();

    @Override
    public JSONObject toJson()
    {
        return new JSONObject().put("type", getType().getValue());
    }
    
    public static enum Type
    {
        UNKNOWN(0), ACTION_ROW(1), BUTTON(2), SELECT_MENU(3), TEXT_INPUT(4);
        
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
            for(Type t: Type.values())
                if(t.value == value)
                    return t;
            return UNKNOWN;
        }
    }
}
