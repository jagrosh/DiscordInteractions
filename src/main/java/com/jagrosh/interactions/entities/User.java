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

import com.jagrosh.interactions.interfaces.ISnowflake;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class User implements ISnowflake
{
    private final long id;
    private final String username, discriminator, avatar;
    private final boolean bot, system, mfaEnabled;
    private final String banner;
    private final int accentColor;
    private final String locale;
    private final boolean verified;
    private final String email;
    private final int flags, premiumType, publicFlags;
    
    
    public User(JSONObject json)
    {
        this.id = json.getLong("id");
        this.username = json.getString("username");
        this.discriminator = json.getString("discriminator");
        this.avatar = json.getString("avatar");
        this.bot = json.optBoolean("bot");
        this.system = json.optBoolean("system");
        this.mfaEnabled = json.optBoolean("mfa_enabled");
        this.banner = json.optString("banner");
        this.accentColor = json.optInt("accent_color");
        this.locale = json.optString("locale");
        this.verified = json.optBoolean("verified");
        this.email = json.optString("email");
        this.flags = json.optInt("flags");
        this.premiumType = json.optInt("premium_type");
        this.publicFlags = json.optInt("public_flags");
    }

    @Override
    public long getIdLong()
    {
        return id;
    }
}
