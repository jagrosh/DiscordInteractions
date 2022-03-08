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
package com.jagrosh.interactions.util;

import com.jagrosh.interactions.interfaces.IJson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class JsonUtil
{
    public static <T> List<T> optArray(JSONObject json, String key, Function<JSONObject,T> func)
    {
        if(!json.has(key))
            return Collections.emptyList();
        ArrayList<T> list = new ArrayList<>();
        JSONArray array = json.getJSONArray(key);
        for(int i = 0; i < array.length(); i++)
            list.add(func.apply(array.getJSONObject(i)));
        return list;
    }
    
    public static <T> List<T> optIntArray(JSONObject json, String key, Function<Integer,T> func)
    {
        if(!json.has(key))
            return Collections.emptyList();
        ArrayList<T> list = new ArrayList<>();
        JSONArray array = json.getJSONArray(key);
        for(int i = 0; i < array.length(); i++)
            list.add(func.apply(array.getInt(i)));
        return list;
    }
    
    public static List<Long> longArray(JSONArray array)
    {
        ArrayList<Long> list = new ArrayList<>();
        for(int i = 0; i < array.length(); i++)
            list.add(array.getLong(i));
        return list;
    }
    
    public static <T> JSONArray buildArray(List<T> list, BiConsumer<T,JSONArray> add)
    {
        if(list == null || list.isEmpty())
            return null;
        JSONArray array = new JSONArray();
        list.forEach(j -> add.accept(j, array));
        return array;
    }
    
    public static <T extends IJson> JSONArray buildArray(List<T> list)
    {
        if(list == null || list.isEmpty())
            return null;
        JSONArray array = new JSONArray();
        list.forEach(j -> array.put(j.toJson()));
        return array;
    }
}
