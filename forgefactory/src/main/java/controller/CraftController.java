package main.java.controller;

import java.awt.Color;
import main.java.model.Exception.NotFound;
import main.java.model.Inventory;
import main.java.model.storage.Value;
import main.java.model.util.Couple;
import main.java.model.util.Objet;
import main.java.model.util.Recipe;
import main.java.view.CraftView;

public class CraftController {
    private final CraftView view; // La vue associée à la fabrication (CraftView)
    private final Inventory playerInventory; // L'inventaire du joueur

    /**
     * Constructeur de la classe CraftController.
     * 
     * @param playerInventory L'inventaire du joueur
     */
    public CraftController(Inventory playerInventory) {
        this.view = new CraftView(); // Crée une vue de fabrication
        this.view.updateInventory(playerInventory.getInventoryContent()); // Met à jour l'inventaire dans la vue

        this.playerInventory = playerInventory; // L'inventaire du joueur

        setHandler(); // Configure les gestionnaires d'événements pour les boutons de fabrication
        update(); // Met à jour l'interface avec les informations actuelles
    }

    /**
     * Met à jour l'interface avec les informations actuelles.
     * - Vérifie si les objets peuvent être fabriqués et met à jour la couleur des
     * boutons
     * - Met à jour l'inventaire
     */
    private void update() {
        // Met à jour la couleur des boutons en fonction de si la recette peut être
        // fabriquée
        this.view.getButtons().forEach((recette, bouton) -> {
            bouton.setBackground(canCraft(recette) ? Color.green : Color.red);
        });

        // Met à jour l'inventaire dans la vue
        this.view.updateInventory(this.playerInventory.getInventoryContent());
    }

    /**
     * Configure les gestionnaires d'événements pour les boutons de fabrication.
     */
    private void setHandler() {
        // Ajoute un gestionnaire d'événements pour chaque bouton de fabrication
        this.view.getButtons().forEach((recette, bouton) -> {
            bouton.addActionListener(e -> {
                if (canCraft(recette)) { // Vérifie si la fabrication est possible
                    // Si c'est possible, retire les ingrédients nécessaires de l'inventaire
                    for (Couple<Objet, Integer> couple : recette.getIngredients()) {
                        try {
                            playerInventory.removeItem(couple.getKey(), couple.getValue());
                        } catch (NotFound ex) {
                            Value.printError("Not Found : " + couple.getKey());
                        }
                    }

                    // Met à jour l'inventaire après la perte des ingrédients
                    update();

                    // Ajoute une tâche à la timeline pour fabriquer l'objet (le résultat de la
                    // recette)
                    Timeline.addCraft(recette.getResult(), recette.getTime(), () -> {
                        playerInventory.addItem(recette.getResult(), recette.getResultQuantity());
                        // Met à jour l'inventaire avec le résultat de la fabrication
                        update();
                    });
                }
            });
        });
    }

    /**
     * Vérifie si une recette peut être fabriquée en fonction des ingrédients
     * disponibles dans l'inventaire.
     * 
     * @param recette La recette à vérifier
     * @return true si la recette peut être fabriquée, false sinon
     */
    private boolean canCraft(Recipe recette) {
        boolean canCraft = true;
        // Vérifie si l'inventaire contient suffisamment d'ingrédients
        for (Couple<Objet, Integer> couple : recette.getIngredients()) {
            if (playerInventory.getQuantityOf(couple.getKey()) < couple.getValue()) {
                canCraft = false; // Impossible de fabriquer si un ingrédient est manquant
                break;
            }
        }
        return canCraft;
    }

    /**
     * Retourne la vue de fabrication.
     * 
     * @return La vue associée à la fabrication
     */
    public CraftView getView() {
        update(); // Met à jour la vue avant de la retourner
        return view;
    }
}
