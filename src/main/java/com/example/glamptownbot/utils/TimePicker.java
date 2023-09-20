package com.example.glamptownbot.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class TimePicker {

    public List<List<InlineKeyboardButton>> generatedTimePicker(int hour, String selectButtonName){

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();
        List<InlineKeyboardButton> rowLine2 = new ArrayList<>();
        var button1 = new InlineKeyboardButton("[-]");
        button1.setCallbackData("-");

        var button2 = new InlineKeyboardButton(String.valueOf(hour));
        button2.setCallbackData("timeH"+hour);

        var button3 = new InlineKeyboardButton("[+]");
        button3.setCallbackData("+");

        rowLine.add(button1);
        rowLine.add(button2);
        rowLine.add(button3);

        var button4 = new InlineKeyboardButton("Выбрать");
        button4.setCallbackData(selectButtonName);
        rowLine2.add(button4);

        keyboard.add(rowLine);
        keyboard.add(rowLine2);
        return keyboard;
    }
}
