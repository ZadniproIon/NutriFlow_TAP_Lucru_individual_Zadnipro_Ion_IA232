package com.myapp.nutrition.ui;

import com.myapp.nutrition.dao.FileGenericDAO;
import com.myapp.nutrition.model.HydrationEntry;
import com.myapp.nutrition.model.MealEntry;
import com.myapp.nutrition.model.NutritionValue;
import com.myapp.nutrition.service.NutritionService;
import com.myapp.nutrition.service.NutritionSettings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import java.io.Serializable;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;

public class MainController implements Serializable {
    private static final long serialVersionUID = 1L;

    @FXML private Label lblDailyTotal, lblDailyProtein, lblDailyCarbs, lblDailyFats, lblWater;
    @FXML private Button btnAddMeal, btnViewReport, btnManageFoods, btnAddWater, btnSettings;

    @FXML private ProgressIndicator piCarbs, piFat, piProtein;
    @FXML private Label lblCarbsValue, lblFatValue, lblProteinValue;
    @FXML private Label lblCarbsLeft, lblFatLeft, lblProteinLeft;



    private NutritionService service;
    private NutritionSettings settings;


    @FXML
    public void initialize() {
        // -- Setări / light-dark switch --
        btnSettings.setOnAction(e -> {
            try {
                // 1) Încarcă dialogul de setări
                URL fxml = getClass().getClassLoader().getResource("ui/settings.fxml");
                FXMLLoader loader = new FXMLLoader(fxml);
                Parent root = loader.load();
                SettingsController ctrl = loader.getController();

                Stage dialog = new Stage();
                dialog.initOwner(lblDailyTotal.getScene().getWindow());

                // 2) Aplică CSS common + tema curentă în dialog
                Scene sc = new Scene(root);
                // common
                URL baseCss = getClass().getClassLoader().getResource("styles/style.css");
                if (baseCss != null) sc.getStylesheets().add(baseCss.toExternalForm());
                // tema
                String theme = NutritionSettings.get().getTheme();
                URL themeCss = getClass().getClassLoader()
                        .getResource("styles/" + theme + ".css");
                if (themeCss != null) sc.getStylesheets().add(themeCss.toExternalForm());

                dialog.setScene(sc);
                ctrl.init(dialog);
                dialog.showAndWait();

                // 3) După închidere, reaplică tema pe fereastra principală
                Scene mainSc = lblDailyTotal.getScene();
                ObservableList<String> sheets = mainSc.getStylesheets();
                sheets.clear();
                if (baseCss != null)          sheets.add(baseCss.toExternalForm());
                if (themeCss != null)         sheets.add(themeCss.toExternalForm());

                updateDashboard();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // -- Restul butoanelor rămâne neschimbat --

        btnAddMeal.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getClassLoader().getResource("ui/addMeal.fxml")
                );
                Parent root = loader.load();
                AddMealController ctrl = loader.getController();
                Stage dialog = new Stage();
                ctrl.init(
                        new NutritionService(
                                new FileGenericDAO<MealEntry>(Paths.get("src/data/meals.dat")),
                                new FileGenericDAO<HydrationEntry>(Paths.get("src/data/water.dat")),
                                NutritionSettings.get().getDailyCalorieGoal()
                        ),
                        dialog
                );
                // aplică CSS în dialog
                Scene scene = new Scene(root);
                URL baseCss = getClass().getClassLoader().getResource("styles/style.css");
                if (baseCss != null) scene.getStylesheets().add(baseCss.toExternalForm());
                URL themeCss = getClass().getClassLoader()
                        .getResource("styles/" + NutritionSettings.get().getTheme() + ".css");
                if (themeCss != null) scene.getStylesheets().add(themeCss.toExternalForm());

                dialog.setScene(scene);
                dialog.initOwner(lblDailyTotal.getScene().getWindow());
                dialog.showAndWait();
                updateDailyTotal();
                updateDashboard();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnViewReport.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getClassLoader().getResource("ui/report.fxml")
                );
                Parent root = loader.load();
                ReportController ctrl = loader.getController();
                Stage dialog = new Stage();
                ctrl.init(
                        new NutritionService(
                                new FileGenericDAO<MealEntry>(Paths.get("src/data/meals.dat")),
                                new FileGenericDAO<HydrationEntry>(Paths.get("src/data/water.dat")),
                                NutritionSettings.get().getDailyCalorieGoal()
                        ),
                        dialog
                );
                Scene scene = new Scene(root);
                URL baseCss = getClass().getClassLoader().getResource("styles/style.css");
                if (baseCss != null) scene.getStylesheets().add(baseCss.toExternalForm());
                URL themeCss = getClass().getClassLoader()
                        .getResource("styles/" + NutritionSettings.get().getTheme() + ".css");
                if (themeCss != null) scene.getStylesheets().add(themeCss.toExternalForm());

                dialog.setScene(scene);
                dialog.initOwner(lblDailyTotal.getScene().getWindow());
                dialog.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnManageFoods.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getClassLoader().getResource("ui/manageFoods.fxml"));
                Parent root = loader.load();
                ManageFoodsController ctrl = loader.getController();
                Stage dialog = new Stage();
                ctrl.init(Paths.get("src/data/foods.dat"), dialog);

                Scene scene = new Scene(root);
                URL baseCss = getClass().getClassLoader().getResource("styles/style.css");
                if (baseCss != null) scene.getStylesheets().add(baseCss.toExternalForm());
                URL themeCss = getClass().getClassLoader()
                        .getResource("styles/" + NutritionSettings.get().getTheme() + ".css");
                if (themeCss != null) scene.getStylesheets().add(themeCss.toExternalForm());

                dialog.setScene(scene);
                dialog.initOwner(lblDailyTotal.getScene().getWindow());
                dialog.showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnAddWater.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getClassLoader().getResource("ui/water.fxml"));
                Parent root = loader.load();
                WaterController ctrl = loader.getController();
                Stage dialog = new Stage();
                ctrl.init(service, dialog);

                Scene scene = new Scene(root);
                URL baseCss = getClass().getClassLoader().getResource("styles/style.css");
                if (baseCss != null) scene.getStylesheets().add(baseCss.toExternalForm());
                URL themeCss = getClass().getClassLoader()
                        .getResource("styles/" + NutritionSettings.get().getTheme() + ".css");
                if (themeCss != null) scene.getStylesheets().add(themeCss.toExternalForm());

                dialog.setScene(scene);
                dialog.initOwner(lblDailyTotal.getScene().getWindow());
                dialog.showAndWait();
                updateWater();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        updateWater();
    }

    /** Injector setter, apelat din Main.start() */
    public void setNutritionService(NutritionService service) {
        this.service = service;
        try {
            this.settings = NutritionSettings.get();
        } catch (Exception e){
            e.printStackTrace();
            this.settings = null;
        }
        updateDashboard();
    }

    private void updateDashboard() {
        updateDailyTotal();
        updateWater();
        updateMacros();
        // aici poți adăuga updateMacros(), updateGoal() etc.
    }

    private void updateDailyTotal() {
        try {
            NutritionValue tot = service.calculateDailyTotal(LocalDate.now());
            lblDailyTotal.setText(String.format("Calorii: %.0f kcal", tot.getCalories()));
            lblDailyProtein.setText(String.format("Proteine: %.1f g", tot.getProtein()));
            lblDailyCarbs .setText(String.format("Carbohidrați: %.1f g", tot.getCarbs()));
            lblDailyFats  .setText(String.format("Grăsimi: %.1f g", tot.getFats()));
        } catch (Exception e) {
            e.printStackTrace();
            lblDailyTotal.setText("Eroare la calcul");
        }
    }

    private void updateWater() {
        try {
            int ml = service.getDailyWater(LocalDate.now());
            lblWater.setText("Apă: " + ml + " ml");
        } catch (Exception e) {
            lblWater.setText("Apă: --");
        }
    }

    private void updateMacros() {
        try {
            NutritionValue total = service.calculateDailyTotal(LocalDate.now());
            int dailyCalories = settings.getDailyCalorieGoal();
            Map<String,Double> ratio = settings.getMacroRatio();

            double targetCarbs   = dailyCalories * ratio.get("carbs")   / 4.0;
            double targetFat     = dailyCalories * ratio.get("fat")     / 9.0;
            double targetProtein = dailyCalories * ratio.get("protein") / 4.0;

            piCarbs.setProgress(   Math.min(1.0, total.getCarbs()   / targetCarbs) );
            piFat  .setProgress(   Math.min(1.0, total.getFats()    / targetFat) );
            piProtein.setProgress(Math.min(1.0, total.getProtein()/ targetProtein) );

            // ─── facem doar "left"
            double leftC = targetCarbs   - total.getCarbs();
            double leftF = targetFat     - total.getFats();
            double leftP = targetProtein - total.getProtein();

            lblCarbsLeft.setText(  String.format("%dg rămase", (int)Math.max(0, leftC)) );
            lblFatLeft.setText(    String.format("%dg rămase", (int)Math.max(0, leftF)) );
            lblProteinLeft.setText(String.format("%dg rămase", (int)Math.max(0, leftP)) );

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
