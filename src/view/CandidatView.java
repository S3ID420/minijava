package view;

import controller.CandidatController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

public class CandidatView {
    private Stage stage;
    private CandidatController controller;
    private HBox footer;
   
    public CandidatView() {
        stage = new Stage();
        stage.setTitle("Gestion des Candidats");
       
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/candidat.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            
            stage.setMinWidth(800);
            stage.setMinHeight(600);
        } catch (IOException e) {
            System.err.println("Error loading CandidatView: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void setFooter(HBox footer) {
        this.footer = footer;
    }
   
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/candidat.fxml"));
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
            System.err.println("Error showing CandidatView: " + e.getMessage());
            e.printStackTrace();
        }
    }
   
    public void close() {
        stage.close();
    }
   
    public CandidatController getController() {
        return controller;
    }
}