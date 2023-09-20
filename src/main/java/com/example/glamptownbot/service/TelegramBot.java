package com.example.glamptownbot.service;

import com.example.glamptownbot.config.BotConfig;
import com.example.glamptownbot.data.Entity.Breakfast;
import com.example.glamptownbot.data.Entity.ImgData;
import com.example.glamptownbot.data.Entity.Rooms;
import com.example.glamptownbot.service.EntityService.*;
import com.example.glamptownbot.service.EntityService.Impl.RoomsServiceImpl;
import com.example.glamptownbot.service.EntityService.Impl.UsersServiceImpl;
import com.example.glamptownbot.utils.*;
import lombok.SneakyThrows;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    static int MONTH = LocalDate.now().getMonthOfYear();
    static HashMap<String, String> USER_SELECTED = new HashMap<>();
    static String ANIMALS_COUNT = "ANIMALS_COUNT_";
    static String SELECTED_DATE = "SELECTED_DATE_";
    static String SELECTED_HOUR = "SELECTED_HOUR_";
    //выбранное время начала n-ой услуги
    static final String BATH_TIME = "BATH_TIME_";
    static final String MASSAGE_TIME = "MASSAGE_TIME_";
    static final String NAIL_STANDING_TIME = "NAIL_STANDING_TIME_";
    //выбранные часы для n-ой услуги
    static String SELECT_BICYCLE_HOUR_COUNT = "BICYCLE_HOUR_COUNT";
    static String SELECT_SKATEBOARD_HOUR_COUNT = "SKATEBOARD_HOUR_COUNT";
    static String SELECT_BADMINTON_HOUR_COUNT = "BADMINTON_HOUR_COUNT";
    static String SELECT_MASSAGE_HOUR_COUNT = "MASSAGE_HOUR_COUNT";
    static String SELECT_BATH_HOUR_COUNT = "SELECT_BATH_HOUR_COUNT";
    //
    static String TIME_TYPE = "TIME";
    static int EXTRA_PEOPLE_FOR_BIG_ROOM = 0;
    static LocalTime END_BREAKFAST = LocalTime.parse("09:45:0.000");
    static final String CALLBACK_ROOM = "BUTTON_ROOM";
    static final String CALLBACK_GET_THERE = "BUTTON_GET_THERE";
    static final String START = "Glamptown — это экоотдых в мини-домиках с большими окнами, посреди хвойного леса. Рядом с глэмпингом располагается озеро Комсомольское, до которого можно пройти пешком, а в нескольких метрах — небольшая река Нокса. Атмосфера места позволит гостям ощутить уединение и спокойствие, не уезжая далеко за черту города Казань.";
    static final String WIFI_PASSWORD = "Пароль от нашего Wi-Fi: \n" +
            " \n" +
            "`dsFWeqw`\n" +
            "\n" +
            "Приятного пользования!";
    static final String GET_THERE = "Если вы выбираете собственный транспорт, вам потребуется следовать по указаниям до адреса.  Наш адрес: 594, Казань, садоводческое некоммерческое товарищество Медик. Прибыв на место, вы сможете оставить свой автомобиль на нашей парковке и наслаждаться прекрасным отдыхом в глэмпинге.\n" +
            "\n" +
            "Если же вы предпочитаете использовать общественный транспорт, то вам следует добраться до остановки «Берёзовая роща». Оттуда вы сможете прогуляться пешком до нашего глэмпинга. Не забудьте взять с собой карту или использовать навигационное приложение на своём телефоне, чтобы быть уверенным в правильном маршруте.\n" +
            "\n" +
            "Независимо от того, каким транспортом вы выберете добраться до нас, мы уверены, что ваше прибытие в наш глэмпинг будет незабываемым и наполненным приключениями!";
    static final String BOOK_A_ROOM = "Вы можете забронировать номер через нашего бота, выбрать удобные даты для Вас даты, а также заранее зарезервировать баню, заказать завтрак и ознакомиться со списком развлечений, которые у нас есть.";
    static final String PHONE_NUMBER = "79991632207";
    static final String IMG1 = new String("src/main/resources/static/images/img2.png");
    final BotConfig botConfig;
    private final UsersService usersService;
    private final RoomsService roomsService;
    private final ImgDataService imgDataService;
    private final BreakfastService breakfastService;
    private final AddServiceService addServiceService;
    private final ReplyKeyboardMaker replyKeyboardMaker;

    @Autowired
    public TelegramBot(BotConfig botConfig, UsersServiceImpl usersBaseService, RoomsServiceImpl roomsBaseService, ReplyKeyboardMaker replyKeyboardMaker,ImgDataService imgDataService, BreakfastService breakfastService, AddServiceService addServiceService) {
        this.usersService = usersBaseService;
        this.roomsService = roomsBaseService;
        this.imgDataService = imgDataService;
        this.breakfastService = breakfastService;
        this.addServiceService = addServiceService;

        this.replyKeyboardMaker = replyKeyboardMaker;
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
                    startCommand(chatId, START);
                    break;

                case "О глэмпинге":
                    aboutGlampingCommand(chatId,IMG1,addServiceService.findByName("О глэмпинге").getDescription());
                    break;

                case "Пароль от WI-FI":
                    wifiPassword(chatId,IMG1, WIFI_PASSWORD);
                    break;

                case "Забронировать номер":
                    bookARoom(chatId, BOOK_A_ROOM, IMG1);
                    break;

                case "Как добраться":
                    getThere(chatId,GET_THERE);
                    break;

                case "Позвонить":
                    betterCallGlamp(chatId, PHONE_NUMBER);
                    break;


                case "Развлечения и доп. услуги":
                    addServices(chatId);
                    break;

                default: sendMessage(chatId, "default");
            }
        }
        //наличие кнопок
        else if (update.hasCallbackQuery()){
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals(CALLBACK_ROOM)){
                sendBookARoom(chatId, IMG1, BOOK_A_ROOM);
            }
            else if (callbackData.contains("BUTTON_ROOM_")){
                sendRoomDesc(chatId, Long.parseLong(callbackData.replace("BUTTON_ROOM_", "")));
            }
            else if (callbackData.contains("BOOK_A_ROOM_")){
                USER_SELECTED.put(String.valueOf(chatId),callbackData);
                sendCalendar(chatId, "На какой день вы хотите сделать бронирование?", LocalDate.now());
            }

            else if (callbackData.equals(">")){
                sendRightCalendarCommand(chatId, messageId);
            }
            else if (callbackData.equals("<")){
                sendLeftCalendarCommand(chatId, messageId);
            }
            else if (callbackData.contains("DATE_BUTTON_")){
                String strDate = callbackData.replace("DATE_BUTTON_", "");
                LocalDate selectedDate = LocalDate.parse(strDate);
                USER_SELECTED.put(SELECTED_DATE+chatId, String.valueOf(selectedDate));

                LocalDate date;


                EditMessageText message = new EditMessageText();
                message.setText("На какой день вы хотите сделать бронирование?!");
                message.setChatId(chatId);
                message.setMessageId((int) messageId);

                var button = new InlineKeyboardButton();
                button.setText(strDate);
                if (USER_SELECTED.containsKey(String.valueOf(chatId)) && USER_SELECTED.get(String.valueOf(chatId)).equals("BOOK_A_ROOM_5")) {
                    date = LocalDate.now();
                    button.setCallbackData("SELECT_DATE_BIG_HOME");
                }
                else if (USER_SELECTED.containsKey(String.valueOf(chatId)) && USER_SELECTED.get(String.valueOf(chatId)).contains("BUTTON_BREAKFAST_")){
                    date = checkBreakFastTime();
                    button.setCallbackData("SELECT_BREAKFAST");
                }
                else if (USER_SELECTED.containsKey(String.valueOf(chatId)) && USER_SELECTED.get(String.valueOf(chatId)).contains("BOOK_A_ROOM_") && !USER_SELECTED.get(String.valueOf(chatId)).equals("BOOK_A_ROOM_5")) {
                    date = LocalDate.parse(callbackData.replace("DATE_BUTTON_",""));
                    button.setCallbackData("SELECT_DATE");
                }
                else if (USER_SELECTED.containsKey(String.valueOf(chatId)) && USER_SELECTED.get(String.valueOf(chatId)).equals("BUTTON_BOOK_A_BATHHOUSE")) {
                    date = LocalDate.now();
                    button.setCallbackData("SELECT_DATE_BOOK_A_BATHHOUSE");
                }
                else if (USER_SELECTED.containsKey(String.valueOf(chatId)) && USER_SELECTED.get(String.valueOf(chatId)).equals("BUTTON_BICYCLE_RENT")) {
                    date = LocalDate.now();
                    button.setCallbackData("SELECT_DATE_RENT_BICYCLE");
                }
                else if (USER_SELECTED.containsKey(String.valueOf(chatId)) && USER_SELECTED.get(String.valueOf(chatId)).equals("BUTTON_SKATEBOARD_RENT")) {
                    date = LocalDate.now();
                    button.setCallbackData("SELECT_DATE_RENT_SKATEBOARD");
                }
                else if (USER_SELECTED.containsKey(String.valueOf(chatId)) && USER_SELECTED.get(String.valueOf(chatId)).equals("BUTTON_BADMINTON_RENT")) {
                    date = LocalDate.now();
                    button.setCallbackData("SELECT_DATE_RENT_BADMINTON");
                }
                else if (USER_SELECTED.containsKey(String.valueOf(chatId)) && USER_SELECTED.get(String.valueOf(chatId)).equals("BUTTON_MASSAGE_RENT")) {
                    date = LocalDate.now();
                    button.setCallbackData("SELECT_DATE_RENT_MASSAGE");
                }
                else if (USER_SELECTED.containsKey(String.valueOf(chatId)) && USER_SELECTED.get(String.valueOf(chatId)).equals("BUTTON_BOOK_A_NAIL_STANDING")) {
                    date = LocalDate.now();
                    button.setCallbackData("SELECT_DATE_RENT_NAIL_STANDING");
                }
                else {date = LocalDate.now();}

                CalendarUtil2 calendarUtil =new CalendarUtil2();
                InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsLine = calendarUtil.generateKeyboard(date);
                List<InlineKeyboardButton> rowLine = new ArrayList<>();



                rowLine.add(button);
                rowsLine.add(rowLine);

                keyboardMarkup.setKeyboard(rowsLine);
                message.setReplyMarkup(keyboardMarkup);
                execute(message);
            }
            else if (callbackData.equals("SELECT_DATE_BIG_HOME")){
                EditMessageText message = new EditMessageText();
                message.setText("Спасибо за ответ, я записал:) \nК сожалению, дом рассчитан на 8 персон. Если вас больше 6 человек, то происходит доплата за 7 и 8 друга в размере 1000 ₽\n Пожалуйста укажите количество человек в вашей компании");
                message.setChatId(chatId);
                message.setMessageId((int) messageId);

                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
                List<InlineKeyboardButton> rowLine = new ArrayList<>();
                rowLine.add(createButton("EXTRA_PEOPLE_BUTTON_0","6 и менее"));
                rowLine.add(createButton("EXTRA_PEOPLE_BUTTON_1","7"));
                rowLine.add(createButton("EXTRA_PEOPLE_BUTTON_2","8"));
                rowsLine.add(rowLine);
                markup.setKeyboard(rowsLine);
                message.setReplyMarkup(markup);

                execute(message);
            }

            else if (callbackData.equals("SELECT_DATE") || callbackData.contains("EXTRA_PEOPLE_BUTTON_")){
                EXTRA_PEOPLE_FOR_BIG_ROOM = (callbackData.contains("EXTRA_PEOPLE_BUTTON_")) ? Integer.parseInt(callbackData.replace("EXTRA_PEOPLE_BUTTON_","")):0;

                EditMessageText message = new EditMessageText();
                message.setText("Спасибо за ответ, я записал:)\nБудут ли с вами четвероногие друзья? \n" +
                        "За каждого из них с вас будет списана доплата в размере 1000 ₽\n" +
                        "\n" +
                        "Пожалуйста укажите их количество");
                message.setChatId(chatId);
                message.setMessageId((int) messageId);

                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
                List<InlineKeyboardButton> rowLine = new ArrayList<>();
                rowLine.add(createButton("ANIMALS_BUTTON_0","Без животных"));
                rowLine.add(createButton("ANIMALS_BUTTON_1`","1"));
                rowLine.add(createButton("ANIMALS_BUTTON_2","2"));
                rowLine.add(createButton("ANIMALS_BUTTON_3","3"));
                rowsLine.add(rowLine);
                markup.setKeyboard(rowsLine);
                message.setReplyMarkup(markup);

                execute(message);
            }
            else if (callbackData.contains("ANIMALS_BUTTON_")){
                USER_SELECTED.put(ANIMALS_COUNT+String.valueOf(chatId),callbackData.replace("ANIMALS_BUTTON_", ""));
                System.out.println(USER_SELECTED.get("ANIMALS_COUNT_"+String.valueOf(chatId)));
                SendMessage message = new SendMessage();
                message.setText("Прекрасно, я запомнил! \nПожалуйста, оплатите бронирование");
                message.setChatId(chatId);

                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
                List<InlineKeyboardButton> rowLine = new ArrayList<>();

                var button = new InlineKeyboardButton();
                button.setText("Оплата");
                button.setCallbackData("PAY_BUTTON");
                button.setUrl("https://madd.band/");
                rowLine.add(button);
                rowsLine.add(rowLine);
                markup.setKeyboard(rowsLine);
                message.setReplyMarkup(markup);

                execute(message);
            }

            else if (callbackData.equals("SERVICES_BUTTON_8")){
                sendBathhouseDescription(chatId);
            }
            else if (callbackData.equals("SERVICES_BUTTON_6")){
                sendNailStandingDescription(chatId);
            }
            else if (callbackData.equals("SERVICES_BUTTON_1")){
                sendBicycleRent(chatId);
            }
            else if (callbackData.equals("SERVICES_BUTTON_2")){
                sendSkateboardRent(chatId);
            }
            else if (callbackData.equals("SERVICES_BUTTON_3")){
                sendBadmintonRent(chatId);
            }
            else if (callbackData.equals("SERVICES_BUTTON_5")){
                sendClassicalMassage(chatId);
            }
            else if (callbackData.equals("SERVICES_BUTTON_4")){
                sendCoals(chatId);
            }
            else if (callbackData.equals("SERVICES_BUTTON_7")){
                sendBreakfast(chatId);
            }
            else if (callbackData.equals("BUTTON_ORDER_BREAKFAST")){
                sendOrderBreakfast(chatId);
            }
            else if (callbackData.contains("BUTTON_BREAKFAST_")){
                USER_SELECTED.put(String.valueOf(chatId), callbackData);
                sendCalendar(chatId,"На какой день вы хотите заказать завтрак?", checkBreakFastTime());
            }
            else if (callbackData.equals("SELECT_BREAKFAST")){
                sendBreakfastBookTime(chatId);
            }
            else if (callbackData.equals("BUTTON_BOOK_A_BATHHOUSE")){
                USER_SELECTED.put(String.valueOf(chatId), callbackData);
                sendCalendar(chatId,"На какой день вы хотите забронировать баню?", LocalDate.now());
            }
            else if (callbackData.equals("SELECT_DATE_BOOK_A_BATHHOUSE")){
                sendSelectTime(chatId, BATH_TIME);
            }
            else if (callbackData.contains(BATH_TIME)){
                USER_SELECTED.put(SELECTED_HOUR+String.valueOf(chatId), "1");
                USER_SELECTED.put(TIME_TYPE+String.valueOf(chatId), SELECT_BATH_HOUR_COUNT);
                sendTimePicker(chatId,"Сколько часов?", 1, SELECT_BATH_HOUR_COUNT);
            }
            //ВЕЛОСИПЕД
            else if (callbackData.equals("BUTTON_BICYCLE_RENT") || callbackData.equals("BUTTON_BADMINTON_RENT") ||callbackData.equals("BUTTON_MASSAGE_RENT") || callbackData.equals("BUTTON_SKATEBOARD_RENT") || callbackData.equals("BUTTON_BOOK_A_NAIL_STANDING")){
                USER_SELECTED.put(String.valueOf(chatId), callbackData);
                sendCalendar(chatId,"На какой день вы хотите арендовать?", LocalDate.now());
            }
            else if (callbackData.equals("SELECT_DATE_RENT_BICYCLE")){
                //кол-во часов
                USER_SELECTED.put(SELECTED_HOUR+String.valueOf(chatId), "1");
                //где часы были выбранны для указания логики перехода далее (для многопользовательской системы)
                USER_SELECTED.put(TIME_TYPE+String.valueOf(chatId), SELECT_BICYCLE_HOUR_COUNT);
                sendTimePicker(chatId,"Сколько часов?", 1, SELECT_BICYCLE_HOUR_COUNT);
            }
            //общий
            else if (callbackData.equals("+") || callbackData.equals("-")){
                plusMinusTimePicker(callbackData, USER_SELECTED.get(SELECTED_HOUR+String.valueOf(chatId)), chatId, messageId,"Сколько часов?", USER_SELECTED.get(TIME_TYPE+String.valueOf(chatId)));
            }
            //
            else if (callbackData.equals(SELECT_BICYCLE_HOUR_COUNT)||callbackData.equals(SELECT_SKATEBOARD_HOUR_COUNT)||callbackData.equals(SELECT_BADMINTON_HOUR_COUNT) || callbackData.equals(SELECT_MASSAGE_HOUR_COUNT) || callbackData.contains(NAIL_STANDING_TIME) || callbackData.contains(SELECT_BATH_HOUR_COUNT)){
                sendPayRent(chatId, messageId);
            }

            //СКЕЙТБОРД
            //sendCalendar
            else if (callbackData.equals("SELECT_DATE_RENT_SKATEBOARD")){
                //кол-во часов
                USER_SELECTED.put(SELECTED_HOUR+String.valueOf(chatId), "1");
                //где часы были выбранны для указания логики перехода далее (для многопользовательской системы)
                USER_SELECTED.put(TIME_TYPE+String.valueOf(chatId), SELECT_SKATEBOARD_HOUR_COUNT);
                sendTimePicker(chatId,"Сколько часов?", 1, SELECT_SKATEBOARD_HOUR_COUNT);
            }
            //Бадминтон
            //sendCalendar
            else if (callbackData.equals("SELECT_DATE_RENT_BADMINTON")) {
                //кол-во часов
                USER_SELECTED.put(SELECTED_HOUR+String.valueOf(chatId), "1");
                //где часы были выбранны для указания логики перехода далее (для многопользовательской системы)
                USER_SELECTED.put(TIME_TYPE+String.valueOf(chatId), SELECT_BADMINTON_HOUR_COUNT);
                sendTimePicker(chatId,"Сколько часов?", 1, SELECT_BADMINTON_HOUR_COUNT);
            }

            //МАССАЖ
            //sendCalendar
            else if (callbackData.equals("SELECT_DATE_RENT_MASSAGE")) {
                sendSelectTime(chatId, MASSAGE_TIME);
            }
            else if (callbackData.contains(MASSAGE_TIME)){
                USER_SELECTED.put(SELECTED_HOUR+String.valueOf(chatId), "1");
                //где часы были выбранны для указания логики перехода далее (для многопользовательской системы)
                USER_SELECTED.put(TIME_TYPE+String.valueOf(chatId), SELECT_MASSAGE_HOUR_COUNT);
                sendTimePicker(chatId,"Сколько часов?", 1, SELECT_MASSAGE_HOUR_COUNT);
            }

            //А ГВОЗДИ ТАК ВОТ ОНИ
            else if (callbackData.equals("SELECT_DATE_RENT_NAIL_STANDING")) {
                sendSelectTime(chatId, NAIL_STANDING_TIME);
            }//потом оплата
        }
    }


    //команды
    @SneakyThrows
    private void sendRightCalendarCommand(long chatId, long messageId){
        MONTH+=1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, MONTH);

        EditMessageText message = new EditMessageText();
        message.setText("На какой день вы хотите сделать бронирование?!");
        message.setChatId(chatId);
        message.setMessageId((int) messageId);


        CalendarUtil2 calendarUtil =new CalendarUtil2();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(calendarUtil.generateKeyboard(LocalDate.fromDateFields(calendar.getTime())));
        message.setReplyMarkup(keyboardMarkup);

        execute(message);
    }

    @SneakyThrows
    private void sendLeftCalendarCommand(long chatId, long messageId){
        MONTH-=1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, MONTH);

        EditMessageText message = new EditMessageText();
        message.setText("На какой день вы хотите сделать бронирование?!");
        message.setChatId(chatId);
        message.setMessageId((int) messageId);

        CalendarUtil2 calendarUtil =new CalendarUtil2();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(calendarUtil.generateKeyboard(LocalDate.fromDateFields(calendar.getTime())));
        message.setReplyMarkup(keyboardMarkup);

        execute(message);
    }

    @SneakyThrows
    private void startCommand(long chatId,String textMsg){
        sendStartMessage(chatId, textMsg);
    }
    @SneakyThrows
    private void aboutGlampingCommand(long chatId, String imgPath, String textMsg) {
        sendAboutGlamping(chatId,textMsg,imgPath);
    }
    private void wifiPassword(long chatId, String imgPath, String textMsg){
        sendWifiPassword(chatId,textMsg,imgPath);
    }
    private void getThere(long chatId, String textMsg){
        sendGetThere(chatId,textMsg);
    }
    private void betterCallGlamp(Long chatId, String phoneNumber){
        sendPhoneNumber(chatId, phoneNumber);
    }
    private void bookARoom(long chatId, String imgPath, String textMsg){
        sendBookARoom(chatId, textMsg, imgPath);
    }
    private void addServices(long chatId){
        sendAddServices(chatId);
    }

    //сообщения
    private void sendStartMessage(long chatId, String textToSend) throws TelegramApiException {
        ImgData imgData = imgDataService.findByDescription("start command");
        SendPhoto sendPhoto = new SendPhoto(String.valueOf(chatId),new InputFile(imgData.getUrl()));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();
        rowLine.add(createButton(CALLBACK_ROOM,"Забронировать номер"));
        rowsLine.add(rowLine);
        markup.setKeyboard(rowsLine);
        sendPhoto.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());

        SendMessage sendMessage = new SendMessage(String.valueOf(chatId),textToSend);
        sendMessage.setReplyMarkup(markup);
        execute(sendPhoto);
        execute(sendMessage);
    }

    public void sendAboutGlamping(long chatId, String textToSend,String imagePath) throws FileNotFoundException, TelegramApiException {
        ImgData imgData = imgDataService.findByDescription("about glamping");
        SendPhoto sendPhoto = new SendPhoto(String.valueOf(chatId),new InputFile(imgData.getUrl())); //new InputFile(new File(imagePath))

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        rowLine.add(createButton(CALLBACK_ROOM,"Забронировать номер"));
        rowsLine.add(rowLine);
        markup.setKeyboard(rowsLine);

        SendMessage sendMessage = new SendMessage(String.valueOf(chatId),textToSend);
        sendMessage.setReplyMarkup(markup);
        execute(sendPhoto);
        execute(sendMessage);

    }


    @SneakyThrows
    public void sendBookARoom(long chatId, String imagePath, String text)  {
        USER_SELECTED.put(String.valueOf(chatId),"");
        ImgData imgData = imgDataService.findByDescription("book a room");
        SendPhoto sendPhoto = new SendPhoto(String.valueOf(chatId),new InputFile(imgData.getUrl()));
        SendMessage message = sendMessage(chatId,text);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<Rooms> rooms = this.roomsService.getAll();

        for (int i = 0; i <rooms.size(); i++) {
            List<InlineKeyboardButton> rowLine = new ArrayList<>();
            var button = new InlineKeyboardButton();
            button.setText(rooms.get(i).getName());
            button.setCallbackData("BUTTON_ROOM_"+rooms.get(i).getId());

            rowLine.add(button);
            rowsLine.add(rowLine);
        }
        markup.setKeyboard(rowsLine);
        message.setReplyMarkup(markup);

        execute(sendPhoto);
        execute(message);

    }

    @SneakyThrows
    public void sendWifiPassword(long chatId, String textMsg, String imgPath){
        ImgData imgData = imgDataService.findByDescription("password wifi");
        SendPhoto sendPhoto = new SendPhoto(String.valueOf(chatId),new InputFile(imgData.getUrl()));
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textMsg);
        sendMessage.enableMarkdown(true);
        execute(sendPhoto);
        execute(sendMessage);
    }

    @SneakyThrows
    public void sendGetThere(long chatId, String textMsg){
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId),textMsg);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();

        var button = new InlineKeyboardButton();
        button.setText("Проложить маршрут");
        button.setCallbackData(CALLBACK_GET_THERE);
        button.setUrl("https://yandex.ru/maps/43/kazan/?ll=49.203657%2C55.855789&mode=routes&rtext=~55.855754%2C49.203762&rtt=auto&ruri=~ymapsbm1%3A%2F%2Forg%3Foid%3D97162354049&z=15");
        rowLine.add(button);
        rowsLine.add(rowLine);
        markup.setKeyboard(rowsLine);
        sendMessage.setReplyMarkup(markup);
        execute(sendMessage);
    }
    @SneakyThrows
    public void sendPhoneNumber(Long chatId, String phoneNumber){
        SendContact sendContact = new SendContact();
        sendContact.setChatId(chatId);
        sendContact.setFirstName("User");
        sendContact.setPhoneNumber(phoneNumber);
        execute(sendContact);
    }

    public void sendRoomDesc(long chatId, Long roomId) throws TelegramApiException {
        Rooms room = roomsService.getById(roomId);
        List<String> imgUrls = room.getImgDataSet().stream().map(ImgData::getUrl).collect(Collectors.toList());

        List<InputMedia> media = new ArrayList<>();
        for (int i = 0; i < imgUrls.size(); i++) {
            media.add(new InputMediaPhoto(imgUrls.get(i)));
        }
        SendMediaGroup sendMediaGroup = new SendMediaGroup(String.valueOf(chatId), media);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(room.getDescription()+"\n"+"\n"+room.getStandard_price()+" руб/сутки пн-пт"+"\n"+room.getWeekend_price()+" руб/сутки сб-вск");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();
        rowLine.add(createButton(CALLBACK_ROOM,"К другим комнатам"));
        rowLine.add(createButton("BOOK_A_ROOM_" + roomId,"Забронировать"));
        rowsLine.add(rowLine);
        markup.setKeyboard(rowsLine);
        sendMessage.setReplyMarkup(markup);


        execute(sendMediaGroup);
        execute(sendMessage);

    }

    @SneakyThrows
    public void sendAddServices(long chatId){
        List<InputMedia> imgUrls = Arrays.asList(
                new InputMediaPhoto(imgDataService.findByDescription("ChoiceOfEntertainment1").getUrl()),
                new InputMediaPhoto(imgDataService.findByDescription("ChoiceOfEntertainment2").getUrl()),
                new InputMediaPhoto(imgDataService.findByDescription("ChoiceOfEntertainment3").getUrl()));
        SendMediaGroup sendMediaGroup = new SendMediaGroup(String.valueOf(chatId), imgUrls);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("В Glamptown вас ожидает множество развлечений, которые покорят ваше сердце! У нас вы найдёте все, что только можно себе представить — от велосипедных прогулок до роскошных сеансов массажа.\n Пожалуйста, выберете интересующую вас услугу");
        sendMessage.setReplyMarkup(new AddServicesKeyboard().addServicesInlineKeyboardMarkup());

        execute(sendMediaGroup);
        execute(sendMessage);
    }

    @SneakyThrows
    public  void sendBathhouseDescription(long chatId){
        List<InputMedia> imgUrls = Arrays.asList(
                new InputMediaPhoto(imgDataService.findByDescription("img1Bath").getUrl()),
                new InputMediaPhoto(imgDataService.findByDescription("img2Bath").getUrl()),
                new InputMediaPhoto(imgDataService.findByDescription("img3Bath").getUrl()));
        SendMediaGroup sendMediaGroup = new SendMediaGroup(String.valueOf(chatId), imgUrls);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Вы можете забронировать баню на любой удобный для вас день и время. Баня оборудована всем необходимым для полноценного отдыха.\n" +
                "\n" +
                "Внутри ждёт уютная обстановка, печь, которая создаёт идеальную температуру и влажность воздуха, а также зона отдыха, где вы сможете расслабиться и насладиться природой.\n" +
                "Мы предоставляем все необходимые аксессуары для полноценного отдыха — от полотенец до веников.\n" +
                "\n" +
                "Забронируйте баню в Glamptown и это поможет вам полностью расслабиться и забыть о повседневных заботах\n" +
                "\n" +
                "Аренда бани 2 000 ₽ в час \n" +
                "\n" +
                "веники:\n" +
                "берёза 200 ₽\n" +
                "ветки пихты 300 ₽\n" +
                "дуб 350 ₽\n" +
                "\n" +
                "чайный сет (чай на травах, варенье из шишек, мёд, орехи, сухофрукты) 1 400 ₽\n" +
                "\n" +
                "банный набор (шапочка, халат, полотенце, тапочки) 400 ₽ комплект");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();
        rowLine.add(createButton("BUTTON_BOOK_A_BATHHOUSE","Забронировать"));
        rowsLine.add(rowLine);
        markup.setKeyboard(rowsLine);
        sendMessage.setReplyMarkup(markup);
        sendMessage.setReplyMarkup(markup);

        execute(sendMediaGroup);
        execute(sendMessage);
    }
    @SneakyThrows
    public void sendNailStandingDescription(long chatId){
        SendPhoto sendPhoto = new SendPhoto(String.valueOf(chatId),new InputFile(imgDataService.findByDescription("nailstanding").getUrl()));
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Практика гвоздестояния — 2 500 ₽\n" + "в практику входит:\n" + "— разминка для стоп \n" + "— медитация\n" + "— формулирование запроса на предстоящую практику\n" + "— гвроздестояние на доске садху \n" + "— практика с метафорическими картами\n" + "\n" + "запись осуществляется при бронировании домика");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();
        rowLine.add(createButton("BUTTON_BOOK_A_NAIL_STANDING","Забронировать"));
        rowsLine.add(rowLine);
        markup.setKeyboard(rowsLine);
        sendMessage.setReplyMarkup(markup);
        sendMessage.setReplyMarkup(markup);

        execute(sendPhoto);
        execute(sendMessage);
    }
    @SneakyThrows
    public void sendBicycleRent(long chatId){
        SendPhoto sendPhoto = new SendPhoto(String.valueOf(chatId),new InputFile(imgDataService.findByDescription("bicycle").getUrl()));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();
        rowLine.add(createButton("BUTTON_BICYCLE_RENT","Арендовать"));
        rowsLine.add(rowLine);
        markup.setKeyboard(rowsLine);
        sendPhoto.setReplyMarkup(markup);
        sendPhoto.setReplyMarkup(markup);
        execute(sendPhoto);
    }
    @SneakyThrows
    public void sendSkateboardRent(long chatId){
        SendPhoto sendPhoto = new SendPhoto(String.valueOf(chatId),new InputFile(imgDataService.findByDescription("skateboard").getUrl()));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();
        rowLine.add(createButton("BUTTON_SKATEBOARD_RENT","Арендовать"));
        rowsLine.add(rowLine);
        markup.setKeyboard(rowsLine);
        sendPhoto.setReplyMarkup(markup);
        sendPhoto.setReplyMarkup(markup);
        execute(sendPhoto);
    }
    @SneakyThrows
    public void sendBadmintonRent(long chatId){
        SendPhoto sendPhoto = new SendPhoto(String.valueOf(chatId),new InputFile(imgDataService.findByDescription("badminton").getUrl()));

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Данная услуга бесплатна\n" +"\n" + "Уточняте у администраторов!");


        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();
        rowLine.add(createButton("BUTTON_BADMINTON_RENT","Арендовать"));
        rowsLine.add(rowLine);
        markup.setKeyboard(rowsLine);
        sendMessage.setReplyMarkup(markup);
        sendMessage.setReplyMarkup(markup);

        execute(sendPhoto);
        execute(sendMessage);
    }
    @SneakyThrows
    public void sendClassicalMassage(long chatId){
        SendPhoto sendPhoto = new SendPhoto(String.valueOf(chatId),new InputFile(imgDataService.findByDescription("massage").getUrl()));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();
        rowLine.add(createButton("BUTTON_MASSAGE_RENT","Записаться"));
        rowsLine.add(rowLine);
        markup.setKeyboard(rowsLine);
        sendPhoto.setReplyMarkup(markup);
        sendPhoto.setReplyMarkup(markup);
        execute(sendPhoto);
    }
    @SneakyThrows
    public void sendCoals(long chatId){
        SendPhoto sendPhoto = new SendPhoto(String.valueOf(chatId),new InputFile(imgDataService.findByDescription("coals").getUrl()));
        execute(sendPhoto);
    }
    @SneakyThrows
    public void sendBreakfast(long chatId){
        List<InputMedia> imgUrls = Arrays.asList(
                new InputMediaPhoto(imgDataService.findByDescription("Breakfast1").getUrl()),
                new InputMediaPhoto(imgDataService.findByDescription("Breakfast2").getUrl()),
                new InputMediaPhoto(imgDataService.findByDescription("Breakfast3").getUrl()));
        SendMediaGroup sendMediaGroup = new SendMediaGroup(String.valueOf(chatId), imgUrls);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Приятно завтракать хрустящей выпечкой или сырниками на отдыхе, не правда ли?\n" + "\n" + "В Glamptown вы можете полноценно насладиться магией утра позавтракав сырниками и круассанами с чашечкой свежесваренного кофе");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();
        rowLine.add(createButton("BUTTON_ORDER_BREAKFAST","Заказать"));
        rowsLine.add(rowLine);
        markup.setKeyboard(rowsLine);
        sendMessage.setReplyMarkup(markup);
        sendMessage.setReplyMarkup(markup);

        execute(sendMediaGroup);
        execute(sendMessage);
    }
    @SneakyThrows
    public void sendOrderBreakfast(long chatId){
        USER_SELECTED.put(String.valueOf(chatId),"");
        List<Breakfast> breakfasts = breakfastService.getAll();
        List<InputMedia> media = new ArrayList<>();

        List<String> breakfastDesk = breakfasts.stream().map(Breakfast::getDescription).collect(Collectors.toList());
        List<String> breakfastName = breakfasts.stream().map(Breakfast::getName).collect(Collectors.toList());
        List<String> breakfastPrice = breakfasts.stream().map(Breakfast::getPrice).collect(Collectors.toList());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String text ="";

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();

        for (int i = 0; i <breakfasts.size(); i++) {
            text += breakfastName.get(i)+":"+breakfastDesk.get(i)+" "+breakfastPrice.get(i)+"Р";
            text += "\n";

            List<InlineKeyboardButton> rowLine = new ArrayList<>();
            rowLine.add(createButton("BUTTON_BREAKFAST_"+(i+1),breakfastName.get(i)));
            rowsLine.add(rowLine);

            List<ImgData> imgUrls = breakfasts.get(i).getImgData().stream().toList();
            for (int j = 0; j < imgUrls.size(); j++) {
                media.add(new InputMediaPhoto(imgUrls.get(j).getUrl()));
            }
        }
        SendMediaGroup sendMediaGroup = new SendMediaGroup(String.valueOf(chatId), media);
        sendMessage.setText(text);

        markup.setKeyboard(rowsLine);
        sendMessage.setReplyMarkup(markup);
        sendMessage.setReplyMarkup(markup);

        execute(sendMediaGroup);
        execute(sendMessage);
    }

    @SneakyThrows
    public void sendBreakfastBookTime(long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Принято! Завтрак проходит с 10:00 до 12:00 часов");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();
        var button = createButton("BREAKFAST_PAY","Оплата");
        button.setUrl("https://madd.band/");
        rowLine.add(button);
        rowsLine.add(rowLine);
        markup.setKeyboard(rowsLine);
        sendMessage.setReplyMarkup(markup);
        sendMessage.setReplyMarkup(markup);

        execute(sendMessage);
    }

    @SneakyThrows
    public void sendPayRent(long chatId, long msgId){
        // TODO: 13.07.2023 потом в платеже извлекать кол-ва часов и подсчитать стоимость
        //System.out.println(USER_SELECTED.get(SELECTED_HOUR+String.valueOf(chatId)));

        EditMessageText message = new EditMessageText();
        message.setText("Отлично я записал!");
        message.setChatId(chatId);
        message.setMessageId((int) msgId);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();
        var button = createButton("BICYCLE_RENT_PAY","Оплата");
        button.setUrl("https://madd.band/");
        rowLine.add(button);
        rowsLine.add(rowLine);
        markup.setKeyboard(rowsLine);
        message.setReplyMarkup(markup);

        execute(message);
    }

    //utils
    public void sendCalendar(long chatId, String text, LocalDate localDate) throws TelegramApiException {
        CalendarUtil2 calendarUtil = new CalendarUtil2();
        SendMessage sendMessage = sendMessage(chatId,text);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(calendarUtil.generateKeyboard(localDate));
        sendMessage.setReplyMarkup(keyboardMarkup);

        execute(sendMessage);
    }

    @SneakyThrows
    public void sendTimePicker(long chatId, String text, int hour, String buttonName){
        TimePicker timePicker = new TimePicker();
        SendMessage sendMessage = sendMessage(chatId,text);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(timePicker.generatedTimePicker(hour, buttonName));
        sendMessage.setReplyMarkup(keyboardMarkup);

        execute(sendMessage);
    }
    @SneakyThrows
    public void plusMinusTimePicker(String operation, String hour, long chatId, long msgId, String text, String buttonName){
        EditMessageText message = new EditMessageText();
        message.setText(text);
        message.setChatId(chatId);
        message.setMessageId((int) msgId);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        if(operation.equals("+")){
            markup.setKeyboard(new TimePicker().generatedTimePicker(Integer.parseInt(hour)+1, buttonName));
            USER_SELECTED.put(SELECTED_HOUR+String.valueOf(chatId), String.valueOf(Integer.parseInt(hour)+1));
        }
        else if(operation.equals("-")){
            markup.setKeyboard(new TimePicker().generatedTimePicker(Integer.parseInt(hour)-1, buttonName));
            USER_SELECTED.put(SELECTED_HOUR+String.valueOf(chatId), String.valueOf(Integer.parseInt(hour)-1));
        }

        message.setReplyMarkup(markup);

        execute(message);
    }
    private SendMessage sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        return message;
    }
    private InlineKeyboardButton createButton(String callBack, String text) {
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setCallbackData(callBack);
        keyboardButton.setText(text);
        return keyboardButton;
    }
    private LocalDate checkBreakFastTime(){
        LocalTime nowTime = LocalTime.now();
        LocalDate nowDate = LocalDate.now();
        if (nowTime.compareTo(END_BREAKFAST) == 1){
            nowDate = nowDate.plusDays(1);
        }
        return nowDate;
    }

    @SneakyThrows
    public void sendSelectTime(long chatId, String timeType){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        LocalTime nowTime = LocalTime.now().plusHours(2);
//        LocalTime nowTime = LocalTime.parse("00:01");

        int h = nowTime.getHourOfDay();
        int m = nowTime.getMinuteOfHour();
        String zero = "0";

        int size = 0;
        if (timeType.equals(BATH_TIME)){
            sendMessage.setText("Бронирование осуществляется за 2 часа. \nДоступное время на выбранный день:");
            if (24-h>=1 && String.valueOf(LocalDate.now()).equals(USER_SELECTED.get(SELECTED_DATE+chatId))){
                size = 24-h;
            }
            else {
                size=24;
                h=0;
                m=0;
            }
        }
        if (timeType.equals(MASSAGE_TIME) || timeType.equals(NAIL_STANDING_TIME)){
            sendMessage.setText("Бронирование осуществляется за 2 часа. \nРежим работы с 09:00 до 18:00 \nДоступное время на выбранный день:");
            if (18-h>=1 && String.valueOf(LocalDate.now()).equals(USER_SELECTED.get(SELECTED_DATE+chatId))){
                size = 17-h;
            }
            else {
                size=9;
                h=9;
                m=0;
            }
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        int t = 0;
        for (int i = 0; i < size; i++) {
            String strH = String.valueOf(h);
            String StrM = String.valueOf(m);
            if (m<10){
                StrM = zero+StrM;
            }if (h<10){
                strH = zero+strH;
            }
            List<InlineKeyboardButton> rowLine = new ArrayList<>();
            // TODO: 14.07.2023 выбранное время записалось в кнопке для будущей оплаты
            rowLine.add(createButton(timeType+strH+":"+StrM,strH+":"+StrM));
            h++;
            rowsLine.add(rowLine);
        }
        markup.setKeyboard(rowsLine);
        sendMessage.setReplyMarkup(markup);
        execute(sendMessage);;
    }
}