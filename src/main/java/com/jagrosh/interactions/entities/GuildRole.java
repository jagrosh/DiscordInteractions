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
import java.awt.Color;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class GuildRole implements ISnowflake
{
    private final long id;
    private final String name;
    private final Color color;
    private final boolean hoist;
    private final String icon, unicodeEmoji;
    private final int position;
    private final String permissions;
    private final boolean managed, mentionable;
    private final Tags tags;
    
    public GuildRole(JSONObject json)
    {
        this.id = json.getLong("id");
        this.name = json.getString("name");
        this.color = new Color(json.getInt("color"));
        this.hoist = json.getBoolean("hoist");
        this.icon = json.optString("icon");
        this.unicodeEmoji = json.optString("unicode_emoji");
        this.position = json.getInt("position");
        this.permissions = json.getString("permissions");
        this.managed = json.optBoolean("managed");
        this.mentionable = json.optBoolean("mentionable");
        this.tags = json.has("tags") ? new Tags(json.getJSONObject("tags")) : null;
    }

    @Override
    public long getIdLong()
    {
        return this.id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public boolean hasPermission(Permission p)
    {
        return (Long.parseLong(this.permissions) & p.getValue()) > 0;
    }
    
    public class Tags
    {
        private final long botId, integrationId;
        private final boolean premiumSubscriber;
        
        private Tags(JSONObject json)
        {
            this.botId = json.optLong("bot_id");
            this.integrationId = json.optLong("integration_id");
            this.premiumSubscriber = json.has("premium_subscriber");
        }
        
        public long getBotId()
        {
            return botId;
        }

        public long getIntegrationId()
        {
            return integrationId;
        }

        public boolean isPremiumSubscriber()
        {
            return premiumSubscriber;
        }
    }
    
    /*
    {
      "id": "771841269684371466",
      "name": "@everyone",
      "permissions": "1071698529857",
      "position": 0,
      "color": 0,
      "hoist": false,
      "managed": false,
      "mentionable": false,
      "icon": null,
      "unicode_emoji": null
    },
    {
      "id": "771841269684371467",
      "name": "Bots",
      "permissions": "6509871168",
      "position": 3,
      "color": 7419530,
      "hoist": true,
      "managed": false,
      "mentionable": false,
      "icon": null,
      "unicode_emoji": null
    },
    */
}
