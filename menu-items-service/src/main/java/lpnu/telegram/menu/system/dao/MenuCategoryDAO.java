package lpnu.telegram.menu.system.dao;

import lpnu.telegram.menu.system.AddCategoryRequest;
import lpnu.telegram.menu.system.ChangeCategoryDetailsRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;

public interface MenuCategoryDAO {
    Flux<String> searchForChildrenCategoriesRecursive(String rootCategoryId);
    Mono<Integer> removeMenuCategories(Collection<String> categoryIds);
    Mono<Integer> updateMenuCategory(ChangeCategoryDetailsRequest changeCategoryDetailsRequest);
    Mono<Void> addMenuCategory(AddCategoryRequest addCategoryRequest);
}
