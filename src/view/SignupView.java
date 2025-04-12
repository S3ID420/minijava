package view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.AuthService;

public class SignupView {
    private AuthService authService;
    private MainAppLauncher mainAppLauncher;

    public SignupView(AuthService authService, MainAppLauncher mainAppLauncher) {
        this.authService = authService;
        this.mainAppLauncher = mainAppLauncher;
    }

    public void show(Stage primaryStage) {
        try {
            // Create main container with two sides
            HBox mainContainer = new HBox();
            
            // Left side - decorative panel
            VBox leftPanel = createDecorationPanel();
            leftPanel.setPadding(new Insets(30));
    leftPanel.setAlignment(Pos.CENTER);
    leftPanel.setMinHeight(600); 
    leftPanel.setMaxHeight(600);
            
            // Right side - signup form
            VBox rightPanel = createSignupForm(primaryStage);
            
            // Add both panels to main container
            mainContainer.getChildren().addAll(leftPanel, rightPanel);
            leftPanel.setPrefWidth(450);
            leftPanel.setMinWidth(450);
            leftPanel.setMaxWidth(450); 
            rightPanel.setPrefWidth(450);
            rightPanel.setMinWidth(450);
            rightPanel.setMaxWidth(450);
            
            // Set background for entire scene
            mainContainer.setStyle("-fx-background-color: white;");
            
            Scene scene = new Scene(mainContainer, 900, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Auto-École - Inscription");
            primaryStage.setResizable(false);
            primaryStage.show();
            
        } catch (Exception e) {
            System.err.println("Error displaying signup view: ");
            e.printStackTrace();
        }
    }
    
    private VBox createDecorationPanel() {
        VBox leftPanel = new VBox(20);
        leftPanel.setPadding(new Insets(30));
        leftPanel.setAlignment(Pos.CENTER);
        
        // Create gradient background - using a different gradient for signup
        Stop[] stops = new Stop[] { 
            new Stop(0, Color.web("#1565c0")),
            new Stop(1, Color.web("#0d47a1"))
        };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, null, stops);
        
        Rectangle background = new Rectangle();
        background.widthProperty().bind(leftPanel.widthProperty());
        background.heightProperty().bind(leftPanel.heightProperty());
        background.setFill(gradient);
        
        // Create and style app logo
        Label appName = new Label("AUTO-ÉCOLE");
        appName.setFont(Font.font("Montserrat", FontWeight.BOLD, 36));
        appName.setTextFill(Color.WHITE);
        
        Label tagline = new Label("CRÉER UN COMPTE");
        tagline.setFont(Font.font("Montserrat", 18));
        tagline.setTextFill(Color.web("#e0e0e0"));
        
        // Create placeholder for car icon
        ImageView signupIcon = createPlaceholderIcon();
        signupIcon.setFitWidth(200);
        signupIcon.setPreserveRatio(true);
        
        // Benefits of registration
        VBox benefits = new VBox(15);
        benefits.setAlignment(Pos.CENTER_LEFT);
        
        String[] benefitTexts = {
            "Accès à toutes les fonctionnalités",
            "Sauvegarde de vos données",
            "Sécurité renforcée",
            "Support technique"
        };
        
        for (String text : benefitTexts) {
            HBox benefitItem = new HBox(10);
            benefitItem.setAlignment(Pos.CENTER_LEFT);
            
            // Create star icon
            Label star = new Label("★");
            star.setTextFill(Color.web("#ffeb3b"));
            star.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            
            Label benefitLabel = new Label(text);
            benefitLabel.setTextFill(Color.WHITE);
            benefitLabel.setFont(Font.font("Segoe UI", 16));
            
            benefitItem.getChildren().addAll(star, benefitLabel);
            benefits.getChildren().add(benefitItem);
        }
        
        // Add all elements to the panel
        VBox contentBox = new VBox(30);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(appName, tagline, signupIcon, benefits);
        
        // Set up container with stacked background and content
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(background, contentBox);
        
        leftPanel.getChildren().add(stackPane);
        
        
        return leftPanel;
    }
    
    private VBox createSignupForm(Stage primaryStage) {
        VBox rightPanel = new VBox(25);
        rightPanel.setPadding(new Insets(50));
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setStyle("-fx-background-color: white;");
        
        // Signup header
        Label welcomeLabel = new Label("Inscription");
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        welcomeLabel.setTextFill(Color.web("#0d47a1"));
        
        Label subheaderLabel = new Label("Créez votre compte pour commencer");
        subheaderLabel.setFont(Font.font("Segoe UI", 16));
        subheaderLabel.setTextFill(Color.web("#757575"));
        
        // Input fields with validation
        TextField usernameField = createStylizedTextField("Nom d'utilisateur");
        PasswordField passwordField = createStylizedPasswordField("Mot de passe");
        PasswordField confirmPasswordField = createStylizedPasswordField("Confirmer le mot de passe");
        
        // Password requirements info
        Label passwordReqLabel = new Label("Le mot de passe doit contenir au moins 6 caractères");
        passwordReqLabel.setTextFill(Color.web("#757575"));
        passwordReqLabel.setFont(Font.font("Segoe UI", 12));
        
        // Submit button with hover effect
        Button signupButton = new Button("S'INSCRIRE");
        signupButton.setPrefHeight(50);
        signupButton.setPrefWidth(250);
        signupButton.setStyle("-fx-background-color: #0d47a1; -fx-text-fill: white; " +
                           "-fx-font-weight: bold; -fx-font-size: 14px; " +
                           "-fx-background-radius: 25px;");
        
        // Hover effect
        signupButton.setOnMouseEntered(e -> 
            signupButton.setStyle("-fx-background-color: #1565c0; -fx-text-fill: white; " +
                              "-fx-font-weight: bold; -fx-font-size: 14px; " +
                              "-fx-background-radius: 25px;")
        );
        signupButton.setOnMouseExited(e -> 
            signupButton.setStyle("-fx-background-color: #0d47a1; -fx-text-fill: white; " +
                              "-fx-font-weight: bold; -fx-font-size: 14px; " +
                              "-fx-background-radius: 25px;")
        );
        
        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#f44336")); // Error color
        
        // Signup button action
        signupButton.setOnAction(e -> {
            try {
                // Validate inputs
                if (usernameField.getText().trim().isEmpty()) {
                    messageLabel.setText("Le nom d'utilisateur est requis");
                    shakeNode(usernameField);
                    return;
                }
                
                if (passwordField.getText().length() < 6) {
                    messageLabel.setText("Le mot de passe doit contenir au moins 6 caractères");
                    shakeNode(passwordField);
                    return;
                }
                
                if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                    messageLabel.setText("Les mots de passe ne correspondent pas");
                    shakeNode(confirmPasswordField);
                    return;
                }
                
                boolean success = authService.register(usernameField.getText(), passwordField.getText());
                if (success) {
                    messageLabel.setTextFill(Color.web("#4caf50")); // Success color
                    messageLabel.setText("Inscription réussie! Redirection...");
                    
                    // Wait for 1.5 seconds before redirecting to login
                    Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(1.5), event -> {
                            new LoginView(authService, mainAppLauncher).show(primaryStage);
                        })
                    );
                    timeline.play();
                } else {
                    messageLabel.setText("Ce nom d'utilisateur existe déjà");
                    shakeNode(usernameField);
                }
            } catch (Exception ex) {
                messageLabel.setText("Erreur lors de l'inscription");
                ex.printStackTrace();
            }
        });
        
        // Login text and link
        HBox loginBox = new HBox(10);
        loginBox.setAlignment(Pos.CENTER);
        
        Label haveAccountLabel = new Label("Vous avez déjà un compte?");
        haveAccountLabel.setTextFill(Color.web("#757575"));
        
        Hyperlink loginLink = new Hyperlink("Se connecter");
        loginLink.setTextFill(Color.web("#0d47a1"));
        loginLink.setOnAction(e -> 
            new LoginView(authService, mainAppLauncher).show(primaryStage)
        );
        
        loginBox.getChildren().addAll(haveAccountLabel, loginLink);
        
        // Add everything to the form
        VBox formElements = new VBox(20);
        formElements.setAlignment(Pos.CENTER);
        formElements.getChildren().addAll(
            welcomeLabel, 
            subheaderLabel, 
            new VBox(20, 
                usernameField, 
                new VBox(5, passwordField, passwordReqLabel), 
                confirmPasswordField
            ),
            signupButton, 
            messageLabel, 
            loginBox
        );
        
        rightPanel.getChildren().add(formElements);
        
        return rightPanel;
    }
    
    private TextField createStylizedTextField(String placeholder) {
        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setPrefHeight(50);
        field.setPrefWidth(300);
        field.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 25px; " +
                      "-fx-padding: 10 20 10 20; -fx-font-size: 14px;");
        
        // Focus effect
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 25px; " +
                              "-fx-padding: 10 20 10 20; -fx-font-size: 14px; " +
                              "-fx-border-color: #0d47a1; -fx-border-radius: 25px; -fx-border-width: 2px;");
            } else {
                field.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 25px; " +
                              "-fx-padding: 10 20 10 20; -fx-font-size: 14px;");
            }
        });
        
        return field;
    }
    
    private PasswordField createStylizedPasswordField(String placeholder) {
        PasswordField field = new PasswordField();
        field.setPromptText(placeholder);
        field.setPrefHeight(50);
        field.setPrefWidth(300);
        field.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 25px; " +
                      "-fx-padding: 10 20 10 20; -fx-font-size: 14px;");
        
        // Focus effect
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 25px; " +
                              "-fx-padding: 10 20 10 20; -fx-font-size: 14px; " +
                              "-fx-border-color: #0d47a1; -fx-border-radius: 25px; -fx-border-width: 2px;");
            } else {
                field.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 25px; " +
                              "-fx-padding: 10 20 10 20; -fx-font-size: 14px;");
            }
        });
        
        return field;
    }
    
    private ImageView createPlaceholderIcon() {
        try {
            return new ImageView(new Image(getClass().getResourceAsStream("/images/signup_icon.png")));
        } catch (Exception e) {
            // Create a placeholder if image is not found
            Rectangle placeholder = new Rectangle(100, 100, Color.WHITE);
            placeholder.setArcWidth(20);
            placeholder.setArcHeight(20);
            ImageView view = new ImageView();
            // Use a dummy image to avoid NullPointerException
            view.setImage(new WritableImage(100, 100));
            return view;
        }
    }
    
    // Animation for error indication
    private void shakeNode(Node node) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(0), new KeyValue(node.translateXProperty(), 0)),
            new KeyFrame(Duration.millis(100), new KeyValue(node.translateXProperty(), -10)),
            new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), 10)),
            new KeyFrame(Duration.millis(300), new KeyValue(node.translateXProperty(), -10)),
            new KeyFrame(Duration.millis(400), new KeyValue(node.translateXProperty(), 0))
        );
        timeline.play();
    }
}