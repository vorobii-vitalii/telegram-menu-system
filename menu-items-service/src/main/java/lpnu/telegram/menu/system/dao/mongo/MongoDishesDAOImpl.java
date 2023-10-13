package lpnu.telegram.menu.system.dao.mongo;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.reactivestreams.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lpnu.telegram.menu.system.Dish;
import lpnu.telegram.menu.system.UpdateDishDetailsRequest;
import lpnu.telegram.menu.system.dao.DishesDAO;
import org.bson.Document;
import org.bson.conversions.Bson;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
public class MongoDishesDAOImpl implements DishesDAO {
    private static final String DISH_ID = "_id";
    private static final String CATEGORY_ID = "categoryId";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String AVAILABILITY = "availability";

    private final MongoCollection<Document> dishesCollection;

    @Override
    public Flux<Dish> getDishesByCategoryId(String categoryId) {
        return Flux.from(dishesCollection.find(Filters.eq(CATEGORY_ID, categoryId)))
                .map(this::deserializeDish);
    }

    @Override
    public Mono<Integer> removeDishesByCategoryIn(Collection<String> categoryIds) {
        return Mono.from(dishesCollection.deleteMany(Filters.in(CATEGORY_ID, categoryIds)))
                .map(deleteResult -> {
                    log.info("Deletion result of dish by category in {} = {}", categoryIds, deleteResult);
                    return (int) deleteResult.getDeletedCount();
                });
    }

    @Override
    public Mono<Integer> removeDishById(String dishId) {
        return Mono.from(dishesCollection.deleteMany(Filters.eq(DISH_ID, dishId)))
                .map(deleteResult -> {
                    log.info("Deletion result of dish by id {} = {}", dishId, deleteResult);
                    return (int) deleteResult.getDeletedCount();
                });
    }

    @Override
    public Mono<Void> addNewDish(Dish dish) {
        return Mono.from(dishesCollection.insertOne(serializeDish(dish)))
                .map(insertOneResult -> {
                    log.info("Insertion result of dish {} = {}", dish, insertOneResult);
                    return insertOneResult;
                })
                .then();
    }

    @Override
    public Mono<Integer> updateDishDetails(UpdateDishDetailsRequest updateDishDetailsRequest) {
        return Mono.from(dishesCollection.updateMany(
                        Filters.eq(updateDishDetailsRequest.getDishId()),
                        calculateUpdates(updateDishDetailsRequest)))
                .map(updateResult -> {
                    log.info("Update result of dish {} = {}", updateDishDetailsRequest.getDishId(), updateResult);
                    return (int) updateResult.getModifiedCount();
                });
    }

    private Bson calculateUpdates(UpdateDishDetailsRequest updateDishDetailsRequest) {
        return Updates.combine(
                Updates.set(CATEGORY_ID, updateDishDetailsRequest.getCategoryId()),
                Updates.set(TITLE, updateDishDetailsRequest.getTitle()),
                Updates.set(DESCRIPTION, updateDishDetailsRequest.getDescription()),
                Updates.set(AVAILABILITY, updateDishDetailsRequest.getAvailability())
        );
    }

    private Dish deserializeDish(Document document) {
        return Dish.newBuilder()
                .setDishId(document.getObjectId(DISH_ID).toString())
                .setCategoryId(document.getString(CATEGORY_ID))
                .setTitle(document.getString(TITLE))
                .setDescription(document.getString(DESCRIPTION))
                .setAvailability(document.getBoolean(AVAILABILITY))
                .build();
    }

    private Document serializeDish(Dish dish) {
        return new Document()
                .append(DISH_ID, dish.getDishId())
                .append(CATEGORY_ID, dish.getCategoryId())
                .append(TITLE, dish.getTitle())
                .append(DESCRIPTION, dish.getDescription())
                .append(AVAILABILITY, dish.getAvailability());
    }
}
