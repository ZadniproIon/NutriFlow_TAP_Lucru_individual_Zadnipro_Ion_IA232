package com.myapp.nutrition.ui;

import com.myapp.nutrition.service.NutritionService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WaterController {
    @FXML private TextField tfWater;
    private NutritionService service;
    private Stage dialog;
    public void init(NutritionService svc, Stage dlg){
        service=svc; dialog=dlg;
        dialog.setResizable(false);
    }
    @FXML private void onOk(){
        try {
            int ml = Integer.parseInt(tfWater.getText());
            service.logWater(ml);
            dialog.close();
        } catch(Exception ex){
            new Alert(Alert.AlertType.ERROR,
                    "Te rog introdu un număr valid.").showAndWait();
        }
    }
}
