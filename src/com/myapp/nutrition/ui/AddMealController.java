package com.myapp.nutrition.ui;

import com.myapp.nutrition.dao.FileGenericDAO;
import com.myapp.nutrition.model.*;
import com.myapp.nutrition.service.NutritionService;
import com.myapp.nutrition.service.NutritionSettings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddMealController {
    @FXML private DatePicker dpDate;
    @FXML private ListView<FoodPortion> lvPortions;
    @FXML private ToggleButton btnBreakfast;
    @FXML private ToggleButton btnLunch;
    @FXML private ToggleButton btnDinner;
    @FXML private ToggleButton btnSnack;



    private final ToggleGroup mealTypeGroup = new ToggleGroup();
    private NutritionService service;
    private Stage dialogStage;

    /**
     * Injector, apelat de MainController.
     */
    public void init(NutritionService service, Stage dialogStage) {
        this.service = service;
        this.dialogStage = dialogStage;
        dialogStage.setResizable(false);

        // 1) Data implicită
        dpDate.setValue(LocalDate.now());

        // 2) Configurează ToggleGroup pentru tipul mesei
        btnBreakfast.setToggleGroup(mealTypeGroup);
        btnLunch    .setToggleGroup(mealTypeGroup);
        btnDinner   .setToggleGroup(mealTypeGroup);
        btnSnack    .setToggleGroup(mealTypeGroup);

        btnBreakfast.setUserData(MealType.BREAKFAST);
        btnLunch    .setUserData(MealType.LUNCH);
        btnDinner   .setUserData(MealType.DINNER);
        btnSnack    .setUserData(MealType.SNACK);

        // 3) Selectează implicit breakfast
        mealTypeGroup.selectToggle(btnBreakfast);

        // 4) Ascultători: schimbare dată sau tip masă
        dpDate.valueProperty().addListener((obs, o, n) -> reloadPortions());
        mealTypeGroup.selectedToggleProperty().addListener((obs, o, n) -> reloadPortions());

        // 5) Încarcă porțiile curente
        reloadPortions();
    }

    /**
     * Reîncarcă lista de porții din DAO pentru data + tipul selectat.
     */
    private void reloadPortions() {
        lvPortions.getItems().clear();
        try {
            MealType selType = (MealType) mealTypeGroup.getSelectedToggle().getUserData();
            Optional<MealEntry> existing = service.findMealEntry(dpDate.getValue(), selType);
            existing.ifPresent(entry ->
                    lvPortions.getItems().setAll(entry.getPortions())
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Șterge porția selectată. */
    @FXML
    private void onDeletePortion() {
        FoodPortion sel = lvPortions.getSelectionModel().getSelectedItem();
        if (sel != null) {
            lvPortions.getItems().remove(sel);
        }
    }

    /**
     * Quick Add: deschide dialog unde utilizatorul introduce macro-uri,
     * iar callback-ul adaugă porția în listă.
     */
    @FXML
    private void onQuickAdd() {
        try {
            // load the FXML from resources/ui/quickAdd.fxml
            URL fxmlUrl = getClass().getClassLoader().getResource("ui/quickAdd.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("Cannot find ui/quickAdd.fxml on classpath");
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            QuickAddController ctrl = loader.getController();
            Stage dialog = new Stage();
            ctrl.init(portion -> lvPortions.getItems().add(portion), dialog);

            Scene scene = new Scene(root);


            // 1) base CSS
            URL baseCss = getClass().getClassLoader().getResource("styles/style.css");
            if (baseCss != null) {
                scene.getStylesheets().add(baseCss.toExternalForm());
            } else {
                System.err.println("⚠️ styles/style.css not found");
            }

            // 2) theme CSS
            String theme = NutritionSettings.get().getTheme();
            URL themeCss = getClass().getClassLoader()
                    .getResource("styles/" + theme + ".css");
            if (themeCss != null) {
                scene.getStylesheets().add(themeCss.toExternalForm());
            } else {
                System.err.println("⚠️ styles/" + theme + ".css not found");
            }

            dialog.setTitle("Quick Add");
            dialog.setScene(scene);
            dialog.initOwner(dpDate.getScene().getWindow());
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Nu am putut deschide Quick Add:\n" + ex.getMessage()
            ).showAndWait();
        }
    }


    /**
     * Adaugă o porție nouă prin dialog de selecție aliment + gramaj.
     */
    @FXML
    private void onAddPortion() {
        try {
            FileGenericDAO<SimpleFood> foodDAO =
                    new FileGenericDAO<>(Paths.get("src/data/foods.dat"));
            List<SimpleFood> catalog = foodDAO.loadAll();
            if (catalog.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION,
                        "Catalogul de alimente este gol."
                ).showAndWait();
                return;
            }

            ChoiceDialog<SimpleFood> chooseFood =
                    new ChoiceDialog<>(catalog.get(0), catalog);
            chooseFood.setTitle("Alege aliment");
            chooseFood.setHeaderText("Selectează un aliment:");
            Optional<SimpleFood> chosen = chooseFood.showAndWait();

            chosen.ifPresent(food -> {
                TextInputDialog weightDlg = new TextInputDialog("100");
                weightDlg.setTitle("Gramaj porție");
                weightDlg.setHeaderText("Câte grame?");
                Optional<String> weightText = weightDlg.showAndWait();

                weightText.ifPresent(text -> {
                    try {
                        double w = Double.parseDouble(text);
                        lvPortions.getItems().add(new FoodPortion(food, w));
                    } catch (NumberFormatException nfe) {
                        new Alert(Alert.AlertType.ERROR,
                                "Te rog introduce un număr valid pentru gramaj."
                        ).showAndWait();
                    }
                });
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Eroare la încărcarea catalogului:\n" + ex.getMessage()
            ).showAndWait();
        }
    }

    /**
     * Salvează intrarea (upsert) şi închide dialogul.
     */
    @FXML
    private void onSave() {
        try {
            LocalDate date = dpDate.getValue();
            MealType type = (MealType) mealTypeGroup.getSelectedToggle().getUserData();
            List<FoodPortion> portions = new ArrayList<>(lvPortions.getItems());
            service.upsertMealEntry(date, type, portions);
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Nu am putut salva masa:\n" + ex.getMessage()
            ).showAndWait();
        }
        dialogStage.close();
    }
}
