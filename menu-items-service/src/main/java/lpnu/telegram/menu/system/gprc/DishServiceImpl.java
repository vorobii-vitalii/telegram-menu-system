package lpnu.telegram.menu.system.gprc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lpnu.telegram.menu.system.*;
import lpnu.telegram.menu.system.dao.DishesDAO;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Slf4j
public class DishServiceImpl extends DishServiceGrpc.DishServiceImplBase {
    private final DishesDAO dishesDAO;

    @Override
    public void addDish(Dish newDish, StreamObserver<Empty> responseObserver) {
        log.info("Received request to add new dish {}", newDish);
        dishesDAO.addNewDish(newDish)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        ignored -> log.info("Dish was added!"),
                        error -> {
                            log.error("Error occurred on dish add", error);
                            responseObserver.onError(error);
                        },
                        () -> {
                            log.info("Dish was added!");
                            responseObserver.onNext(Empty.newBuilder().build());
                            responseObserver.onCompleted();
                        });
    }

    @Override
    public void updateDishDetails(
            UpdateDishDetailsRequest updateDishDetailsRequest,
            StreamObserver<Empty> responseObserver
    ) {
        log.info("Received request to update dish {}", updateDishDetailsRequest);
        dishesDAO.updateDishDetails(updateDishDetailsRequest)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        updatedCount -> log.info("{} dishes were updated", updatedCount),
                        error -> {
                            log.error("Error occurred on dish update", error);
                            responseObserver.onError(error);
                        },
                        () -> {
                            responseObserver.onNext(Empty.newBuilder().build());
                            responseObserver.onCompleted();
                        });
    }

    @Override
    public void removeDish(
            RemoveDishRequest removeDishRequest,
            StreamObserver<Empty> responseObserver
    ) {
        log.info("Received request to remove dish {}", removeDishRequest);
        var dishId = removeDishRequest.getDishId();
        dishesDAO.removeDishById(dishId)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        numDeleted -> log.info("{} dishes have been removed!", numDeleted),
                        error -> {
                            log.error("Error occurred on dish removal", error);
                            responseObserver.onError(error);
                        },
                        () -> {
                            responseObserver.onNext(Empty.newBuilder().build());
                            responseObserver.onCompleted();
                        });
    }
}
