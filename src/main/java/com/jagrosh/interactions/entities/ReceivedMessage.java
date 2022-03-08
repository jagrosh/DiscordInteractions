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
import java.time.OffsetDateTime;
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
    private boolean mentionEveryone;
    // todo: the rest of the fields
    
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
    }
    
    @Override
    public long getIdLong()
    {
        return id;
    }
}
