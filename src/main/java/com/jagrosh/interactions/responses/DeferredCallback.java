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

import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class DeferredCallback implements InteractionResponse
{
    private final boolean isUpdate;
    
    public DeferredCallback(boolean isUpdate)
    {
        this.isUpdate = isUpdate;
    }
    
    @Override
    public Type getType()
    {
        return isUpdate ? Type.DEFERRED_UPDATE_MESSAGE : Type.DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE;
    }

    @Override
    public JSONObject getData()
    {
        return null;
    }
}
