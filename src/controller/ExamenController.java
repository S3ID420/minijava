package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Examen;
import service.ExamenService;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class ExamenController implements Initializable {

    @FXML private TextField txtCandidatId;
    @FXML private ComboBox<String> cmbType;
    @FXML private RadioButton rbCode;
    @FXML private RadioButton rbConduite;
    @FXML private ToggleGroup partieGroup;
    @FXML private DatePicker dpDate;
    @FXML private ComboBox<String> cmbTime;
    @FXML private TextField txtVehiculeId;
    @FXML private TextField txtPrix;
    @FXML private ComboBox<String> cmbEtat;
    
    @FXML private TableView<Examen> tblExamens;
    @FXML private TableColumn<Examen, Integer> colId;
    @FXML private TableColumn<Examen, Integer> colCandidatId;
    @FXML private TableColumn<Examen, String> colType;
    @FXML private TableColumn<Examen, String> colPartie;
    @FXML private TableColumn<Examen, String> colEtat;
    @FXML private TableColumn<Examen, LocalDateTime> colDateTime;
    @FXML private TableColumn<Examen, Double> colPrix;
    @FXML private TableColumn<Examen, Integer> colVehiculeId;
    
    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnClear;
    @FXML private TextField txtSearchCandidatId;
    @FXML private Button btnSearch;
    
    private ExamenService examenService;
    private Examen currentExamen;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        examenService = new ExamenService();
        examenService.createExamenTableIfNotExists();
        
        setupUIComponents();
        setupTableView();
        loadAllExamens();
    }
    
    private void setupUIComponents() {
        // Setup type combobox
        cmbType.setItems(FXCollections.observableArrayList("A", "B", "C"));
        
        // Setup partie toggle group
        if (partieGroup == null) {
            partieGroup = new ToggleGroup();
        }
        rbCode.setToggleGroup(partieGroup);
        rbConduite.setToggleGroup(partieGroup);
        rbCode.setSelected(true);
        
        // Setup time combobox
        cmbTime.setItems(FXCollections.observableArrayList(
            "08:00", "10:00", "12:00", "14:00", "16:00"
        ));
        
        // Setup etat combobox
        cmbEtat.setItems(FXCollections.observableArrayList(
            "ANONYME", "PASSE", "ECHOUE"
        ));
        cmbEtat.setValue("ANONYME");
        
        // Setup listeners for price calculation
        cmbType.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updatePrice());
        rbCode.setOnAction(e -> updatePrice());
        rbConduite.setOnAction(e -> updatePrice());
        
        // Add listener for partie selection to check eligibility for CONDUITE
        rbConduite.setOnAction(e -> {
            if (rbConduite.isSelected() && !txtCandidatId.getText().isEmpty() && cmbType.getValue() != null) {
                try {
                    int candidatId = Integer.parseInt(txtCandidatId.getText().trim());
                    String type = cmbType.getValue();
                    
                    if (!examenService.canCandidatTakeConduiteExam(candidatId, type)) {
                        showAlert(Alert.AlertType.WARNING, "Warning", "CODE Exam Required", 
                                 "This candidate must pass the CODE exam for type " + type + " before taking the CONDUITE exam.");
                    }
                } catch (NumberFormatException ex) {
                    // Ignore if candidat ID is not a valid number
                }
            }
        });
        
        // Add listener for candidat ID changes
        txtCandidatId.textProperty().addListener((obs, oldVal, newVal) -> {
            if (rbConduite.isSelected() && !newVal.isEmpty() && cmbType.getValue() != null) {
                try {
                    int candidatId = Integer.parseInt(newVal.trim());
                    String type = cmbType.getValue();
                    
                    if (!examenService.canCandidatTakeConduiteExam(candidatId, type)) {
                        showAlert(Alert.AlertType.WARNING, "Warning", "CODE Exam Required", 
                                 "This candidate must pass the CODE exam for type " + type + " before taking the CONDUITE exam.");
                    }
                } catch (NumberFormatException ex) {
                    // Ignore if candidat ID is not a valid number
                }
            }
        });
    }
    
    private void setupTableView() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCandidatId.setCellValueFactory(new PropertyValueFactory<>("candidatId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPartie.setCellValueFactory(new PropertyValueFactory<>("partie"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        colDateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colVehiculeId.setCellValueFactory(new PropertyValueFactory<>("vehiculeId"));
        
        // Add row selection listener
        tblExamens.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    currentExamen = newSelection;
                    populateFormFromExamen(currentExamen);
                }
            });
    }
    
    private void updatePrice() {
        String type = cmbType.getValue();
        String partie = rbCode.isSelected() ? "CODE" : "CONDUITE";
        
        if (type != null) {
            double prix = examenService.calculerPrix(type, partie);
            txtPrix.setText(String.valueOf(prix));
        }
    }
    
    private void loadAllExamens() {
        List<Examen> examens = examenService.getTousExamens();
        tblExamens.setItems(FXCollections.observableArrayList(examens));
    }
    
    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            validateInputs();
            
            int candidatId = Integer.parseInt(txtCandidatId.getText().trim());
            String type = cmbType.getValue();
            String partie = rbCode.isSelected() ? "CODE" : "CONDUITE";
            String etat = cmbEtat.getValue();
            LocalDate date = dpDate.getValue();
            String timeStr = cmbTime.getValue();
            int vehiculeId = Integer.parseInt(txtVehiculeId.getText().trim());
            double prix = Double.parseDouble(txtPrix.getText().trim());
            
            // Create LocalDateTime from date and time
            LocalTime time = LocalTime.parse(timeStr);
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            LocalTime duree = LocalTime.of(2, 0); // Default duration is 2 hours
            
            // Check if vehicle is available
            if (!examenService.isVehiculeAvailable(vehiculeId, dateTime, duree, null)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Vehicle Not Available", 
                         "The selected vehicle is not available at the specified time.");
                return;
            }
            
            // If trying to take CONDUITE exam, check if CODE exam is passed
            if ("CONDUITE".equals(partie) && !examenService.canCandidatTakeConduiteExam(candidatId, type)) {
                showAlert(Alert.AlertType.ERROR, "Error", "CODE Exam Required", 
                         "Candidate must pass the CODE exam for type " + type + " before taking the CONDUITE exam.");
                return;
            }
            
            // Check if candidate has already passed this exam
            if (examenService.hasCandidatPassedExam(candidatId, type, partie)) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirmation");
                confirmAlert.setHeaderText("Exam Already Passed");
                confirmAlert.setContentText("Candidate has already passed this exam type. Do you want to continue?");
                
                if (confirmAlert.showAndWait().get() != ButtonType.OK) {
                    return;
                }
            }
            
            // Create new exam
            Examen examen = new Examen(candidatId, type, partie, etat, dateTime, duree, prix, vehiculeId);
            
            if (examenService.ajouterExamen(examen)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Exam Added", 
                          "Exam has been successfully added.");
                clearForm();
                loadAllExamens();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to Add Exam", 
                          "There was an error adding the exam.");
            }
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", e.getMessage());
        }
    }
    
    @FXML
    private void handleModifier(ActionEvent event) {
        if (currentExamen == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "No Exam Selected", 
                      "Please select an exam from the table to update.");
            return;
        }
        
        try {
            validateInputs();
            
            int candidatId = Integer.parseInt(txtCandidatId.getText().trim());
            String type = cmbType.getValue();
            String partie = rbCode.isSelected() ? "CODE" : "CONDUITE";
            String etat = cmbEtat.getValue();
            LocalDate date = dpDate.getValue();
            String timeStr = cmbTime.getValue();
            int vehiculeId = Integer.parseInt(txtVehiculeId.getText().trim());
            double prix = Double.parseDouble(txtPrix.getText().trim());
            
            // Create LocalDateTime from date and time
            LocalTime time = LocalTime.parse(timeStr);
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            LocalTime duree = LocalTime.of(2, 0); // Default duration is 2 hours
            
            // Check if vehicle is available (excluding the current exam)
            if (!examenService.isVehiculeAvailable(vehiculeId, dateTime, duree, currentExamen.getId())) {
                showAlert(Alert.AlertType.ERROR, "Error", "Vehicle Not Available", 
                         "The selected vehicle is not available at the specified time.");
                return;
            }
            
            // If trying to take CONDUITE exam, check if CODE exam is passed
            if ("CONDUITE".equals(partie) && !examenService.canCandidatTakeConduiteExam(candidatId, type)) {
                showAlert(Alert.AlertType.ERROR, "Error", "CODE Exam Required", 
                         "Candidate must pass the CODE exam for type " + type + " before taking the CONDUITE exam.");
                return;
            }
            
            // Update the exam object
            currentExamen.setCandidatId(candidatId);
            currentExamen.setType(type);
            currentExamen.setPartie(partie);
            currentExamen.setEtat(etat);
            currentExamen.setDateTime(dateTime);
            currentExamen.setDuree(duree);
            currentExamen.setVehiculeId(vehiculeId);
            currentExamen.setPrix(prix);
            
            if (examenService.modifierExamen(currentExamen)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Exam Updated", 
                          "Exam has been successfully updated.");
                clearForm();
                loadAllExamens();
                currentExamen = null;
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to Update Exam", 
                          "There was an error updating the exam.");
            }
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", e.getMessage());
        }
    }
    
    @FXML
    private void handleSupprimer(ActionEvent event) {
        if (currentExamen == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "No Exam Selected", 
                      "Please select an exam from the table to delete.");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Exam");
        confirmAlert.setContentText("Are you sure you want to delete this exam?");
        
        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            if (examenService.supprimerExamen(currentExamen.getId())) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Exam Deleted", 
                          "Exam has been successfully deleted.");
                clearForm();
                loadAllExamens();
                currentExamen = null;
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to Delete Exam", 
                          "There was an error deleting the exam.");
            }
        }
    }
    
    @FXML
    private void handleSearch(ActionEvent event) {
        String candidatIdStr = txtSearchCandidatId.getText().trim();
        
        if (!candidatIdStr.isEmpty()) {
            try {
                int candidatId = Integer.parseInt(candidatIdStr);
                List<Examen> examens = examenService.getExamensParCandidatId(candidatId);
                tblExamens.setItems(FXCollections.observableArrayList(examens));
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid Candidate ID", 
                          "Please enter a valid numeric ID.");
            }
        } else {
            loadAllExamens();
        }
    }
    
    @FXML
    private void handleClear(ActionEvent event) {
        clearForm();
        currentExamen = null;
        tblExamens.getSelectionModel().clearSelection();
    }
    
    private void clearForm() {
        txtCandidatId.clear();
        cmbType.getSelectionModel().clearSelection();
        rbCode.setSelected(true);
        dpDate.setValue(null);
        cmbTime.getSelectionModel().clearSelection();
        txtVehiculeId.clear();
        txtPrix.clear();
        cmbEtat.setValue("ANONYME");
    }
    
    private void populateFormFromExamen(Examen examen) {
        txtCandidatId.setText(String.valueOf(examen.getCandidatId()));
        cmbType.setValue(examen.getType());
        
        if ("CODE".equals(examen.getPartie())) {
            rbCode.setSelected(true);
        } else {
            rbConduite.setSelected(true);
        }
        
        dpDate.setValue(examen.getDateTime().toLocalDate());
        cmbTime.setValue(String.format("%02d:%02d", examen.getDateTime().getHour(), examen.getDateTime().getMinute()));
        txtVehiculeId.setText(String.valueOf(examen.getVehiculeId()));
        txtPrix.setText(String.valueOf(examen.getPrix()));
        cmbEtat.setValue(examen.getEtat());
    }
    
    private void validateInputs() throws Exception {
        if (txtCandidatId.getText().trim().isEmpty()) {
            throw new Exception("Candidat ID is required");
        }
        
        if (cmbType.getValue() == null) {
            throw new Exception("Exam type is required");
        }
        
        if (dpDate.getValue() == null) {
            throw new Exception("Date is required");
        }
        
        if (cmbTime.getValue() == null) {
            throw new Exception("Time is required");
        }
        
        if (txtVehiculeId.getText().trim().isEmpty()) {
            throw new Exception("Vehicle ID is required");
        }
        
        // Check if the fields contain valid numbers
        try {
            Integer.parseInt(txtCandidatId.getText().trim());
        } catch (NumberFormatException e) {
            throw new Exception("Candidat ID must be a number");
        }
        
        try {
            Integer.parseInt(txtVehiculeId.getText().trim());
        } catch (NumberFormatException e) {
            throw new Exception("Vehicle ID must be a number");
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
};