package com.myapp.nutrition.ui;

import com.myapp.nutrition.service.NutritionSettings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.HashMap;
import java.util.Map;

public class SettingsController {
    @FXML private TextField tfDailyGoal;
    @FXML private TextField tfCarbsRatio;
    @FXML private TextField tfProteinRatio;
    @FXML private TextField tfFatRatio;
    @FXML private Button btnLight;
    @FXML private Button btnDark;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private Stage dialogStage;
    private NutritionSettings settings;

    /** apelat din MainController când deschizi dialogul */
    public void init(Stage stage) {
        this.dialogStage = stage;
        try {
            this.settings = NutritionSettings.get();

            // populează câmpurile cu valorile curente
            tfDailyGoal     .setText(String.valueOf(settings.getDailyCalorieGoal()));
            Map<String,Double> ratio = settings.getMacroRatio();
            tfCarbsRatio    .setText(String.valueOf(ratio.getOrDefault("carbs",   0.0)));
            tfProteinRatio  .setText(String.valueOf(ratio.getOrDefault("protein", 0.0)));
            tfFatRatio      .setText(String.valueOf(ratio.getOrDefault("fat",     0.0)));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Nu am putut încărca setările:\n" + e.getMessage()
            ).showAndWait();
        }
    }

    @FXML
    private void onLightMode() {
        settings.setTheme("light");
        applyTheme();
    }

    @FXML
    private void onDarkMode() {
        settings.setTheme("dark");
        applyTheme();
    }

    /** aplică imediat schimbarea de temă în toate ferestrele deschise */
    private void applyTheme() {
        // baza comună de CSS
        String baseCss = getClass().getClassLoader()
                .getResource("styles/style.css").toExternalForm();
        // tema curentă (light.css sau dark.css)
        String themeCss = getClass().getClassLoader()
                .getResource("styles/" + settings.getTheme() + ".css")
                .toExternalForm();

        // aplică pe dialogul curent
        Scene dlgScene = dialogStage.getScene();
        dlgScene.getStylesheets().setAll(baseCss, themeCss);

        // apoi pe toate ferestrele (inclusiv main + orice dialoguri deja deschise)
        for (Window w : Window.getWindows()) {
            if (w instanceof Stage) {
                Scene sc = ((Stage) w).getScene();
                if (sc != null) {
                    sc.getStylesheets().setAll(baseCss, themeCss);
                }
            }
        }
    }

    @FXML
    private void onSave() {
        try {
            // 1) Calorie goal
            int goal = Integer.parseInt(tfDailyGoal.getText().trim());
            settings.setDailyCalorieGoal(goal);

            // 2) Macro ratios
            double carbs   = Double.parseDouble(tfCarbsRatio.getText().trim());
            double protein = Double.parseDouble(tfProteinRatio.getText().trim());
            double fat     = Double.parseDouble(tfFatRatio.getText().trim());

            // verificăm că însumează ~1.0
            double sum = carbs + protein + fat;
            if (Math.abs(sum - 1.0) > 0.01) {
                throw new IllegalArgumentException(
                        "Macro-ratio trebuie să însumeze 1.0 (ai " + sum + ")"
                );
            }

            Map<String,Double> newRatio = new HashMap<>();
            newRatio.put("carbs",   carbs);
            newRatio.put("protein", protein);
            newRatio.put("fat",     fat);
            settings.setMacroRatio(newRatio);

            // 3) Scrie toate setările în fișier
            settings.save();

        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR,
                    "Te rog introdu doar numere valide în toate câmpurile."
            ).showAndWait();
            return;
        } catch (IllegalArgumentException ex) {
            new Alert(Alert.AlertType.WARNING,
                    ex.getMessage()
            ).showAndWait();
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Eroare la salvare:\n" + ex.getMessage()
            ).showAndWait();
            return;
        }

        dialogStage.close();
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }
}
