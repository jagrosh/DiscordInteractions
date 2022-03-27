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
import com.jagrosh.interactions.interfaces.IJson;
import com.jagrosh.interactions.util.JsonUtil;
import com.jagrosh.interactions.util.OtherUtil;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class SentMessage extends Message implements IJson
{
    private final List<Embed> embeds;
    private final AllowedMentions allowedMentions;
    private final int flags;
    private final List<Component> components;
    private final List<Attachment> attachments;
    private final long referenceMessageId;
    private final boolean forceComponents, forceEmbeds;
    
    public SentMessage(boolean tts, String content, List<Embed> embeds, AllowedMentions allowedMentions, int flags, List<Component> components, List<Attachment> attachments, long referenceMessageId, boolean forceComponents, boolean forceEmbeds)
    {
        super(content, tts);
        this.embeds = embeds;
        this.allowedMentions = allowedMentions;
        this.flags = flags;
        this.components = components;
        this.attachments = attachments;
        this.referenceMessageId = referenceMessageId;
        this.forceComponents = forceComponents;
        this.forceEmbeds = forceEmbeds;
    }
    
    @Override
    public JSONObject toJson()
    {
        return new JSONObject()
                .putOpt("tts", tts)
                .putOpt("content", content)
                .putOpt("embeds", forceEmbeds && embeds.isEmpty() ? new JSONArray() : JsonUtil.buildArray(embeds))
                .putOpt("allowed_mentions", allowedMentions == null ? null : allowedMentions.toJson())
                .putOpt("flags", flags)
                .putOpt("components", forceComponents && components.isEmpty() ? new JSONArray() : JsonUtil.buildArray(components))
                .putOpt("attachments", JsonUtil.buildArray(attachments))
                .putOpt("message_reference", referenceMessageId == 0L ? null : new JSONObject().put("message_id", referenceMessageId));
    }
    
    public static class Builder
    {
        private final List<Embed> embeds = new ArrayList<>();
        private final List<Component> components = new ArrayList<>();
        private final List<Attachment> attachments = new ArrayList<>();
        private final BitSet flags = new BitSet();
        
        private String content = "";
        private boolean tts = false;
        private AllowedMentions allowedMentions = new AllowedMentions();
        private long referenceMessageId = 0L;
        private boolean forceComponents = false, forceEmbeds = false;
        
        public Builder setContent(String content)
        {
            this.content = content;
            return this;
        }
        
        public Builder setTts(boolean tts)
        {
            this.tts = tts;
            return this;
        }
        
        public Builder addEmbed(Embed embed)
        {
            this.embeds.add(embed);
            return this;
        }
        
        public Builder removeEmbeds()
        {
            this.forceEmbeds = true;
            this.embeds.clear();
            return this;
        }
        
        public Builder addComponent(Component component)
        {
            this.components.add(component);
            return this;
        }
        
        public Builder removeComponents()
        {
            this.forceComponents = true;
            this.components.clear();
            return this;
        }
        
        public Builder setReferenceMessage(long messageId)
        {
            this.referenceMessageId = messageId;
            return this;
        }
        
        // flags
        public Builder setEphemeral(boolean value)
        {
            this.flags.set(6, value);
            return this;
        }
        
        public Builder setSuppressEmbeds(boolean value)
        {
            this.flags.set(2, value);
            return this;
        }
        
        public SentMessage build()
        {
            return new SentMessage(tts, content, embeds, allowedMentions, OtherUtil.getBitsetValue(flags), components, attachments, referenceMessageId, forceComponents, forceEmbeds);
        }
    }
}
