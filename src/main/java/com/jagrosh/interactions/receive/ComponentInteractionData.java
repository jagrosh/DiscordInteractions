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

import com.jagrosh.interactions.components.Component;
import com.jagrosh.interactions.components.ComponentValue;
import com.jagrosh.interactions.util.JsonUtil;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class ComponentInteractionData
{
    private final String customId;
    private final Component.Type componentType;
    private final List<String> values;
    private final List<ComponentValue> components;
    
    public ComponentInteractionData(JSONObject json)
    {
        this.customId = json.optString("custom_id");
        this.componentType = Component.Type.of(json.optInt("component_type", 0));
        this.values = null;
        this.components = JsonUtil.optArray(json, "components", j -> new ComponentValue(j));
    }
    
    public String getCustomId()
    {
        return customId;
    }
    
    public Component.Type getType()
    {
        return componentType;
    }
    
    public List<ComponentValue> getComponents()
    {
        return components;
    }
    
    public String getModalValueByCustomId(String customId)
    {
        for(ComponentValue cv: components)
        {
            String cvval = cv.getValueByCustomId(customId);
            if(cvval != null)
                return cvval;
        }
        return null;
    }
}
