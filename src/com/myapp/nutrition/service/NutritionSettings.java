package com.myapp.nutrition.service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class NutritionSettings {
    private int dailyCalorieGoal;
    private Map<String, Double> macroRatio = new HashMap<>();
    private String theme;

    // singleton
    private static NutritionSettings instance;
    private static final File F = new File("src/data/settings.properties");

    private NutritionSettings() {}

    public static NutritionSettings get() throws IOException {
        if (instance == null) {
            instance = new NutritionSettings();
            instance.load();
        }
        return instance;
    }

    private void load() throws IOException {
        Properties p = new Properties();
        try (FileReader r = new FileReader(F)) {
            p.load(r);
        }
        dailyCalorieGoal = Integer.parseInt(p.getProperty("dailyCalorieGoal", "2000"));
        macroRatio.put("protein", Double.parseDouble(p.getProperty("macroProtein", "0.20")));
        macroRatio.put("carbs",   Double.parseDouble(p.getProperty("macroCarbs",   "0.50")));
        macroRatio.put("fat",     Double.parseDouble(p.getProperty("macroFat",     "0.30")));
        theme = p.getProperty("theme", "light");
    }

    public void save() throws IOException {
        Properties p = new Properties();
        p.setProperty("dailyCalorieGoal", String.valueOf(dailyCalorieGoal));
        p.setProperty("macroProtein",     String.valueOf(macroRatio.getOrDefault("protein", 0.20)));
        p.setProperty("macroCarbs",       String.valueOf(macroRatio.getOrDefault("carbs",   0.50)));
        p.setProperty("macroFat",         String.valueOf(macroRatio.getOrDefault("fat",     0.30)));
        p.setProperty("theme",            theme);

        try (FileWriter w = new FileWriter(F)) {
            p.store(w, "Nutrition Tracker Settings");
        }
    }

    // getters / setters

    public int getDailyCalorieGoal() {
        return dailyCalorieGoal;
    }

    public void setDailyCalorieGoal(int dailyCalorieGoal) {
        this.dailyCalorieGoal = dailyCalorieGoal;
    }

    public Map<String, Double> getMacroRatio() {
        return macroRatio;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setMacroRatio(Map<String,Double> ratio) {
        // golim vechiul conținut
        this.macroRatio.clear();
        // adăugăm valorile noi
        this.macroRatio.putAll(ratio);
    }



}
