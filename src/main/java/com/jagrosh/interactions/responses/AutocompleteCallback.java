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

import com.jagrosh.interactions.command.Choice;
import com.jagrosh.interactions.util.JsonUtil;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 * @param <T> type of choice
 */
public class AutocompleteCallback<T> implements InteractionResponse
{
    private final List<Choice<T>> choices;
    
    public AutocompleteCallback(List<Choice<T>> choices)
    {
        this.choices = choices;
    }
    
    @Override
    public Type getType()
    {
        return Type.APPLICATION_COMMAND_AUTOCOMPLETE_RESULT;
    }
    
    @Override
    public JSONObject getData()
    {
        JSONArray arr = JsonUtil.buildArray(choices);
        return new JSONObject()
                .put("choices", arr == null ? new JSONArray() : arr);
    }
    
    public static class EmptyAutocompleteCallback extends AutocompleteCallback<Void>
    {
        public EmptyAutocompleteCallback()
        {
            super(Collections.emptyList());
        }
    }
}
