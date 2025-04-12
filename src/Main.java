import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.AutoEcoleView;
import view.CandidatView;
import view.PaiementView;
import view.ExamenView;
import service.AutoEcoleService;
import model.AutoEcole;

public class Main extends Application {
    private AutoEcoleView autoEcoleView;
    private CandidatView candidatView;
    private PaiementView paiementView;
    private ExamenView examenView;
    private AutoEcoleService autoEcoleService;
    private HBox footer;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestion Auto-École");
        
        // Initialize service
        autoEcoleService = new AutoEcoleService();
        
        // Initialize views
        autoEcoleView = new AutoEcoleView();
        candidatView = new CandidatView();
        paiementView = new PaiementView();
        examenView = new ExamenView();
        
        // Create footer
        createFooter();
        
        // Set footer for all views
        
        candidatView.setFooter(footer);
        paiementView.setFooter(footer);
        examenView.setFooter(footer);
        
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
        
        Button examenButton = new Button("Gestion des Examens");
        examenButton.setMaxWidth(Double.MAX_VALUE);
        examenButton.setPrefHeight(40);
        examenButton.setOnAction(e -> examenView.show());
        
        Button paiementButton = new Button("Gestion des Paiements");
        paiementButton.setMaxWidth(Double.MAX_VALUE);
        paiementButton.setPrefHeight(40);
        paiementButton.setOnAction(e -> paiementView.show());
        
        Button quitterButton = new Button("Quitter");
        quitterButton.setMaxWidth(Double.MAX_VALUE);
        quitterButton.setPrefHeight(40);
        quitterButton.setOnAction(e -> primaryStage.close());
        
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(titleLabel, autoEcoleButton, candidatButton, examenButton, paiementButton, quitterButton);
        
        // Create BorderPane to include both content and footer
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vbox);
        borderPane.setBottom(footer);
        
        // Create scene
        Scene scene = new Scene(borderPane, 400, 450);
        
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    private void createFooter() {
        footer = new HBox(10);
        footer.setPadding(new Insets(10));
        footer.setAlignment(Pos.CENTER);
        footer.setStyle("-fx-background-color: #f0f0f0; -fx-border-width: 1 0 0 0; -fx-border-color: #cccccc;");
        
        // Load auto-école information
        AutoEcole autoEcole = autoEcoleService.getAutoEcole();
        if (autoEcole != null) {
            VBox infoBox = new VBox(2);
            infoBox.setAlignment(Pos.CENTER);
            
            Label nameLabel = new Label(autoEcole.getNom());
            nameLabel.setStyle("-fx-font-weight: bold;");
            
            Label addressLabel = new Label(autoEcole.getAdresse());
            
            Label contactLabel = new Label("Tél: " + autoEcole.getTelephone() + 
                                         (autoEcole.getEmail() != null && !autoEcole.getEmail().isEmpty() ? 
                                          " | Email: " + autoEcole.getEmail() : "") +
                                         (autoEcole.getFax() != null && !autoEcole.getFax().isEmpty() ? 
                                          " | Fax: " + autoEcole.getFax() : ""));
            
            infoBox.getChildren().addAll(nameLabel, addressLabel, contactLabel);
            
            // Add website if available
            if (autoEcole.getSiteWeb() != null && !autoEcole.getSiteWeb().isEmpty()) {
                Label webLabel = new Label("Site web: " + autoEcole.getSiteWeb());
                infoBox.getChildren().add(webLabel);
            }
            
            footer.getChildren().add(infoBox);
        } else {
            Label infoLabel = new Label("Auto-École - Informations non disponibles");
            footer.getChildren().add(infoLabel);
        }
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