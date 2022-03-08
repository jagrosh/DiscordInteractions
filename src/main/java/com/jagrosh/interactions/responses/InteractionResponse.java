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
package com.jagrosh.interactions.responses;

import com.jagrosh.interactions.interfaces.IJson;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public interface InteractionResponse extends IJson
{
    public Type getType();
    
    public JSONObject getData();
    
    @Override
    default JSONObject toJson()
    {
        return new JSONObject()
                .put("type", getType().getValue())
                .putOpt("data", getData());
    }
    
    public enum Type
    {
        UNKNOWN(0), PONG(1), CHANNEL_MESSAGE_WITH_SOURCE(4), DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE(5), DEFERRED_UPDATE_MESSAGE(6), UPDATE_MESSAGE(7), APPLICATION_COMMAND_AUTOCOMPLETE_RESULT(8), MODAL(9);
    
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
            for(Type t: values())
                if(t.value == val)
                    return t;
            return UNKNOWN;
        }
    }
}
