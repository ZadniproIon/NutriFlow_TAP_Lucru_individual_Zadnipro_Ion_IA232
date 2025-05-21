package com.nutriflow;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Load local HTML from resources/ui/index.html
        URL url = getClass().getResource("/ui/index.html");
        if (url != null) {
            webEngine.load(url.toExternalForm());
        } else {
            System.out.println("HTML file not found!");
        }

        Scene scene = new Scene(webView);
        stage.setTitle("NutriFlow - Nutrition Tracker");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
