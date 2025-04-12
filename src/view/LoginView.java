package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import service.AuthService;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class LoginView {
    private AuthService authService;
    private MainAppLauncher mainAppLauncher;

    public LoginView(AuthService authService, MainAppLauncher mainAppLauncher) {
        this.authService = authService;
        this.mainAppLauncher = mainAppLauncher;
    }

    public void show(Stage primaryStage) {
        try {
            // Create main container with two sides
            HBox mainContainer = new HBox();

            // Left side - decorative panel
            VBox leftPanel = createDecorationPanel();

            // Right side - login form
            VBox rightPanel = createLoginForm(primaryStage);

            // Add both panels to main container
            mainContainer.getChildren().addAll(leftPanel, rightPanel);
            leftPanel.setPrefWidth(450);
            leftPanel.setMinWidth(450);
            leftPanel.setMaxWidth(450); 
            rightPanel.setPrefWidth(450);
            rightPanel.setMinWidth(450);
            rightPanel.setMaxWidth(450);
            mainContainer.setMinWidth(900);
            mainContainer.setAlignment(Pos.CENTER);

            // Set background for entire scene
            mainContainer.setStyle("-fx-background-color: white;");

            Scene scene = new Scene(mainContainer, 900, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Auto-École - Login");
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Error displaying login view: ");
            e.printStackTrace();
        }
    }

    private VBox createDecorationPanel() {
        VBox leftPanel = new VBox(20);
        leftPanel.setPadding(new Insets(30));
        leftPanel.setAlignment(Pos.CENTER);

        // Create gradient background
        Stop[] stops = new Stop[] {
                new Stop(0, Color.web("#1a237e")),
                new Stop(1, Color.web("#3949ab"))
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

        Label tagline = new Label("SYSTÈME DE GESTION");
        tagline.setFont(Font.font("Montserrat", 18));
        tagline.setTextFill(Color.web("#e0e0e0"));

        // Create placeholder for car icon
        ImageView carIcon = createPlaceholderIcon();
        carIcon.setFitWidth(200);
        carIcon.setPreserveRatio(true);

        // Feature highlights
        VBox features = new VBox(15);
        features.setAlignment(Pos.CENTER_LEFT);

        String[] featureTexts = {
                "Gestion des candidats",
                "Suivi des examens",
                "Gestion des paiements",
                "Rapports détaillés"
        };

        for (String text : featureTexts) {
            HBox featureItem = new HBox(10);
            featureItem.setAlignment(Pos.CENTER_LEFT);

            // Create checkmark icon
            Label checkmark = new Label("✓");
            checkmark.setTextFill(Color.web("#69f0ae"));
            checkmark.setFont(Font.font("Arial", FontWeight.BOLD, 18));

            Label featureLabel = new Label(text);
            featureLabel.setTextFill(Color.WHITE);
            featureLabel.setFont(Font.font("Segoe UI", 16));

            featureItem.getChildren().addAll(checkmark, featureLabel);
            features.getChildren().add(featureItem);
        }

        // Add all elements to the panel
        VBox contentBox = new VBox(30);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(appName, tagline, carIcon, features);

        // Set up container with stacked background and content
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(background, contentBox);

        leftPanel.getChildren().add(stackPane);
        VBox.setVgrow(stackPane, Priority.ALWAYS);

        return leftPanel;
    }

    private VBox createLoginForm(Stage primaryStage) {
        VBox rightPanel = new VBox(25);
        rightPanel.setPadding(new Insets(50));
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setStyle("-fx-background-color: white;");

        // Login header
        Label welcomeLabel = new Label("Bienvenue");
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        welcomeLabel.setTextFill(Color.web("#1a237e"));

        Label subheaderLabel = new Label("Connectez-vous à votre compte");
        subheaderLabel.setFont(Font.font("Segoe UI", 16));
        subheaderLabel.setTextFill(Color.web("#757575"));

        // Input fields
        TextField usernameField = createStylizedTextField("Nom d'utilisateur");
        PasswordField passwordField = createStylizedPasswordField("Mot de passe");

        // Login button with hover effect
        Button loginButton = new Button("SE CONNECTER");
        loginButton.setPrefHeight(50);
        loginButton.setPrefWidth(250);
        loginButton.setStyle("-fx-background-color: #1a237e; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 14px; " +
                "-fx-background-radius: 25px;");

        // Hover effect
        loginButton
                .setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #3949ab; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-font-size: 14px; " +
                        "-fx-background-radius: 25px;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #1a237e; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 14px; " +
                "-fx-background-radius: 25px;"));

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#f44336")); // Error color

        // Login button action
        loginButton.setOnAction(e -> {
            try {
                boolean success = authService.login(usernameField.getText(), passwordField.getText());
                if (success) {
                    mainAppLauncher.launchMainApp(primaryStage);
                } else {
                    messageLabel.setText("Nom d'utilisateur ou mot de passe incorrect");

                    // Shake animation to indicate error
                    shakeNode(usernameField);
                    shakeNode(passwordField);
                }
            } catch (Exception ex) {
                messageLabel.setText("Erreur lors de la connexion");
                ex.printStackTrace();
            }
        });

        // Sign up text and link
        HBox signupBox = new HBox(10);
        signupBox.setAlignment(Pos.CENTER);

        Label noAccountLabel = new Label("Vous n'avez pas de compte?");
        noAccountLabel.setTextFill(Color.web("#757575"));

        Hyperlink signupLink = new Hyperlink("S'inscrire");
        signupLink.setTextFill(Color.web("#1a237e"));
        signupLink.setOnAction(e -> new SignupView(authService, mainAppLauncher).show(primaryStage));

        signupBox.getChildren().addAll(noAccountLabel, signupLink);

        // Add everything to the form
        VBox formElements = new VBox(25);
        formElements.setAlignment(Pos.CENTER);
        formElements.getChildren().addAll(
                welcomeLabel,
                subheaderLabel,
                new VBox(20, usernameField, passwordField),
                loginButton,
                messageLabel,
                signupBox);

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

        return field;
    }

    private PasswordField createStylizedPasswordField(String placeholder) {
        PasswordField field = new PasswordField();
        field.setPromptText(placeholder);
        field.setPrefHeight(50);
        field.setPrefWidth(300);
        field.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 25px; " +
                "-fx-padding: 10 20 10 20; -fx-font-size: 14px;");

        return field;
    }

    private ImageView createPlaceholderIcon() {
        try {
            return new ImageView(new Image(getClass().getResourceAsStream("/images/car_icon.png")));
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
                new KeyFrame(Duration.millis(400), new KeyValue(node.translateXProperty(), 0)));
        timeline.play();
    }
}