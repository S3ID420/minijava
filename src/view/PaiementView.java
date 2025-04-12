package view;

import controller.PaiementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PaiementView {
    private Stage stage;
    private PaiementController controller;
    
    public PaiementView() {
        stage = new Stage();
        stage.setTitle("Gestion des Paiements");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/paiement.fxml"));
            
            Parent root = loader.load();
            controller = loader.getController();
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(true);
        } catch (IOException e) {
            System.err.println("Error loading PaiementView: " + e.getMessage());
            e.printStackTrace();
        }
        
    }
    
    public void show() {
        stage.show();
    }
    
    public void close() {
        stage.close();
    }
    
    public PaiementController getController() {
        return controller;
    }
}