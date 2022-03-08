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
package com.jagrosh.interactions.examples.command;

import com.jagrosh.interactions.command.Command;
import com.jagrosh.interactions.command.ApplicationCommand;
import com.jagrosh.interactions.receive.Interaction;
import com.jagrosh.interactions.responses.InteractionResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class CharinfoCmd implements Command
{
    private final static Pattern REGEX = Pattern.compile("<a?:([a-zA-Z0-9_-]{2,32}):(\\d{17,20})>");
    private final static String URL = "https://cdn.discordapp.com/emojis/%s.%s";
    
    public CharinfoCmd()
    {
    }
    
    @Override
    public InteractionResponse execute(Interaction interaction)
    {
        /*String str = interaction.getData().getOptionByName("text").getStringValue();
        String res;
        if(str.matches(REGEX.pattern()))
        {
            Matcher m = REGEX.matcher(str);
            m.find();
            boolean animated = str.startsWith("<a");
            res = str + " Emote Info:"
                    + "\nName: **" + m.group(1) + "**"
                    + "\nID: **" + m.group(2) + "**"
                    + "\nAnimated: **" + (animated ? "Yes" : "No") + "**"
                    + "\nUrl: **" + String.format(URL, m.group(2), animated ? "gif" : "png") + "**";
        }
        else
        {
            StringBuilder builder = new StringBuilder("\uD83D\uDD23 Emoji/Character Info:");
            str.codePoints().limit(15).forEachOrdered(code -> 
            {
                char[] chars = Character.toChars(code);
                String hex = Integer.toHexString(code).toUpperCase();
                while(hex.length()<4)
                    hex = "0"+hex;
                builder.append("\n`\\u").append(hex).append("`   ");
                if(chars.length>1)
                {
                    String hex0 = Integer.toHexString(chars[0]).toUpperCase();
                    String hex1 = Integer.toHexString(chars[1]).toUpperCase();
                    while(hex0.length()<4)
                        hex0 = "0"+hex0;
                    while(hex1.length()<4)
                        hex1 = "0"+hex1;
                    builder.append("[`\\u").append(hex0).append("\\u").append(hex1).append("`]   ");
                }
                builder.append(String.valueOf(chars)).append("   _").append(Character.getName(code)).append("_");
            });
            res = builder.toString();
        }
        return new CommandResponse(false, true, res);*/
        return null;
    }

    @Override
    public ApplicationCommand getApplicationCommand()
    {
        return null;
    }
}
