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
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class ActionRowComponent extends Component
{
    private final List<Component> components = new ArrayList<>();
    
    protected ActionRowComponent(JSONObject json)
    {
        List<Component> list = JsonUtil.optArray(json, "components", Component::parseComponent);
        if(list != null)
            components.addAll(list);
    }
    
    public ActionRowComponent(Component... cs)
    {
        for(Component c: cs)
            components.add(c);
    }
    
    public void addComponents(Component... cs)
    {
        for(Component c: cs)
            components.add(c);
    }
    
    public List<Component> getComponents()
    {
        return components;
    }
    
    @Override
    public Component.Type getType()
    {
        return Type.ACTION_ROW;
    }
    
    @Override
    public JSONObject toJson()
    {
        return super.toJson()
                .put("components", JsonUtil.buildArray(components));
    }
}
