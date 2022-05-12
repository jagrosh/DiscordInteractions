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
import com.jagrosh.interactions.util.JsonUtil;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class Guild implements ISnowflake
{
    private final long id;
    private final boolean unavailable;
    private final String name, icon, splash, discoverySplash;
    private final long ownerId, afkChannelId;
    private final int afkTimeout;
    private final boolean widgetEnabled;
    private final long widgetChannelId;
    private final VerificationLevel verificationLevel;
    private final NotificationLevel defaultMessageNotifications;
    private final ContentFilterLevel contentFilter;
    private final List<GuildRole> roles;
    //private final List<GuildEmoji> emojis;
    private final List<Feature> features;
    private final MFALevel mfaLevel;
    private final long systemChannelId;
    private final int systemChannelFlags;
    private final long rulesChannelId;
    private final WebLocale preferredLocale;
    // todo: the rest

    public Guild(JSONObject json)
    {
        this.id = json.getLong("id");
        this.unavailable = json.optBoolean("unavailable", false);
        this.name = json.optString("name");
        this.icon = json.optString("icon");
        this.splash = json.optString("splash");
        this.discoverySplash = json.optString("discovery_splash");
        this.ownerId = json.optLong("owner_id");
        this.afkChannelId = json.optLong("afk_channel_id");
        this.afkTimeout = json.optInt("afk_timeout");
        this.widgetEnabled = json.optBoolean("widget_enabled", false);
        this.widgetChannelId = json.optLong("widget_channel_id");
        this.verificationLevel = VerificationLevel.of(json.optInt("verification_level"));
        this.defaultMessageNotifications = NotificationLevel.of(json.optInt("default_message_notifications"));
        this.contentFilter = ContentFilterLevel.of(json.optInt("explicit_content_filter"));
        this.roles = JsonUtil.optArray(json, "roles", GuildRole::new);
        this.features = JsonUtil.optStringArray(json, "features", s -> Feature.of(s));
        this.mfaLevel = MFALevel.of(json.optInt("mfa_level"));
        this.systemChannelId = json.optLong("system_channel_id");
        this.systemChannelFlags = json.optInt("system_channel_flags");
        this.rulesChannelId = json.optLong("rules_channel_id");
        this.preferredLocale = WebLocale.of(json.optString("preferred_locale"));
    }
    
    @Override
    public long getIdLong()
    {
        return id;
    }
    
    public long getOwnerId()
    {
        return ownerId;
    }
    
    public List<GuildRole> getRoles()
    {
        return roles;
    }
    
    public String getName()
    {
        return name;
    }

    public WebLocale getPreferredLocale()
    {
        return preferredLocale;
    }
    
    
    public enum VerificationLevel
    {
        NONE(0), LOW(1), MEDIUM(2), HIGH(3), VERY_HIGH(4);
        
        private final int value;
        
        private VerificationLevel(int value)
        {
            this.value = value;
        }
        
        protected static VerificationLevel of(int value)
        {
            for(VerificationLevel vl: values())
                if(vl.value == value)
                    return vl;
            return NONE;
        }
    }
    
    public enum NotificationLevel
    {
        ALL_MESSAGES(0), ONLY_MENTIONS(1);
        
        private final int value;
        
        private NotificationLevel(int value)
        {
            this.value = value;
        }
        
        protected static NotificationLevel of(int value)
        {
            for(NotificationLevel l: values())
                if(l.value == value)
                    return l;
            return ALL_MESSAGES;
        }
    }
    
    public enum ContentFilterLevel
    {
        DISABLED(0), MEMBERS_WITHOUT_ROLES(1), ALL_MEMBERS(2);
        
        private final int value;
        
        private ContentFilterLevel(int value)
        {
            this.value = value;
        }
        
        protected static ContentFilterLevel of(int value)
        {
            for(ContentFilterLevel l: values())
                if(l.value == value)
                    return l;
            return DISABLED;
        }
    }
    
    public enum MFALevel
    {
        NONE(0), ELEVATED(1);
        
        private final int value;
        
        private MFALevel(int value)
        {
            this.value = value;
        }
        
        protected static MFALevel of(int value)
        {
            for(MFALevel l: values())
                if(l.value == value)
                    return l;
            return NONE;
        }
    }
    
    public enum Feature
    {
        UNKNOWN, ANIMATED_ICON, BANNER, COMMERCE, COMMUNITY, DISCOVERABLE, FEATURABLE, INVITE_SPLASH, 
        MEMBER_VERIFICATION_GATE_ENABLED, MONETIZATION_ENABLED, MORE_STICKERS, NEWS, PARTNERED, 
        PREVIEW_ENABLED, PRIVATE_THREADS, ROLE_ICONS, SEVEN_DAY_THREAD_ARCHIVE, THREE_DAY_THREAD_ARCHIVE, 
        TICKETED_EVENTS_ENABLED, VANITY_URL, VERIFIED, VIP_REGIONS, WELCOME_SCREEN_ENABLED;
        
        public static Feature of(String name)
        {
            for(Feature f: values())
                if(f.name().equalsIgnoreCase(name))
                    return f;
            return UNKNOWN;
        }
    }
}