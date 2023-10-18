package lpnu.telegram.menu.system.handler.impl;

import lpnu.telegram.menu.system.constants.CallbackConstants;
import lpnu.telegram.menu.system.handler.UpdateHandler;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


public class ShowContactsHandler implements UpdateHandler {
    public static final String HTML_PARSE_MODE = "HTML";

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery() && CallbackConstants.CONTACTS.equals(update.getCallbackQuery().getData());
    }

    @Override
    public void handle(Update update, AbsSender absSender) throws TelegramApiException {
        var callbackQuery = update.getCallbackQuery();
        var message = callbackQuery.getMessage();
        absSender.execute(EditMessageText.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .parseMode(HTML_PARSE_MODE)
                .text("<i>Номер телефону</i>: <b>+38092257256</b>")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(
                                InlineKeyboardButton.builder()
                                        .callbackData(CallbackConstants.MAIN_SCREEN)
                                        .text("Повернутись")
                                        .build()
                        ))
                        .build())
                .build());
    }
}
