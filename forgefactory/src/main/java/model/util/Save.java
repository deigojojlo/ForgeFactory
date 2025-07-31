package main.java.model.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import main.java.controller.MachineController;
import main.java.controller.MarketController;
import main.java.model.Exception.InvalidSaveFormat;
import main.java.model.Factory;
import main.java.model.Harvester;
import main.java.model.Interface.Clickable;
import main.java.model.Interface.Savable;
import main.java.model.Machine;
import main.java.model.Map;
import main.java.model.Resources;

/**
 * La classe Save gère l'enregistrement et la restauration de l'état du jeu dans
 * un fichier.
 * Elle permet de sauvegarder l'état actuel du jeu dans un fichier et de le
 * restaurer ultérieurement.
 */
public class Save {
    // Le chemin du fichier de sauvegarde
    private static final String saveFilePath = "ForgeFactory/Resources/save/sauvegarde.txt";

    /**
     * Sauvegarde l'état actuel du jeu dans un fichier.
     * Le fichier contient l'état de la carte, du joueur, des usines et des
     * moissonneuses.
     *
     * @param map L'objet Map représentant l'état actuel de la carte du jeu.
     */
    public static void save(Map map) {
        try (// Création d'un FileWriter pour écrire dans le fichier
        FileWriter writer = new FileWriter(saveFilePath);){
            // Sauvegarde de la carte
            writer.write(map.save() + "\n");

            // Rendre l'argent si une machine est en attente de placement
            if (map.getIsPlacing()) {
                int valeur ;
                if (map.getToPlace().getFragile()) {
                    valeur = 75;
                } else if (map.getToPlace().getCountBonus().isEmpty()) {
                    valeur = 150;
                } else {
                    valeur = 250;
                }
                map.getPlayer().getWallet().addAmount(valeur);
            }

            // Sauvegarde du joueur
            writer.write(map.getPlayer().save() + "\n");

            // Sauvegarde des usines et moissonneuses
            StringBuilder factorySave = new StringBuilder();
            StringBuilder harvesterSave = new StringBuilder();

            // Parcours de la carte cliquable et enregistrement des machines
            map.getClickableMap().forEach((Position position, Clickable clickable) -> {
                // Ne pas sauvegarder les MarketController ou Resources
                if (clickable instanceof MarketController || clickable instanceof Resources)
                    return;
                
                String saveData = "";
                
                // Cast du Clickable en MachineController pour récupérer la machine associée
                Machine machine = ((MachineController) clickable).getModel();
                
                // Ajout de la position de la machine dans la sauvegarde
                saveData += position.getRow() + ":" + position.getCol() + ",";
                
                // Sauvegarde des données spécifiques à la machine
                saveData += ((Savable) machine).save();
                
                // Ajout de la machine à la sauvegarde des usines ou moissonneuses
                if (machine instanceof Factory) {
                    factorySave.append(saveData).append(";");
                } else if (machine instanceof Harvester) {
                    harvesterSave.append(saveData).append(";");
                }
            });

            // Ajout d'un espace pour séparer les usines et les moissonneuses
            factorySave.append(" ");
            harvesterSave.append(" ");

            // Écriture des données des usines et moissonneuses dans le fichier
            writer.write(factorySave + "\n" + harvesterSave);

            // Fermeture du fichier après l'écriture
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }

    /**
     * Vérifie si un fichier de sauvegarde existe et n'est pas vide.
     *
     * @return true si le fichier existe et contient des données, false sinon.
     */
    public static boolean saveExists() {
        try (BufferedReader reader = new BufferedReader(new FileReader(saveFilePath))) {

            // Lire la première ligne
            String firstLine = reader.readLine();

            // Si le fichier est vide ou que la première ligne est vide
            if (firstLine == null || firstLine.trim().isEmpty()) {
                return false; // Le fichier est vide ou invalide
            }

            // Le fichier existe et contient des données
            return true;
        } catch (Exception e) {
            // Si une exception est levée, cela signifie que le fichier n'existe pas
            return false;
        }
    }

    /**
     * Restaure l'état du jeu à partir du fichier de sauvegarde.
     * Cette méthode restaure l'état de la carte, du joueur, des usines et des
     * moissonneuses.
     *
     * @param map L'objet Map dans lequel l'état du jeu sera restauré.
     */
    public static void restore(Map map) {
        try (BufferedReader reader = new BufferedReader(new FileReader(saveFilePath));) {
            
            // Restauration de la carte
            String mapSave = reader.readLine();
            map.restore(mapSave);

            // Restauration du joueur
            String walletSave = reader.readLine();
            String inventorySave = reader.readLine();
            map.getPlayer().restore(walletSave + ";" + inventorySave);

            // Restauration des usines
            String[] factoryData = reader.readLine().split(";");
            for (String data : factoryData) {
                if (!data.equals(" "))
                    map.restoreFactory(data);
            }

            // Restauration des moissonneuses
            String[] harvesterData = reader.readLine().split(";");
            for (String data : harvesterData) {
                if (!data.equals(" "))
                    map.restoreHarvester(data);
            }

            // Fermeture du fichier après lecture
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
        } catch (InvalidSaveFormat e) {
            System.out.println("Invalid format. Please create a new game.");
        }
    }

    /**
     * Compte le nombre d'occurrences d'un caractère dans une chaîne de caractères.
     *
     * @param c Le caractère à rechercher.
     * @param s La chaîne de caractères dans laquelle compter les occurrences.
     * @return Le nombre d'occurrences du caractère dans la chaîne.
     */
    public static int countOccurrences(char c, String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c)
                count++;
        }
        return count;
    }
}
