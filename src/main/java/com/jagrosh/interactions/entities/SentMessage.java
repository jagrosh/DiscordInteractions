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
    
    public SentMessage(boolean tts, String content, List<Embed> embeds, AllowedMentions allowedMentions, int flags, List<Component> components, List<Attachment> attachments)
    {
        super(content, tts);
        this.embeds = embeds;
        this.allowedMentions = allowedMentions;
        this.flags = flags;
        this.components = components;
        this.attachments = attachments;
    }
    
    @Override
    public JSONObject toJson()
    {
        return new JSONObject()
                .putOpt("tts", tts)
                .putOpt("content", content)
                .putOpt("embeds", JsonUtil.buildArray(embeds))
                .putOpt("allowed_mentions", allowedMentions == null ? null : allowedMentions.toJson())
                .putOpt("flags", flags)
                .putOpt("components", JsonUtil.buildArray(components))
                .putOpt("attachments", JsonUtil.buildArray(attachments));
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
        
        public Builder addComponent(Component component)
        {
            this.components.add(component);
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
            return new SentMessage(tts, content, embeds, allowedMentions, OtherUtil.getBitsetValue(flags), components, attachments);
        }
    }
}
