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
package com.jagrosh.interactions.requests;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class RestClient
{
    private final OkHttpClient client = new OkHttpClient.Builder().build();
    private final String authorization;
    
    public RestClient(String authorization)
    {
        this.authorization = authorization;
    }
    
    public CompletableFuture<RestResponse> request(Route.FormattedRoute route, JSONObject json)
    {
        //System.out.println("Route " + route.getURL() + " body " + json.toString());
        return CompletableFuture.supplyAsync(() -> 
        {
            try
            {
                Response res = client.newCall(new Request.Builder()
                    .url(route.getURL())
                    .method(route.getRoute().getType().name(), RequestBody.create(json.toString().getBytes()))
                    .header("Authorization", "Bot " + authorization)
                    .header("Content-Type", "Application/Json")
                    .header("User-Agent", "DiscordBot (DiscordInteractions, 0.1)").build()).execute();
                String body = res.body().string();
                return new RestResponse(res.code(), new JSONObject(body));
            }
            catch(IOException ex)
            {
                return new RestResponse(0, null);
            }
        });
    }
    
    public class RestResponse
    {
        private final int status;
        private final JSONObject body;
        
        private RestResponse(int status, JSONObject body)
        {
            this.status = status;
            this.body = body;
        }
        
        public int getStatus()
        {
            return status;
        }
        
        public JSONObject getBody()
        {
            return body;
        }
    }
}
