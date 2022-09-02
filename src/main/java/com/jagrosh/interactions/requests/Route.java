/*
 * Copyright 2020 John Grosh (john.a.grosh@gmail.com).
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

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public enum Route
{
    // global
    GET_COMMANDS(Type.GET, "applications/%d/commands"),
    PUT_COMMANDS(Type.PUT, "applications/%d/commands"),
    POST_COMMANDS(Type.POST, "applications/%d/commands"),
    PATCH_COMMAND(Type.PATCH, "applications/%d/commands/%d"),
    DELETE_COMMAND(Type.DELETE, "applications/%d/commands/%d"),
    GET_GATEWAY(Type.GET, "gateway/bot"),
    
     // guild
    GET_GUILD_COMMANDS(Type.GET, "applications/%d/guilds/%d/commands"),
    POST_GUILD_COMMANDS(Type.POST, "applications/%d/guilds/%d/commands"),
    PATCH_GUILD_COMMAND(Type.PATCH, "applications/%d/guilds/%d/commands/%d"),
    DELETE_GUILD_COMMAND(Type.DELETE, "applications/%d/guilds/%d/commands/%d"),
    
    // send message
    GET_MESSAGE(Type.GET, "channels/%d/messages/%d"),
    POST_MESSAGE(Type.POST, "channels/%d/messages"),
    PATCH_MESSAGE(Type.PATCH, "channels/%d/messages/%d"),
    DELETE_MESSAGE(Type.DELETE, "channels/%d/messages/%d"),
    
    // get guild
    GET_GUILD(Type.GET, "guilds/%d"),
    ADD_GUILD_MEMBER_ROLE(Type.PUT, "/guilds/%d/members/%d/roles/%d"),
    REMOVE_GUILD_MEMBER_ROLE(Type.DELETE, "/guilds/%d/members/%d/roles/%d"),
    ;
    
    public static final String BASE_URL = "https://discordapp.com/api/v8/";
    
    private final String route;
    private final Type type;

    private Route(Type type, String route)
    {
        this.type = type;
        this.route = route;
    }
    
    public FormattedRoute format(Object... params)
    {
        return new FormattedRoute(this, BASE_URL + String.format(route, params));
    }
    
    public Type getType()
    {
        return type;
    }
    
    public enum Type
    {
        GET, POST, PATCH, DELETE, PUT
    }
    
    public static class FormattedRoute
    {
        private final Route.Type type;
        private final String url;
        
        protected FormattedRoute(Route route, String url)
        {
            this(route.type, url);
        }
        
        protected FormattedRoute(Route.Type type, String url)
        {
            this.type = type;
            this.url = url;
        }
        
        public Route.Type getType()
        {
            return type;
        }
        
        public String getURL()
        {
            return url;
        }
    }
}
