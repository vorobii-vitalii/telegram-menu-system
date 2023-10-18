package lpnu.telegram.menu.system.dao;

import lpnu.telegram.menu.system.AddDishRequest;
import lpnu.telegram.menu.system.Dish;
import lpnu.telegram.menu.system.UpdateDishDetailsRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface DishesDAO {
    Mono<Dish> getDishById(String dishId);
    Flux<Dish> getDishesByCategoryId(String categoryId);
    Mono<Integer> removeDishesByCategoryIn(Collection<String> categoryIds);
    Mono<Integer> removeDishById(String dishId);
    Mono<Void> addNewDish(AddDishRequest addDishRequest);
    Mono<Integer> updateDishDetails(UpdateDishDetailsRequest updateDishDetailsRequest);
}
