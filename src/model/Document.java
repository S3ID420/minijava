package model;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Objects;

public class Document {
    private int id;
    private String nom;
    private String type;
    private LocalDate dateAjout;
    private Path cheminFichier;
    
  
    public Document() {
    }
    
  
    public Document(int id, String nom, String type, LocalDate dateAjout, Path cheminFichier) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.dateAjout = dateAjout;
        this.cheminFichier = cheminFichier;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public LocalDate getDateAjout() {
        return dateAjout;
    }
    
    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }
    
    public Path getCheminFichier() {
        return cheminFichier;
    }
    
    public void setCheminFichier(Path cheminFichier) {
        this.cheminFichier = cheminFichier;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return id == document.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", dateAjout=" + dateAjout +
                ", cheminFichier=" + cheminFichier +
                '}';
    }
}