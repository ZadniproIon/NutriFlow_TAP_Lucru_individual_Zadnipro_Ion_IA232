package com.myapp.nutrition.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MealEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDate date;
    private MealType type;
    private List<FoodPortion> portions = new ArrayList<>();

    public MealEntry(LocalDate date, MealType type) {
        this.date = date;
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public MealType getType() {
        return type;
    }

    public List<FoodPortion> getPortions() {
        return portions;
    }

    public void addPortion(FoodPortion portion) {
        portions.add(portion);
    }

    public void removePortion(FoodPortion portion) {
        portions.remove(portion);
    }

    public NutritionValue getTotalNutrition() {
        return portions.stream()
                .map(FoodPortion::getTotalNutrition)
                .reduce(new NutritionValue(0,0,0,0), NutritionValue::add);
    }


}
