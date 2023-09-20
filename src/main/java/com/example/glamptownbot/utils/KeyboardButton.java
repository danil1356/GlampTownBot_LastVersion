package com.example.glamptownbot.utils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyboardButton {
    private String text;
    private String callbackData;
}
