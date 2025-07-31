package main.java.view;

import java.util.LinkedList;
import javax.swing.*;
import main.java.model.Inventory;
import main.java.model.Machine;
import main.java.model.Map;
import main.java.model.Map.ResourceList;
import main.java.model.util.Couple;

/**
 * Classe HarvesterView
 *
 * <p>
 * Représente la vue d'une machine de type "Harvester". Elle hérite de
 * MachineView et ajoute des fonctionnalités spécifiques, comme la gestion de
 * boutons liés à des ressources.
 * </p>
 */
public class HarvesterView extends MachineView {

    // Liste des couples (ressource, bouton)
    private final LinkedList<Couple<Map.ResourceList, JButton>> buttonList;

    /**
     * Constructeur de la classe HarvesterView.
     *
     * @param model           La machine associée à la vue.
     * @param playerInventory L'inventaire du joueur.
     */
    public HarvesterView( Machine model, Inventory playerInventory) {
        super(model, playerInventory);
        this.buttonList = new LinkedList<>();
    }

    /**
     * Crée et configure le panneau des actions.
     */
    @Override
    public void createActionPane() {
        super.addActionsPane();
    }

    /**
     * Retourne le panneau des actions.
     *
     * @return Le panneau des actions.
     */
    @Override
    public JPanel getActionsPane() {
        return super.getActionsPane();
    }

    /**
     * Ajoute un bouton associé à une ressource dans la liste des boutons.
     *
     * @param resource La ressource à associer au bouton.
     * @param button   Le bouton à ajouter.
     */
    public void addButton(ResourceList resource, JButton button) {
        this.buttonList.add(new Couple<>(resource, button));
    }

    /**
     * Désactive un bouton spécifique dans la liste en fonction de son index.
     *
     * @param index L'index du bouton à désactiver.
     */
    public void enableButton(int index) {
        for (int i = 0; i < buttonList.size(); i++) {
            if (i == index) {
                buttonList.get(i).getValue().setEnabled(false);
            }
        }
    }

    /**
     * Retourne la liste des couples (ressource, bouton).
     *
     * @return La liste des boutons.
     */
    public LinkedList<Couple<Map.ResourceList, JButton>> getButtons() {
        return this.buttonList;
    }
}
