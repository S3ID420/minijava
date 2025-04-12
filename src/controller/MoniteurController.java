package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Moniteur;
import service.MoniteurService;

public class MoniteurController implements Initializable {
    
    @FXML
    private TextField cinField;
    
    @FXML
    private TextField nomField;
    
    @FXML
    private TextField prenomField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField telephoneField;
    
    @FXML
    private TextField codeCnssField;
    
    @FXML
    private TextField idVehiculeField;
    
    @FXML
    private Button ajouterButton;
    
    @FXML
    private Button modifierButton;
    
    @FXML
    private Button supprimerButton;
    
    @FXML
    private Button effacerButton;
    
    @FXML
    private TableView<Moniteur> moniteurTable;
    
    @FXML
    private TableColumn<Moniteur, Integer> cinColumn;
    
    @FXML
    private TableColumn<Moniteur, String> nomColumn;
    
    @FXML
    private TableColumn<Moniteur, String> prenomColumn;
    
    @FXML
    private TableColumn<Moniteur, String> emailColumn;
    
    @FXML
    private TableColumn<Moniteur, Integer> telephoneColumn;
    
    @FXML
    private TableColumn<Moniteur, Integer> codeCnssColumn;
    
    @FXML
    private TableColumn<Moniteur, Integer> idVehiculeColumn;
    
    private MoniteurService moniteurService;
    private ObservableList<Moniteur> moniteurList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        moniteurService = new MoniteurService();
        moniteurList = FXCollections.observableArrayList();
        
        // Configure table columns
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        codeCnssColumn.setCellValueFactory(new PropertyValueFactory<>("codeCnss"));
        idVehiculeColumn.setCellValueFactory(new PropertyValueFactory<>("idVehicule"));
        
        // Load data
        refreshTable();
        
        // Set up table selection listener
        moniteurTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
                ajouterButton.setDisable(true);
                modifierButton.setDisable(false);
                supprimerButton.setDisable(false);
            }
        });
        
        // Initial button states
        modifierButton.setDisable(true);
        supprimerButton.setDisable(true);
    }
    
    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            Moniteur moniteur = getMoniteurFromFields();
            moniteurService.ajouterMoniteur(moniteur);
            refreshTable();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Moniteur ajouté avec succès!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleModifier(ActionEvent event) {
        try {
            Moniteur moniteur = getMoniteurFromFields();
            moniteurService.modifierMoniteur(moniteur);
            refreshTable();
            clearFields();
            ajouterButton.setDisable(false);
            modifierButton.setDisable(true);
            supprimerButton.setDisable(true);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Moniteur modifié avec succès!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSupprimer(ActionEvent event) {
        try {
            int cin = Integer.parseInt(cinField.getText());
            moniteurService.supprimerMoniteur(cin);
            refreshTable();
            clearFields();
            ajouterButton.setDisable(false);
            modifierButton.setDisable(true);
            supprimerButton.setDisable(true);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Moniteur supprimé avec succès!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleEffacer(ActionEvent event) {
        clearFields();
        ajouterButton.setDisable(false);
        modifierButton.setDisable(true);
        supprimerButton.setDisable(true);
        moniteurTable.getSelectionModel().clearSelection();
    }
    
    private Moniteur getMoniteurFromFields() {
        try {
            int cin = Integer.parseInt(cinField.getText());
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();
            int telephone = Integer.parseInt(telephoneField.getText());
            int codeCnss = Integer.parseInt(codeCnssField.getText());
            int idVehicule = Integer.parseInt(idVehiculeField.getText());
            
            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
                throw new IllegalArgumentException("Veuillez remplir tous les champs obligatoires");
            }
            
            return new Moniteur(cin, nom, prenom, email, telephone, codeCnss, idVehicule);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Veuillez saisir des nombres valides pour CIN, téléphone, code CNSS et ID véhicule");
        }
    }
    
    private void populateFields(Moniteur moniteur) {
        cinField.setText(String.valueOf(moniteur.getCin()));
        nomField.setText(moniteur.getNom());
        prenomField.setText(moniteur.getPrenom());
        emailField.setText(moniteur.getEmail());
        telephoneField.setText(String.valueOf(moniteur.getTelephone()));
        codeCnssField.setText(String.valueOf(moniteur.getCodeCnss()));
        idVehiculeField.setText(String.valueOf(moniteur.getIdVehicule()));
        
        // Make CIN field not editable when updating
        cinField.setEditable(false);
    }
    
    private void clearFields() {
        cinField.setText("");
        nomField.setText("");
        prenomField.setText("");
        emailField.setText("");
        telephoneField.setText("");
        codeCnssField.setText("");
        idVehiculeField.setText("");
        
        // Make CIN field editable when adding new
        cinField.setEditable(true);
    }
    
    private void refreshTable() {
        moniteurList.clear();
        moniteurList.addAll(moniteurService.getTousMoniteurs());
        moniteurTable.setItems(moniteurList);
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}