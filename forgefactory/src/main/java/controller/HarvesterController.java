package main.java.controller;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import main.java.model.Harvester;
import main.java.model.Inventory;
import main.java.model.Map;
import main.java.model.Map.ResourceList;
import main.java.model.Money;
import main.java.model.storage.DB;
import main.java.model.util.Couple;
import main.java.model.util.Task;
import main.java.view.HarvesterView;

public class HarvesterController extends MachineController {
    private final Harvester model; // Le modèle représentant le récolteur
    private final HarvesterView view; // La vue associée au récolteur
    private Task task; // La tâche qui gère l'activité du récolteur

    /**
     * Constructeur de HarvesterController.
     *
     * @param model           Le modèle de récolteur
     * @param playerInventory L'inventaire du joueur
     * @param wallet          L'argent du joueur
     * @param resourceArround Les ressources autour du récolteur
     */
    public HarvesterController( Harvester model, Inventory playerInventory, Money wallet,
            Map.ResourceList[] resourceArround) {
        super(new HarvesterView( model, playerInventory), model, playerInventory, wallet);
        this.model = model;
        this.view = ((HarvesterView) super.getView());

        // Initialisation de la description de la ressource du récolteur
        this.setDescription("Resource : " + this.model.getResource());

        this.view.addActionsPane(); // Ajout du panneau des actions

        // création des panneaux content dans scrollPane dans actionPane
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(content);

        JPanel actionsPane = view.getActionsPane();
        actionsPane.add(scrollPane);

        // Création des boutons pour chaque ressource autour du récolteur
        for (ResourceList list : resourceArround) {
            JButton button = new JButton(list.toString());
            content.add(button); // add to content pane
            button.setBackground(Color.white); // Fond blanc pour le bouton
            button.setFocusable(false); // Empêche le focus sur le bouton
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75)); // Définir une taille maximale pour le bouton
            view.addButton(list, button);
        }

        // Configuration des gestionnaires d'événements pour les boutons de ressource
        initListner();

        // Création de la tâche qui gère la récolte des ressources
        task = new Task(0, () -> {
            // si elle est fragile et pas casser on a un probabilité de 10% quelle casse
            if (model.getFragile() && !model.getBreaked()) {
                if (Math.random() <= 0.1) {
                    model.setBreaked(true);
                    setReparation();
                    notifyView();
                }
            }

            // --- partie action ---
            if (model.getFragile() && model.getBreaked()) {
                // ne rien faire
            } else if (model.getResource() != null) {
                // Si la ressource est valide et que le récolteur est encore fonctionnel
                if (model.getDurability() > 0 &&
                        model.getInventorySize() >= model.getInventoryCount()
                                + DB.listToObjet.get(model.getResource()).getQuantity()) {
                    model.setDurability(model.getDurability() - 1); // Réduction de la durabilité
                    model.getInventory().addItem(DB.listToObjet.get(model.getResource()),
                            DB.listToObjet.get(model.getResource()).getQuantity()); // Ajout de la ressource
                    notifyView(); // Mise à jour de la vue
                }
            }
            // Mise à jour du temps de la tâche en fonction de la vitesse du récolteur
            task.setTime(
                    (int) ((1 - this.model.getExtraSpeed())
                            * Math.max(DB.listToObjet.get(model.getResource()).getDuration(),
                                    DB.listToObjet.get(model.getResource()).getRecovery())));
            Timeline.add(task); // Ajout de la tâche à la timeline
        });
        Timeline.add(task); // Ajout de la tâche à la timeline
        this.model.setTask(task); // Affectation de la tâche au modèle
    }

    /**
     * Récupère les boutons des ressources possibles et ajoute leur événement.
     */
    private void initListner() {
        // Gestion des événements des boutons associés aux ressources
        for (Couple<Map.ResourceList, JButton> couple : view.getButtons()) {
            couple.getValue().addActionListener(e -> {
                // Désactive tous les boutons
                for (Couple<Map.ResourceList, JButton> c : view.getButtons()) {
                    c.getValue().setEnabled(true);
                }
                // Désactive le bouton de la ressource sélectionnée
                couple.getValue().setEnabled(false);
                // Mise à jour de la ressource sélectionnée pour le récolteur
                this.model.setResource(couple.getKey());
                // Mise à jour de la description de la ressource
                this.setDescription("Resource : " + couple.getKey());
                // Mise à jour du temps de la tâche en fonction de la ressource choisie
                task.setTime(Math.max(DB.listToObjet.get(couple.getKey()).getDuration(),
                        DB.listToObjet.get(couple.getKey()).getRecovery()));
            });
        }
    }
}
