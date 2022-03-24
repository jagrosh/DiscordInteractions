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

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public enum Permission
{
    CREATE_INSTANT_INVITE(0),
    KICK_MEMBERS(1),
    BAN_MEMBERS(2),
    ADMINISTRATOR(3),
    MANAGE_CHANNELS(4),
    MANAGE_GUILD(5),
    ADD_REACTIONS(6),
    VIEW_AUDIT_LOG(7),
    PRIORITY_SPEAKER(8),
    STREAM(9),
    VIEW_CHANNEL(10),
    SEND_MESSAGES(11),
    SEND_TTS_MESSAGES(12),
    MANAGE_MESSAGES(13),
    EMBED_LINKS(14),
    ATTACH_FILES(15),
    READ_MESSAGE_HISTORY(16),
    MENTION_EVERYONE(17),
    USE_EXTERNAL_EMOJIS(18),
    VIEW_GUILD_INSIGHTS(19),
    CONNECT(20),
    SPEAK(21),
    MUTE_MEMBERS(22),
    DEAFEN_MEMBERS(23),
    MOVE_MEMBERS(24),
    USE_VAD(25),
    CHANGE_NICKNAME(26),
    MANAGE_NICKNAMES(27),
    MANAGE_ROLES(28),
    MANAGE_WEBHOOKS(29),
    MANAGE_EMOJIS_AND_STICKERS(30),
    USE_APPLICATION_COMMANDS(31),
    REQUEST_TO_SPEAK(32),
    MANAGE_EVENTS(33),
    MANAGE_THREADS(34),
    CREATE_PUBLIC_THREADS(35),
    CREATE_PRIVATE_THREADS(36),
    USE_EXTERNAL_STICKERS(37),
    SEND_MESSAGES_IN_THREADS(38),
    USE_EMBEDDED_ACTIVITIES(39),
    MODERATE_MEMBERS(40);
    
    private final long value;
    
    private Permission(int bitshift)
    {
        this.value = 1 << bitshift;
    }
    
    public long getValue()
    {
        return value;
    }
}
