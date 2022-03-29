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
import org.json.JSONArray;
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
    
    public CompletableFuture<RestResponse> request(Route.FormattedRoute route, JSONArray json)
    {
        return request(route, json.toString());
    }
    
    public CompletableFuture<RestResponse> request(Route.FormattedRoute route, JSONObject json)
    {
        return request(route, json.toString());
    }
    
    public CompletableFuture<RestResponse> request(Route.FormattedRoute route, String str)
    {
        return CompletableFuture.supplyAsync(() -> 
        {
            try
            {
                Response res = client.newCall(new Request.Builder()
                    .url(route.getURL())
                    .method(route.getType().name(), RequestBody.create(str.getBytes()))
                    .header("Authorization", "Bot " + authorization)
                    .header("Content-Type", "Application/Json")
                    .header("User-Agent", "DiscordBot (DiscordInteractions, 0.1)").build()).execute();
                String body = res.body().string();
                return new RestResponse(res.code(), body.startsWith("[") ? new JSONObject().put("_", new JSONArray(body)) : new JSONObject(body));
            }
            catch(IOException ex)
            {
                return new RestResponse(0, null);
            }
        });
    }
    
    public CompletableFuture<RestResponse> simpleRequest(String url)
    {
        return CompletableFuture.supplyAsync(() -> 
        {
            try
            {
                Response res = client.newCall(new Request.Builder()
                    .url(url).get()
                    .header("Content-Type", "Application/Json")
                    .header("User-Agent", "DiscordBot (DiscordInteractions, 0.1)").build()).execute();
                String body = res.body().string();
                return new RestResponse(res.code(), body.startsWith("[") ? new JSONObject().put("_", new JSONArray(body)) : new JSONObject(body));
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
        
        public JSONArray getBodyAsArray()
        {
            return body.has("_") ? body.getJSONArray("_") : new JSONArray();
        }
        
        public boolean isSuccess()
        {
            return status >= 200 && status < 300;
        }
        
        public RestError getError()
        {
            return RestError.of(status);
        }
        
        public int getErrorSpecific()
        {
            if(isSuccess())
                return 0;
            return body.has("code") ? body.getInt("code") : 0;
        }
    }
    
    public enum RestError
    {
        UNKNOWN(0),
        NO_ERROR(200,299),
        NOT_MODIFIED(304),
        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        FORBIDDEN(403),
        NOT_FOUND(404),
        RATELIMITED(429),
        GATEWAY_UNAVAILABLE(502),
        SERVER_ERROR(500,599);
        
        private final int min, max;
        
        private RestError(int val)
        {
            this(val, val);
        }
        
        private RestError(int min, int max)
        {
            this.min = min;
            this.max = max;
        }
        
        private static RestError of(int code)
        {
            for(RestError re: values())
                if(code >= re.min && code <= re.max)
                    return re;
            return UNKNOWN;
        }
    }
}
