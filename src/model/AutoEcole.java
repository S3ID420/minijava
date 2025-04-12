package model;

import java.util.Objects;

public class AutoEcole {
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    private String fax;
    private String siteWeb;

    // Default constructor
    public AutoEcole() {
    }

    // Parameterized constructor
    public AutoEcole(String nom, String adresse, String telephone, String email, String fax, String siteWeb) {
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.fax = fax;
        this.siteWeb = siteWeb;
    }

    // Getters and Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutoEcole autoEcole = (AutoEcole) o;
        return Objects.equals(nom, autoEcole.nom) &&
                Objects.equals(adresse, autoEcole.adresse) &&
                Objects.equals(telephone, autoEcole.telephone) &&
                Objects.equals(email, autoEcole.email) &&
                Objects.equals(fax, autoEcole.fax) &&
                Objects.equals(siteWeb, autoEcole.siteWeb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, adresse, telephone, email, fax, siteWeb);
    }

    @Override
    public String toString() {
        return "AutoEcole{" +
                "nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", fax='" + fax + '\'' +
                ", siteWeb='" + siteWeb + '\'' +
                '}';
    }
}