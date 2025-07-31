package main.java.controller;

import javax.swing.JButton;
import main.java.model.Exception.NotFound;
import main.java.model.Factory;
import main.java.model.Inventory;
import main.java.model.Money;
import main.java.model.storage.DB;
import main.java.model.storage.Value;
import main.java.model.util.Couple;
import main.java.model.util.Objet;
import main.java.model.util.Recipe;
import main.java.model.util.Task;
import main.java.view.FactoryView;

public class FactoryController extends MachineController {
    private final Factory model; // Le modèle de la machine (Factory)
    private final FactoryView view; // La vue associée à la machine (FactoryView)
    private Task task; // La tâche qui sera ajoutée à la timeline pour la fabrication

    /**
     * Constructeur de la classe FactoryController.
     * 
     * @param model           Le modèle de la machine (Factory)
     * @param playerInventory L'inventaire du joueur
     * @param wallet          Le portefeuille du joueur
     */
    public FactoryController( Factory model, Inventory playerInventory, Money wallet) {
        super(new FactoryView( model, playerInventory), model, playerInventory, wallet);
        this.model = model; // Initialise le modèle (Factory)
        this.view = ((FactoryView) super.getView()); // Récupère la vue associée

        // Définir la description de la recette actuellement active
        this.setDescription("Recette : " + model.getRecipe().toString());

        setHandler(); // Configure les gestionnaires d'événements pour les boutons
        view.enableButton(DB.recipeMap.getIndexOf(model.getRecipe())); // Active le bouton correspondant à la recette
                                                                       // active

        // Crée une tâche qui simule la fabrication d'un objet en fonction de la recette
        task = new Task(0, () -> {
            // si elle est fragile et pas casser on a un probabilité de 10% quelle casse
            if (model.getFragile() && !model.getBreaked()) {
                if (Math.random() <= 0.1) {
                    model.setBreaked(true);
                    setReparation();
                }
            }

            // --- partie action ---
            if (model.getFragile() && model.getBreaked()) {
                // ne rien faire
            } else if (model.getRecipe() != null) {
                if (canCraft()) {
                    removeFromInventory(); // Retirer les ingrédients de l'inventaire
                    model.setDurability(model.getDurability() - 1); // Décrémenter la durabilité de la machine
                    // Ajouter l'objet fabriqué à l'inventaire de la machine
                    model.getInventory().addItem(model.getRecipe().getResult(),
                            model.getRecipe().getResultQuantity());
                    notifyView(); // Mettre à jour la vue
                }
            }
            task.setTime(model.getRecipe().getTime()); // Définir le temps de fabrication de la tâche
            Timeline.add(task); // Ajouter la tâche à la timeline
        });

        Timeline.add(task); // Ajouter la tâche initiale à la timeline
        this.model.setTask(task); // Associer la tâche à la machine
        super.applyBonus(); // Appliquer les bonus à la machine
    }

    /**
     * Vérifie si la machine peut fabriquer l'objet en fonction de la recette.
     * 
     * @return true si la fabrication est possible, sinon false
     */
    private boolean canCraft() {
        boolean result = true;
        // Vérifie si l'inventaire de la machine a suffisamment d'espace pour les objets
        // fabriqués
        if (model.getInventory().getCurrentCount() + model.getRecipe().getResultQuantity() - model.getRecipe().sum() > model.getInventory()
                .getCapacity()
                || model.getDurability() == 0)
            return false;

        // Vérifie si les ingrédients nécessaires sont présents dans l'inventaire de la
        // machine
        for (Couple<Objet, Integer> couple : model.getRecipe().getIngredients()) {
            if (model.getInventory().getQuantityOf(couple.getKey()) < couple.getValue()) {
                result = false; // La fabrication n'est pas possible si les ingrédients sont manquants
            }
        }
        return result;
    }

    /**
     * Retire les ingrédients nécessaires à la fabrication de l'inventaire de la
     * machine.
     */
    private void removeFromInventory() {
        try {
            // Retirer chaque ingrédient de l'inventaire de la machine
            for (Couple<Objet, Integer> couple : model.getRecipe().getIngredients()) {
                model.getInventory().removeItem(couple.getKey(), couple.getValue());
            }
        } catch (NotFound e) {
            Value.printError("Un item est manquant");
        }
    }

    /**
     * Configure les gestionnaires d'événements pour les boutons de recette.
     */
    private void setHandler() {
        // Configure un gestionnaire d'événements pour chaque bouton de recette
        for (Couple<Recipe, JButton> couple : view.getButtons()) {
            couple.getValue().addActionListener(e -> {
                // Désactive tous les boutons de recette
                for (Couple<Recipe, JButton> c : view.getButtons()) {
                    c.getValue().setEnabled(true);
                }
                // Active uniquement le bouton de la recette sélectionnée
                couple.getValue().setEnabled(false);
                this.model.setRecipe(couple.getKey()); // Change la recette active dans le modèle
                this.setDescription("Recette : " + couple.getKey().toString()); // Met à jour la description de la
                                                                                // recette
                task.setTime(couple.getKey().getTime()); // Met à jour le temps de fabrication
            });
        }
    }
}
