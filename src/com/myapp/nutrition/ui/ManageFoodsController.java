package com.myapp.nutrition.ui;

import com.myapp.nutrition.dao.FileGenericDAO;
import com.myapp.nutrition.model.SimpleFood;
import com.myapp.nutrition.model.NutritionValue;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

public class ManageFoodsController {
    @FXML private ListView<SimpleFood> lvFoods;
    @FXML private TextField tfName, tfProtein, tfCarbs, tfFats;

    private FileGenericDAO<SimpleFood> foodDAO;
    private List<SimpleFood> foods;
    private Stage dialogStage;

    /** Injector apelat din MainController */
    public void init(Path foodsFile, Stage dialogStage) {
        this.dialogStage = dialogStage;
        dialogStage.setResizable(false);
        try {
            foodDAO = new FileGenericDAO<>(foodsFile);
            foods = new ArrayList<>(foodDAO.loadAll());
            lvFoods.setItems(FXCollections.observableArrayList(foods));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Nu am putut încărca catalogul.")
                    .showAndWait();
        }
    }

    /** Adaugă un nou SimpleFood în lista și fișier */
    @FXML
    private void onAddFood() {
        String name = tfName.getText().trim();
        try {
            double p = Double.parseDouble(tfProtein.getText().trim());
            double c = Double.parseDouble(tfCarbs.getText().trim());
            double f = Double.parseDouble(tfFats.getText().trim());
            if (name.isEmpty()) throw new IllegalArgumentException("Nume gol");

            NutritionValue val = new NutritionValue(p*4 + c*4 + f*9, p, c, f);
            SimpleFood food = new SimpleFood(name, val);
            foods.add(food);
            foodDAO.saveAll(foods);
            lvFoods.getItems().add(food);

            // curăță câmpurile
            tfName.clear();
            tfProtein.clear();
            tfCarbs.clear();
            tfFats.clear();

        } catch(NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Te rog introdu valori numerice valide.")
                    .showAndWait();
        } catch(Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Eroare la salvare: " + ex.getMessage())
                    .showAndWait();
        }
    }

    /** Șterge alimentul selectat */
    @FXML
    private void onDeleteFood() {
        SimpleFood sel = lvFoods.getSelectionModel().getSelectedItem();
        if (sel != null) {
            foods.remove(sel);
            try {
                foodDAO.saveAll(foods);
                lvFoods.getItems().remove(sel);
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Nu am putut șterge alimentul.")
                        .showAndWait();
            }
        }
    }
}
