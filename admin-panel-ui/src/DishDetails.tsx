import React, {useEffect, useState} from 'react';
import {Button, Switch, Typography} from "antd";
import {
    DishServiceClient,
} from "./Menu-items-serviceServiceClientPb";
import {
    Dish,
    RemoveDishRequest,
    UpdateDishDetailsRequest
} from "./menu-items-service_pb";
// @ts-ignore
import EasyEdit from 'react-easy-edit';


export interface DishDetailsProps {
    dish: Dish;
    dishService: DishServiceClient;
    onBack: () => void
}

export const DishDetailsComponent = ({dish, dishService, onBack}: DishDetailsProps) => {
    const [dishName, setDishName] = useState<string>(dish.getTitle());
    const [dishDescription, setDishDescription] = useState<string>(dish.getDescription());
    const [isAvailable, setAvailable] = useState<boolean>(dish.getAvailability());

    useEffect(() => {
        const updateDishDetailsRequest = new UpdateDishDetailsRequest();
        updateDishDetailsRequest.setDishid(dish.getDishid());
        updateDishDetailsRequest.setCategoryid(dish.getCategoryid());
        updateDishDetailsRequest.setTitle(dishName || dish.getTitle());
        updateDishDetailsRequest.setDescription(dishDescription || dish.getDescription());
        updateDishDetailsRequest.setAvailability(isAvailable !== undefined ? isAvailable : dish.getAvailability());
        dishService.updateDishDetails(updateDishDetailsRequest, {})
            .then(ignored => {
                console.log("Updated :)");
            });
    }, [dishName, dishDescription, isAvailable, dishService, dish]);

    const onDeleteDish = () => {
        const removeDishRequest = new RemoveDishRequest();
        removeDishRequest.setDishid(dish.getDishid());
        dishService.removeDish(removeDishRequest, {})
            .then(ignored => {
                console.log("Dish removed!");
                onBack();
            });
    };
    return (
        <>
            <Typography.Title>Dish {dish.getTitle()}</Typography.Title>
            <EasyEdit
                value={dishName}
                type="text"
                onSave={setDishName}
                saveButtonLabel="Save"
                cancelButtonLabel="Cancel"
                attributes={{name: "awesome-input", id: 1}}
            />
            <EasyEdit
                value={dishDescription}
                type="text"
                onSave={setDishDescription}
                saveButtonLabel="Save"
                cancelButtonLabel="Cancel"
                attributes={{name: "awesome-input", id: 1}}
            />
            <Switch checked={isAvailable} onChange={setAvailable}/>
            <Button type="dashed" onClick={onDeleteDish}>Delete dish</Button>
            <Button type="link" onClick={() => onBack()}>Back</Button>
        </>
    );
};