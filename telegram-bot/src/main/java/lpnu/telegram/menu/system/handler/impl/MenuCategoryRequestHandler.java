package lpnu.telegram.menu.system.handler.impl;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lpnu.telegram.menu.system.CategoryDetails;
import lpnu.telegram.menu.system.CategoryDetailsServiceGrpc;
import lpnu.telegram.menu.system.FetchCategoryDetailsRequest;
import lpnu.telegram.menu.system.constants.CallbackConstants;
import lpnu.telegram.menu.system.handler.UpdateHandler;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class MenuCategoryRequestHandler implements UpdateHandler {
    private static final String HTML_PARSE_MODE = "HTML";
    private static final int MENU_PREFIX_LENGTH = CallbackConstants.MENU.length();

    private final CategoryDetailsServiceGrpc.CategoryDetailsServiceStub categoryDetailsService;

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith(CallbackConstants.MENU);
    }

    @Override
    public void handle(Update update, AbsSender absSender) {
        var callbackQuery = update.getCallbackQuery();
        var message = callbackQuery.getMessage();
        var categorySerialized = callbackQuery.getData().substring(MENU_PREFIX_LENGTH);
        var categoryId = categorySerialized.isEmpty() ? null : categorySerialized;
        var builder = FetchCategoryDetailsRequest.newBuilder();
        if (categoryId != null) {
            builder.setCategoryId(categoryId);
        }
        categoryDetailsService.fetchMenuCategoryDetails(builder.build(), new StreamObserver<>() {
            @SneakyThrows
            @Override
            public void onNext(CategoryDetails categoryDetails) {
                var backButton = InlineKeyboardButton.builder()
                        .text("Повернутись")
                        .callbackData(
                                categoryId == null
                                        ? CallbackConstants.MAIN_SCREEN
                                        : CallbackConstants.MENU + categoryDetails.getParentCategoryId())
                        .build();
                var childrenCategories = categoryDetails.getChildCategoryList()
                        .stream()
                        .map(category -> InlineKeyboardButton.builder()
                                .text(category.getName())
                                .callbackData(CallbackConstants.MENU + category.getCategoryId())
                                .build())
                        .map(List::of);

                var dishes = categoryDetails.getDishList()
                        .stream()
                        .map(dish -> InlineKeyboardButton.builder()
                                .text(dish.getTitle())
                                .callbackData(CallbackConstants.DISH + dish.getDishId())
                                .build())
                        .map(List::of);

                String categoryDetailsName = (
                        categoryId == null
                                ? "Меню"
                                : "Категорія \"<b>%s</b>\"\n\n".formatted(categoryDetails.getName())
                        );

                absSender.execute(EditMessageText.builder()
                        .chatId(message.getChatId())
                        .messageId(message.getMessageId())
                        .parseMode(HTML_PARSE_MODE)
                        .text(categoryDetailsName)
                        .replyMarkup(InlineKeyboardMarkup.builder()
                                .keyboard(concat(
                                        childrenCategories,
                                        Stream.of(List.of()),
                                        dishes,
                                        Stream.of((List.of())),
                                        Stream.of(List.of(backButton))
                                    ).collect(Collectors.toList()))
                                .build())
                        .build());
            }

            @SafeVarargs
            private <T> Stream<T> concat(Stream<T>... streams) {
                return Stream.of(streams)
                        .flatMap(e -> e);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("Error", throwable);
            }

            @Override
            public void onCompleted() {

            }
        });
    }
}
