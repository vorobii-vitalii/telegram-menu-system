package lpnu.telegram.menu.system.bot;

import lombok.extern.slf4j.Slf4j;
import lpnu.telegram.menu.system.handler.UpdateHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
public class DeleteOnUnknownTelegramBot extends TelegramLongPollingBot {
    private final String botUsername;
    private final List<UpdateHandler> updateHandlers;

    public DeleteOnUnknownTelegramBot(
            String botToken,
            String botUsername,
            List<UpdateHandler> updateHandlers
    ) {
        super(botToken);
        this.botUsername = botUsername;
        this.updateHandlers = updateHandlers;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Processing update {}", update);
        updateHandlers.stream()
                .filter(handler -> handler.canHandle(update))
                .findFirst()
                .ifPresentOrElse(
                        handler -> {
                            try {
                                handler.handle(update, this);
                            } catch (TelegramApiException e) {
                                log.error("Telegram API...", e);
                            }
                        },
                        () -> {
                            log.info("No handler found for update {}", update);
                            try {
                                if (update.getMessage() != null) {
                                    execute(DeleteMessage.builder()
                                            .chatId(update.getMessage().getChat().getId())
                                            .messageId(update.getMessage().getMessageId())
                                            .build());
                                }
                                else {
                                    log.info("message not found...");
                                }
                            } catch (TelegramApiException e) {
                                log.error("Message send failed...", e);
                            }
                        });
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onRegister() {
        log.info("Bot registered!");
    }
}
