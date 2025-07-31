package main.java.model;

import main.java.controller.Timeline;
import main.java.model.Interface.Clickable;
import main.java.model.Interface.Savable;
import main.java.model.storage.DB;
import main.java.model.storage.Value;
import main.java.model.util.Objet;
import main.java.model.util.Position;
import main.java.model.util.Task;
import main.java.view.GUI;

/**
 * La classe Resources représente une ressource dans le jeu.
 * Elle étend la classe Clickable et fournit des méthodes pour interagir avec
 * les ressources.
 */
public class Resources implements Savable, Clickable {

    private final Player player; // Le joueur qui interagit avec la ressource
    private final Objet item; // L'objet représentant la ressource
    private boolean canCollect = true; // Indicateur de si la ressource peut être récoltée
    private final Position position; // La position de la ressource dans le jeu

    /**
     * Constructeur de la classe Resources.
     * Initialise la ressource avec le joueur, la liste des ressources et la
     * position.
     *
     * @param player       Le joueur interagissant avec la ressource.
     * @param resourceList La liste des ressources disponibles.
     * @param pos          La position de la ressource.
     */
    public Resources(Player player, Map.ResourceList resourceList, Position pos) {
        this.player = player;
        this.item = DB.listToObjet.get(resourceList); // Clone l'objet pour éviter des changements accidentels
        this.position = pos;
    }

    /**
     * Effectue une action lorsqu'une ressource est cliquée.
     * Cette méthode gère la récolte de la ressource et l'animation associée.
     *
     * @param frame L'interface graphique du jeu.
     */
    @Override
    public void action(GUI frame) {
        // Si la superposition n'est pas activée et que la ressource peut être récoltée
        if (!frame.isOverlayOn && canCollect) {
            canCollect = false;

            // Si la récolte est laborieuse (avec une durée associée)
            if (item.getDuration() != 0) {
                Value.player.setCanMove(false); // Empêche le mouvement du joueur
                this.player.addItem(item, item.getQuantity()); // Donne la ressource au joueur, pas de problème de
                                                               // taille d'inventaire
                Value.game.setUnusable(false, Value.game.getCellSize(), this.position); // Fait descendre la jauge

                // Décrémente la jauge chaque seconde durant la récolte
                for (int i = 0; i <= item.getDuration(); i++) {
                    int x = i; // Variable finale effective
                    Timeline.add(new Task(x, () -> {
                        Value.game.setUnusable(false,
                                Value.game.getCellSize()
                                        - (x + 1) * ((double) Value.game.getCellSize() / (item.getDuration())),
                                this.position);
                    }));
                }

                // Remet la ressource en état normal après la récolte
                Timeline.add(new Task(item.getDuration(), () -> {
                    this.canCollect = true;
                    Value.player.setCanMove(true);
                }));
            } else {
                // Si la récolte est temporisée
                this.player.addItem(item, item.getQuantity()); // Donne la ressource au joueur
                Value.game.setUnusable(false, Value.game.getCellSize(), this.position); // Réduit la jauge
                for (int i = 0; i <= item.getRecovery(); i++) {
                    int x = i; // Variable finale effective
                    Timeline.add(new Task(x, () -> {
                        Value.game.setUnusable(false,
                                (x) * ((double) Value.game.getCellSize() / (item.getRecovery())),
                                this.position);
                    }));
                }
                // Attente avant de pouvoir récolter à nouveau la ressource
                Timeline.add(new Task(item.getRecovery(), () -> this.canCollect = true));
            }
        }
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de la
     * ressource.
     *
     * @return La chaîne représentant l'objet de la ressource.
     */
    @Override
    public String toString() {
        return item.toString();
    }

    /**
     * Sauvegarde l'état de la ressource.
     *
     * @return null pour l'instant, la sauvegarde n'est pas implémentée.
     */
    @Override
    public String save() {
        return null; // La méthode de sauvegarde n'est pas implémentée
    }

    /**
     * Restaure l'état de la ressource à partir d'une chaîne de caractères.
     *
     * @param s La chaîne représentant l'état sauvegardé de la ressource.
     */
    @Override
    public void restore(String s) {
        // Restauration non implémentée pour l'instant
    }
}
