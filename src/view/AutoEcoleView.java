package view;

import controller.AutoEcoleController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AutoEcoleView {
    private Stage stage;
    private AutoEcoleController controller;
    
    public AutoEcoleView() {
        stage = new Stage();
        stage.setTitle("Configuration Auto-École");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/auto_ecole.fxml"));

            Parent root = loader.load();
            controller = loader.getController();
        
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
        } catch (IOException e) {
            System.err.println("Error loading AutoEcoleView: " + e.getMessage());
            e.printStackTrace();
        }
        
    }
    
    public void show() {
        stage.show();
    }
    
    public void close() {
        stage.close();
    }
    
    public AutoEcoleController getController() {
        return controller;
    }
}