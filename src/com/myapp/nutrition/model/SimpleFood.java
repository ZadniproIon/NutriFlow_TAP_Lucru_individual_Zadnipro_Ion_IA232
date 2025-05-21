package com.myapp.nutrition.model;
import java.io.Serializable;

public class SimpleFood extends FoodItem implements Serializable {
    private static final long serialVersionUID = 1L;

    public SimpleFood(String name, NutritionValue per100g) {
        super(name, per100g);
    }

    @Override
    public String toString() {
        return getName() + " — " + getPer100g().getCalories() + " kcal per 100g";
    }
}
