package view;

import controller.CandidatController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CandidatView {
    private Stage stage;
    private CandidatController controller;
    
    public CandidatView() {
        stage = new Stage();
        stage.setTitle("Gestion des Candidats");
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/candidat.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            
            Scene scene = new Scene(root);
            
            
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
        } catch (IOException e) {
            System.err.println("Error loading CandidatView: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void show() {
        stage.show();
    }
    
    public void close() {
        stage.close();
    }
    
    public CandidatController getController() {
        return controller;
    }
}