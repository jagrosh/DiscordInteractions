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
import java.time.format.DateTimeFormatter;
//import net.dv8tion.jda.api.JDA;
//import net.dv8tion.jda.api.entities.User;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class UserinfoCmd implements Command
{
    private final static String BOT_EMOJI = "<:botTag:230105988211015680>";
    private final static String USER_EMOJI = "\uD83D\uDC64"; // 👤
    private final static String LINESTART = "\u25AB"; // ▫
    
    @Override
    public InteractionResponse execute(Interaction interaction)
    {
        /*ApplicationCommandInteractionData.ApplicationCommandInteractionDataOption option = interaction.getData().getOptionByName("user");
        long userId = option == null ? interaction.getMember().getIdLong() : Long.parseLong(option.getStringValue().replaceAll("[^0-9]", ""));
        User user = jda.retrieveUserById(userId).complete();
        String title = (user.isBot() ? BOT_EMOJI : USER_EMOJI)+" Information about **"+user.getName()+"** #"+user.getDiscriminator()+":";
        StringBuilder str = new StringBuilder(LINESTART + "Discord ID: **" + user.getId() + "**\n" + LINESTART + "Badges:");
        user.getFlags().forEach(flag -> str.append(" ").append(OtherUtil.getEmoji(flag)));
        String avyId = user.getAvatarId();
        if(avyId != null && avyId.startsWith("a_"))
            str.append(" ").append(OtherUtil.NITRO);
        str.append("\n" + LINESTART + "Account Creation: **").append(user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME)).append("**");
        return new CommandResponse(title, new WebhookEmbedBuilder().setDescription(str.toString()).setThumbnailUrl(user.getEffectiveAvatarUrl()).build());*/
        return null;
    }

    @Override
    public ApplicationCommand getApplicationCommand()
    {
        return null;
    }
}
