package com.myapp.nutrition.model;
import java.io.Serializable;

public abstract class FoodItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private NutritionValue per100g;

    public FoodItem(String name, NutritionValue per100g) {
        this.name = name;
        this.per100g = per100g;
    }

    public String getName() {
        return name;
    }

    public NutritionValue getPer100g() {
        return per100g;
    }

    @Override
    public String toString() {
        return name + " (" +
                per100g.getCalories() + " kcal /100g)";
    }
}
