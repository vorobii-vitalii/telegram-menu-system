package lpnu.telegram.menu.system.gprc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lpnu.telegram.menu.system.AddCategoryRequest;
import lpnu.telegram.menu.system.ChangeCategoryDetailsRequest;
import lpnu.telegram.menu.system.Empty;
import lpnu.telegram.menu.system.MenuCategoriesServiceGrpc;
import lpnu.telegram.menu.system.RemoveCategoryRequest;
import lpnu.telegram.menu.system.dao.DishesDAO;
import lpnu.telegram.menu.system.dao.MenuCategoryDAO;
import lpnu.telegram.menu.system.dao.TransactionalService;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
public class MenuCategoriesServiceImpl extends MenuCategoriesServiceGrpc.MenuCategoriesServiceImplBase {
    private final MenuCategoryDAO menuDAO;
    private final DishesDAO dishesDAO;
    private final TransactionalService transactionalService;

    @Override
    public void addCategory(AddCategoryRequest request, StreamObserver<Empty> responseObserver) {
        log.info("Received request to add new category {}", request);
        menuDAO.addMenuCategory(request)
                .subscribe(
                        ignored -> {
                            log.info("New category was added...");
                            responseObserver.onNext(Empty.newBuilder().build());
                            responseObserver.onCompleted();
                        },
                        error -> {
                            log.error("Error occurred on addition of category", error);
                            responseObserver.onError(error);
                        });
    }

    @Override
    public void changeCategoryDetails(ChangeCategoryDetailsRequest request, StreamObserver<Empty> responseObserver) {
        log.info("Received request to update new category {}", request);
        menuDAO.updateMenuCategory(request)
                .subscribe(
                        count -> {
                            log.info("{} categories were updated", count);
                            responseObserver.onNext(Empty.newBuilder().build());
                            responseObserver.onCompleted();
                        },
                        error -> {
                            log.error("Error occurred on change of category", error);
                            responseObserver.onError(error);
                        });
    }

    @Override
    public void removeCategory(RemoveCategoryRequest request, StreamObserver<Empty> responseObserver) {
        log.info("Received request to remove category {}", request);
        menuDAO.searchForChildrenCategoriesRecursive(request.getCategoryId())
                .buffer()
                .flatMap(categoryIds -> {
                    log.info("Going to remove categories and dishes by category in {}", categoryIds);
                    return transactionalService.performTransitionally(
                            Flux.merge(
                                            menuDAO.removeMenuCategories(categoryIds),
                                            dishesDAO.removeDishesByCategoryIn(categoryIds))
                                    .then());
                })
                .subscribe(v -> {
                    log.info("Removal of category {} completed successfully", request.getCategoryId());
                    responseObserver.onNext(Empty.newBuilder().build());
                    responseObserver.onCompleted();
                }, error -> {
                    log.error("Error occurred on removal of category", error);
                    responseObserver.onError(error);
                });
    }
}
