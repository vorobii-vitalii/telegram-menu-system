package lpnu.telegram.menu.system.gprc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lpnu.telegram.menu.system.CategoryDetails;
import lpnu.telegram.menu.system.CategoryDetailsServiceGrpc;
import lpnu.telegram.menu.system.FetchCategoryDetailsRequest;
import lpnu.telegram.menu.system.dao.DishesDAO;
import lpnu.telegram.menu.system.dao.MenuCategoryDAO;
import lpnu.telegram.menu.system.dto.FullCategoryDetail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CategoryDetailsServiceImpl extends CategoryDetailsServiceGrpc.CategoryDetailsServiceImplBase {
    private final DishesDAO dishesDAO;
    private final MenuCategoryDAO menuCategoryDAO;

    @Override
    public void fetchMenuCategoryDetails(
            FetchCategoryDetailsRequest request,
            StreamObserver<CategoryDetails> responseObserver
    ) {
        var categoryId = request.getCategoryId();
        log.info("Received request to fetch menu categories details {}", categoryId);
        Flux.zip(
                        dishesDAO.getDishesByCategoryId(categoryId).buffer().switchIfEmpty(Mono.just(List.of())),
                        menuCategoryDAO.getChildMenuCategories(categoryId).buffer().switchIfEmpty(Mono.just(List.of())),
                        menuCategoryDAO.getCategoryDetails(categoryId)
                                .switchIfEmpty(Mono.just(FullCategoryDetail.builder().build()))
                ).map(tuple -> {
                    var dishes = tuple.getT1();
                    var childCategories = tuple.getT2();
                    var categoryDetails = tuple.getT3();
                    log.info("Dishes = {}, children = {}, categoryDetails = {}", dishes, childCategories, categoryDetails);
                    var builder = CategoryDetails.newBuilder()
                            .addAllChildCategory(childCategories)
                            .addAllDish(dishes);
                    if (categoryDetails.categoryId() != null) {
                        builder.setCategoryId(categoryDetails.categoryId());
                    }
                    if (categoryDetails.parentCategoryId() != null) {
                        builder.setParentCategoryId(categoryDetails.parentCategoryId());
                    }
                    if (categoryDetails.name() != null) {
                        builder.setName(categoryDetails.name());
                    }
                    return builder.build();
                })
                .subscribe(categoryDetails -> {
                            responseObserver.onNext(categoryDetails);
                            responseObserver.onCompleted();
                        },
                        error -> {
                            log.error("Operation to get menu category details failed...", error);
                            responseObserver.onError(error);
                        });
    }
}
