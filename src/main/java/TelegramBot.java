import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class TelegramBot extends TelegramLongPollingBot {
    private Map<User,Boolean> userMap = new HashMap<>();

    public Map<User,Boolean> getUserSet() {
        return userMap;
    }

    public void setUserSet(Map<User,Boolean> userSet) {
        this.userMap = userSet;
    }

    @Override
    public String getBotUsername() {
        return "@ReduplicationRus_bot";
    }

    @Override
    public String getBotToken() {
        return "5244376628:AAE_yd6CsKpFPGIvEiK9UEtOh8ozWkggnEI";
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        User userMes = update.getMessage().getFrom();
        if(!userMap.keySet().contains(userMes)){
            userMap.put(userMes,true);
            try {
                File file = new File("src/main/resources/users.txt");
                file.delete();
                file.createNewFile();
                FileOutputStream fout = new FileOutputStream(file.getPath());
                ObjectOutputStream out = new ObjectOutputStream(fout);
                out.writeObject(userMap);
                out.close();
            } catch (IOException ignored){
                System.out.println("a");
            }
            System.out.println(userMes.getFirstName() + " " + userMes.getUserName());
        }
        if(update.hasMessage() && !update.getMessage().getText().isEmpty()){
            SendMessage answer = new SendMessage();
            answer.setChatId(String.valueOf(update.getMessage().getChatId()));
            answer.setText(getCommands(update));
            try {
                if (!answer.getText().isEmpty()){
                    execute(answer);
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    public String getCommands(Update update) {
        String command = update.getMessage().getText();
        Chat chat = update.getMessage().getChat();
        BotCommand start = new BotCommand();
        start.setCommand("/start" + getBotUsername());
        BotCommand all = new BotCommand();
        BotCommand sleep = new BotCommand();
        sleep.setCommand("/sleep" + getBotUsername());
        all.setCommand("/all" + getBotUsername());
        if(command.equals(start.getCommand()))
        {
            return "Слава Україні";
        } else if(command.equals(all.getCommand()))
        {
           StringBuilder text = new StringBuilder();
            userMap.forEach((user,callable) -> {
                if(callable) {
                    text.append(user.getUserName());
                    text.append(" ");
                }
           });
           return text.toString();
        /*} else if (command.equals("да") || command.equals("ДА") || command.equals("Да") || command.equals("да")){
            return "Пизда";*/
        } else if(command.equals(sleep.getCommand())){
            User user = update.getMessage().getFrom();
            userMap.put(user,false);
        }
        return "";
    }
}
