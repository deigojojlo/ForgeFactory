package main.java.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import main.java.model.Inventory;
import main.java.model.Machine;
import main.java.model.storage.DB;
import main.java.model.util.Couple;
import main.java.model.util.Recipe;

/**
 * La classe FactoryView représente la vue d'une machine de type "Factory"
 * (usine).
 * Elle hérite de MachineView et fournit des fonctionnalités pour afficher et
 * gérer les actions disponibles pour la fabrication d'objets dans l'interface
 * utilisateur.
 * Chaque recette disponible dans la fabrique est associée à un bouton dans
 * l'interface.
 */
public class FactoryView extends MachineView {
    // Liste des boutons associés aux recettes disponibles dans la fabrique
    private final LinkedList<Couple<Recipe, JButton>> button;

    /**
     * Constructeur de la vue de la fabrique.
     * Initialise les boutons associés aux recettes et configure le panneau des
     * actions.
     *
     * @param model           Le modèle de la machine (usine) que cette vue
     *                        représente
     * @param playerInventory L'inventaire du joueur pour vérifier les ressources
     *                        disponibles
     */
    public FactoryView( Machine model, Inventory playerInventory) {
        super( model, playerInventory); // Appel du constructeur de la classe parente
        this.button = new LinkedList<>();
        createActionPane(); // Crée et affiche les boutons d'actions de la fabrique
    }

    /**
     * Crée et ajoute les boutons d'action à la vue de la fabrique en fonction des
     * recettes disponibles.
     * Chaque recette a un bouton correspondant dans l'interface.
     */
    @Override
    public void createActionPane() {
        JScrollPane scrollPane = new JScrollPane();
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        super.getActionsPane().removeAll(); // Retirer les composants précédents
        for (int i = 0; i < DB.recipeMap.length(); i++) { // Parcours de toutes les recettes disponibles
            JButton jtb = new JButton(DB.recipeMap.get(i).toString()); // Crée un bouton avec le nom de la recette
            jtb.setBackground(Color.white); // Fond blanc pour le bouton
            jtb.setFocusable(false); // Empêche le focus sur le bouton
            jtb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75)); // Définir une taille maximale pour le bouton

            // Ajoute le couple (recette, bouton) à la liste des boutons
            button.add(new Couple<>(DB.recipeMap.get(i), jtb));

            content.add(jtb); // Ajoute le bouton au panneau des actions
        }
        scrollPane.setViewportView(content);
        super.getActionsPane().add(scrollPane);
        super.addActionsPane(); // Appelle la méthode de la classe parente pour finaliser l'ajout du panneau
                                // d'actions
    }

    /**
     * Désactive un bouton spécifié dans la liste des boutons.
     * Utilisé pour empêcher l'utilisateur de cliquer sur une action déjà exécutée
     * ou impossible à exécuter.
     *
     * @param i L'indice du bouton à désactiver dans la liste des boutons
     */
    public void enableButton(int i) {
        for (int j = 0; j < button.size(); j++) {
            if (j == i) {
                // Désactive le bouton à l'indice 'i' dans la liste
                button.get(i).getValue().setEnabled(false);
            }
        }
    }

    /**
     * Retourne la liste des boutons créés pour chaque recette.
     *
     * @return La liste des boutons associés aux recettes
     */
    public LinkedList<Couple<Recipe, JButton>> getButtons() {
        return this.button;
    }
}
