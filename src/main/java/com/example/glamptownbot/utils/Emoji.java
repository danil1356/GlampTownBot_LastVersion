package com.example.glamptownbot.utils;

import com.vdurmont.emoji.EmojiParser;

public enum Emoji {
    CHECK(":white_check_mark:"),
    FLAG(":checkered_flag:");

    private String value;

    Emoji(String value) {
        this.value = value;
    }

    public String get(){
        return EmojiParser.parseToUnicode(value);
    }
}
