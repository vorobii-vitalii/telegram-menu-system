package lpnu.telegram.menu.system.dao;

import lpnu.telegram.menu.system.AddCategoryRequest;
import lpnu.telegram.menu.system.Category;
import lpnu.telegram.menu.system.ChangeCategoryDetailsRequest;
import lpnu.telegram.menu.system.dto.FullCategoryDetail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;

public interface MenuCategoryDAO {
    Mono<FullCategoryDetail> getCategoryDetails(String categoryId);
    Flux<Category> getChildMenuCategories(String parentCategoryId);
    Flux<String> searchForChildrenCategoriesRecursive(String rootCategoryId);
    Mono<Integer> removeMenuCategories(Collection<String> categoryIds);
    Mono<Integer> updateMenuCategory(ChangeCategoryDetailsRequest changeCategoryDetailsRequest);
    Mono<Void> addMenuCategory(AddCategoryRequest addCategoryRequest);
}
