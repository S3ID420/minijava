package model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Examen {
    private int id;
    private int candidatId;
    private String type;
    private String partie;
    private String etat;
    private LocalDateTime dateTime;
    private LocalTime duree;
    private double prix;
    private int vehiculeId;

    // Constructor with ID (for existing records)
    public Examen(int id, int candidatId, String type, String partie, String etat, 
                  LocalDateTime dateTime, LocalTime duree, double prix, int vehiculeId) {
        this.id = id;
        this.candidatId = candidatId;
        this.type = type;
        this.partie = partie;
        this.etat = etat;
        this.dateTime = dateTime;
        this.duree = duree;
        this.prix = prix;
        this.vehiculeId = vehiculeId;
    }

    // Constructor without ID (for new records)
    public Examen(int candidatId, String type, String partie, String etat, 
                  LocalDateTime dateTime, LocalTime duree, double prix, int vehiculeId) {
        this.candidatId = candidatId;
        this.type = type;
        this.partie = partie;
        this.etat = etat;
        this.dateTime = dateTime;
        this.duree = duree;
        this.prix = prix;
        this.vehiculeId = vehiculeId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCandidatId() {
        return candidatId;
    }

    public void setCandidatId(int candidatId) {
        this.candidatId = candidatId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPartie() {
        return partie;
    }

    public void setPartie(String partie) {
        this.partie = partie;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalTime getDuree() {
        return duree;
    }

    public void setDuree(LocalTime duree) {
        this.duree = duree;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getVehiculeId() {
        return vehiculeId;
    }

    public void setVehiculeId(int vehiculeId) {
        this.vehiculeId = vehiculeId;
    }

    @Override
    public String toString() {
        return "Examen{" +
                "id=" + id +
                ", candidatId=" + candidatId +
                ", type='" + type + '\'' +
                ", partie='" + partie + '\'' +
                ", etat='" + etat + '\'' +
                ", dateTime=" + dateTime +
                ", duree=" + duree +
                ", prix=" + prix +
                ", vehiculeId=" + vehiculeId +
                '}';
    }
}