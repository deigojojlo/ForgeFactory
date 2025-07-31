package main.java.model.storage;

import main.java.controller.PlayerController;
import main.java.model.Map;
import main.java.view.GUI;
import main.java.view.GameView;

/**
 * La classe Value contient des valeurs statiques et des méthodes utilitaires.
 * Elle sert principalement à stocker des constantes, des références globales et
 * des méthodes d'affichage utilisées dans tout le jeu.
 */
public class Value {

    // Taille maximale de l'inventaire des machines
    public final static int machineInventorySize = 200;
    // Nombre maximal de réparations (durabilité avant de casser)
    public final static int maxUnbreaking = 3;
    // Vitesse maximale dans le jeu
    public final static int maxSpeed = 5;
    // Taille maximale pour un objet de type XL
    public final static int maxXL = 3;

    // Références globales pour les vues et contrôleurs
    public static GUI frame; // Fenêtre graphique principale du jeu
    public static GameView game; // Vue du jeu (affichage de l'état du jeu)
    public static Map map; // Carte actuelle du jeu
    public static PlayerController player; // Contrôleur du joueur

    // Dimensions de la carte
    public static final int rows = 22; // Nombre de lignes dans la carte
    public static final int cols = 35; // Nombre de colonnes dans la carte

    /**
     * Affiche une chaîne de caractères en couleur verte dans la console.
     * Utilisé pour afficher des messages d'information.
     *
     * @param str Le message à afficher.
     */
    public static void print(String str) {
        System.out.println("\033[0;32m" + str + "\033[0m"); // affiche sur la sortie standard
    }

    /**
     * Affiche une chaîne de caractères en couleur rouge dans la console.
     * Utilisé pour afficher des messages d'erreur.
     *
     * @param str Le message d'erreur à afficher.
     */
    public static void printError(String str) {
        System.err.println("\033[0;31m" + str + "\033[0m"); // affiche sur la sortie erreur
    }
}
