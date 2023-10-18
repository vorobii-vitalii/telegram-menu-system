import React, {useEffect, useState} from 'react';
import {Button, Divider, Form, Input, Switch, Table, Typography} from "antd";
import {
    CategoryDetailsServiceClient,
    DishServiceClient,
    MenuCategoriesServiceClient
} from "./Menu-items-serviceServiceClientPb";
import {
    AddCategoryRequest, AddDishRequest,
    CategoryDetails, ChangeCategoryDetailsRequest, Dish,
    FetchCategoryDetailsRequest,
    RemoveCategoryRequest
} from "./menu-items-service_pb";
import {v4 as uuidv4} from 'uuid';
// @ts-ignore
import EasyEdit from 'react-easy-edit';
import {DishDetailsComponent} from "./DishDetails";

export interface CategoryDetailsProps {
    onBack?: () => void,
    categoryId?: string,
    categoriesService: MenuCategoriesServiceClient,
    categoryDetailsService: CategoryDetailsServiceClient,
    dishService: DishServiceClient
}

export const CategoryDetailsComponent = (props: CategoryDetailsProps) => {
    const categoryId = props.categoryId;
    const [categoryDetails, setCategoryDetails] = useState<CategoryDetails>();
    const [categoryName, setCategoryName] = useState<string>()
    const [categories, setCategories] = useState<Array<any> | undefined>();
    const [dishes, setDishes] = useState<Array<any> | undefined>()
    const [selectedChildCategory, setSelectedChildCategory] = useState<string>();
    const [selectedDishId, setSelectedDishId] = useState<string>()

    const categoriesTableColumns = [
        {
            title: 'Category id',
            dataIndex: 'categoryId',
            key: 'categoryId'
        },
        {
            title: 'Category name',
            dataIndex: 'categoryName',
            key: 'categoryName'
        },
        {
            title: 'Show more details',
            key: 'showMoreDetails',
            dataIndex: 'showMoreDetails',
            render: (text: any, record: any) => (
                <Button type="primary" onClick={() => setSelectedChildCategory(record.categoryId)}>Show more
                    details</Button>
            )
        }
    ];

    const dishesTableColumns = [
        {
            title: 'Dish id',
            dataIndex: 'dishId',
            key: 'dishId'
        },
        {
            title: 'Dish name',
            dataIndex: 'dishName',
            key: 'dishName'
        },
        {
            title: 'Dish details',
            key: 'showMoreDetails',
            dataIndex: 'showMoreDetails',
            render: (text: any, record: any) => (
                <Button type="primary" onClick={() => {
                    console.log("Opening dish details.")
                    setSelectedDishId(record.dishId);
                }}>Show more details</Button>
            )
        }
    ];

    const removeCurrentCategory = () => {
        if (!categoryId) {
            console.warn("Root category should be removed");
            return;
        }
        const removeCategoryRequest = new RemoveCategoryRequest();
        removeCategoryRequest.setCategoryid(categoryId);
        props.categoriesService.removeCategory(removeCategoryRequest, {})
            .then(ignored => {
                console.log(`Category ${categoryId} was removed.`);
                props.onBack && props.onBack();
            });
    };

    useEffect(() => {
        const fetchCategoryDetailsRequest = new FetchCategoryDetailsRequest();
        // Can be null if root
        if (categoryId) {
            fetchCategoryDetailsRequest.setCategoryid(categoryId);
        }
        props.categoryDetailsService.fetchMenuCategoryDetails(fetchCategoryDetailsRequest, {})
            .then(details => {
                setCategoryDetails(details);
                setCategoryName(details.getName());
                setCategories(details.getChildcategoryList()
                    .map(e => ({
                        categoryId: e.getCategoryid(),
                        categoryName: e.getName()
                    })));
                setDishes(details.getDishList().map(e => ({
                    dishId: e.getDishid(),
                    dishName: e.getTitle()
                })))
            });
    }, [props.categoryDetailsService, categoryId, selectedChildCategory, selectedDishId]);

    if (!categoryDetails) {
        return <Typography.Text>Category details are still loading...</Typography.Text>;
    }
    if (selectedChildCategory) {
        return (
            <CategoryDetailsComponent
                key={selectedChildCategory}
                onBack={() => setSelectedChildCategory(undefined)}
                categoriesService={props.categoriesService}
                categoryDetailsService={props.categoryDetailsService}
                categoryId={selectedChildCategory}
                dishService={props.dishService}
            />
        )
    }
    if (selectedDishId) {
        console.log("Opening dish details.")
        const dish : Dish = categoryDetails.getDishList().find(e => e.getDishid() === selectedDishId)!!;
        return (
            <DishDetailsComponent
                key={selectedDishId}
                dish={dish}
                dishService={props.dishService}
                onBack={() => setSelectedDishId(undefined)}
            />
        );
    }
    const onCategoryNameEdit = (newCategoryName: string) => {
        if (!categoryId) {
            return;
        }
        const changeCategoryDetailsRequest = new ChangeCategoryDetailsRequest();
        changeCategoryDetailsRequest.setCategoryid(categoryId);
        changeCategoryDetailsRequest.setName(newCategoryName);
        changeCategoryDetailsRequest.setParentcategoryid(categoryDetails.getParentcategoryid());
        props.categoriesService.changeCategoryDetails(changeCategoryDetailsRequest, {})
            .then(ignored => {
                console.log(`Category ${categoryId} name edited!`);
                setCategoryName(newCategoryName);
            });
    };

    const onNewCategoryCreate = (values: any) => {
        const categoryName = values.categoryName;
        const addCategoryRequest = new AddCategoryRequest();
        const newCategoryId = uuidv4();
        addCategoryRequest.setCategoryid(newCategoryId);
        addCategoryRequest.setName(categoryName);
        if (categoryId) {
            addCategoryRequest.setParentcategoryid(categoryId);
        }
        props.categoriesService.addCategory(addCategoryRequest, {})
            .then(ignored => {
                console.log(`New category ${newCategoryId} created!`);
                setCategories([
                    ...(categories || []),
                    {
                        categoryId: newCategoryId,
                        categoryName: categoryName,
                        key: newCategoryId
                    }
                ])
            });
    };

    const toDish = (addDishRequest: AddDishRequest): Dish => {
        const dish = new Dish();
        dish.setCategoryid(addDishRequest.getCategoryid());
        dish.setDishid(addDishRequest.getDishid());
        dish.setTitle(addDishRequest.getTitle());
        dish.setDescription(addDishRequest.getDescription());
        dish.setAvailability(addDishRequest.getAvailability());
        return dish;
    };

    const onNewDishCreate = (values: any) => {
        const dishName = values.dishName;
        const dishDescription = values.dishDescription;
        const isAvailable = values.availability;
        const dishId = uuidv4();
        const addDishRequest = new AddDishRequest();
        categoryId && addDishRequest.setCategoryid(categoryId);
        addDishRequest.setDishid(dishId);
        addDishRequest.setTitle(dishName);
        addDishRequest.setDescription(dishDescription);
        addDishRequest.setAvailability(isAvailable);
        categoryDetails.addDish(toDish(addDishRequest));
        props.dishService.addDish(addDishRequest, {})
            .then(ignored => {
                console.log("Dish created!");
                setDishes([
                    ...(dishes || []),
                    {
                        dishId: dishId,
                        dishName: dishName,
                        key: dishId
                    }
                ]);
            });
    };

    return (
        <>
            <Typography.Title>Category details</Typography.Title>
            {
                categoryDetails && categoryDetails.getName() && (
                    <EasyEdit
                        value={categoryName}
                        type="text"
                        onSave={onCategoryNameEdit}
                        saveButtonLabel="Save"
                        cancelButtonLabel="Cancel"
                        attributes={{name: "awesome-input", id: 1}}
                    />
                )
            }
            {
                categoryDetails.getCategoryid() && (
                    <Button type="dashed" onClick={() => removeCurrentCategory()}>Remove category</Button>
                )
            }
            {
                props.onBack && (
                    <Button type="link" onClick={() => props.onBack && props.onBack()}>Go back</Button>
                )
            }

            <Typography.Title>Children categories:</Typography.Title>
            <Table
                bordered={true}
                dataSource={categories}
                columns={categoriesTableColumns}
            />

            <Typography.Title>Create new children category:</Typography.Title>
            <Form colon={true} onFinish={onNewCategoryCreate}>
                <Form.Item label="Category name" name="categoryName" rules={[{required: true}]}>
                    <Input/>
                </Form.Item>
                <Form.Item label="">
                    <Button type="primary" htmlType="submit">
                        Create new category
                    </Button>
                </Form.Item>
            </Form>
            <Divider />

            <Typography.Title>Dishes:</Typography.Title>
            <Table
                bordered={true}
                dataSource={dishes}
                columns={dishesTableColumns}
            />
            <Typography.Title>Add new dish</Typography.Title>
            <Form colon={true} onFinish={onNewDishCreate}>
                <Form.Item label="Dish name" name="dishName" rules={[{required: true}]}>
                    <Input/>
                </Form.Item>
                <Form.Item label="Dish description" name="dishDescription" rules={[{required: true}]}>
                    <Input/>
                </Form.Item>
                <Form.Item label="Is available?" name="availability" rules={[{required: true}]}>
                    <Switch />
                </Form.Item>
                <Form.Item label="">
                    <Button type="primary" htmlType="submit">
                        Create new dish
                    </Button>
                </Form.Item>
            </Form>
        </>
    );
};