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

import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class TextInputComponent extends Component
{
    private final String customId;
    private final Style style;
    private final String label;
    private final Integer minLength, maxLength;
    private final boolean required;
    private final String value, placeholder;
    
    public TextInputComponent(String customId, Style style, String label, Integer minLength, Integer maxLength, boolean required, String value, String placeholder)
    {
        this.customId = customId;
        this.style = style;
        this.label = label;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.required = required;
        this.value = value;
        this.placeholder = placeholder;
    }
    
    @Override
    public Type getType()
    {
        return Type.TEXT_INPUT;
    }

    @Override
    public JSONObject toJson()
    {
        return super.toJson()
                .put("custom_id", customId)
                .put("style", style.getValue())
                .put("label", label)
                .putOpt("min_length", minLength)
                .putOpt("max_length", maxLength)
                .putOpt("value", value)
                .putOpt("placeholder", placeholder);
    }
    
    public enum Style
    {
        UNKNOWN(0), SHORT(1), PARAGRAPH(2);
        
        private final int value;
        
        private Style(int value)
        {
            this.value = value;
        }
        
        public int getValue()
        {
            return value;
        }
    }
}
