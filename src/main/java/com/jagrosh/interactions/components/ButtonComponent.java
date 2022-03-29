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
public class ButtonComponent extends Component
{
    private final Style style;
    private final String label;
    private final PartialEmoji emoji;
    private final String customId, url;
    private final boolean disabled;
    
    protected ButtonComponent(JSONObject json)
    {
        this(Style.of(json.getInt("style")), json.optString("label"), json.has("emoji") ? new PartialEmoji(json.getJSONObject("emoji")) : null, json.optString("custom_id"), json.optString("url"), json.optBoolean("disabled", false));
    }
    
    public ButtonComponent(Style style, String label, String customId)
    {
        this(style, label, null, customId, null, false);
    }
    
    public ButtonComponent(String label, String url)
    {
        this(Style.LINK, label, null, null, url, false);
    }
    
    public ButtonComponent(Style style, PartialEmoji emoji, String customId)
    {
        this(style, null, emoji, customId, null, false);
    }
    
    public ButtonComponent(Style style, String label, PartialEmoji emoji, String customId, String url, boolean disabled)
    {
        this.style = style;
        this.label = label;
        this.emoji = emoji;
        this.customId = customId;
        this.url = url;
        this.disabled = disabled;
    }

    @Override
    public Type getType()
    {
        return Type.BUTTON;
    }
    
    @Override
    public JSONObject toJson()
    {
        return super.toJson()
                .put("style", style.getValue())
                .putOpt("label", label)
                .putOpt("emoji", emoji == null ? null : emoji.toJson())
                .putOpt("custom_id", customId)
                .putOpt("url", url)
                .putOpt("disabled", disabled);
    }
    
    public enum Style
    {
        UNKNOWN(0), PRIMARY(1), SECONDARY(2), SUCCESS(3), DANGER(4), LINK(5);
        
        private final int value;
        
        private Style(int value)
        {
            this.value = value;
        }
        
        public int getValue()
        {
            return value;
        }
        
        public static Style of(int value)
        {
            for(Style s: values())
                if(s.value == value)
                    return s;
            return UNKNOWN;
        }
    }
}
