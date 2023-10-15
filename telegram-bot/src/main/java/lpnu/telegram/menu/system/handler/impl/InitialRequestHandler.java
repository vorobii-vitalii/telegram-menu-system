package lpnu.telegram.menu.system.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lpnu.telegram.menu.system.handler.UpdateHandler;
import lpnu.telegram.menu.system.service.InitialMessageCreator;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@RequiredArgsConstructor
public class InitialRequestHandler implements UpdateHandler {
    private static final String START_COMMAND = "/start";

    private final InitialMessageCreator initialMessageCreator;

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().getText().equals(START_COMMAND);
    }

    @Override
    public void handle(Update update, AbsSender absSender) throws TelegramApiException {
        var chatId = update.getMessage().getChat().getId();
        var fromUser = update.getMessage().getFrom();
        absSender.execute(initialMessageCreator.createInitialMessage(chatId, fromUser));
    }
}
