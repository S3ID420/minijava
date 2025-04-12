

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.AutoEcoleView;
import view.CandidatView;

 public class Main extends Application {
    private AutoEcoleView autoEcoleView;
    private CandidatView candidatView;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestion Auto-École");
        
        // Initialize views
        autoEcoleView = new AutoEcoleView();
        candidatView = new CandidatView();
        
        // Create main menu
        Label titleLabel = new Label("Auto-École - Menu Principal");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Button autoEcoleButton = new Button("Paramètres de l'Auto-École");
        autoEcoleButton.setMaxWidth(Double.MAX_VALUE);
        autoEcoleButton.setPrefHeight(40);
        autoEcoleButton.setOnAction(e -> autoEcoleView.show());
        
        Button candidatButton = new Button("Gestion des Candidats");
        candidatButton.setMaxWidth(Double.MAX_VALUE);
        candidatButton.setPrefHeight(40);
        candidatButton.setOnAction(e -> candidatView.show());
        
        Button quitterButton = new Button("Quitter");
        quitterButton.setMaxWidth(Double.MAX_VALUE);
        quitterButton.setPrefHeight(40);
        quitterButton.setOnAction(e -> primaryStage.close());
        
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(titleLabel, autoEcoleButton, candidatButton, quitterButton);
        
        // Create scene
        Scene scene = new Scene(vbox, 400, 300);
        
        
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        // Create necessary directories
        createDirectories();
        
        launch(args);
    }
    
    // Create necessary directories for the application
    private static void createDirectories() {
        try {
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("documents"));
        } catch (Exception e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }
}