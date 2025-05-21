package com.myapp.nutrition.service;
import com.myapp.nutrition.dao.GenericDAO;
import com.myapp.nutrition.model.FoodPortion;
import com.myapp.nutrition.model.HydrationEntry;
import com.myapp.nutrition.model.MealEntry;
import com.myapp.nutrition.model.MealType;
import com.myapp.nutrition.model.NutritionValue;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class NutritionService {
    private final GenericDAO<MealEntry> mealDAO;
    private final GenericDAO<HydrationEntry> waterDAO;
    private final int dailyCalorieGoal;

    public NutritionService(GenericDAO<MealEntry> mealDAO,
                            GenericDAO<HydrationEntry> waterDAO,
                            int dailyCalorieGoal) {
        this.mealDAO         = mealDAO;
        this.waterDAO        = waterDAO;
        this.dailyCalorieGoal = dailyCalorieGoal;
    }

    public NutritionValue calculateDailyTotal(LocalDate date) throws Exception {
        List<MealEntry> entries = mealDAO.loadAll().stream()
                .filter(e -> e.getDate().equals(date))
                .collect(Collectors.toList());

        NutritionValue total = new NutritionValue(0,0,0,0);
        for (MealEntry e : entries) {
            total = total.add(e.getTotalNutrition());
        }
        return total;
    }

    public boolean isOverCalorieGoal(LocalDate date) throws Exception {
        return calculateDailyTotal(date).getCalories() > dailyCalorieGoal;
    }

    public Map<LocalDate, NutritionValue> weeklyTrend() throws Exception {
        Map<LocalDate,NutritionValue> trend = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            trend.put(d, calculateDailyTotal(d));
        }
        return trend;
    }

    public void addMealEntry(LocalDate date, MealType type, List<FoodPortion> portions) throws Exception {
        List<MealEntry> all = mealDAO.loadAll();
        MealEntry entry = new MealEntry(date, type);
        portions.forEach(entry::addPortion);
        all.add(entry);
        mealDAO.saveAll(all);
    }

    public Optional<MealEntry> findMealEntry(LocalDate date, MealType type) throws Exception {
        return mealDAO.loadAll().stream()
                .filter(e -> e.getDate().equals(date) && e.getType() == type)
                .findFirst();
    }

    public void upsertMealEntry(LocalDate date, MealType type, List<FoodPortion> portions) throws Exception {
        List<MealEntry> all = mealDAO.loadAll();
        // 1) Șterge vechea intrare (dacă există)
        all.removeIf(e -> e.getDate().equals(date) && e.getType() == type);
        // 2) Dacă lista de porții nu e goală, adaugă noua intrare
        if (!portions.isEmpty()) {
            MealEntry entry = new MealEntry(date, type);
            portions.forEach(entry::addPortion);
            all.add(entry);
        }
        mealDAO.saveAll(all);
    }

    public void logWater(int ml) throws Exception {
        List<HydrationEntry> list = waterDAO.loadAll();
        list.add(new HydrationEntry(LocalDate.now(), ml));
        waterDAO.saveAll(list);
    }

    public int getDailyWater(LocalDate date) throws Exception {
        // Stream<HydrationEntry> -> DoubleStream
        double total = waterDAO.loadAll().stream()
                .filter(w -> w.getDate().equals(date))
                .mapToDouble(w -> w.getAmountMl()) // atenţie la numele exact al getter-ului
                .sum();
        return (int) total;
    }



}
