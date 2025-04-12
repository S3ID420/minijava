package service;

import model.AutoEcole;
import util.DatabaseConnection;

import java.sql.*;

public class AutoEcoleService {
    private AutoEcole autoEcole;
    
    public AutoEcoleService() {
        this.autoEcole = new AutoEcole();
        chargerInfos();
    }
    
    // Load auto-school information from database
    public void chargerInfos() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM auto_ecole WHERE id = 1")) {
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                autoEcole.setNom(rs.getString("nom"));
                autoEcole.setAdresse(rs.getString("adresse"));
                autoEcole.setTelephone(rs.getString("telephone"));
                autoEcole.setEmail(rs.getString("email"));
                autoEcole.setFax(rs.getString("fax"));
                autoEcole.setSiteWeb(rs.getString("site_web"));
            }
        } catch (SQLException e) {
            System.err.println("Error loading auto-école data: " + e.getMessage());
        }
    }
    
    // Save auto-school information to database
    public boolean sauvegarderInfos() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE auto_ecole SET nom = ?, adresse = ?, telephone = ?, email = ?, fax = ?, site_web = ? WHERE id = 1")) {
            
            stmt.setString(1, autoEcole.getNom());
            stmt.setString(2, autoEcole.getAdresse());
            stmt.setString(3, autoEcole.getTelephone());
            stmt.setString(4, autoEcole.getEmail());
            stmt.setString(5, autoEcole.getFax());
            stmt.setString(6, autoEcole.getSiteWeb());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error saving auto-école data: " + e.getMessage());
            return false;
        }
    }
    
    public AutoEcole getAutoEcole() {
        return autoEcole;
    }
    
    public void setAutoEcole(AutoEcole autoEcole) {
        this.autoEcole = autoEcole;
    }
    
    // Method to update auto-school information
    public boolean updateAutoEcole(String nom, String adresse, String telephone, 
                                String email, String fax, String siteWeb) {
        autoEcole.setNom(nom);
        autoEcole.setAdresse(adresse);
        autoEcole.setTelephone(telephone);
        autoEcole.setEmail(email);
        autoEcole.setFax(fax);
        autoEcole.setSiteWeb(siteWeb);
        
        return sauvegarderInfos();
    }
}