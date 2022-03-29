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
package com.jagrosh.interactions.entities;

import com.jagrosh.interactions.interfaces.IJson;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class AllowedMentions implements IJson
{
    private final boolean parseNothing;
    private final Set<ParseType> parseTypes = new HashSet<>();
    //private final List<Long> roles = new ArrayList<>();
    //private final List<Long> users = new ArrayList<>();
    
    public AllowedMentions(ParseType... types)
    {
        this(false);
        this.parseTypes.addAll(Arrays.asList(types));
    }
    
    public AllowedMentions()
    {
        this(true);
    }
    
    public AllowedMentions(boolean parseNothing)
    {
        this.parseNothing = parseNothing;
    }
    
    @Override
    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();
        if(parseNothing)
            json.put("parse", new JSONArray());
        else if(!parseTypes.isEmpty())
        {
            JSONArray arr = new JSONArray();
            parseTypes.forEach(p -> arr.put(p.name().toLowerCase()));
            json.put("parse", arr);
        }
        return json;
    }
    
    public enum ParseType
    {
        ROLES, USERS, EVERYONE
    }
}
