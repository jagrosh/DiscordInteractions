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
    UNKNOWN(       "",      "Unknown Locale"),
    ENGLISH_US(    "en-US", "English (United States)"),
    ENGLISH_UK(    "en-GB", "English (Great Britain)"),
    BULGARIAN(     "bg",    "Bulgarian",                "български"),
    CHINESE_CHINA( "zh-CN", "Chinese (China)",          "中文"),
    CHINESE_TAIWAN("zh-TW", "Chinese (Taiwan)",         "繁體中文"),
    CROATION(      "hr",    "Croatian",                 "Hrvatski"),
    CZECH(         "cs",    "Czech",                    "Čeština"),
    DANISH(        "da",    "Danish",                   "Dansk"),
    DUTCH(         "nl",    "Dutch",                    "Nederlands"),
    FINNISH(       "fi",    "Finnish",                  "Suomi"),
    FRENCH(        "fr",    "French",                   "Français"),
    GERMAN(        "de",    "German",                   "Deutsch"),
    GREEK(         "el",    "Greek",                    "Ελληνικά"),
    HINDI(         "hi",    "Hindi",                    "हिन्दी"),
    HUNGARIAN(     "hu",    "Hungarian",                "Magyar"),
    ITALIAN(       "it",    "Italian",                  "Italiano"),
    JAPANESE(      "ja",    "Japanese",                 "日本語"),
    KOREAN(        "ko",    "Korean",                   "한국어"),
    LITHUANIAN(    "lt",    "Lithuanian",               "Lietuviškai"),
    NORWEGIAN(     "no",    "Norwegian",                "Norsk"),
    POLISH(        "pl",    "Polish",                   "Polski"),
    PORTUGUESE(    "pt-BR", "Portuguese (Brazil)",      "Português do Brasil"),
    ROMANIAN(      "ro",    "Romanian",                 "Română"),
    RUSSIAN(       "ru",    "Russian",                  "Pусский"),
    SPANISH(       "es-ES", "Spanish (Spain)",          "Español"),
    SWEDISH(       "sv-SE", "Swedish",                  "Svenska"),
    THAI(          "th",    "Thai",                     "ไทย"),
    TURKISH(       "tr",    "Turkish",                  "Türkçe"),
    UKRAINIAN(     "uk",    "Ukrainian",                "Українська"),
    VIETNAMESE(    "vi",    "Vietnamese",               "Tiếng Việt");

    private final String code, text, nativ;

    private WebLocale(String code, String text)
    {
        this(code, text, text);
    }
    
    private WebLocale(String code, String text, String nativ)
    {
        this.code = code;
        this.text = text;
        this.nativ = nativ;
    }

    public String getTextualName()
    {
        return text;
    }
    
    public String getNativeName()
    {
        return nativ;
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
