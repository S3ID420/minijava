package model;

import java.time.LocalDate;

public class Paiement {
    private int id;
    private double montant;
    private LocalDate datePaiement;
    private TypePaiement typePaiement;
    private String description;
    private int candidatId;
    private String numeroCheque; // Only used for check payments
    private VehicleType vehicleType; // New field for vehicle type
    private int nombreSeances; // For session payments
    private int nombreTranches; // For installment payments
    private int trancheActuelle; // Current installment number

    public enum TypePaiement {
        ESPECE("Espèce"),
        CHEQUE("Chèque"),
        TRANCHE("Paiement par tranche"),
        SEANCE("Paiement par séance");

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
    
    public enum VehicleType {
        A("Type A"),
        B("Type B"),
        C("Type C");
        
        private final String displayName;
        
        VehicleType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
        
        // Default prices by vehicle type
        public double getDefaultSessionPrice() {
            switch(this) {
                case A: return 30.0; // Example price for Type A
                case B: return 40.0; // Example price for Type B
                case C: return 50.0; // Example price for Type C
                default: return 0.0;
            }
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
    
    // Constructor for session-based payments
    public Paiement(double montant, int candidatId, String description, VehicleType vehicleType, int nombreSeances) {
        this.montant = montant;
        this.datePaiement = LocalDate.now();
        this.typePaiement = TypePaiement.SEANCE;
        this.candidatId = candidatId;
        this.description = description;
        this.vehicleType = vehicleType;
        this.nombreSeances = nombreSeances;
    }
    
    // Constructor for installment payments
    public Paiement(double montant, int candidatId, String description, int nombreTranches, int trancheActuelle) {
        this.montant = montant;
        this.datePaiement = LocalDate.now();
        this.typePaiement = TypePaiement.TRANCHE;
        this.candidatId = candidatId;
        this.description = description;
        this.nombreTranches = nombreTranches;
        this.trancheActuelle = trancheActuelle;
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
    
    // Full constructor with all fields
    public Paiement(int id, double montant, LocalDate datePaiement, TypePaiement typePaiement,
                   int candidatId, String description, String numeroCheque, 
                   VehicleType vehicleType, int nombreSeances, int nombreTranches, int trancheActuelle) {
        this.id = id;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.candidatId = candidatId;
        this.description = description;
        this.numeroCheque = numeroCheque;
        this.vehicleType = vehicleType;
        this.nombreSeances = nombreSeances;
        this.nombreTranches = nombreTranches;
        this.trancheActuelle = trancheActuelle;
    }

    // Getters and setters for existing fields
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
    
    // Getters and setters for new fields
    public VehicleType getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    public int getNombreSeances() {
        return nombreSeances;
    }
    
    public void setNombreSeances(int nombreSeances) {
        this.nombreSeances = nombreSeances;
    }
    
    public int getNombreTranches() {
        return nombreTranches;
    }
    
    public void setNombreTranches(int nombreTranches) {
        this.nombreTranches = nombreTranches;
    }
    
    public int getTrancheActuelle() {
        return trancheActuelle;
    }
    
    public void setTrancheActuelle(int trancheActuelle) {
        this.trancheActuelle = trancheActuelle;
    }
    
    // Helper method to get formatted payment info based on type
    public String getFormattedPaymentInfo() {
        switch(typePaiement) {
            case SEANCE:
                return String.format("Véhicule %s - %d séances", 
                        vehicleType != null ? vehicleType.toString() : "N/A", 
                        nombreSeances);
            case TRANCHE:
                return String.format("Tranche %d/%d", trancheActuelle, nombreTranches);
            case CHEQUE:
                return String.format("Chèque N° %s", numeroCheque != null ? numeroCheque : "N/A");
            default:
                return typePaiement.toString();
        }
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
                ", vehicleType=" + vehicleType +
                ", nombreSeances=" + nombreSeances +
                ", nombreTranches=" + nombreTranches +
                ", trancheActuelle=" + trancheActuelle +
                '}';
    }
}