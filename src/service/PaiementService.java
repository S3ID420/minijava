package service;

import model.Paiement;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaiementService {
    
    // Add a new payment
    public boolean ajouterPaiement(Paiement paiement) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO paiement (montant, date_paiement, type_paiement, description, candidat_id, numero_cheque) " +
                     "VALUES (?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setDouble(1, paiement.getMontant());
            stmt.setDate(2, Date.valueOf(paiement.getDatePaiement()));
            stmt.setString(3, paiement.getTypePaiement().name());
            stmt.setString(4, paiement.getDescription());
            stmt.setInt(5, paiement.getCandidatId());
            stmt.setString(6, paiement.getNumeroCheque());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    paiement.setId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error creating payment: " + e.getMessage());
            return false;
        }
    }
    
    // Get all payments
    public List<Paiement> getTousPaiements() {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement ORDER BY date_paiement DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Paiement paiement = extractPaiementFromResultSet(rs);
                paiements.add(paiement);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all payments: " + e.getMessage());
        }
        
        return paiements;
    }
    
    // Get payments by candidate ID
    public List<Paiement> getPaiementsParCandidatId(int candidatId) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement WHERE candidat_id = ? ORDER BY date_paiement DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, candidatId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Paiement paiement = extractPaiementFromResultSet(rs);
                    paiements.add(paiement);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting payments by candidate ID: " + e.getMessage());
        }
        
        return paiements;
    }
    
    // Get payment by ID
    public Paiement getPaiementParId(int paiementId) {
        String sql = "SELECT * FROM paiement WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, paiementId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractPaiementFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting payment by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // Update an existing payment
    public boolean modifierPaiement(Paiement paiement) {
        String sql = "UPDATE paiement SET montant = ?, date_paiement = ?, type_paiement = ?, " +
                     "description = ?, candidat_id = ?, numero_cheque = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, paiement.getMontant());
            pstmt.setDate(2, Date.valueOf(paiement.getDatePaiement()));
            pstmt.setString(3, paiement.getTypePaiement().name());
            pstmt.setString(4, paiement.getDescription());
            pstmt.setInt(5, paiement.getCandidatId());
            pstmt.setString(6, paiement.getNumeroCheque());
            pstmt.setInt(7, paiement.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating payment: " + e.getMessage());
            return false;
        }
    }
    
    // Delete a payment
    public boolean supprimerPaiement(int paiementId) {
        String sql = "DELETE FROM paiement WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, paiementId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting payment: " + e.getMessage());
            return false;
        }
    }
    
    // Helper method to extract payment from result set
    private Paiement extractPaiementFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        double montant = rs.getDouble("montant");
        LocalDate datePaiement = rs.getDate("date_paiement").toLocalDate();
        Paiement.TypePaiement typePaiement = Paiement.TypePaiement.valueOf(rs.getString("type_paiement"));
        String description = rs.getString("description");
        int candidatId = rs.getInt("candidat_id");
        String numeroCheque = rs.getString("numero_cheque");
        
        return new Paiement(id, montant, datePaiement, typePaiement, candidatId, description, numeroCheque);
    }
    
    // Method to create the payments table if it doesn't exist
    public void createPaiementsTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS paiement (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "montant DOUBLE NOT NULL, " +
                     "date_paiement DATE NOT NULL, " +
                     "type_paiement VARCHAR(10) NOT NULL, " +
                     "description TEXT, " +
                     "candidat_id INT NOT NULL, " +
                     "numero_cheque VARCHAR(50), " +
                     "FOREIGN KEY (candidat_id) REFERENCES candidat(id) ON DELETE CASCADE)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
            System.out.println("Paiements table created or already exists");
            
        } catch (SQLException e) {
            System.err.println("Error creating payments table: " + e.getMessage());
        }
    }
    
    // Calculate total payments for a candidate
    public double calculerTotalPaiementsParCandidatId(int candidatId) {
        List<Paiement> paiements = getPaiementsParCandidatId(candidatId);
        return paiements.stream().mapToDouble(Paiement::getMontant).sum();
    }
}