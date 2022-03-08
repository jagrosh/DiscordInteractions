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
package com.jagrosh.interactions.receive;

import com.jagrosh.interactions.command.ApplicationCommand;
import com.jagrosh.interactions.interfaces.ISnowflake;
import com.jagrosh.interactions.util.JsonUtil;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class CommandInteractionData implements ISnowflake
{
    private final long id;
    private final String name;
    private final ApplicationCommand.Type type;
    private final ResolvedData resolved;
    private final List<CommandInteractionDataOption> options;
    private final Long targetId;
    
    public CommandInteractionData(JSONObject json)
    {
        this.id = json.getLong("id");
        this.name = json.getString("name");
        this.type = ApplicationCommand.Type.of(json.getInt("type"));
        this.resolved = json.has("resolved") ? new ResolvedData(json.getJSONObject("resolved")) : null;
        this.options = JsonUtil.optArray(json, "options", CommandInteractionDataOption::new);
        this.targetId = json.optLong("target_id");
    }

    @Override
    public long getIdLong()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
