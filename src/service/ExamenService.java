package service;

import model.Examen;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import service.VehiculeService;


public class ExamenService {
    private VehiculeService vehiculeService = new VehiculeService();
   
    public void createExamenTableIfNotExists() {
        vehiculeService.createVehiculeTableIfNotExists();
        String sql = "CREATE TABLE IF NOT EXISTS examen (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "candidat_id INT NOT NULL, " +
                "type VARCHAR(5) NOT NULL, " +
                "partie VARCHAR(10) NOT NULL, " +
                "etat VARCHAR(10) DEFAULT 'ANONYME', " +
                "date_time DATETIME NOT NULL, " +
                "duree TIME NOT NULL DEFAULT '02:00:00', " +
                "prix DOUBLE NOT NULL, " +
                "vehicule_id INT, " +
                "FOREIGN KEY (candidat_id) REFERENCES candidat(id), " +
                "FOREIGN KEY (vehicule_id) REFERENCES vehicule(id)" +
                ")";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
            System.out.println("Examen table created or already exists");
            
        } catch (SQLException e) {
            System.err.println("Error creating examen table: " + e.getMessage());
        }
    }

    // Add a new exam
    public boolean ajouterExamen(Examen examen) {
        String sql = "INSERT INTO examen (candidat_id, type, partie, etat, date_time, duree, prix, vehicule_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, examen.getCandidatId());
            pstmt.setString(2, examen.getType());
            pstmt.setString(3, examen.getPartie());
            pstmt.setString(4, examen.getEtat());
            pstmt.setTimestamp(5, Timestamp.valueOf(examen.getDateTime()));
            pstmt.setTime(6, Time.valueOf(examen.getDuree()));
            pstmt.setDouble(7, examen.getPrix());
            pstmt.setInt(8, examen.getVehiculeId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    examen.setId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error creating exam: " + e.getMessage());
            return false;
        }
    }

    // Get all exams
    public List<Examen> getTousExamens() {
        List<Examen> examens = new ArrayList<>();
        String sql = "SELECT * FROM examen ORDER BY date_time";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Examen examen = extractExamenFromResultSet(rs);
                examens.add(examen);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all exams: " + e.getMessage());
        }
        
        return examens;
    }
    
    // Get exam by ID
    public Examen getExamenParId(int examenId) {
        String sql = "SELECT * FROM examen WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, examenId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractExamenFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting exam by ID: " + e.getMessage());
        }
        
        return null;
    }

    // Get exams by candidate ID
    public List<Examen> getExamensParCandidatId(int candidatId) {
        List<Examen> examens = new ArrayList<>();
        String sql = "SELECT * FROM examen WHERE candidat_id = ? ORDER BY date_time";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, candidatId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Examen examen = extractExamenFromResultSet(rs);
                    examens.add(examen);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting exams by candidate ID: " + e.getMessage());
        }
        
        return examens;
    }

    // Update an existing exam
    public boolean modifierExamen(Examen examen) {
        String sql = "UPDATE examen SET candidat_id = ?, type = ?, partie = ?, etat = ?, " +
                     "date_time = ?, duree = ?, prix = ?, vehicule_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, examen.getCandidatId());
            pstmt.setString(2, examen.getType());
            pstmt.setString(3, examen.getPartie());
            pstmt.setString(4, examen.getEtat());
            pstmt.setTimestamp(5, Timestamp.valueOf(examen.getDateTime()));
            pstmt.setTime(6, Time.valueOf(examen.getDuree()));
            pstmt.setDouble(7, examen.getPrix());
            pstmt.setInt(8, examen.getVehiculeId());
            pstmt.setInt(9, examen.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating exam: " + e.getMessage());
            return false;
        }
    }

    // Delete an exam
    public boolean supprimerExamen(int examenId) {
        String sql = "DELETE FROM examen WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, examenId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting exam: " + e.getMessage());
            return false;
        }
    }

    // Calculate price based on exam type and partie
    public double calculerPrix(String type, String partie) {
        if (type == null || partie == null) {
            return 0;
        }

        switch (type) {
            case "A":
                return "CODE".equals(partie) ? 150.0 : 300.0;
            case "B":
                return "CODE".equals(partie) ? 200.0 : 900.0;
            case "C":
                return "CODE".equals(partie) ? 300.0 : 1200.0;
            default:
                return 0.0;
        }
    }
    
    // Helper method to extract exam from result set
    private Examen extractExamenFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int candidatId = rs.getInt("candidat_id");
        String type = rs.getString("type");
        String partie = rs.getString("partie");
        String etat = rs.getString("etat");
        LocalDateTime dateTime = rs.getTimestamp("date_time").toLocalDateTime();
        LocalTime duree = rs.getTime("duree").toLocalTime();
        double prix = rs.getDouble("prix");
        int vehiculeId = rs.getInt("vehicule_id");
        
        return new Examen(id, candidatId, type, partie, etat, dateTime, duree, prix, vehiculeId);
    }
    
    // Check if a vehicle is available at a given time
    public boolean isVehiculeAvailable(int vehiculeId, LocalDateTime dateTime, LocalTime duree, Integer excludeExamenId) {
        String sql = "SELECT COUNT(*) FROM examen WHERE vehicule_id = ? AND " +
                     "((date_time BETWEEN ? AND ?) OR " +
                     "(DATE_ADD(date_time, INTERVAL TIME_TO_SEC(duree) SECOND) BETWEEN ? AND ?))";
        
        if (excludeExamenId != null) {
            sql += " AND id != ?";
        }
        
        LocalDateTime endDateTime = dateTime.plusHours(duree.getHour())
                                           .plusMinutes(duree.getMinute())
                                           .plusSeconds(duree.getSecond());
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, vehiculeId);
            pstmt.setTimestamp(2, Timestamp.valueOf(dateTime));
            pstmt.setTimestamp(3, Timestamp.valueOf(endDateTime));
            pstmt.setTimestamp(4, Timestamp.valueOf(dateTime));
            pstmt.setTimestamp(5, Timestamp.valueOf(endDateTime));
            
            if (excludeExamenId != null) {
                pstmt.setInt(6, excludeExamenId);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0; // vehicle is available if no overlapping exams
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking vehicle availability: " + e.getMessage());
        }
        
        return false; // If there was an error, assume the vehicle is not available
    }
    
    // Check if candidate has passed the specific type and partie of exam
    public boolean hasCandidatPassedExam(int candidatId, String type, String partie) {
        String sql = "SELECT COUNT(*) FROM examen WHERE candidat_id = ? AND type = ? AND partie = ? AND etat = 'PASSE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, candidatId);
            pstmt.setString(2, type);
            pstmt.setString(3, partie);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // Candidate has passed this exam type/partie
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking candidate exam status: " + e.getMessage());
        }
        
        return false;
    }
    
    // Check if candidate has passed the CODE exam before taking CONDUITE
    public boolean canCandidatTakeConduiteExam(int candidatId, String type) {
        // If trying to take CONDUITE exam, check if CODE exam is passed
        String sql = "SELECT COUNT(*) FROM examen WHERE candidat_id = ? AND type = ? AND partie = 'CODE' AND etat = 'PASSE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, candidatId);
            pstmt.setString(2, type);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // Candidate has passed CODE exam of this type
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking if candidate can take CONDUITE exam: " + e.getMessage());
        }
        
        return false;
    }
}