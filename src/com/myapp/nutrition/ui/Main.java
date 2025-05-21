package com.myapp.nutrition.ui;

import com.myapp.nutrition.dao.FileGenericDAO;
import com.myapp.nutrition.model.HydrationEntry;
import com.myapp.nutrition.model.MealEntry;
import com.myapp.nutrition.service.NutritionSettings;
import com.myapp.nutrition.service.NutritionService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Paths;

public class Main extends Application {

    // câmp static pentru Stage-ul principal
    private static Stage primary;

    // metodă statică prin care ceilalți pot obține Stage-ul
    public static Stage getPrimaryStage() {
        return primary;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // stocăm referința
        Main.primary = primaryStage;

        // 1. Încarcă FXML-ul
        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("main.fxml")
        );
        Parent root = loader.load();

        // 2. Încarcă setările (din settings.json)
        NutritionSettings settings = NutritionSettings.get();

        // 3. Crează DAO-urile pentru mese și hidratare
        FileGenericDAO<MealEntry> mealDAO =
                new FileGenericDAO<>(Paths.get("src/data/meals.dat"));
        FileGenericDAO<HydrationEntry> waterDAO =
                new FileGenericDAO<>(Paths.get("src/data/water.dat"));

        // 4. Configurează NutritionService și injectează-l în controller
        NutritionService service = new NutritionService(
                mealDAO,
                waterDAO,
                settings.getDailyCalorieGoal()
        );
        MainController controller = loader.getController();
        controller.setNutritionService(service);

        // 5. Construiește scena și aplică tema CSS (light/dark)
        Scene scene = new Scene(root);
        //String theme = settings.getTheme(); // "light" sau "dark"
        //String theme = NutritionSettings.get().getTheme(); // "light" sau "dark"
        // 1) CSS comun
        URL baseCss = getClass().getClassLoader().getResource("styles/style.css");
        if (baseCss!=null) {
            scene.getStylesheets().add(baseCss.toExternalForm());
        }

        // 2) CSS temă
        String theme = settings.getTheme();  // "light" sau "dark"
        URL themeCss = getClass().getClassLoader()
                .getResource("styles/" + theme + ".css");
        if (themeCss!=null) {
            scene.getStylesheets().add(themeCss.toExternalForm());
        }


        // 6. Afișează fereastra
        primaryStage.setTitle("Nutrition Tracker");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
