package lpnu.telegram.menu.system.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface UpdateHandler {
    boolean canHandle(Update update);
    void handle(Update update, AbsSender absSender) throws TelegramApiException;
}
