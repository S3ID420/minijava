package service;

import model.Moniteur;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoniteurService {

    public boolean ajouterMoniteur(Moniteur moniteur) {
        if (getMoniteurParCin(moniteur.getCin()) != null) {
            throw new IllegalArgumentException("Un moniteur avec ce CIN existe déjà");
        }

        String query = "INSERT INTO moniteur (cin, nom, prenom, email, telephone, code_cnss, id_vehicule) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, moniteur.getCin());
            stmt.setString(2, moniteur.getNom());
            stmt.setString(3, moniteur.getPrenom());
            stmt.setString(4, moniteur.getEmail());
            stmt.setInt(5, moniteur.getTelephone());
            stmt.setInt(6, moniteur.getCodeCnss());
            stmt.setInt(7, moniteur.getIdVehicule());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur d'ajout : " + e.getMessage());
            return false;
        }
    }

    public boolean modifierMoniteur(Moniteur moniteur) {
        String query = "UPDATE moniteur SET nom = ?, prenom = ?, email = ?, telephone = ?, code_cnss = ?, id_vehicule = ? WHERE cin = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, moniteur.getNom());
            stmt.setString(2, moniteur.getPrenom());
            stmt.setString(3, moniteur.getEmail());
            stmt.setInt(4, moniteur.getTelephone());
            stmt.setInt(5, moniteur.getCodeCnss());
            stmt.setInt(6, moniteur.getIdVehicule());
            stmt.setInt(7, moniteur.getCin());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur de modification : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerMoniteur(int cin) {
        String query = "DELETE FROM moniteur WHERE cin = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, cin);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur de suppression : " + e.getMessage());
            return false;
        }
    }

    public Moniteur getMoniteurParCin(int cin) {
        String query = "SELECT * FROM moniteur WHERE cin = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, cin);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Moniteur(
                    rs.getInt("cin"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getInt("telephone"),
                    rs.getInt("code_cnss"),
                    rs.getInt("id_vehicule")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur de récupération : " + e.getMessage());
        }

        return null;
    }

    public List<Moniteur> getTousMoniteurs() {
        List<Moniteur> moniteurs = new ArrayList<>();
        String query = "SELECT * FROM moniteur ORDER BY nom, prenom";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Moniteur moniteur = new Moniteur(
                    rs.getInt("cin"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getInt("telephone"),
                    rs.getInt("code_cnss"),
                    rs.getInt("id_vehicule")
                );
                moniteurs.add(moniteur);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des moniteurs : " + e.getMessage());
        }

        return moniteurs;
    }
}
