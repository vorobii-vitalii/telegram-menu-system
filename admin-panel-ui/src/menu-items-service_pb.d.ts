import * as jspb from 'google-protobuf'



export class AddCategoryRequest extends jspb.Message {
  getCategoryid(): string;
  setCategoryid(value: string): AddCategoryRequest;

  getParentcategoryid(): string;
  setParentcategoryid(value: string): AddCategoryRequest;

  getName(): string;
  setName(value: string): AddCategoryRequest;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): AddCategoryRequest.AsObject;
  static toObject(includeInstance: boolean, msg: AddCategoryRequest): AddCategoryRequest.AsObject;
  static serializeBinaryToWriter(message: AddCategoryRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): AddCategoryRequest;
  static deserializeBinaryFromReader(message: AddCategoryRequest, reader: jspb.BinaryReader): AddCategoryRequest;
}

export namespace AddCategoryRequest {
  export type AsObject = {
    categoryid: string,
    parentcategoryid: string,
    name: string,
  }
}

export class ChangeCategoryDetailsRequest extends jspb.Message {
  getCategoryid(): string;
  setCategoryid(value: string): ChangeCategoryDetailsRequest;

  getParentcategoryid(): string;
  setParentcategoryid(value: string): ChangeCategoryDetailsRequest;

  getName(): string;
  setName(value: string): ChangeCategoryDetailsRequest;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): ChangeCategoryDetailsRequest.AsObject;
  static toObject(includeInstance: boolean, msg: ChangeCategoryDetailsRequest): ChangeCategoryDetailsRequest.AsObject;
  static serializeBinaryToWriter(message: ChangeCategoryDetailsRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): ChangeCategoryDetailsRequest;
  static deserializeBinaryFromReader(message: ChangeCategoryDetailsRequest, reader: jspb.BinaryReader): ChangeCategoryDetailsRequest;
}

export namespace ChangeCategoryDetailsRequest {
  export type AsObject = {
    categoryid: string,
    parentcategoryid: string,
    name: string,
  }
}

export class RemoveCategoryRequest extends jspb.Message {
  getCategoryid(): string;
  setCategoryid(value: string): RemoveCategoryRequest;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): RemoveCategoryRequest.AsObject;
  static toObject(includeInstance: boolean, msg: RemoveCategoryRequest): RemoveCategoryRequest.AsObject;
  static serializeBinaryToWriter(message: RemoveCategoryRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): RemoveCategoryRequest;
  static deserializeBinaryFromReader(message: RemoveCategoryRequest, reader: jspb.BinaryReader): RemoveCategoryRequest;
}

export namespace RemoveCategoryRequest {
  export type AsObject = {
    categoryid: string,
  }
}

export class FetchCategoryDetailsRequest extends jspb.Message {
  getCategoryid(): string;
  setCategoryid(value: string): FetchCategoryDetailsRequest;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): FetchCategoryDetailsRequest.AsObject;
  static toObject(includeInstance: boolean, msg: FetchCategoryDetailsRequest): FetchCategoryDetailsRequest.AsObject;
  static serializeBinaryToWriter(message: FetchCategoryDetailsRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): FetchCategoryDetailsRequest;
  static deserializeBinaryFromReader(message: FetchCategoryDetailsRequest, reader: jspb.BinaryReader): FetchCategoryDetailsRequest;
}

export namespace FetchCategoryDetailsRequest {
  export type AsObject = {
    categoryid: string,
  }
}

export class AddDishRequest extends jspb.Message {
  getDishid(): string;
  setDishid(value: string): AddDishRequest;

  getCategoryid(): string;
  setCategoryid(value: string): AddDishRequest;

  getTitle(): string;
  setTitle(value: string): AddDishRequest;

  getDescription(): string;
  setDescription(value: string): AddDishRequest;

  getAvailability(): boolean;
  setAvailability(value: boolean): AddDishRequest;

  getImagebase64(): string;
  setImagebase64(value: string): AddDishRequest;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): AddDishRequest.AsObject;
  static toObject(includeInstance: boolean, msg: AddDishRequest): AddDishRequest.AsObject;
  static serializeBinaryToWriter(message: AddDishRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): AddDishRequest;
  static deserializeBinaryFromReader(message: AddDishRequest, reader: jspb.BinaryReader): AddDishRequest;
}

export namespace AddDishRequest {
  export type AsObject = {
    dishid: string,
    categoryid: string,
    title: string,
    description: string,
    availability: boolean,
    imagebase64: string,
  }
}

export class UpdateDishDetailsRequest extends jspb.Message {
  getDishid(): string;
  setDishid(value: string): UpdateDishDetailsRequest;

  getCategoryid(): string;
  setCategoryid(value: string): UpdateDishDetailsRequest;

  getTitle(): string;
  setTitle(value: string): UpdateDishDetailsRequest;

  getDescription(): string;
  setDescription(value: string): UpdateDishDetailsRequest;

  getAvailability(): boolean;
  setAvailability(value: boolean): UpdateDishDetailsRequest;

  getImagebase64(): string;
  setImagebase64(value: string): UpdateDishDetailsRequest;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): UpdateDishDetailsRequest.AsObject;
  static toObject(includeInstance: boolean, msg: UpdateDishDetailsRequest): UpdateDishDetailsRequest.AsObject;
  static serializeBinaryToWriter(message: UpdateDishDetailsRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): UpdateDishDetailsRequest;
  static deserializeBinaryFromReader(message: UpdateDishDetailsRequest, reader: jspb.BinaryReader): UpdateDishDetailsRequest;
}

export namespace UpdateDishDetailsRequest {
  export type AsObject = {
    dishid: string,
    categoryid: string,
    title: string,
    description: string,
    availability: boolean,
    imagebase64: string,
  }
}

export class RemoveDishRequest extends jspb.Message {
  getDishid(): string;
  setDishid(value: string): RemoveDishRequest;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): RemoveDishRequest.AsObject;
  static toObject(includeInstance: boolean, msg: RemoveDishRequest): RemoveDishRequest.AsObject;
  static serializeBinaryToWriter(message: RemoveDishRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): RemoveDishRequest;
  static deserializeBinaryFromReader(message: RemoveDishRequest, reader: jspb.BinaryReader): RemoveDishRequest;
}

export namespace RemoveDishRequest {
  export type AsObject = {
    dishid: string,
  }
}

export class Dish extends jspb.Message {
  getDishid(): string;
  setDishid(value: string): Dish;

  getCategoryid(): string;
  setCategoryid(value: string): Dish;

  getTitle(): string;
  setTitle(value: string): Dish;

  getDescription(): string;
  setDescription(value: string): Dish;

  getAvailability(): boolean;
  setAvailability(value: boolean): Dish;

  getImageuri(): string;
  setImageuri(value: string): Dish;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): Dish.AsObject;
  static toObject(includeInstance: boolean, msg: Dish): Dish.AsObject;
  static serializeBinaryToWriter(message: Dish, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): Dish;
  static deserializeBinaryFromReader(message: Dish, reader: jspb.BinaryReader): Dish;
}

export namespace Dish {
  export type AsObject = {
    dishid: string,
    categoryid: string,
    title: string,
    description: string,
    availability: boolean,
    imageuri: string,
  }
}

export class GetDishRequest extends jspb.Message {
  getDishid(): string;
  setDishid(value: string): GetDishRequest;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): GetDishRequest.AsObject;
  static toObject(includeInstance: boolean, msg: GetDishRequest): GetDishRequest.AsObject;
  static serializeBinaryToWriter(message: GetDishRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): GetDishRequest;
  static deserializeBinaryFromReader(message: GetDishRequest, reader: jspb.BinaryReader): GetDishRequest;
}

export namespace GetDishRequest {
  export type AsObject = {
    dishid: string,
  }
}

export class CategoryDetails extends jspb.Message {
  getCategoryid(): string;
  setCategoryid(value: string): CategoryDetails;

  getParentcategoryid(): string;
  setParentcategoryid(value: string): CategoryDetails;

  getName(): string;
  setName(value: string): CategoryDetails;

  getChildcategoryList(): Array<Category>;
  setChildcategoryList(value: Array<Category>): CategoryDetails;
  clearChildcategoryList(): CategoryDetails;
  addChildcategory(value?: Category, index?: number): Category;

  getDishList(): Array<Dish>;
  setDishList(value: Array<Dish>): CategoryDetails;
  clearDishList(): CategoryDetails;
  addDish(value?: Dish, index?: number): Dish;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): CategoryDetails.AsObject;
  static toObject(includeInstance: boolean, msg: CategoryDetails): CategoryDetails.AsObject;
  static serializeBinaryToWriter(message: CategoryDetails, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): CategoryDetails;
  static deserializeBinaryFromReader(message: CategoryDetails, reader: jspb.BinaryReader): CategoryDetails;
}

export namespace CategoryDetails {
  export type AsObject = {
    categoryid: string,
    parentcategoryid: string,
    name: string,
    childcategoryList: Array<Category.AsObject>,
    dishList: Array<Dish.AsObject>,
  }
}

export class Category extends jspb.Message {
  getCategoryid(): string;
  setCategoryid(value: string): Category;

  getName(): string;
  setName(value: string): Category;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): Category.AsObject;
  static toObject(includeInstance: boolean, msg: Category): Category.AsObject;
  static serializeBinaryToWriter(message: Category, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): Category;
  static deserializeBinaryFromReader(message: Category, reader: jspb.BinaryReader): Category;
}

export namespace Category {
  export type AsObject = {
    categoryid: string,
    name: string,
  }
}

export class Categories extends jspb.Message {
  getCategoryList(): Array<Category>;
  setCategoryList(value: Array<Category>): Categories;
  clearCategoryList(): Categories;
  addCategory(value?: Category, index?: number): Category;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): Categories.AsObject;
  static toObject(includeInstance: boolean, msg: Categories): Categories.AsObject;
  static serializeBinaryToWriter(message: Categories, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): Categories;
  static deserializeBinaryFromReader(message: Categories, reader: jspb.BinaryReader): Categories;
}

export namespace Categories {
  export type AsObject = {
    categoryList: Array<Category.AsObject>,
  }
}

export class Empty extends jspb.Message {
  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): Empty.AsObject;
  static toObject(includeInstance: boolean, msg: Empty): Empty.AsObject;
  static serializeBinaryToWriter(message: Empty, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): Empty;
  static deserializeBinaryFromReader(message: Empty, reader: jspb.BinaryReader): Empty;
}

export namespace Empty {
  export type AsObject = {
  }
}

