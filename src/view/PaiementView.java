package view;

import controller.PaiementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

public class PaiementView {
    private Stage stage;
    private PaiementController controller;
    private HBox footer;
    
    public PaiementView() {
        stage = new Stage();
        stage.setTitle("Gestion des Paiements");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/paiement.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            stage.setResizable(true);
        } catch (IOException e) {
            System.err.println("Error loading PaiementView: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void setFooter(HBox footer) {
        this.footer = footer;
    }
    
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/paiement.fxml"));
            Parent content = loader.load();
            controller = loader.getController();
            
            // Create BorderPane to hold content and footer
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(content);
            
            // Add footer if available
            if (footer != null) {
                borderPane.setBottom(footer);
            }
            
            Scene scene = new Scene(borderPane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error showing PaiementView: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void close() {
        stage.close();
    }
    
    public PaiementController getController() {
        return controller;
    }
}