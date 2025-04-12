package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.AutoEcole;
import service.AutoEcoleService;

public class AutoEcoleController {
    @FXML
    private TextField nomField;
    
    @FXML
    private TextField adresseField;
    
    @FXML
    private TextField telephoneField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField faxField;
    
    @FXML
    private TextField siteWebField;
    
    private AutoEcoleService autoEcoleService;
    
    public void initialize() {
        autoEcoleService = new AutoEcoleService();
        chargerInfos();
    }
    
    // Load auto-school information into fields
    private void chargerInfos() {
        AutoEcole autoEcole = autoEcoleService.getAutoEcole();
        nomField.setText(autoEcole.getNom());
        adresseField.setText(autoEcole.getAdresse());
        telephoneField.setText(autoEcole.getTelephone());
        emailField.setText(autoEcole.getEmail());
        faxField.setText(autoEcole.getFax());
        siteWebField.setText(autoEcole.getSiteWeb());
    }
    
    // Save auto-school information
    @FXML
    private void sauvegarder() {
        String nom = nomField.getText().trim();
        String adresse = adresseField.getText().trim();
        String telephone = telephoneField.getText().trim();
        String email = emailField.getText().trim();
        String fax = faxField.getText().trim();
        String siteWeb = siteWebField.getText().trim();
        
        // Basic validation
        if (nom.isEmpty()) {
            afficherErreur("Le nom de l'auto-école est obligatoire.");
            return;
        }
        
        if (autoEcoleService.updateAutoEcole(nom, adresse, telephone, email, fax, siteWeb)) {
            afficherInfo("Les informations de l'auto-école ont été sauvegardées avec succès.");
        } else {
            afficherErreur("Erreur lors de la sauvegarde des informations.");
        }
    }
    
    // Reset form
    @FXML
    private void reinitialiser() {
        chargerInfos();
    }
    
    // Display error message
    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Display information message
    private void afficherInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}