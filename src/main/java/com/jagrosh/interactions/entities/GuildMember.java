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
package com.jagrosh.interactions.entities;

import com.jagrosh.interactions.interfaces.ISnowflake;
import com.jagrosh.interactions.util.JsonUtil;
import java.time.OffsetDateTime;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class GuildMember implements ISnowflake
{
    private final User user;
    private final String nick, avatar;
    private final List<Long> roles;
    private final OffsetDateTime joinedAt, premiumSince;
    private final boolean deaf, mute, pending;
    private final long permissions;
    private final OffsetDateTime communicationDisabledUntil;
    
    public GuildMember(JSONObject json)
    {
        this.user = json.has("user") ? new User(json.getJSONObject("user")) : null;
        this.nick = json.optString("nick");
        this.avatar = json.optString("avatar");
        this.roles = JsonUtil.longArray(json.getJSONArray("roles"));
        this.joinedAt = OffsetDateTime.parse(json.getString("joined_at"));
        this.premiumSince = json.isNull("premium_since") ? null : OffsetDateTime.parse(json.getString("premium_since"));
        this.deaf = json.optBoolean("deaf", false);
        this.mute = json.optBoolean("mute", false);
        this.pending = json.optBoolean("pending", false);
        this.permissions = json.optLong("permissions", 0L);
        this.communicationDisabledUntil = json.isNull("communication_disabled_until") ? null : OffsetDateTime.parse(json.getString("communication_disabled_until"));
    }

    @Override
    public long getIdLong()
    {
        return user.getIdLong();
    }
    
    public User getUser()
    {
        return user;
    }
    
    public boolean hasPermission(Permission p)
    {
        return (this.permissions & p.getValue()) > 0;
    }
    
    public boolean hasRole(long roleId)
    {
        return roles.contains(roleId);
    }
    
    public List<Long> getRoles()
    {
        return roles;
    }
}
