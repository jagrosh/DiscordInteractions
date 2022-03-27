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

import com.jagrosh.interactions.util.JsonUtil;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class ComponentValue extends Component
{
    private final List<ComponentValue> components;
    private final String customId, value;
    private final Type type;
    
    public ComponentValue(JSONObject json)
    {
        this.type = Type.of(json.getInt("type"));
        this.customId = json.optString("custom_id");
        this.value = json.optString("value");
        this.components = JsonUtil.optArray(json, "components", j -> new ComponentValue(j));
    }
    
    @Override
    public Type getType()
    {
        return this.type;
    }
    
    public String getCustomId()
    {
        return customId;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public List<ComponentValue> getComponentValues()
    {
        return components;
    }
    
    public String getValueByCustomId(String customId)
    {
        if(customId.equals(this.customId))
            return value;
        for(ComponentValue cv: components)
        {
            String cvval = cv.getValueByCustomId(customId);
            if(cvval != null)
                return cvval;
        }
        return null;
    }
}
