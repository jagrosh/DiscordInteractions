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
package com.jagrosh.interactions.util;

//import net.dv8tion.jda.api.entities.User;

import java.util.BitSet;


/**
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class OtherUtil
{
    public static int getBitsetValue(BitSet bits)
    {
        if(bits == null)
            return 0;
        long[] arr = bits.toLongArray();
        if(arr.length == 0)
            return 0;
        return (int) arr[0];
    }
    
    public static String random(String... choices)
    {
        return choices[(int)(Math.random() * choices.length)];
    }
    
    public static String limitLength(String str, int max)
    {
        return str == null ? null : str.length() <= max ? str : str.substring(0, max);
    }
}
