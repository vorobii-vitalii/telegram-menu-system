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

public class MainScreenHandler implements UpdateHandler {
    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery() && CallbackConstants.MAIN_SCREEN.equals(update.getCallbackQuery().getData());
    }

    @Override
    public void handle(Update update, AbsSender absSender) throws TelegramApiException {
        var callbackQuery = update.getCallbackQuery();
        var message = callbackQuery.getMessage();
        var from = update.getCallbackQuery().getFrom();
        absSender.execute(EditMessageText.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .text("Привіт " + from.getFirstName() + " " + (from.getLastName() == null ? "" : from.getLastName()))
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(
                                List.of(
                                        createButton("Меню", CallbackConstants.MENU),
                                        createButton("Контакти", CallbackConstants.CONTACTS)))
                        .build())
                .build());
    }

    private InlineKeyboardButton createButton(String label, String callbackData) {
        var button = new InlineKeyboardButton();
        button.setText(label);
        button.setCallbackData(callbackData);
        return button;
    }

}
