package main.java.controller;

import java.util.Map;
import main.java.model.Exception.NotFound;
import main.java.model.Inventory;
import main.java.model.util.Objet;
import main.java.view.InventoryView;

public class InventoryController {

    private final Inventory model;
    private final InventoryView view;

    /**
     * Constructeur pour initialiser le contrôleur d'inventaire.
     * Ce contrôleur gère l'ajout et la suppression d'objets dans l'inventaire du
     * modèle.
     * Il met également à jour la vue pour refléter l'état actuel de l'inventaire.
     *
     * @param model Le modèle d'inventaire.
     */
    public InventoryController(Inventory model) {
        this.model = model;
        this.view = new InventoryView(); // Création de la vue avec une taille par défaut
        updateView(); // Affiche l'état initial de l'inventaire
    }

    /**
     * Ajoute un objet à l'inventaire.
     * Cette méthode ajoute un certain nombre d'objets au modèle d'inventaire et met
     * à jour la vue.
     *
     * @param item     L'objet à ajouter à l'inventaire.
     * @param quantity La quantité d'objets à ajouter.
     */
    public void addItem(Objet item, int quantity) {
        model.addItem(item, quantity); // Ajouter l'objet au modèle
        updateView(); // Mettre à jour la vue pour refléter les changements
    }

    /**
     * Supprime un objet de l'inventaire.
     * Cette méthode tente de supprimer un certain nombre d'objets du modèle
     * d'inventaire.
     * Si une exception est levée, un message d'erreur est affiché.
     *
     * @param item     L'objet à supprimer de l'inventaire.
     * @param quantity La quantité d'objets à supprimer.
     */
    public void removeItem(Objet item, int quantity) {
        try {
            model.removeItem(item, quantity); // Retirer l'objet du modèle
            updateView(); // Mettre à jour la vue après la suppression
        } catch (NotFound e) {
            System.out.println("Erreur : " + e.getMessage()); // Afficher un message d'erreur en cas de problème
        }
    }

    /**
     * Met à jour la vue pour refléter l'état actuel du modèle d'inventaire.
     * Cette méthode récupère le contenu actuel de l'inventaire du modèle et met à
     * jour la vue.
     */
    private void updateView() {
        // Récupère le contenu actuel de l'inventaire sous forme de carte (objet,
        // quantité)
        Map<Objet, Integer> items = model.getInventoryContent();
        view.updateInventory(items); // Met à jour l'affichage dans la vue
    }

    /**
     * Retourne la vue de l'inventaire mise à jour.
     * Cette méthode met d'abord à jour la vue avant de la renvoyer.
     *
     * @return La vue de l'inventaire.
     */
    public InventoryView getView() {
        updateView(); // Met à jour la vue avant de la retourner
        return this.view;
    }
}
