package service;

import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class VehiculeService {
    
    public void createVehiculeTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS vehicule (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "marque VARCHAR(50) NOT NULL, " +
                "modele VARCHAR(50) NOT NULL, " +
                "immatriculation VARCHAR(20) NOT NULL, " +
                "annee INT, " +
                "type VARCHAR(5) NOT NULL" +
                ")";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
            System.out.println("Vehicule table created or already exists");
            
        } catch (SQLException e) {
            System.err.println("Error creating vehicule table: " + e.getMessage());
        }
    }
}