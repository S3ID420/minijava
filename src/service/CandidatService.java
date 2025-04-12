package service;

import model.Candidat;
import model.Document;
import util.DatabaseConnection;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CandidatService {
    private FileService fileService;
    
    public CandidatService() {
        this.fileService = new FileService();
    }
    
    // Add a new candidate
    public boolean ajouterCandidat(String nom, String prenom, LocalDate dateNaissance, 
                                String cin, String adresse, String telephone, String email) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO candidat (nom, prenom, date_naissance, cin, adresse, telephone, email) VALUES (?, ?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setDate(3, Date.valueOf(dateNaissance));
            stmt.setString(4, cin);
            stmt.setString(5, adresse);
            stmt.setString(6, telephone);
            stmt.setString(7, email);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error adding candidate: " + e.getMessage());
            return false;
        }
    }
    
    // Update a candidate
    public boolean modifierCandidat(int id, String nom, String prenom, LocalDate dateNaissance, 
                                 String cin, String adresse, String telephone, String email) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE candidat SET nom = ?, prenom = ?, date_naissance = ?, cin = ?, adresse = ?, telephone = ?, email = ? WHERE id = ?")) {
            
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setDate(3, Date.valueOf(dateNaissance));
            stmt.setString(4, cin);
            stmt.setString(5, adresse);
            stmt.setString(6, telephone);
            stmt.setString(7, email);
            stmt.setInt(8, id);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating candidate: " + e.getMessage());
            return false;
        }
    }
    
    // Delete a candidate
    public boolean supprimerCandidat(int id) {
        // First, get all documents to delete files
        List<Document> documents = getDocumentsByCandidat(id);
        for (Document doc : documents) {
            try {
                Files.deleteIfExists(doc.getCheminFichier());
            } catch (Exception e) {
                System.err.println("Error deleting document file: " + e.getMessage());
            }
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM candidat WHERE id = ?")) {
            
            stmt.setInt(1, id);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting candidate: " + e.getMessage());
            return false;
        }
    }
    
    // Find a candidate by ID
    public Candidat trouverCandidatParId(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM candidat WHERE id = ?")) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Candidat candidat = new Candidat(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getDate("date_naissance").toLocalDate(),
                    rs.getString("cin"),
                    rs.getString("adresse"),
                    rs.getString("telephone"),
                    rs.getString("email")
                );
                
                // Load documents for this candidate
                List<Document> documents = getDocumentsByCandidat(id);
                for (Document doc : documents) {
                    candidat.ajouterDocument(doc);
                }
                
                return candidat;
            }
        } catch (SQLException e) {
            System.err.println("Error finding candidate: " + e.getMessage());
        }
        return null;
    }
    
    public List<Candidat> getTousCandidats() {
        List<Candidat> candidats = new ArrayList<>();
    
        // First, retrieve all candidates
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM candidat ORDER BY nom, prenom")) {
    
            while (rs.next()) {
                int id = rs.getInt("id");
    
                Candidat candidat = new Candidat(
                    id,
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getDate("date_naissance").toLocalDate(),
                    rs.getString("cin"),
                    rs.getString("adresse"),
                    rs.getString("telephone"),
                    rs.getString("email")
                );
    
                candidats.add(candidat);
            }
    
        } catch (SQLException e) {
            System.err.println("Error getting all candidates: " + e.getMessage());
            return candidats;
        }
    
        // Then, for each candidate, fetch their documents separately
        for (Candidat c : candidats) {
            List<Document> documents = getDocumentsByCandidat(c.getId());
            for (Document doc : documents) {
                c.ajouterDocument(doc);
            }
        }
    
        return candidats;
    }
    
    // Get documents for a candidate
    private List<Document> getDocumentsByCandidat(int candidatId) {
        List<Document> documents = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM document WHERE candidat_id = ?")) {
            
            stmt.setInt(1, candidatId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Document document = new Document(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("type"),
                    rs.getDate("date_ajout").toLocalDate(),
                    Paths.get(rs.getString("chemin_fichier"))
                );
                documents.add(document);
            }
        } catch (SQLException e) {
            System.err.println("Error getting documents: " + e.getMessage());
        }
        
        return documents;
    }
    
    // Add a document to a candidate
    public boolean ajouterDocumentACandidat(int candidatId, String nomDocument, String typeDocument, File fichier) {
        try {
            // Create directory for candidate documents if it doesn't exist
            Path dossierCandidat = Paths.get("documents", String.valueOf(candidatId));
            Files.createDirectories(dossierCandidat);
            
            // Create a unique file name
            String extension = fileService.getFileExtension(fichier.getName());
            String uniqueFileName = System.currentTimeMillis() + "." + extension;
            Path destination = dossierCandidat.resolve(uniqueFileName);
            
            // Copy file to destination
            Files.copy(fichier.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            
            // Save document to database
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO document (candidat_id, nom, type, date_ajout, chemin_fichier) VALUES (?, ?, ?, ?, ?)")) {
                
                stmt.setInt(1, candidatId);
                stmt.setString(2, nomDocument);
                stmt.setString(3, typeDocument);
                stmt.setDate(4, Date.valueOf(LocalDate.now()));
                stmt.setString(5, destination.toString());
                
                int rows = stmt.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            System.err.println("Error adding document: " + e.getMessage());
            return false;
        }
    }
    
    // Delete a document from a candidate
    public boolean supprimerDocumentDeCandidat(int candidatId, int documentId) {
        try {
            // Get document path
            String cheminFichier = null;
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT chemin_fichier FROM document WHERE id = ? AND candidat_id = ?")) {
                
                stmt.setInt(1, documentId);
                stmt.setInt(2, candidatId);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    cheminFichier = rs.getString("chemin_fichier");
                }
            }
            
            if (cheminFichier != null) {
                // Delete physical file
                Files.deleteIfExists(Paths.get(cheminFichier));
                
                // Delete from database
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement("DELETE FROM document WHERE id = ? AND candidat_id = ?")) {
                    
                    stmt.setInt(1, documentId);
                    stmt.setInt(2, candidatId);
                    
                    int rows = stmt.executeUpdate();
                    return rows > 0;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting document: " + e.getMessage());
            return false;
        }
    }
}