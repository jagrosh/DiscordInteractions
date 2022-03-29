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
import com.jagrosh.interactions.util.JsonUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class SelectMenuComponent extends Component
{
    private final String customId;
    private final List<SelectOption> options = new ArrayList<>();
    private final String placeholder;
    private final Integer minValues, maxValues;
    private final boolean disabled;
    
    protected SelectMenuComponent(JSONObject json)
    {
        this(json.optString("custom_id"), json.optString("placeholder"), json.optInt("min_values", 1), json.optInt("max_values", 1), json.optBoolean("disabled", false));
    }
    
    public SelectMenuComponent(String customId)
    {
        this(customId, null, null, null, false);
    }
    
    public SelectMenuComponent(String customId, String placeholder, Integer minValues, Integer maxValues, boolean disabled)
    {
        this.customId = customId;
        this.placeholder = placeholder;
        this.minValues = minValues;
        this.maxValues = maxValues;
        this.disabled = disabled;
    }
    
    public void addOption(String label, String value)
    {
        addOption(label, value, null, null, false);
    }
    
    public void addOption(String label, String value, String description, PartialEmoji emoji)
    {
        addOption(label, value, description, emoji, false);
    }
    
    public void addOption(String label, String value, String description, PartialEmoji emoji, boolean defaultRender)
    {
        this.options.add(new SelectOption(label, value, description, emoji, defaultRender));
    }
    
    @Override
    public Type getType()
    {
        return Type.SELECT_MENU;
    }

    @Override
    public JSONObject toJson()
    {
        return super.toJson()
                .put("custom_id", customId)
                .put("options", JsonUtil.buildArray(options))
                .putOpt("placeholder", placeholder)
                .putOpt("min_values", minValues)
                .putOpt("max_values", maxValues)
                .putOpt("disabled", disabled);
    }
    
    private class SelectOption implements IJson
    {
        private final String label, value, description;
        private final PartialEmoji emoji;
        private final boolean defaultRender;
        
        private SelectOption(String label, String value, String description, PartialEmoji emoji, boolean defaultRender)
        {
            this.label = label;
            this.value = value;
            this.description = description;
            this.emoji = emoji;
            this.defaultRender = defaultRender;
        }
        
        @Override
        public JSONObject toJson()
        {
            return new JSONObject()
                    .put("label", label)
                    .put("value", value)
                    .putOpt("description", description)
                    .putOpt("emoji", emoji == null ? null : emoji.toJson())
                    .putOpt("default", defaultRender);
        }
    }
}
