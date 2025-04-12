package view;

import controller.ExamenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

public class ExamenView {
    private Stage stage;
    private ExamenController controller;
    private HBox footer;
   
    public ExamenView() {
        stage = new Stage();
        stage.setTitle("Gestion des Examens");
        stage.setResizable(false);
       
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/ExamenView.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            System.err.println("Error loading Examen view: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void setFooter(HBox footer) {
        this.footer = footer;
    }
   
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/ExamenView.fxml"));
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
            System.err.println("Error showing ExamenView: " + e.getMessage());
            e.printStackTrace();
        }
    }
   
    public void close() {
        stage.close();
    }
    
    public ExamenController getController() {
        return controller;
    }
}