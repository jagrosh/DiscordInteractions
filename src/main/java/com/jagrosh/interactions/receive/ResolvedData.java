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
package com.jagrosh.interactions.receive;

import com.jagrosh.interactions.entities.*;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class ResolvedData
{
    private final Map<Long,User> users = new HashMap<>();
    private final Map<Long,GuildMember> members = new HashMap<>();
    private final Map<Long,GuildRole> roles = new HashMap<>();
    private final Map<Long,ReceivedMessage> messages = new HashMap<>();
    private final Map<Long,Attachment> attachments = new HashMap<>();
    
    public ResolvedData(JSONObject json)
    {
        JSONObject u = json.optJSONObject("users");
        if(u != null)
            u.keySet().forEach(k -> users.put(Long.parseLong(k), new User(u.getJSONObject(k))));
        JSONObject m = json.optJSONObject("members");
        if(m != null)
            m.keySet().forEach(k -> members.put(Long.parseLong(k), new GuildMember(m.getJSONObject(k))));
        JSONObject r = json.optJSONObject("roles");
        if(r != null)
            r.keySet().forEach(k -> roles.put(Long.parseLong(k), new GuildRole(r.getJSONObject(k))));
        JSONObject s = json.optJSONObject("messages");
        if(s != null)
            s.keySet().forEach(k -> messages.put(Long.parseLong(k), new ReceivedMessage(s.getJSONObject(k))));
        //JSONObject a = json.optJSONObject("attachments");
        //if(a != null)
        //    a.keySet().forEach(k -> attachments.put(Long.parseLong(k), new Attachment()));
    }
    
    public Map<Long,User> getUsers()
    {
        return users;
    }

    public Map<Long, GuildMember> getMembers()
    {
        return members;
    }

    public Map<Long, GuildRole> getRoles()
    {
        return roles;
    }

    public Map<Long, ReceivedMessage> getMessages()
    {
        return messages;
    }

    public Map<Long, Attachment> getAttachments()
    {
        return attachments;
    }
}
