package lpnu.telegram.menu.system.handler.impl;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lpnu.telegram.menu.system.Dish;
import lpnu.telegram.menu.system.DishServiceGrpc;
import lpnu.telegram.menu.system.GetDishRequest;
import lpnu.telegram.menu.system.constants.CallbackConstants;
import lpnu.telegram.menu.system.handler.UpdateHandler;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

import static lpnu.telegram.menu.system.handler.impl.ShowContactsHandler.HTML_PARSE_MODE;

@RequiredArgsConstructor
@Slf4j
public class DishDetailsHandler implements UpdateHandler {
    private static final int DISH_PREFIX_LENGTH = CallbackConstants.DISH.length();

    private final DishServiceGrpc.DishServiceStub dishService;

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith(CallbackConstants.DISH);
    }

    @Override
    public void handle(Update update, AbsSender absSender) {
        var callbackQuery = update.getCallbackQuery();
        var message = callbackQuery.getMessage();
        var dishIdSerialized = callbackQuery.getData().substring(DISH_PREFIX_LENGTH);
        var getDishRequest = GetDishRequest.newBuilder().setDishId(dishIdSerialized).build();
        log.info("Fetching dish details for {}...", dishIdSerialized);
        dishService.getDishById(getDishRequest, new StreamObserver<>() {
            @SneakyThrows
            @Override
            public void onNext(Dish dish) {
                log.info("Dish details fetched {}", dish);
                var backButton = InlineKeyboardButton.builder()
                        .text("Повернутись")
                        .callbackData(CallbackConstants.MENU + dish.getCategoryId())
                        .build();
                var dishDetails = """
                        <b>"%s"</b>
                        <i>%s</i>
                        <i>В наявності: %s</i>
                        """.formatted(dish.getTitle(), dish.getDescription(), dish.getAvailability() ? "&#10004;" : "&#10060;");

                absSender.execute(EditMessageText.builder()
                        .chatId(message.getChatId())
                        .messageId(message.getMessageId())
                        .parseMode(HTML_PARSE_MODE)
                        .text(dishDetails)
                        .replyMarkup(InlineKeyboardMarkup.builder()
                                .keyboardRow(List.of(backButton))
                                .build())
                        .build());
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("ERROR", throwable);
            }

            @Override
            public void onCompleted() {
            }
        });
    }
}
