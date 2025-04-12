package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import model.Moniteur;

public class MoniteurService {
    private static final String FILE_PATH = "documents/moniteurs.dat";
    private List<Moniteur> moniteurs;
    
    public MoniteurService() {
        moniteurs = new ArrayList<>();
        loadMoniteurs();
    }
    
    public void ajouterMoniteur(Moniteur moniteur) {
        // Vérifier si un moniteur avec ce CIN existe déjà
        for (Moniteur m : moniteurs) {
            if (m.getCin() == moniteur.getCin()) {
                throw new IllegalArgumentException("Un moniteur avec ce CIN existe déjà");
            }
        }
        
        moniteurs.add(moniteur);
        saveMoniteurs();
    }
    
    public void modifierMoniteur(Moniteur moniteur) {
        for (int i = 0; i < moniteurs.size(); i++) {
            if (moniteurs.get(i).getCin() == moniteur.getCin()) {
                moniteurs.set(i, moniteur);
                saveMoniteurs();
                return;
            }
        }
        
        throw new IllegalArgumentException("Moniteur non trouvé");
    }
    
    public void supprimerMoniteur(int cin) {
        for (int i = 0; i < moniteurs.size(); i++) {
            if (moniteurs.get(i).getCin() == cin) {
                moniteurs.remove(i);
                saveMoniteurs();
                return;
            }
        }
        
        throw new IllegalArgumentException("Moniteur non trouvé");
    }
    
    public Moniteur getMoniteur(int cin) {
        for (Moniteur m : moniteurs) {
            if (m.getCin() == cin) {
                return m;
            }
        }
        
        return null;
    }
    
    public List<Moniteur> getMoniteurs() {
        return new ArrayList<>(moniteurs);
    }
    
    private void loadMoniteurs() {
        File file = new File(FILE_PATH);
        
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                moniteurs = (List<Moniteur>) ois.readObject();
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement des moniteurs: " + e.getMessage());
                e.printStackTrace();
                moniteurs = new ArrayList<>();
            }
        }
    }
    
    private void saveMoniteurs() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(moniteurs);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde des moniteurs: " + e.getMessage());
            e.printStackTrace();
        }
    }
}