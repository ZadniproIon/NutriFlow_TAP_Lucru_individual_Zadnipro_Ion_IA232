package com.myapp.nutrition.ui;

import com.myapp.nutrition.dao.FileGenericDAO;
import com.myapp.nutrition.model.SimpleFood;
import com.myapp.nutrition.model.NutritionValue;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Clasă de inițializare: populați foods.dat cu alimente de start.
 */
public class FoodDataInitializer {
    public static void main(String[] args) {
        try {
            // Creează DAO-ul pentru foods.dat
            FileGenericDAO<SimpleFood> foodDAO =
                    new FileGenericDAO<>(Paths.get("src/data/foods.dat"));

            // Definim lista de alimente
            List<SimpleFood> foods = new ArrayList<>();
            foods.add(new SimpleFood("Apple",   new NutritionValue(52, 0.3, 14, 0.2)));
            foods.add(new SimpleFood("Banana",  new NutritionValue(89, 1.1, 23, 0.3)));
            foods.add(new SimpleFood("Rice",    new NutritionValue(130, 2.4, 28, 0.3)));
            foods.add(new SimpleFood("Chicken", new NutritionValue(239, 27, 0, 14)));
            foods.add(new SimpleFood("Egg",     new NutritionValue(155, 13, 1.1, 11)));
            foods.add(new SimpleFood("Milk",    new NutritionValue(42, 3.4, 5, 1)));
            foods.add(new SimpleFood("Almonds", new NutritionValue(576, 21, 22, 49)));
            foods.add(new SimpleFood("Oats",    new NutritionValue(389, 17, 66, 7)));
            foods.add(new SimpleFood("Broccoli",new NutritionValue(55, 3.7, 11, 0.6)));

            // Salvăm lista în foods.dat (va suprascrie ce era înainte)
            foodDAO.saveAll(foods);

            System.out.println("Initialized foods.dat with " + foods.size() + " items.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Eroare la inițializarea foods.dat: " + e.getMessage());
        }
    }
}
