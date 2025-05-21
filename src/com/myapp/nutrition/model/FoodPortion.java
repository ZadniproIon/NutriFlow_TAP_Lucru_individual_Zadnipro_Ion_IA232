package com.myapp.nutrition.model;

import java.io.Serializable;

public class FoodPortion implements Serializable {
    private static final long serialVersionUID = 1L;

    private final FoodItem item;
    private double weightGrams;

    public FoodPortion(FoodItem item, double weightGrams) {
        if (weightGrams <= 0) {
            throw new IllegalArgumentException("Weight must be > 0");
        }
        this.item = item;
        this.weightGrams = weightGrams;
    }

    public FoodItem getItem() {
        return item;
    }

    public double getWeightGrams() {
        return weightGrams;
    }

    public void setWeightGrams(double weightGrams) {
        if (weightGrams <= 0) {
            throw new IllegalArgumentException("Weight must be > 0");
        }
        this.weightGrams = weightGrams;
    }

    public NutritionValue getTotalNutrition() {
        return item.getPer100g()
                .scale(weightGrams / 100.0);
    }

    @Override
    public String toString() {
        return item.getName() + " – " + weightGrams + "g";
    }
}
