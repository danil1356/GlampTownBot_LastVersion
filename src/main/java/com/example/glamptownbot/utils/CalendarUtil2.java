package com.example.glamptownbot.utils;

import java.text.SimpleDateFormat;
import org.joda.time.LocalDate;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class CalendarUtil2 {
    public static final String IGNORE = "ignore!@#$%^&";

    public static LocalDate NOW = LocalDate.now();

    public static final String[] WD = {"Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};

    public List<List<InlineKeyboardButton>> generateKeyboard(LocalDate date) {
        //System.out.println(NOW);
        if (date == null) {
            return null;
        }
        if (date.getDayOfMonth()>NOW.getDayOfMonth()){
            NOW = NOW.plusDays(date.getDayOfMonth()-NOW.getDayOfMonth());
        }
        else {
            NOW = LocalDate.now();
        }

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        // row - Month and Year
        List<InlineKeyboardButton> headerRow = new ArrayList<>();
        headerRow.add(createButton(IGNORE, new SimpleDateFormat("MMM yyyy").format(date.toDate())));
        keyboard.add(headerRow);

        // row - Days of the week
        List<InlineKeyboardButton> daysOfWeekRow = new ArrayList<>();
        for (String day : WD) {
            daysOfWeekRow.add(createButton(IGNORE, day));
        }
        keyboard.add(daysOfWeekRow);

        LocalDate firstDay = date.dayOfMonth().withMinimumValue();

        int shift = firstDay.dayOfWeek().get() - 1;
        int daysInMonth = firstDay.dayOfMonth().getMaximumValue();
        int rows = ((daysInMonth + shift) % 7 > 0 ? 1 : 0) + (daysInMonth + shift) / 7;
        for (int i = 0; i < rows; i++) {
            keyboard.add(buildRow(firstDay, shift, date.getMonthOfYear()));
            firstDay = firstDay.plusDays(7 - shift);
            shift = 0;
        }

        List<InlineKeyboardButton> controlsRow = new ArrayList<>();
        controlsRow.add(createButton("<", "<"));
        controlsRow.add(createButton(">", ">"));
        keyboard.add(controlsRow);
        return keyboard;
    }

    private InlineKeyboardButton createButton(String callBack, String text) {
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setCallbackData(callBack);
        keyboardButton.setText(text);
        return keyboardButton;
    }

    private List<InlineKeyboardButton> buildRow(LocalDate date, int shift, int month) {


        List<InlineKeyboardButton> row = new ArrayList<>();
        int day = date.getDayOfMonth();
        LocalDate callbackDate = date;
        for (int j = 0; j < shift; j++) {
            row.add(createButton(IGNORE, " "));
        }
        for (int j = shift; j < 7; j++) {
            if (day <= (date.dayOfMonth().getMaximumValue())) {
                if (NOW.getDayOfMonth()<=callbackDate.getDayOfMonth() && NOW.getMonthOfYear()==callbackDate.getMonthOfYear()){
                    row.add(createButton("DATE_BUTTON_"+callbackDate.toString(), Integer.toString(day++)));
                    callbackDate = callbackDate.plusDays(1);
                }
                if(NOW.getMonthOfYear()<callbackDate.getMonthOfYear() && callbackDate.getMonthOfYear() == month || NOW.getYear()<callbackDate.getYear()){
                    row.add(createButton("DATE_BUTTON_"+callbackDate.toString(), Integer.toString(day++)));
                    callbackDate = callbackDate.plusDays(1);
                }
                if (NOW.getDayOfMonth()>callbackDate.getDayOfMonth() && NOW.getMonthOfYear()>=callbackDate.getMonthOfYear() && NOW.getYear()>=callbackDate.getYear())  {
                    row.add(createButton(IGNORE, " "));
                    day++;
                    callbackDate = callbackDate.plusDays(1);
                }

            } else {
                row.add(createButton(IGNORE, " "));
            }
        }
        return row;
    }


//    private List<InlineKeyboardButton> buildRow(LocalDate date, int shift) {
//        List<InlineKeyboardButton> row = new ArrayList<>();
//        int day = date.getDayOfMonth();
//        LocalDate callbackDate = date;
//        for (int j = 0; j < shift; j++) {
//            row.add(createButton(IGNORE, " "));
//        }
//        for (int j = shift; j < 7; j++) {
//            if (day <= (date.dayOfMonth().getMaximumValue())) {
//                row.add(createButton("DATE_BUTTON_"+callbackDate.toString(), Integer.toString(day++)));
//                callbackDate = callbackDate.plusDays(1);
//            } else {
//                row.add(createButton(IGNORE, " "));
//            }
//        }
//        return row;
//    }
}
