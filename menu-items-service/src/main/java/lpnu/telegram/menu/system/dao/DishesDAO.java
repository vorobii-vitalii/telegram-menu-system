package lpnu.telegram.menu.system.dao;

import lpnu.telegram.menu.system.Dish;
import lpnu.telegram.menu.system.UpdateDishDetailsRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface DishesDAO {
    Flux<Dish> getDishesByCategoryId(String categoryId);
    Mono<Integer> removeDishesByCategoryIn(Collection<String> categoryIds);
    Mono<Integer> removeDishById(String dishId);
    Mono<Void> addNewDish(Dish dish);
    Mono<Integer> updateDishDetails(UpdateDishDetailsRequest updateDishDetailsRequest);
}
