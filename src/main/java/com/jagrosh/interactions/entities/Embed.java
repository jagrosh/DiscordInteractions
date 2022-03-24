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

import com.jagrosh.interactions.interfaces.IJson;
import java.awt.Color;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class Embed implements IJson
{
    private final String title, description, url;
    private final Instant timestamp;
    private final Color color;
    private final String footerText, footerIconUrl, imageUrl, thumbnailUrl;
    
    private Embed(String title, String description, String url, Instant timestamp, Color color, String footerText, String footerIconUrl, String imageUrl, String thumbnailUrl)
    {
        this.title = title;
        this.description = description;
        this.url = url;
        this.timestamp = timestamp;
        this.color = color;
        this.footerText = footerText;
        this.footerIconUrl = footerIconUrl;
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
    }
    
    @Override
    public JSONObject toJson()
    {
        return new JSONObject()
                .putOpt("title", title)
                .putOpt("description", description)
                .putOpt("url", url)
                .putOpt("timestamp", timestamp == null ? null : timestamp.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME))
                .putOpt("color", color == null ? null : color.getRGB() & 0x00FFFFFF)
                .putOpt("footer", footerText == null ? null : new JSONObject().put("text", footerText).putOpt("icon_url", footerIconUrl))
                .putOpt("image", imageUrl == null ? null : new JSONObject().put("url", imageUrl))
                .putOpt("thumbnail", thumbnailUrl == null ? null : new JSONObject().put("url", thumbnailUrl));
    }
    
    public static class Builder
    {
        private String title, description, url;
        private Instant timestamp;
        private Color color;
        private String footerText, footerIconUrl, imageUrl, thumbnailUrl;
        
        public Embed build()
        {
            return new Embed(title, description, url, timestamp, color, footerText, footerIconUrl, imageUrl, thumbnailUrl);
        }
        
        public Builder setTitle(String title, String url)
        {
            this.title = title;
            this.url = url;
            return this;
        }
        
        public Builder setDescription(String description)
        {
            this.description = description;
            return this;
        }
        
        public Builder setTimestamp(Instant timestamp)
        {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setColor(Color color)
        {
            this.color = color;
            return this;
        }

        public Builder setFooter(String footerText, String footerIconUrl)
        {
            this.footerText = footerText;
            this.footerIconUrl = footerIconUrl;
            return this;
        }
        
        public Builder setImage(String url)
        {
            this.imageUrl = url;
            return this;
        }
        
        public Builder setThumbnail(String url)
        {
            this.thumbnailUrl = url;
            return this;
        }
    }
}
