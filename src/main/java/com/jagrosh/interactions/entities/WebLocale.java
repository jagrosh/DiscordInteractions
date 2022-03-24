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
public enum WebLocale
{
    UNKNOWN("", "Unknown Locale"),
    ENGLISH_US("en-US", "English (United States)"),
    ENGLISH_UK("en-GB", "English (Great Britain)"),
    BULGARIAN("bg", "Bulgarian"),
    CHINESE_CHINA("zh-CN", "Chinese (China)"),
    CHINESE_TAIWAN("zh-TW", "Chinese (Taiwan)"),
    CROATION("hr", "Croatian"),
    CZECH("cs", "Czech"),
    DANISH("da", "Danish"),
    DUTCH("nl", "Dutch"),
    FINNISH("fi", "Finnish"),
    FRENCH("fr", "French"),
    GERMAN("de", "German"),
    GREEK("el", "Greek"),
    HINDI("hi", "Hindi"),
    HUNGARIAN("hu", "Hungarian"),
    ITALIAN("it", "Italian"),
    JAPANESE("ja", "Japanese"),
    KOREAN("ko", "Korean"),
    LITHUANIAN("lt", "Lithuanian"),
    NORWEGIAN("no", "Norwegian"),
    POLISH("pl", "Polish"),
    PORTUGUESE("pt-BR", "Portuguese (Brazil)"),
    ROMANIAN("ro", "Romanian"),
    RUSSIAN("ru", "Russian"),
    SPANISH("es-ES", "Spanish (Spain)"),
    SWEDISH("sv-SE", "Swedish"),
    THAI("th", "Thai"),
    TURKISH("tr", "Turkish"),
    UKRAINIAN("uk", "Ukrainian"),
    VIETNAMESE("vi", "Vietnamese");

    private final String code, text;

    private WebLocale(String code, String text)
    {
        this.code = code;
        this.text = text;
    }

    public String getTextualName()
    {
        return text;
    }
    
    public String getCode()
    {
        return code;
    }

    public static WebLocale of(String code)
    {
        if(code == null)
            return UNKNOWN;
        for(WebLocale lo: values())
            if(lo.code.equals(code))
                return lo;
        return UNKNOWN;
    }
}
