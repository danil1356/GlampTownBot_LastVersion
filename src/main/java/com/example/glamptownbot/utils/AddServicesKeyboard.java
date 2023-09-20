package com.example.glamptownbot.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class AddServicesKeyboard {

    public InlineKeyboardMarkup addServicesInlineKeyboardMarkup(){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();

        List<InlineKeyboardButton> rowLine1 = new ArrayList<>();
        rowLine1.add(createButton("SERVICES_BUTTON_1","Велосипед"));
        rowLine1.add(createButton("SERVICES_BUTTON_2","Скейтборд"));
        rowsLine.add(rowLine1);
        markup.setKeyboard(rowsLine);

        List<InlineKeyboardButton> rowLine2 = new ArrayList<>();
        rowLine2.add(createButton("SERVICES_BUTTON_3","Бадминтон"));
        rowLine2.add(createButton("SERVICES_BUTTON_4","Угли и розжиг"));
        rowsLine.add(rowLine2);
        markup.setKeyboard(rowsLine);

        List<InlineKeyboardButton> rowLine3 = new ArrayList<>();
        rowLine3.add(createButton("SERVICES_BUTTON_5","Классический массаж"));
        rowLine3.add(createButton("SERVICES_BUTTON_6","Практика гвоздестояния"));
        rowsLine.add(rowLine3);
        markup.setKeyboard(rowsLine);

        List<InlineKeyboardButton> rowLine4 = new ArrayList<>();
        rowLine4.add(createButton("SERVICES_BUTTON_7","Организация завтрака"));
        rowLine4.add(createButton("SERVICES_BUTTON_8","Аренда бани"));
        rowsLine.add(rowLine4);
        markup.setKeyboard(rowsLine);

        return markup;
    }

    private InlineKeyboardButton createButton(String callBack, String text) {
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setCallbackData(callBack);
        keyboardButton.setText(text);
        return keyboardButton;
    }
}
