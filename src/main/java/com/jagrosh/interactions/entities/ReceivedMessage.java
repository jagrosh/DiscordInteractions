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

import com.jagrosh.interactions.components.Component;
import com.jagrosh.interactions.interfaces.ISnowflake;
import com.jagrosh.interactions.util.JsonUtil;
import java.time.OffsetDateTime;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class ReceivedMessage extends Message implements ISnowflake
{
    private final long id, channelId, guildId;
    private final User author;
    private final GuildMember member;
    private final OffsetDateTime timestamp, editedTimestamp;
    private final boolean mentionEveryone;
    //private final List<User> mentions;
    //private final List<Role> roleMentions;
    //private final List<Channel> channelMentions;
    //private final List<Attachment> attachments;
    //private final List<Embed> embeds;
    //private final List<Reaction> reactions;
    //private final String nonce;
    //private final boolean pinned;
    //private final long webhookId;
    //private final type
    //private final message activity
    //private final long applicationId;
    //private final message reference
    //private final int flags
    // todo: the rest of the fields
    private final List<Component> components;
    
    public ReceivedMessage(JSONObject json)
    {
        super(json.getString("content"), json.getBoolean("tts"));
        this.id = json.getLong("id");
        this.channelId = json.getLong("channel_id");
        this.guildId = json.optLong("guild_id");
        this.author = new User(json.getJSONObject("author"));
        this.member = json.has("member") ? new GuildMember(json.getJSONObject("member")) : null;
        this.timestamp = OffsetDateTime.parse(json.getString("timestamp"));
        this.editedTimestamp = json.has("edited_timestamp") && !json.isNull("edited_timestamp") ? OffsetDateTime.parse(json.getString("edited_timestamp")) : null;
        this.mentionEveryone = json.getBoolean("mention_everyone");
        this.components = JsonUtil.optArray(json, "components", Component::parseComponent);
    }
    
    @Override
    public long getIdLong()
    {
        return id;
    }

    public User getAuthor()
    {
        return author;
    }

    public long getChannelId()
    {
        return channelId;
    }

    public String getContent()
    {
        return content;
    }

    public OffsetDateTime getEditedTimestamp()
    {
        return editedTimestamp;
    }

    public long getGuildId()
    {
        return guildId;
    }

    public GuildMember getMember()
    {
        return member;
    }

    public OffsetDateTime getTimestamp()
    {
        return timestamp;
    }
    
    public List<Component> getComponents()
    {
        return components;
    }
}
