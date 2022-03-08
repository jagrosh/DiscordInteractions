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

/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class RandCmd implements Command
{
    //private final boolean silent;
    //private final String[] choices;
    
    @Override
    public InteractionResponse execute(Interaction interaction)
    {
        return null;
    }
    
    @Override
    public ApplicationCommand getApplicationCommand()
    {
        return null;
    }

}
