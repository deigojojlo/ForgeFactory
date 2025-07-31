package main.java.view;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import main.java.controller.CraftController;
import main.java.controller.InventoryController;
import main.java.model.Factory;
import main.java.model.Machine;
import main.java.model.Map;
import main.java.model.Map.ResourceList;
import main.java.model.storage.Value;
import main.java.model.util.Position;

/**
 * La classe GameView représente la vue du jeu, incluant la grille du jeu, les
 * boutons de contrôle et l'affichage des informations liées à l'état du joueur.
 * Cette classe gère l'affichage de la carte, les déplacements du joueur,
 * l'ajout de machines et l'affichage de l'argent du joueur.
 */
public class GameView extends JPanel {

    // Final view
    InventoryController inventoryController; // Contrôleur pour gérer l'inventaire du joueur
    CraftController craftController; // Contrôleur pour gérer la fabrication d'objets
    private int cellSize; // Taille des cases de la grille
    private final int rows; // Nombre de lignes de la grille
    private final int cols; // Nombre de colonnes de la grille
    // Images utilisées pour les différentes cases
    private ImageIcon PLAYERImg; // Image du joueur
    private ImageIcon[] imageIconList; // Liste des icônes représentant les différents types de cases
    // Panneaux de l'interface utilisateur
    private final JPanel centerPanel; // Panneau central où la grille du jeu est dessinée
    private final JPanel leftPanel; // Panneau à gauche contenant les boutons de contrôle
    private GridBagConstraints gbc; // Contraintes utilisées pour placer les composants dans la grille
    private JLabel[][] cellTab; // Tableau contenant les cellules de la grille
    private final GUI frame; // Référence à la fenêtre principale
    // Boutons de contrôle
    private JButton saveAndExitButton; // Bouton permettant de sauvegarder et quitter
    private JButton saveButton; // Bouton de sauvegarde
    private JButton craftButton; // Bouton pour la fabrication d'objets
    private JButton inventoryButton; // Bouton pour afficher l'inventaire
    private ArrayList<JButton> buttons; // Liste de tous les boutons
    // Label affichant la quantité d'argent du joueur
    private JLabel moneyLabel;

    // Utilitaires de construction
    private Map.ResourceList[][] representativeMap; // Représentation de la carte sous forme de liste d'éléments

    /**
     * Constructeur de la classe Game.
     * Initialisation de la fenêtre du jeu avec une carte représentant les éléments,
     * ainsi que des contrôles pour l'interaction.
     * 
     * @param f                 La fenêtre principale du jeu
     * @param representativeMap La carte représentant l'état du jeu
     * @param b                 Vrai si la carte doit être restaurée, faux pour
     *                          générer une nouvelle carte
     */
    public GameView(GUI f, Map.ResourceList[][] representativeMap, boolean b) {
        this.frame = f;
        this.representativeMap = representativeMap;
        Value.game = this;

        this.rows = Value.rows; // Initialiser le nombre de lignes de la grille
        this.cols = Value.cols; // Initialiser le nombre de colonnes de la grille

        // Définir la mise en page de l'interface utilisateur
        setLayout(new BorderLayout());

        // Initialiser le panneau central de la grille
        centerPanel = new JPanel();
        centerPanel.setBounds(0, 0, f.getWidth() - 200, f.getHeight());
        centerPanel.setBackground(Color.BLACK);

        // Initialiser le panneau des boutons de contrôle à gauche
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(10, 1));
        leftPanel.setPreferredSize(new Dimension(100, f.getHeight()));
        leftPanel.setMinimumSize(new Dimension(100, f.getHeight()));
        leftPanel.setMaximumSize(new Dimension(100, f.getHeight()));

        add(centerPanel, BorderLayout.CENTER); // Ajouter le panneau central
        add(leftPanel, BorderLayout.LINE_END); // Ajouter le panneau des boutons

        // Dessiner la grille de jeu
        setGrid();

        // Créer les boutons et les labels
        createButtons();
        createMoneyLabel();
    }

    /**
     * Crée les boutons nécessaires à l'interaction de l'utilisateur, comme "exit",
     * "save", "craft", "inventory".
     * Les boutons sont ajoutés au panneau gauche.
     */
    private void createButtons() {
        saveAndExitButton = new JButton("exit");
        inventoryButton = new JButton("Inv");
        craftButton = new JButton("Craft");
        saveButton = new JButton("save");
        buttons = new ArrayList<>();
        buttons.add(saveAndExitButton);
        buttons.add(saveButton);
        buttons.add(craftButton);
        buttons.add(inventoryButton);

        // Style des boutons
        Font font = new Font("Arial", 0, 20);
        Border border = new LineBorder(Color.BLACK, 1, true);
        buttons.forEach(e -> {
            e.setFocusable(false);
            e.setBackground(Color.LIGHT_GRAY);
            e.setFont(font);
            e.setBorder(border);
            leftPanel.add(e);
        });
    }

    /**
     * Crée un label pour afficher la quantité d'argent actuelle du joueur.
     * Ce label est ajouté au panneau gauche.
     */
    private void createMoneyLabel() {
        JPanel moneyPanel = new JPanel();
        moneyPanel.add(new Label("Money"));
        moneyLabel = new JLabel("" + Value.player.getWallet().getAmount());
        moneyLabel.setFont(new Font("", 1, 10));
        moneyLabel.setSize(cellSize, cellSize);
        moneyPanel.setBorder(new LineBorder(Color.BLACK));
        moneyPanel.add(moneyLabel);
        leftPanel.add(moneyPanel, 0, 2);
    }

    /**
     * Configure la grille de jeu en fonction de la taille de la fenêtre et des
     * données de la carte.
     * La grille est dessinée avec des icônes représentant différents éléments comme
     * les usines, le joueur, etc.
     */
    private void setGrid() {
        // Calculer la taille des cellules en fonction de la taille de la fenêtre
        cellSize = Math.min((frame.getWidth() - 100) / cols, frame.getHeight() / rows);

        // Initialiser le tableau des cellules
        cellTab = new JLabel[rows][cols];

        // Définir les contraintes pour la mise en page
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0); // Pas de marge autour des cases

        // Charger les images et remplir le panneau central avec les cellules
        loadImages();
        centerPanel.add(fillImage(gbc));

        // Rafraîchir l'affichage de la fenêtre
        revalidate();
        repaint();
    }

    /**
     * Charge les images des éléments du jeu (usines, joueur, etc.) à partir des
     * fichiers PNG.
     * Les images sont redimensionnées en fonction de la taille des cellules.
     */
    private void loadImages() {
        try {
            int listCount = 9; // Nombre d'images à charger
            imageIconList = new ImageIcon[9];
            int i = 0;
            for (ResourceList list : ResourceList.values()) {
                if (i == listCount)
                    break;
                imageIconList[i++] = new ImageIcon(
                        ImageIO.read(new File(String.format("ForgeFactory/Resources/Image/%s.png", list)))
                                .getScaledInstance(cellSize - 1, cellSize - 1, Image.SCALE_SMOOTH));
            }
            PLAYERImg = new ImageIcon(ImageIO.read(new File("ForgeFactory/Resources/Image/PLAYER.png"))
                    .getScaledInstance(cellSize - 1, cellSize - 1, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remplit la grille avec les icônes correspondantes aux éléments de la carte.
     * 
     * @param gbc Contraintes utilisées pour placer chaque cellule dans la grille
     * @return Le panneau contenant la grille remplie de cellules
     */
    private JPanel fillImage(GridBagConstraints gbc) {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        frame.revalidate();
        frame.repaint();

        // Remplir chaque cellule de la grille avec l'icône correspondante
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JLabel cell = new JLabel();
                if (representativeMap[row][col] != ResourceList.SPAWN) {
                    cell.setIcon(imageIconList[representativeMap[row][col].ordinal()]);
                } else {
                    cell.setIcon(PLAYERImg);
                }

                // Sauvegarder la cellule dans le tableau pour les mises à jour ultérieures
                cellTab[row][col] = cell;
                cell.setPreferredSize(new Dimension(cellSize, cellSize));
                cell.setMaximumSize(new Dimension(cellSize, cellSize));
                cell.setMinimumSize(new Dimension(cellSize, cellSize));

                // Ajouter la cellule à la grille
                gbc.gridx = col;
                gbc.gridy = row;
                p.add(cell, gbc);
            }
        }

        p.setBackground(Color.BLACK);
        return p;
    }

    /**
     * Cette méthode définit si une cellule est inutilisable en fonction de l'état
     * de la collecte ou d'autres critères.
     * Elle modifie l'apparence visuelle de la cellule en assombrissant l'image
     * affichée pour indiquer qu'elle est inutilisable.
     * 
     * @param increaseOrNot Un booléen indiquant si la hauteur doit augmenter ou non
     *                      (lié à l'état de la collecte ou à une autre action).
     * @param newHeight     La nouvelle hauteur (peut être utilisée pour déterminer
     *                      l'état de la collecte).
     * @param pos           La position (ligne, colonne) de la cellule dans la
     *                      grille à modifier.
     */
    public void setUnusable(boolean increaseOrNot, double newHeight, Position pos) {
        // Réinitialiser l'icône de la cellule à son état initial selon la carte de
        // représentation.
        cellTab[pos.getRow()][pos.getCol()]
                .setIcon(imageIconList[representativeMap[pos.getRow()][pos.getCol()].ordinal()]);

        // Vérifier si la collecte est terminée (en fonction de l'augmentation de la
        // hauteur ou de la baisse de la hauteur).
        if (((increaseOrNot && newHeight > cellSize) || (!increaseOrNot && newHeight < 0))) {
            // Si la collecte est terminée, réinitialiser l'icône (elle ne sera plus
            // inutilisable).
            cellTab[pos.getRow()][pos.getCol()]
                    .setIcon(imageIconList[representativeMap[pos.getRow()][pos.getCol()].ordinal()]);
        } else {
            // Si la collecte n'est pas terminée, assombrir l'image pour marquer la cellule
            // comme inutilisable.
            ImageIcon im = (ImageIcon) cellTab[pos.getRow()][pos.getCol()].getIcon();
            Image img = im.getImage();
            int width = img.getWidth(null); // Largeur de l'image
            int height = img.getHeight(null); // Hauteur de l'image

            // Créer une nouvelle image bufferisée avec un filtre d'assombrissement appliqué
            BufferedImage darkenedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = darkenedImage.createGraphics();
            g2d.drawImage(img, 0, 0, null);

            // Appliquer un filtre de couleur pour assombrir l'image (réduire l'opacité et
            // appliquer une couleur noire transparente)
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f)); // Réduire l'opacité à 50%
            g2d.setColor(new Color(0, 0, 0, 150)); // Appliquer un filtre noir avec une transparence

            // Remplir un rectangle pour assombrir une partie de l'image
            g2d.fillRect(0, (int) newHeight, width, cellSize - (int) newHeight);
            g2d.dispose();

            // Créer une nouvelle icône avec l'image assombrie
            ImageIcon darkerIcon = new ImageIcon(darkenedImage);
            // Appliquer cette nouvelle icône à la cellule pour la marquer comme
            // inutilisable
            cellTab[pos.getRow()][pos.getCol()].setIcon(darkerIcon);
        }
    }

    /**
     * Permet de déplacer le joueur d'une cellule à une autre sur la grille.
     * 
     * @param fromx La position x de départ
     * @param fromy La position y de départ
     * @param tox   La position x de destination
     * @param toy   La position y de destination
     */
    public void move(int fromx, int fromy, int tox, int toy) {
        cellTab[fromy][fromx].setIcon(imageIconList[ResourceList.NULL.ordinal()]);
        cellTab[toy][tox].setIcon(PLAYERImg);

        // Rafraîchir l'affichage
        revalidate();
        repaint();
    }

    /**
     * Ajoute une machine (usine ou récolteur) à une cellule de la grille.
     * 
     * @param row La ligne où la machine doit être placée
     * @param col La colonne où la machine doit être placée
     * @param m   La machine à ajouter (peut être une Factory ou un Harvester)
     */
    public void addMachine(int row, int col, Machine m) {
        if (m instanceof Factory) {
            cellTab[row][col].setIcon(imageIconList[ResourceList.FACTORY.ordinal()]);
        } else {
            cellTab[row][col].setIcon(imageIconList[ResourceList.HARVESTER.ordinal()]);
        }
    }

    /**
     * Met à jour l'affichage de la quantité d'argent du joueur.
     * 
     * @param amount La nouvelle quantité d'argent
     */
    public void updateMoneyLabel(int amount) {
        moneyLabel.setText(amount + "");
    }

    // **Getters** pour accéder aux éléments de l'interface utilisateur
    public int getCellSize() {
        return this.cellSize;
    }

    public JButton getSaveButton() {
        return this.saveButton;
    }

    public JButton getSaveAndExitButton() {
        return this.saveAndExitButton;
    }

    public JButton getCraftButton() {
        return this.craftButton;
    }

    public JButton getInventoryButton() {
        return this.inventoryButton;
    }

    public JLabel[][] getCellTab() {
        return this.cellTab;
    }

    public ImageIcon[] getImageIcons() {
        return this.imageIconList;
    }
}
