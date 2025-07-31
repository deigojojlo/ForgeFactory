package main.java.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import main.java.model.Harvester;
import main.java.model.Map;
import main.java.model.Map.ResourceList;
import main.java.model.storage.DB;
import main.java.model.storage.Value;
import main.java.model.util.Save;
import main.java.view.GUI;
import main.java.view.GameView;
import main.java.view.OverlayPanel;

public class GameController {
    private final GameView view; // La vue associée au jeu

    private final PlayerController playerController; // Le contrôleur du joueur
    private final CraftController craftController; // Le contrôleur de la fabrication
    private final InventoryController inventoryController; // Le contrôleur de l'inventaire

    private final Map map; // La carte sur laquelle le joueur évolue
    private final GUI frame; // La fenêtre principale de l'application

    /**
     * Constructeur du contrôleur de jeu.
     * 
     * @param frame       La fenêtre principale du jeu
     * @param restoration Indique si le jeu doit être restauré (chargé à partir
     *                    d'une sauvegarde)
     */
    public GameController(GUI frame, boolean restoration) {
        this.frame = frame;

        // Initialisation du contrôleur du joueur
        this.playerController = new PlayerController(frame, this);

        // Initialisation de la carte, soit en restauration, soit en nouvelle carte
        if (restoration) { // Si restauration du jeu
            this.map = new Map(frame, playerController.getPlayer());
        } else { // Si nouveau jeu
            this.map = new Map(frame, playerController.getPlayer(), Value.rows,
                    Value.cols);
        }

        // Initialisation de la vue du jeu
        this.view = new GameView(frame, map.getRepresentativeMap(), restoration);

        // Initialisation du contrôleur d'inventaire
        this.inventoryController = new InventoryController(this.playerController.getInventory());

        // Initialisation du contrôleur de fabrication
        this.craftController = new CraftController(
                this.playerController.getInventory());

        // Affectation du joueur à la valeur statique
        Value.player = this.playerController;

        // Initialisation du joueur dans la carte
        this.playerController.initPlayer(map.getSpawn(), map);
        ((MarketController)this.map.getClickableMap().get(map.getMarket())).updateView();

        // Affichage d'informations de debug dans la console
        Value.print("\nList de la map d'objet à list \n" + DB.objetToInt);
        Value.print("\nList de la map de list à objet \n" + DB.intToObjet);
        Value.print("\nLa taille de la map en hauteur : " + map.getRepresentativeMap().length);
        Value.print("\nLa taille de la map en largeur : " + map.getRepresentativeMap()[0].length);
        Value.print("\nJoueur : " + map.getPlayer());
        Value.print("\nLa liste des objet à l'id \n" + DB.objetToInt);
        Value.print("\nLa liste des id à l'objet \n" + DB.intToList);
        Value.print("\nLa liste des recettes \n" + DB.recipeMap);

        // Initialisation des gestionnaires de boutons et de cellules
        setButtonsHandler();
        setCellHandler();
    }

    /**
     * Déplace le joueur d'une position à une autre.
     * 
     * @param fromx La position de départ en x
     * @param fromy La position de départ en y
     * @param tox   La position d'arrivée en x
     * @param toy   La position d'arrivée en y
     */
    public void movePlayer(int fromx, int fromy, int tox, int toy) {
        this.view.move(fromx, fromy, tox, toy);
    }

    /**
     * Vérifie si une cellule de la carte est disponible pour interagir avec elle.
     * 
     * @param row L'indice de ligne de la cellule
     * @param col L'indice de colonne de la cellule
     * @return true si la cellule est disponible, sinon false
     */
    public boolean mapIsAvailable(int row, int col) {
        return this.map.isAvailable(row, col);
    }

    /**
     * Configure les gestionnaires d'événements pour les boutons de la vue du jeu.
     */
    private void setButtonsHandler() {
        // Gestionnaire pour le bouton de sauvegarde
        this.view.getSaveButton().addActionListener(e -> Save.save(map));

        // Gestionnaire pour le bouton de sauvegarde et sortie
        this.view.getSaveAndExitButton().addActionListener(e -> {
            Save.save(map);
            System.exit(0); // Quitter l'application après la sauvegarde
        });

        // Gestionnaire pour le bouton d'inventaire
        this.view.getInventoryButton().addActionListener(e-> {
            frame.showOverlayPanel(new OverlayPanel(frame, this.inventoryController.getView()));
        });

        // Gestionnaire pour le bouton de fabrication
        this.view.getCraftButton().addActionListener(e -> {
            frame.showOverlayPanel(new OverlayPanel(frame, this.craftController.getView()));
        });
    }

    /**
     * Configure les gestionnaires d'événements pour les cellules de la carte (clics
     * de souris).
     */
    private void setCellHandler() {
        JLabel[][] cellTab = this.view.getCellTab(); // Tableau des cellules de la carte
        for (int row = 0; row < cellTab.length; row++) {
            for (int col = 0; col < cellTab[row].length; col++) {
                int localRow = row;
                int localCol = col;
                JLabel cell = cellTab[row][col];

                // Ajout d'un écouteur de clic sur chaque cellule
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        // Si le joueur peut se déplacer, il effectue l'action sur la cellule
                        if (!frame.isOverlayOn && map.getPlayer().canMove()) {
                            map.action(localRow, localCol);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // Si on est en mode placement et que la cellule est vide, on affiche un aperçu
                        // de l'objet à placer
                        if (map.getIsPlacing() && map.getRepresentativeMap()[localRow][localCol] == ResourceList.NULL
                                && (map.getPlayer().getRow() != localRow || map.getPlayer().getCol() != localCol)) {
                            ResourceList type = map.getToPlace() instanceof Harvester ? ResourceList.HARVESTER : ResourceList.FACTORY;
                            cell.setIcon(view.getImageIcons()[type.ordinal()]);
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // Lorsqu'on quitte la cellule, on réaffiche l'icône correspondant à l'état
                        // actuel
                        if (map.getIsPlacing() && map.getRepresentativeMap()[localRow][localCol] == ResourceList.NULL
                                && (map.getPlayer().getRow() != localRow || map.getPlayer().getCol() != localCol)) {
                            cell.setIcon(
                                    view.getImageIcons()[map.getRepresentativeMap()[localRow][localCol].ordinal()]);
                        }
                    }
                });
            }
        }
    }

    /**
     * Récupère la vue du jeu.
     * 
     * @return La vue du jeu
     */
    public GameView getView() {
        return this.view;
    }
}
