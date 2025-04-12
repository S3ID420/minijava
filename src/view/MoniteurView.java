package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MoniteurView {
    private static MoniteurView instance;
    private Stage stage;
    private HBox footer;
    
    public MoniteurView() {
        instance = this;
    }
    
    public static MoniteurView getInstance() {
        if (instance == null) {
            instance = new MoniteurView();
        }
        return instance;
    }
    
    public void setFooter(HBox footer) {
        this.footer = footer;
    }
    
    public void show() {
        try {
            if (stage == null) {
                stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Gestion des Moniteurs");
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/MoniteurView.fxml"));
                Parent root = loader.load();
                
                // Add footer if available
                if (footer != null) {
                    BorderPane borderPane = new BorderPane();
                    borderPane.setCenter(root);
                    borderPane.setBottom(footer);
                    
                    Scene scene = new Scene(borderPane, 800, 600);
                    stage.setScene(scene);
                } else {
                    Scene scene = new Scene(root, 800, 600);
                    stage.setScene(scene);
                }
                
                stage.setResizable(true);
            }
            
            stage.show();
            stage.toFront();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error showing MoniteurView: " + e.getMessage());
        }
    }
    
    public void close() {
        if (stage != null) {
            stage.close();
        }
    }
}