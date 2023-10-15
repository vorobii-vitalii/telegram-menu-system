package lpnu.telegram.menu.system;

import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import lpnu.telegram.menu.system.bot.DeleteOnUnknownTelegramBot;
import lpnu.telegram.menu.system.handler.impl.*;
import lpnu.telegram.menu.system.service.InitialMessageCreator;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Slf4j
public class TelegramBotMain {

    public static void main(String[] args) throws TelegramApiException {
        var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            log.info("Registering bot...");
            var channel = ManagedChannelBuilder.forAddress(
                            System.getenv("MANAGEMENT_SERVICE_HOST"),
                            Integer.parseInt(System.getenv("MANAGEMENT_SERVICE_PORT")))
                    .enableRetry()
                    .usePlaintext()
                    .build();
            var menuCategoriesServiceStub =
                    lpnu.telegram.menu.system.CategoryDetailsServiceGrpc.newStub(channel);
            var dishDetailsServiceStub = lpnu.telegram.menu.system.DishServiceGrpc.newStub(channel);
            var initialMessageCreator = new InitialMessageCreator();
            telegramBotsApi.registerBot(new DeleteOnUnknownTelegramBot(
                    getTelegramBotToken(),
                    "Menu view bot",
                    List.of(
                            new InitialRequestHandler(initialMessageCreator),
                            new MainScreenHandler(),
                            new ShowContactsHandler(),
                            new MenuCategoryRequestHandler(menuCategoriesServiceStub),
                            new DishDetailsHandler(dishDetailsServiceStub)
                    )
            ));
        } catch (TelegramApiRequestException e) {
            log.error("Start of bot failed", e);
            throw e;
        }
        log.info("Bot registered");
    }

    private static String getTelegramBotToken() {
        return System.getenv("TELEGRAM_BOT_TOKEN");
    }

}
