package lpnu.telegram.menu.system.dao.mongo;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import com.mongodb.reactivestreams.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lpnu.telegram.menu.system.AddCategoryRequest;
import lpnu.telegram.menu.system.ChangeCategoryDetailsRequest;
import lpnu.telegram.menu.system.dao.MenuCategoryDAO;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class MongoMenuCategoryDAOImpl implements MenuCategoryDAO {
    private static final String CATEGORY_ID = "_id";
    private static final String PARENT_CATEGORY_ID = "parentCategoryId";
    private static final String NAME = "name";

    private final MongoCollection<Document> menuCategoryCollection;

    @Override
    public Flux<String> searchForChildrenCategoriesRecursive(String rootMenuId) {
        return searchForChildrenCategoriesRecursive(List.of(rootMenuId));
    }

    @Override
    public Mono<Integer> removeMenuCategories(Collection<String> categoryIds) {
        return Mono.from(menuCategoryCollection.deleteMany(Filters.in(CATEGORY_ID, categoryIds)))
                .map(deleteResult -> {
                    log.info("Delete result of menu categories {}", deleteResult);
                    return (int) deleteResult.getDeletedCount();
                });
    }

    private Flux<String> searchForChildrenCategoriesRecursive(Collection<String> parentCategoryIds) {
        if (parentCategoryIds.isEmpty()) {
            return Flux.empty();
        }
        return Flux.from(menuCategoryCollection.find(Filters.in(PARENT_CATEGORY_ID, parentCategoryIds))
                        .projection(Projections.include(CATEGORY_ID)))
                .map(e -> e.getObjectId(CATEGORY_ID))
                .map(ObjectId::toString)
                .buffer()
                .flatMap(categoryIds -> Flux.concat(
                        Flux.fromIterable(categoryIds),
                        searchForChildrenCategoriesRecursive(categoryIds)
                ));
    }

    @Override
    public Mono<Integer> updateMenuCategory(ChangeCategoryDetailsRequest request) {
        var categoryId = request.getCategoryId();
        return Mono.from(menuCategoryCollection.updateMany(Filters.eq(CATEGORY_ID, categoryId), calculateUpdates(request)))
                .map(updateResult -> {
                    log.info("Update menu category result = {}", updateResult);
                    return (int) updateResult.getModifiedCount();
                });
    }

    private Bson calculateUpdates(ChangeCategoryDetailsRequest request) {
        return Updates.combine(
                Updates.set(NAME, request.getName()),
                Updates.set(PARENT_CATEGORY_ID, request.getParentCategoryId()));
    }

    @Override
    public Mono<Void> addMenuCategory(AddCategoryRequest addCategoryRequest) {
        return Flux.from(menuCategoryCollection.insertOne(serializeCategory(addCategoryRequest)))
                .map(insertOneResult -> {
                    log.info("Menu category insert result = {}", insertOneResult);
                    return insertOneResult;
                })
                .then();
    }

    private Document serializeCategory(AddCategoryRequest request) {
        return new Document()
                .append(CATEGORY_ID, request.getCategoryId())
                .append(PARENT_CATEGORY_ID, request.getParentCategoryId())
                .append(NAME, request.getName());
    }

}
