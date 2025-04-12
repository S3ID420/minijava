package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.Candidat;
import model.Document;
import service.CandidatService;
import service.FileService;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

public class CandidatController {
    @FXML
    private TextField nomField;
    
    @FXML
    private TextField prenomField;
    
    @FXML
    private DatePicker dateNaissancePicker;
    
    @FXML
    private TextField cinField;
    
    @FXML
    private TextField adresseField;
    
    @FXML
    private TextField telephoneField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TableView<Candidat> candidatTable;
    
    @FXML
    private TableColumn<Candidat, Integer> idColumn;
    
    @FXML
    private TableColumn<Candidat, String> nomColumn;
    
    @FXML
    private TableColumn<Candidat, String> prenomColumn;
    
    @FXML
    private TableColumn<Candidat, String> cinColumn;
    
    @FXML
    private TableView<Document> documentTable;
    
    @FXML
    private TableColumn<Document, Integer> docIdColumn;
    
    @FXML
    private TableColumn<Document, String> docNomColumn;
    
    @FXML
    private TableColumn<Document, String> docTypeColumn;
    
    @FXML
    private TableColumn<Document, LocalDate> docDateColumn;
    
    @FXML
    private ImageView documentPreview;
    
    private CandidatService candidatService;
    private FileService fileService;
    private ObservableList<Candidat> candidatsList;
    private ObservableList<Document> documentsList;
    private Candidat selectedCandidat;
    
    public void initialize() {
        candidatService = new CandidatService();
        fileService = new FileService();
        
        // Initialize tables
        initializeCandidatTable();
        initializeDocumentTable();
        
        // Load candidates
        chargerCandidats();
        
        // Clear document preview
        documentPreview.setImage(null);
    }
    
    // Initialize candidate table
    private void initializeCandidatTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        
        candidatTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    afficherDetailsCandidat(newSelection);
                }
            }
        );
    }
    
    // Initialize document table
    private void initializeDocumentTable() {
        docIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        docNomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        docTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        docDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateAjout"));
        
        documentTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    afficherDocument(newSelection);
                }
            }
        );
    }
    
    // Load candidates
    private void chargerCandidats() {
        candidatsList = FXCollections.observableArrayList(candidatService.getTousCandidats());
        candidatTable.setItems(candidatsList);
    }
    
    // Display candidate details
    private void afficherDetailsCandidat(Candidat candidat) {
        selectedCandidat = candidat;
        
        // Fill form fields
        nomField.setText(candidat.getNom());
        prenomField.setText(candidat.getPrenom());
        dateNaissancePicker.setValue(candidat.getDateNaissance());
        cinField.setText(candidat.getCin());
        adresseField.setText(candidat.getAdresse());
        telephoneField.setText(candidat.getTelephone());
        emailField.setText(candidat.getEmail());
        
        // Load candidate documents
        documentsList = FXCollections.observableArrayList(candidat.getDocuments());
        documentTable.setItems(documentsList);
    }
    
    // Display document preview
    private void afficherDocument(Document document) {
        Path path = document.getCheminFichier();
        
        if (fileService.isImage(path.toFile())) {
            documentPreview.setImage(fileService.loadImage(path));
        } else {
            // For non-image documents, show a placeholder or icon
            documentPreview.setImage(null);
            
            // Show an alert with option to open the file externally
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Ouvrir le document");
            alert.setHeaderText("Ce type de document ne peut pas être prévisualisé.");
            alert.setContentText("Voulez-vous ouvrir ce document avec l'application par défaut?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Open file with default system application
                    java.awt.Desktop.getDesktop().open(path.toFile());
                } catch (Exception e) {
                    afficherErreur("Impossible d'ouvrir le fichier: " + e.getMessage());
                }
            }
        }
    }
    
    // Add a new candidate
    @FXML
    private void ajouterCandidat() {
        if (!validerFormulaire()) {
            return;
        }
        
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        String cin = cinField.getText().trim();
        String adresse = adresseField.getText().trim();
        String telephone = telephoneField.getText().trim();
        String email = emailField.getText().trim();
        
        if (candidatService.ajouterCandidat(nom, prenom, dateNaissance, cin, adresse, telephone, email)) {
            afficherInfo("Candidat ajouté avec succès.");
            reinitialiserFormulaire();
            chargerCandidats();
        } else {
            afficherErreur("Erreur lors de l'ajout du candidat.");
        }
    }
    
    // Update a candidate
    @FXML
    private void modifierCandidat() {
        if (selectedCandidat == null) {
            afficherErreur("Veuillez sélectionner un candidat à modifier.");
            return;
        }
        
        if (!validerFormulaire()) {
            return;
        }
        
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        String cin = cinField.getText().trim();
        String adresse = adresseField.getText().trim();
        String telephone = telephoneField.getText().trim();
        String email = emailField.getText().trim();
        
        if (candidatService.modifierCandidat(selectedCandidat.getId(), nom, prenom, dateNaissance, 
                                          cin, adresse, telephone, email)) {
            afficherInfo("Candidat modifié avec succès.");
            chargerCandidats();
            
            // Re-select the updated candidate
            for (Candidat c : candidatsList) {
                if (c.getId() == selectedCandidat.getId()) {
                    candidatTable.getSelectionModel().select(c);
                    break;
                }
            }
        } else {
            afficherErreur("Erreur lors de la modification du candidat.");
        }
    }
    
    // Delete a candidate
    @FXML
    private void supprimerCandidat() {
        if (selectedCandidat == null) {
            afficherErreur("Veuillez sélectionner un candidat à supprimer.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le candidat");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce candidat ? Cette action supprimera également tous les documents associés.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (candidatService.supprimerCandidat(selectedCandidat.getId())) {
                afficherInfo("Candidat supprimé avec succès.");
                reinitialiserFormulaire();
                chargerCandidats();
                documentsList.clear();
                documentPreview.setImage(null);
            } else {
                afficherErreur("Erreur lors de la suppression du candidat.");
            }
        }
    }
    
    // Add a document to a candidate
    @FXML
    private void ajouterDocument() {
        if (selectedCandidat == null) {
            afficherErreur("Veuillez sélectionner un candidat pour ajouter un document.");
            return;
        }
        
        // Create a dialog to get document information
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un document");
        dialog.setHeaderText("Entrez les informations du document");
        
        // Set the button types
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Create the document name and type fields
        TextField nomField = new TextField();
        nomField.setPromptText("Nom du document");
        
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("CIN", "Permis", "Photo", "Certificat médical", "Autre");
        typeComboBox.setPromptText("Type de document");
        
        // Create layout
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeComboBox, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        
        // Convert the result to an array of strings when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new String[]{nomField.getText(), typeComboBox.getValue()};
            }
            return null;
        });
        
        Optional<String[]> result = dialog.showAndWait();
        
        result.ifPresent(docInfo -> {
            String nomDocument = docInfo[0];
            String typeDocument = docInfo[1];
            
            if (nomDocument == null || nomDocument.trim().isEmpty()) {
                afficherErreur("Le nom du document est obligatoire.");
                return;
            }
            
            if (typeDocument == null || typeDocument.trim().isEmpty()) {
                afficherErreur("Le type du document est obligatoire.");
                return;
            }
            
            // Show file chooser to select document file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner un document");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("Documents PDF", "*.pdf")
            );
            
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                if (candidatService.ajouterDocumentACandidat(selectedCandidat.getId(), nomDocument, typeDocument, selectedFile)) {
                    afficherInfo("Document ajouté avec succès.");
                    // Refresh the document list
                    afficherDetailsCandidat(candidatService.trouverCandidatParId(selectedCandidat.getId()));
                } else {
                    afficherErreur("Erreur lors de l'ajout du document.");
                }
            }
        });
    }
    
    // Delete a document
    @FXML
    private void supprimerDocument() {
        if (selectedCandidat == null) {
            afficherErreur("Veuillez sélectionner un candidat.");
            return;
        }
        
        Document selectedDocument = documentTable.getSelectionModel().getSelectedItem();
        if (selectedDocument == null) {
            afficherErreur("Veuillez sélectionner un document à supprimer.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le document");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce document ?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (candidatService.supprimerDocumentDeCandidat(selectedCandidat.getId(), selectedDocument.getId())) {
                afficherInfo("Document supprimé avec succès.");
                // Refresh the document list
                afficherDetailsCandidat(candidatService.trouverCandidatParId(selectedCandidat.getId()));
                documentPreview.setImage(null);
            } else {
                afficherErreur("Erreur lors de la suppression du document.");
            }
        }
    }
    
    // Reset form
    @FXML
    private void reinitialiserFormulaire() {
        nomField.clear();
        prenomField.clear();
        dateNaissancePicker.setValue(null);
        cinField.clear();
        adresseField.clear();
        telephoneField.clear();
        emailField.clear();
        selectedCandidat = null;
        candidatTable.getSelectionModel().clearSelection();
        documentsList.clear();
        documentPreview.setImage(null);
    }
    
    // Validate form
    private boolean validerFormulaire() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        LocalDate dateNaissance = dateNaissancePicker.getValue();
        String cin = cinField.getText().trim();
        
        if (nom.isEmpty()) {
            afficherErreur("Le nom est obligatoire.");
            return false;
        }
        
        if (prenom.isEmpty()) {
            afficherErreur("Le prénom est obligatoire.");
            return false;
        }
        
        if (dateNaissance == null) {
            afficherErreur("La date de naissance est obligatoire.");
            return false;
        }
        
        if (cin.isEmpty()) {
            afficherErreur("Le numéro CIN est obligatoire.");
            return false;
        }
        
        return true;
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