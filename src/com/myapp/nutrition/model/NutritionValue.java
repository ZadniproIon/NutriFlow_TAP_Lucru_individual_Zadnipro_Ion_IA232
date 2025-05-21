package com.myapp.nutrition.model;
import java.io.Serializable;

public class NutritionValue implements Serializable {
    private static final long serialVersionUID = 1L;
    private double calories;
    private double protein;
    private double carbs;
    private double fats;

    public NutritionValue(double calories, double protein, double carbs, double fats) {
        this.calories = calories;
        this.protein  = protein;
        this.carbs    = carbs;
        this.fats     = fats;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFats() {
        return fats;
    }

    public NutritionValue add(NutritionValue other) {
        return new NutritionValue(
                this.calories + other.calories,
                this.protein  + other.protein,
                this.carbs    + other.carbs,
                this.fats     + other.fats
        );
    }

    public NutritionValue scale(double factor) {
        return new NutritionValue(
                this.calories * factor,
                this.protein  * factor,
                this.carbs    * factor,
                this.fats     * factor
        );
    }
}
