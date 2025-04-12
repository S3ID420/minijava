package model;

import java.io.Serializable;

public class Moniteur implements Serializable{
    private int cin;          // Cin, clé primaire
    private String nom;
    private String prenom;
    private String email;
    private int telephone;
    private int codeCnss;
    private int idVehicule;   // Clé étrangère vers le véhicule

    // Constructeurs, getters et setters

    public Moniteur(int cin, String nom, String prenom, String email, int telephone, int codeCnss, int idVehicule) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.codeCnss = codeCnss;
        this.idVehicule = idVehicule;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public int getCodeCnss() {
        return codeCnss;
    }

    public void setCodeCnss(int codeCnss) {
        this.codeCnss = codeCnss;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }
}

