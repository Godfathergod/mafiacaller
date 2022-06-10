import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

public class TelegramApplication {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        TelegramBot bot = new TelegramBot();
        FileInputStream fin = new FileInputStream("src/main/resources/users.txt");
        ObjectInputStream in = new ObjectInputStream(fin);
        Map<User,Boolean> map = (Map) in.readObject();
        bot.setUserSet(map);
        in.close();
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
