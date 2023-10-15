import React from 'react';
import {
    CategoryDetailsServiceClient,
    DishServiceClient,
    MenuCategoriesServiceClient
} from "./Menu-items-serviceServiceClientPb";
import {CategoryDetailsComponent} from "./CategoryDetailsComponent";

const URL = "http://localhost:8080";

const categoriesService = new MenuCategoriesServiceClient(URL, null, null);
const categoriesDetailsService = new CategoryDetailsServiceClient(URL, null, null);
const dishServiceClient = new DishServiceClient(URL, null, null);

function App() {
    return (
        <CategoryDetailsComponent
            categoryDetailsService={categoriesDetailsService}
            categoriesService={categoriesService}
            dishService={dishServiceClient}
        />
    );
}

export default App;
