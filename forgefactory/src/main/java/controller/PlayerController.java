package main.java.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import main.java.model.Inventory;
import main.java.model.Map;
import main.java.model.Money;
import main.java.model.Player;
import main.java.model.storage.Value;
import main.java.model.util.Position;
import main.java.view.GUI;

public class PlayerController {
    @SuppressWarnings("FieldMayBeFinal")
    private Player model; // Le modèle représentant le joueur

    private final GUI frame; // La fenêtre GUI
    private final GameController gameController; // Le contrôleur du jeu

    /**
     * Constructeur du contrôleur du joueur.
     * Ce contrôleur permet de gérer les actions du joueur dans le jeu, telles que
     * les mouvements.
     *
     * @param frame          La fenêtre de l'interface utilisateur.
     * @param gameController Le contrôleur principal du jeu.
     */
    public PlayerController(GUI frame, GameController gameController) {
        this.model = new Player(); // Initialisation du modèle du joueur
        this.frame = frame;
        this.gameController = gameController;
        Value.player = this; // Définir cette instance comme le joueur actuel dans la valeur globale
    }

    /**
     * Initialisation du joueur avec une position, une carte, et la fenêtre GUI.
     *
     * @param position La position initiale du joueur.
     * @param map      La carte sur laquelle le joueur évolue.
     */
    public void initPlayer(Position position, Map map) {
        this.model.initPlayer(position, map); // Initialise le joueur avec la position et la carte
        setHandler(gameController.getView()); // Configure les gestionnaires d'événements pour les touches
    }

    /**
     * Définit le gestionnaire de touches pour le joueur.
     *
     * @param panel Le JPanel sur lequel on ajoute l'écouteur de touches.
     */
    private void setHandler(JPanel panel) {
        panel.setFocusable(true); // Le panneau doit pouvoir recevoir le focus
        panel.requestFocus(); // Demander le focus pour capter les événements de touche
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                // Si un overlay est actif ou si le joueur ne peut pas se déplacer, ignorer les
                // événements
                if (frame.isOverlayOn || !model.canMove()) {
                    return;
                }
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_LEFT: // Flèche gauche
                        move(0, -1); // Déplacer le joueur à gauche
                        break;
                    case KeyEvent.VK_UP: // Flèche haut
                        move(-1, 0); // Déplacer le joueur vers le haut
                        break;
                    case KeyEvent.VK_RIGHT: // Flèche droite
                        move(0, 1); // Déplacer le joueur à droite
                        break;
                    case KeyEvent.VK_DOWN: // Flèche bas
                        move(1, 0); // Déplacer le joueur vers le bas
                        break;
                }
            }
        });
    }

    /**
     * Déplace le joueur vers une nouvelle position basée sur les changements de
     * coordonnées.
     *
     * @param deltaX Le changement de la coordonnée x (lignes).
     * @param deltaY Le changement de la coordonnée y (colonnes).
     */
    private void move(int deltaX, int deltaY) {
        if (this.model.canMove() &&
                this.gameController.mapIsAvailable(this.model.getPosition().getRow() + deltaX,
                        this.model.getPosition().getCol() + deltaY)) {
            // Effectuer le mouvement si la nouvelle position est valide
            this.gameController.movePlayer(this.model.getPosition().getCol(), this.model.getPosition().getRow(),
                    this.model.getPosition().getCol() + deltaY,
                    this.model.getPosition().getRow() + deltaX);
            // Mettre à jour la position du joueur
            this.model.getPosition().setCol(deltaY + this.model.getPosition().getCol());
            this.model.getPosition().setRow(deltaX + this.model.getPosition().getRow());
        }
    }

    /**
     * Déplace le joueur vers une position spécifique.
     *
     * @param row La ligne de la position où déplacer le joueur.
     * @param col La colonne de la position où déplacer le joueur.
     */
    public void moveTo(int row, int col) {
        if (this.model.canMove()
                && (Math.abs(model.getPosition().getRow() - row) <= 1 && col - model.getPosition().getCol() == 0) ||
                (row == model.getPosition().getRow() && Math.abs(col - model.getPosition().getCol()) <= 1)) {
            // Vérification si le mouvement est valide, puis mise à jour de la position
            this.gameController.movePlayer(this.model.getPosition().getCol(), this.model.getPosition().getRow(), col,
                    row);
            this.model.getPosition().setCol(col);
            this.model.getPosition().setRow(row);
        }
    }

    /**
     * Définit si le joueur peut se déplacer ou non.
     *
     * @param canMove True si le joueur peut se déplacer, false sinon.
     */
    public void setCanMove(boolean canMove) {
        this.model.setCanMove(canMove); // Met à jour la possibilité de déplacement du joueur
    }

    /**
     * Récupère le portefeuille du joueur (son argent).
     *
     * @return Le portefeuille du joueur.
     */
    public Money getWallet() {
        return this.model.getWallet();
    }

    /**
     * Récupère le modèle du joueur.
     *
     * @return Le modèle du joueur.
     */
    public Player getPlayer() {
        return this.model;
    }

    /**
     * Récupère l'inventaire du joueur.
     *
     * @return L'inventaire du joueur.
     */
    public Inventory getInventory() {
        return this.model.getInventory();
    }

    /**
     * Récupère la position actuelle du joueur.
     *
     * @return La position du joueur.
     */
    public Position getPosition() {
        return this.model.getPosition();
    }
}
