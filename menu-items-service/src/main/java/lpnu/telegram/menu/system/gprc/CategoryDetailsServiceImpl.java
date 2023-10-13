package lpnu.telegram.menu.system.gprc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lpnu.telegram.menu.system.CategoryDetails;
import lpnu.telegram.menu.system.CategoryDetailsServiceGrpc;
import lpnu.telegram.menu.system.FetchCategoryDetailsRequest;
import lpnu.telegram.menu.system.dao.DishesDAO;
import lpnu.telegram.menu.system.dao.MenuCategoryDAO;
import reactor.core.publisher.Flux;

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
                        dishesDAO.getDishesByCategoryId(categoryId).buffer(),
                        menuCategoryDAO.getChildMenuCategories(categoryId).buffer(),
                        menuCategoryDAO.getCategoryDetails(categoryId)
                ).map(tuple -> {
                    var dishes = tuple.getT1();
                    var childCategories = tuple.getT2();
                    var categoryDetails = tuple.getT3();
                    return CategoryDetails.newBuilder()
                            .addAllChildCategory(childCategories)
                            .addAllDish(dishes)
                            .setCategoryId(categoryDetails.categoryId())
                            .setParentCategoryId(categoryDetails.parentCategoryId())
                            .setName(categoryDetails.name())
                            .build();
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
