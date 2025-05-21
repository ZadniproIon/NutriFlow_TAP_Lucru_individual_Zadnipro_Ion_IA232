package com.myapp.nutrition.ui;

import com.myapp.nutrition.dao.FileGenericDAO;
import com.myapp.nutrition.model.MealEntry;
import com.myapp.nutrition.model.NutritionValue;
import com.myapp.nutrition.service.NutritionService;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;

public class ReportController {
    @FXML private BarChart<String,Number> barChart;

    private NutritionService service;
    private Stage dialogStage;

    /** Injector, apelat din MainController */
    public void init(NutritionService service, Stage dialogStage) {
        this.service      = service;
        this.dialogStage  = dialogStage;
        loadData();
    }

    /** Încarcă datele în barChart */
    private void loadData() {
        try {
            // generează trend-ul
            Map<LocalDate, NutritionValue> trend = service.weeklyTrend();
            XYChart.Series<String,Number> series = new XYChart.Series<>();
            series.setName("Calorii");

            trend.forEach((date, val) -> {
                series.getData().add(
                        new XYChart.Data<>(date.toString(), val.getCalories())
                );
            });

            barChart.getData().add(series);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
