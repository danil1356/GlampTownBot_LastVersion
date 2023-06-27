package com.example.glamptownbot.service;

import com.example.glamptownbot.config.BotConfig;
import com.example.glamptownbot.data.Entity.Users;
import com.example.glamptownbot.service.EntityService.BaseService;
import com.example.glamptownbot.service.EntityService.UsersServiceImpl;
import com.example.glamptownbot.utils.Emoji;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    static final String START = "start command";
    static final String IMG1 = new String("src/main/resources/static/images/img.png");
    final BotConfig botConfig;

    private final BaseService<Users> usersBaseService;

    @Autowired
    public TelegramBot(BotConfig botConfig, BaseService<Users> usersBaseService) {
        this.usersBaseService = usersBaseService;

        this.botConfig = botConfig;
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start", "начать"));
        botCommandList.add(new BotCommand("/help", "помощь"));
        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e){

        }

    }


    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        //наличие текста(комад)
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                case "/start":
                    startCommand(chatId);
                    break;

                case "/help" :


                    // TODO: 24.06.2023 default
                default: sendMessage(chatId, "default");
            }
        }
        //наличие кнопок
        else if (update.hasCallbackQuery()){
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals("BUTTON_1")){
                sendBookARoom(chatId, "Номера");
            }
            else if (callbackData.equals("BUTTON_2")) {
                sendAboutGlamping(chatId,"о глэмпинге", IMG1);
            }
        }
    }


    //команды
    @SneakyThrows
    private void startCommand(long chatId){
        String answer = START;
        sendStartMessage(chatId,answer);

        this.usersBaseService.add(new Users(null, String.valueOf(chatId),null,null,null,null));
    }
    private void helpCommand(){

    }


    //сообщения
    // TODO: 26.06.2023 рефакторинг на обобщение сообщений
    private SendMessage sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        return message;
    }

    private void sendStartMessage(long chatId, String textToSend){
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(new File("src/main/resources/static/images/img.png")));
        sendPhoto.setCaption(textToSend);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();

        String[][] text = {{"ЗАБРОНИРОВАТЬ НОМЕР","О ГЛЭМПИНГЕ"},{"РАЗВЛЕЧЕНИЯ","АРЕНДА БАНИ"},{"АРЕНДА ОБОРУДОВАНИЯ","КАК ДОБРАТЬСЯ"},{"ПАРОЛЬ ОТ WIFI","ОРГАНИЗАЦИЯ ЗАВТРАКА"},{"ПОЗВОНИТЬ"}};
        String[][] callBack = {{"BUTTON_1","BUTTON_2"},{"BUTTON_3","BUTTON_4"},{"BUTTON_5","BUTTON_6"},{"BUTTON_7","BUTTON_8"},{"BUTTON_9"}};

        for (int i = 0; i <text.length; i++) {
            List<InlineKeyboardButton> rowLine = new ArrayList<>();
            for (int j = 0; j <text[i].length; j++) {
                var button = new InlineKeyboardButton();
                button.setText(String.valueOf(Emoji.CHECK.get())+" "+text[i][j]);
                button.setCallbackData(callBack[i][j]);
                rowLine.add(button);
            }
            rowsLine.add(rowLine);
        }

        markup.setKeyboard(rowsLine);
        sendPhoto.setReplyMarkup(markup);

        try {
            execute(sendPhoto);
        }catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }

    public void sendAboutGlamping(long chatId, String imageCaption, String imagePath) throws FileNotFoundException, TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(new File(imagePath)));
        sendPhoto.setCaption(imageCaption);


        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        var button = new InlineKeyboardButton();
        button.setText(String.valueOf(Emoji.CHECK.get())+" ЗАБРОНИРОВАТЬ НОМЕР");
        button.setCallbackData("BUTTON_1");
        rowLine.add(button);
        rowsLine.add(rowLine);
        markup.setKeyboard(rowsLine);

        sendPhoto.setReplyMarkup(markup);


        try {
            execute(sendPhoto);
        }catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }

    public void sendBookARoom(long chatId, String text){
        SendMessage message = sendMessage(chatId,text);

        // TODO: 26.06.2023 рефакторинг создания n кнопок
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();

        // TODO: 26.06.2023 возможно брать из бд
        String[] names = {"НОМЕР 1","НОМЕР 2","НОМЕР 3","НОМЕР 4","НОМЕР 5","НОМЕР 6"};
        String[] callBack = {"BUTTON_ROOM_1","BUTTON_ROOM_2","BUTTON_ROOM_3","BUTTON_ROOM_4","BUTTON_ROOM_5","BUTTON_ROOM_6"};

        for (int i = 0; i <names.length; i++) {
            List<InlineKeyboardButton> rowLine = new ArrayList<>();
            var button = new InlineKeyboardButton();
            button.setText(names[i]);
            button.setCallbackData(callBack[i]);

            rowLine.add(button);
            rowsLine.add(rowLine);
        }
        markup.setKeyboard(rowsLine);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
