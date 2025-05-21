package com.myapp.nutrition.ui;

import com.myapp.nutrition.model.SimpleFood;
import com.myapp.nutrition.model.NutritionValue;
import com.myapp.nutrition.model.FoodPortion;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class QuickAddController {
    @FXML private TextField tfProtein;
    @FXML private TextField tfCarbs;
    @FXML private TextField tfFats;

    private Stage dialogStage;
    private Consumer<FoodPortion> addCallback;

    public void init(Consumer<FoodPortion> addCallback, Stage dialogStage) {
        this.addCallback = addCallback;
        this.dialogStage = dialogStage;
    }

    @FXML
    private void onSubmit() {
        try {
            double p = Double.parseDouble(tfProtein.getText().trim());
            double c = Double.parseDouble(tfCarbs.getText().trim());
            double f = Double.parseDouble(tfFats.getText().trim());
            double cal = p*4 + c*4 + f*9;

            SimpleFood quickFood = new SimpleFood(
                    "Quick Add",
                    new NutritionValue(cal, p, c, f)
            );
            FoodPortion portion = new FoodPortion(quickFood, 100);
            addCallback.accept(portion);
            dialogStage.close();
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR,
                    "Te rog introdu valori numerice valide.")
                    .showAndWait();
        }
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }
}
