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
import com.jagrosh.interactions.interfaces.ISnowflake;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class PartialEmoji implements IJson, ISnowflake
{
    private final String name;
    private final long id;
    private final boolean animated;

    public PartialEmoji(JSONObject json)
    {
        this(json.optString("name"), json.optLong("id"), json.optBoolean("animated"));
    }
    
    public PartialEmoji(String name, long id, boolean animated)
    {
        this.name = name;
        this.id = id;
        this.animated = animated;
    }

    @Override
    public JSONObject toJson()
    {
        return new JSONObject()
                .put("name", name)
                .put("id", getId())
                .put("animated", animated);
    }

    @Override
    public long getIdLong()
    {
        return id;
    }
}
