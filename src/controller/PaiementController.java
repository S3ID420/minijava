package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import model.Candidat;
import model.Paiement;
import service.CandidatService;
import service.PaiementService;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class PaiementController implements Initializable {

    @FXML private ComboBox<Candidat> candidatComboBox;
    @FXML private TextField montantField;
    @FXML private ComboBox<Paiement.TypePaiement> typePaiementComboBox;
    @FXML private TextField numeroChequeField;
    @FXML private TextArea descriptionArea;
    @FXML private TableView<Paiement> paiementsTable;
    @FXML private TableColumn<Paiement, Integer> colId;
    @FXML private TableColumn<Paiement, Double> colMontant;
    @FXML private TableColumn<Paiement, LocalDate> colDate;
    @FXML private TableColumn<Paiement, String> colTypePaiement;
    @FXML private TableColumn<Paiement, String> colDescription;
    @FXML private TableColumn<Paiement, String> colNomCandidat;
    @FXML private TableColumn<Paiement, String> colNumeroCheque;
    @FXML private Label montantTotalLabel;
    
    private final PaiementService paiementService;
    private final CandidatService candidatService;
    private ObservableList<Paiement> paiementsList;
    
    public PaiementController() {
        this.paiementService = new PaiementService();
        this.candidatService = new CandidatService();
        this.paiementsList = FXCollections.observableArrayList();
        
       
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize payment types combo box
        typePaiementComboBox.getItems().setAll(Paiement.TypePaiement.values());
        typePaiementComboBox.getSelectionModel().selectFirst();
        
        // Setup payment type listener to show/hide check number field
        typePaiementComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isCheque = newVal == Paiement.TypePaiement.CHEQUE;
            numeroChequeField.setVisible(isCheque);
            numeroChequeField.setManaged(isCheque);
            if (!isCheque) {
                numeroChequeField.clear();
            }
        });
        
        // Initialize candidates combo box
        List<Candidat> candidats = candidatService.getTousCandidats();
        candidatComboBox.setItems(FXCollections.observableArrayList(candidats));
        
        // Set custom converter for candidate combo box to display name
        candidatComboBox.setConverter(new StringConverter<Candidat>() {
            @Override
            public String toString(Candidat candidat) {
                if (candidat == null) return "";
                return candidat.getNom() + " " + candidat.getPrenom() + " (" + candidat.getCin() + ")";
            }

            @Override
            public Candidat fromString(String string) {
                return null; // Not needed for this application
            }
        });
        
        // Configure table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("datePaiement"));
        colTypePaiement.setCellValueFactory(new PropertyValueFactory<>("typePaiement"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        // For candidate name, we need a custom cell factory
        colNomCandidat.setCellValueFactory(cellData -> {
            int candidatId = cellData.getValue().getCandidatId();
            Candidat candidat = candidatService.trouverCandidatParId(candidatId);
            return new javafx.beans.property.SimpleStringProperty(
                candidat != null ? candidat.getNom() + " " + candidat.getPrenom() : "Inconnu");
        });
        
        colNumeroCheque.setCellValueFactory(new PropertyValueFactory<>("numeroCheque"));
        
        // Date format for the table
        colDate.setCellFactory(column -> {
            TableCell<Paiement, LocalDate> cell = new TableCell<Paiement, LocalDate>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty || item == null) {
                        setText(null);
                    } else {
                        setText(formatter.format(item));
                    }
                }
            };
            return cell;
        });
        
        // Format for amount column
        colMontant.setCellFactory(column -> {
            TableCell<Paiement, Double> cell = new TableCell<Paiement, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f DT", item));
                    }
                }
            };
            return cell;
        });
        
        // Load all payments
        chargerPaiements();
        
        // Add listener for table selection
        paiementsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                afficherDetailsPaiement(newSelection);
            }
        });
    }
    
    @FXML
    private void handleAjouterPaiement() {
        try {
            // Validate input
            if (candidatComboBox.getValue() == null) {
                afficherAlerte("Erreur", "Veuillez sélectionner un candidat.");
                return;
            }
            
            double montant;
            try {
                montant = Double.parseDouble(montantField.getText().replace(',', '.'));
                if (montant <= 0) {
                    afficherAlerte("Erreur", "Le montant doit être supérieur à zéro.");
                    return;
                }
            } catch (NumberFormatException e) {
                afficherAlerte("Erreur", "Veuillez entrer un montant valide.");
                return;
            }
            
            Paiement.TypePaiement typePaiement = typePaiementComboBox.getValue();
            if (typePaiement == null) {
                afficherAlerte("Erreur", "Veuillez sélectionner un type de paiement.");
                return;
            }
            
            if (typePaiement == Paiement.TypePaiement.CHEQUE && (numeroChequeField.getText() == null || numeroChequeField.getText().trim().isEmpty())) {
                afficherAlerte("Erreur", "Veuillez entrer un numéro de chèque.");
                return;
            }
            
            // Create new payment
            int candidatId = candidatComboBox.getValue().getId();
            String description = descriptionArea.getText();
            
            Paiement paiement = new Paiement(montant, typePaiement, candidatId, description);
            
            if (typePaiement == Paiement.TypePaiement.CHEQUE) {
                paiement.setNumeroCheque(numeroChequeField.getText().trim());
            }
            
            // Save to database
            boolean success = paiementService.ajouterPaiement(paiement);
            
            if (success) {
                viderFormulaire();
                chargerPaiements();
                afficherAlerte("Succès", "Paiement ajouté avec succès.");
            } else {
                afficherAlerte("Erreur", "Échec de l'ajout du paiement.");
            }
            
        } catch (Exception e) {
            afficherAlerte("Erreur", "Une erreur inattendue s'est produite: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleModifierPaiement() {
        Paiement paiementSelectionne = paiementsTable.getSelectionModel().getSelectedItem();
        if (paiementSelectionne == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner un paiement à modifier.");
            return;
        }
        
        try {
            // Validate input
            if (candidatComboBox.getValue() == null) {
                afficherAlerte("Erreur", "Veuillez sélectionner un candidat.");
                return;
            }
            
            double montant;
            try {
                montant = Double.parseDouble(montantField.getText().replace(',', '.'));
                if (montant <= 0) {
                    afficherAlerte("Erreur", "Le montant doit être supérieur à zéro.");
                    return;
                }
            } catch (NumberFormatException e) {
                afficherAlerte("Erreur", "Veuillez entrer un montant valide.");
                return;
            }
            
            Paiement.TypePaiement typePaiement = typePaiementComboBox.getValue();
            if (typePaiement == null) {
                afficherAlerte("Erreur", "Veuillez sélectionner un type de paiement.");
                return;
            }
            
            if (typePaiement == Paiement.TypePaiement.CHEQUE && (numeroChequeField.getText() == null || numeroChequeField.getText().trim().isEmpty())) {
                afficherAlerte("Erreur", "Veuillez entrer un numéro de chèque.");
                return;
            }
            
            // Update payment
            paiementSelectionne.setMontant(montant);
            paiementSelectionne.setTypePaiement(typePaiement);
            paiementSelectionne.setCandidatId(candidatComboBox.getValue().getId());
            paiementSelectionne.setDescription(descriptionArea.getText());
            
            if (typePaiement == Paiement.TypePaiement.CHEQUE) {
                paiementSelectionne.setNumeroCheque(numeroChequeField.getText().trim());
            } else {
                paiementSelectionne.setNumeroCheque(null);
            }
            
            // Save to database
            boolean success = paiementService.modifierPaiement(paiementSelectionne);
            
            if (success) {
                viderFormulaire();
                chargerPaiements();
                afficherAlerte("Succès", "Paiement mis à jour avec succès.");
            } else {
                afficherAlerte("Erreur", "Échec de la mise à jour du paiement.");
            }
            
        } catch (Exception e) {
            afficherAlerte("Erreur", "Une erreur inattendue s'est produite: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSupprimerPaiement() {
        Paiement paiementSelectionne = paiementsTable.getSelectionModel().getSelectedItem();
        if (paiementSelectionne == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner un paiement à supprimer.");
            return;
        }
        
        // Confirm deletion
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmer la suppression");
        confirmDialog.setHeaderText("Supprimer le paiement");
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer ce paiement?");
        
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = paiementService.supprimerPaiement(paiementSelectionne.getId());
                
                if (success) {
                    viderFormulaire();
                    chargerPaiements();
                    afficherAlerte("Succès", "Paiement supprimé avec succès.");
                } else {
                    afficherAlerte("Erreur", "Échec de la suppression du paiement.");
                }
            }
        });
    }
    
    @FXML
    private void handleViderFormulaire() {
        viderFormulaire();
    }
    
    private void chargerPaiements() {
        List<Paiement> paiements = paiementService.getTousPaiements();
        paiementsList.setAll(paiements);
        paiementsTable.setItems(paiementsList);
        
        // Calculate and display total
        double total = paiements.stream().mapToDouble(Paiement::getMontant).sum();
        montantTotalLabel.setText(String.format("%.2f DT", total));
    }
    
    private void afficherDetailsPaiement(Paiement paiement) {
        // Find the candidate in the combobox
        Candidat candidat = candidatService.trouverCandidatParId(paiement.getCandidatId());
        if (candidat != null) {
            candidatComboBox.setValue(candidat);
        }
        
        montantField.setText(String.format("%.2f", paiement.getMontant()));
        typePaiementComboBox.setValue(paiement.getTypePaiement());
        descriptionArea.setText(paiement.getDescription());
        
        if (paiement.getTypePaiement() == Paiement.TypePaiement.CHEQUE) {
            numeroChequeField.setVisible(true);
            numeroChequeField.setManaged(true);
            numeroChequeField.setText(paiement.getNumeroCheque());
        } else {
            numeroChequeField.setVisible(false);
            numeroChequeField.setManaged(false);
            numeroChequeField.clear();
        }
    }
    
    private void viderFormulaire() {
        candidatComboBox.getSelectionModel().clearSelection();
        montantField.clear();
        typePaiementComboBox.getSelectionModel().selectFirst();
        numeroChequeField.clear();
        descriptionArea.clear();
        paiementsTable.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void handleFiltrerParCandidat() {
        Candidat candidatSelectionne = candidatComboBox.getValue();
        if (candidatSelectionne != null) {
            List<Paiement> paiementsCandidat = paiementService.getPaiementsParCandidatId(candidatSelectionne.getId());
            paiementsList.setAll(paiementsCandidat);
            
            // Calculate and display total for this candidate
            double total = paiementsCandidat.stream().mapToDouble(Paiement::getMontant).sum();
            montantTotalLabel.setText(String.format("%.2f DT", total));
        } else {
            chargerPaiements(); // If no candidate selected, show all payments
        }
    }
    
    @FXML
    private void handleAfficherTousPaiements() {
        chargerPaiements();
    }
    
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}