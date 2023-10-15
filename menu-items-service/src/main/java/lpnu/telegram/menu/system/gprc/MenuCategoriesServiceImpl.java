package lpnu.telegram.menu.system.gprc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lpnu.telegram.menu.system.*;
import lpnu.telegram.menu.system.dao.DishesDAO;
import lpnu.telegram.menu.system.dao.MenuCategoryDAO;
import lpnu.telegram.menu.system.dao.TransactionalService;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MenuCategoriesServiceImpl extends MenuCategoriesServiceGrpc.MenuCategoriesServiceImplBase {
    private final MenuCategoryDAO menuCategoryDAO;
    private final DishesDAO dishesDAO;
    private final TransactionalService transactionalService;

    @Override
    public void getAllCategories(Empty request, StreamObserver<Categories> responseObserver) {
        log.info("Received request to get all categories");
        menuCategoryDAO.getAllCategories()
                .buffer()
                .switchIfEmpty(Flux.just(List.of()))
                .subscribe(
                        categories -> {
                            log.info("All categories = {}", categories);
                            responseObserver.onNext(Categories.newBuilder().addAllCategory(categories).build());
                        },
                        error -> {
                            log.error("Error occurred on fetch of categories", error);
                            responseObserver.onError(error);
                        },
                        () -> {
                            log.info("All categories found...");
                            responseObserver.onCompleted();
                        });
    }

    @Override
    public void addCategory(AddCategoryRequest request, StreamObserver<Empty> responseObserver) {
        log.info("Received request to add new category {}", request);
        menuCategoryDAO.addMenuCategory(request)
                .subscribe(
                        ignored -> log.info("New category was added..."),
                        error -> {
                            log.error("Error occurred on addition of category", error);
                            responseObserver.onError(error);
                        },
                        () -> {
                            responseObserver.onNext(Empty.newBuilder().build());
                            responseObserver.onCompleted();
                        });
    }

    @Override
    public void changeCategoryDetails(ChangeCategoryDetailsRequest request, StreamObserver<Empty> responseObserver) {
        log.info("Received request to update new category {}", request);
        menuCategoryDAO.updateMenuCategory(request)
                .subscribe(
                        count -> log.info("{} categories were updated", count),
                        error -> {
                            log.error("Error occurred on change of category", error);
                            responseObserver.onError(error);
                        },
                        () -> {
                            responseObserver.onNext(Empty.newBuilder().build());
                            responseObserver.onCompleted();
                        });
    }

    @Override
    public void removeCategory(RemoveCategoryRequest request, StreamObserver<Empty> responseObserver) {
        log.info("Received request to remove category {}", request);
        menuCategoryDAO.searchForChildrenCategoriesRecursive(request.getCategoryId())
                .buffer()
                .flatMap(categoryIds -> {
                    log.info("Going to remove categories and dishes by category in {}", categoryIds);
                    return transactionalService.performTransitionally(
                            Flux.merge(
                                            menuCategoryDAO.removeMenuCategories(categoryIds),
                                            dishesDAO.removeDishesByCategoryIn(categoryIds))
                                    .then());
                })
                .subscribe(
                        v -> log.info("Removal of category {} completed successfully", request.getCategoryId()),
                        error -> {
                            log.error("Error occurred on removal of category", error);
                            responseObserver.onError(error);
                        },
                        () -> {
                            responseObserver.onNext(Empty.newBuilder().build());
                            responseObserver.onCompleted();
                        });
    }
}
