package lpnu.telegram.menu.system;

import com.mongodb.reactivestreams.client.MongoClients;
import io.grpc.ServerBuilder;
import lpnu.telegram.menu.system.dao.mongo.MongoDishesDAOImpl;
import lpnu.telegram.menu.system.dao.mongo.MongoMenuCategoryDAOImpl;
import lpnu.telegram.menu.system.dao.mongo.MongoTransactionalService;
import lpnu.telegram.menu.system.gprc.CategoryDetailsServiceImpl;
import lpnu.telegram.menu.system.gprc.DishServiceImpl;
import lpnu.telegram.menu.system.gprc.MenuCategoriesServiceImpl;

import java.io.IOException;

public class MenuManagementServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        var mongoClient = MongoClients.create(getMongoConnectionURL());
        var mongoClientDatabase = mongoClient.getDatabase(getDatabaseName());
        var dishesCollection = mongoClientDatabase.getCollection(getDishesCollection());
        var menuCategoriesCollection = mongoClientDatabase.getCollection(getMenuCategoriesName());

        var dishDAO = new MongoDishesDAOImpl(dishesCollection);
        var menuCategoriesDAO = new MongoMenuCategoryDAOImpl(menuCategoriesCollection);
        var transactionalService = new MongoTransactionalService(mongoClient);

        var server = ServerBuilder.forPort(getPort())
                .addService(new DishServiceImpl(dishDAO))
                .addService(new CategoryDetailsServiceImpl(dishDAO, menuCategoriesDAO))
                .addService(new MenuCategoriesServiceImpl(menuCategoriesDAO, dishDAO, transactionalService))
                .build()
                .start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.awaitTermination();
    }

    private static int getPort() {
        return Integer.parseInt(System.getenv("PORT"));
    }

    private static String getMongoConnectionURL() {
        return System.getenv("MONGO_URL");
    }

    private static String getDatabaseName() {
        return System.getenv("MONGO_DB_NAME");
    }

    private static String getDishesCollection() {
        return System.getenv("DISHES_COLLECTION_NAME");
    }

    private static String getMenuCategoriesName() {
        return System.getenv("MENU_CATEGORIES_COLLECTION_NAME");
    }

}
