package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Candidat {
    private int id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String cin;
    private String adresse;
    private String telephone;
    private String email;
    private List<Document> documents;

    // Default constructor
    public Candidat() {
        this.documents = new ArrayList<>();
    }

    // Parameterized constructor
    public Candidat(int id, String nom, String prenom, LocalDate dateNaissance, String cin, 
                    String adresse, String telephone, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.cin = cin;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.documents = new ArrayList<>();
    }

    // Method to add a document
    public void ajouterDocument(Document document) {
        if (document != null) {
            documents.add(document);
        }
    }

    // Method to remove a document
    public boolean supprimerDocument(Document document) {
        return documents.remove(document);
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidat candidat = (Candidat) o;
        return id == candidat.id && 
               Objects.equals(cin, candidat.cin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cin);
    }

    @Override
    public String toString() {
        return "Candidat{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", cin='" + cin + '\'' +
                ", adresse='" + adresse + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", documents=" + documents +
                '}';
    }
}