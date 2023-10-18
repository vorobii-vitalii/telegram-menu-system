package lpnu.telegram.menu.system.service;

import lpnu.telegram.menu.system.constants.CallbackConstants;
import org.telegram.telegrambots.Constants;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class InitialMessageCreator {

    public SendMessage createInitialMessage(long chatId, User from) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Привіт " + from.getFirstName() + " " + (from.getLastName() == null ? "" : from.getLastName()))
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(
                                List.of(
                                        createButton("Menu", CallbackConstants.MENU),
                                        createButton("Contacts", CallbackConstants.CONTACTS)))
                        .build())
                .build();
    }

    private InlineKeyboardButton createButton(String label, String callbackData) {
        var button = new InlineKeyboardButton();
        button.setText(label);
        button.setCallbackData(callbackData);
        return button;
    }

}
