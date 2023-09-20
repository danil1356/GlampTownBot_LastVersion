package com.example.glamptownbot.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarUtil {
    public SendMessage Calendar(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("На какой день вы хотите сделать бронирование?");


        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        //keyboardMarkup.setResizeKeyboard(true); // Разрешаем автоматическое изменение размера клавиатуры
        //List<KeyboardRow> keyboardRows = new ArrayList<>();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        //KeyboardRow row = new KeyboardRow();

        LocalDate currentDate = LocalDate.now();
        int daysInMonth = currentDate.lengthOfMonth();
        int currentDay = currentDate.getDayOfMonth();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        System.out.println(dayOfWeek);

        var button1 = new InlineKeyboardButton();
        button1.setText(String.valueOf(currentYear));
        var button2 = new InlineKeyboardButton();
        button2.setText(String.valueOf(currentMonth));



        for (int day = 1; day <= daysInMonth; day++) {
            if (day < currentDay) {
                // Добавляем пустую кнопку для прошедших дней
                var button = new InlineKeyboardButton();
                button.setText(" ");
                button.setCallbackData(String.valueOf(day));
                row.add(button);
            } else {
                // Создаем кнопку с датой в формате "день.месяц.год"
                //String date = String.format("%02d.%02d.%04d", day, currentMonth, currentYear);
                var button = new InlineKeyboardButton();
                button.setText(String.format("%02d", day));
                button.setCallbackData(String.valueOf(day));
                //String date = String.format("%02d", day);
                row.add(button);
            }

            // Добавляем кнопки каждый 7 день для отображения в несколько строк
            if (day % 7 == 0) {
                keyboardRows.add(row);
                //row = new KeyboardRow();
                row = new ArrayList<>();
            }
        }

        // Добавляем оставшиеся кнопки
        if (!row.isEmpty()) {
            keyboardRows.add(row);
        }

        // Устанавливаем созданные кнопки на клавиатуру
        keyboardMarkup.setKeyboard(keyboardRows);

        // Устанавливаем клавиатуру на сообщение
        message.setReplyMarkup(keyboardMarkup);

        // Отправляем сообщение с клавиатурой
        return message;
    }
}
