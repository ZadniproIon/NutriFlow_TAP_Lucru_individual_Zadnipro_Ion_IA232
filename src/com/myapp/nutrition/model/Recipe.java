package com.myapp.nutrition.model;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

public class Recipe extends FoodItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<FoodPortion> ingredients = new ArrayList<>();

    public Recipe(String name) {
        super(name, new NutritionValue(0,0,0,0));
    }

    public void addIngredient(FoodPortion portion) {
        ingredients.add(portion);
    }

    @Override
    public NutritionValue getPer100g() {
        // TODO: implementează calculul pe baza ingredientelor
        return super.getPer100g();
    }
}
