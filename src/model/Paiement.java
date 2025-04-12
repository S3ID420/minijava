package model;

import java.time.LocalDate;
import java.util.UUID;

public class Paiement {
    private int id;
    private double montant;
    private LocalDate datePaiement;
    private TypePaiement typePaiement;
    private String description;
    private int candidatId;
    private String numeroCheque; // Only used for check payments

    public enum TypePaiement {
        ESPECE("Espèce"),
        CHEQUE("Chèque");

        private final String displayName;

        TypePaiement(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    // Constructor for new payments
    public Paiement(double montant, TypePaiement typePaiement, int candidatId, String description) {
        this.montant = montant;
        this.datePaiement = LocalDate.now();
        this.typePaiement = typePaiement;
        this.candidatId = candidatId;
        this.description = description;
    }

    // Constructor with ID for database loading
    public Paiement(int id, double montant, LocalDate datePaiement, TypePaiement typePaiement, 
                  int candidatId, String description, String numeroCheque) {
        this.id = id;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.candidatId = candidatId;
        this.description = description;
        this.numeroCheque = numeroCheque;
    }

    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public TypePaiement getTypePaiement() {
        return typePaiement;
    }

    public void setTypePaiement(TypePaiement typePaiement) {
        this.typePaiement = typePaiement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCandidatId() {
        return candidatId;
    }

    public void setCandidatId(int candidatId) {
        this.candidatId = candidatId;
    }

    public String getNumeroCheque() {
        return numeroCheque;
    }

    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "id=" + id +
                ", montant=" + montant +
                ", datePaiement=" + datePaiement +
                ", typePaiement=" + typePaiement +
                ", description='" + description + '\'' +
                ", candidatId=" + candidatId +
                ", numeroCheque='" + numeroCheque + '\'' +
                '}';
    }
}