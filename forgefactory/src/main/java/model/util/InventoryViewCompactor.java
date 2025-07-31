package main.java.model.util;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * La classe InventoryViewCompactor permet de stocker et de gérer les composants
 * d'affichage
 * dans l'inventaire des machines. Elle compacte les éléments d'une ligne de
 * l'inventaire
 * afin de faciliter l'accès et la modification des composants visuels associés
 * à chaque objet
 * dans l'inventaire.
 */
public class InventoryViewCompactor {

    private final JLabel itemLabel; // Étiquette affichant le nom de l'objet
    private final JLabel myQuantityLabel; // Étiquette affichant la quantité de l'objet du joueur
    private final JLabel theirQuantityLabel; // Étiquette affichant la quantité de l'objet disponible
    private final JButton put; // Bouton pour ajouter 1 objet à l'inventaire
    private final JButton get; // Bouton pour retirer 1 objet de l'inventaire
    private final JPanel panel; // Panel contenant tous les composants d'affichage

    private final Objet item; // L'objet lié à cette ligne de l'inventaire

    /**
     * Constructeur de la classe InventoryViewCompactor.
     * Il initialise les composants visuels de l'inventaire pour un objet donné.
     *
     * @param item  L'objet à afficher dans l'inventaire.
     */
    public InventoryViewCompactor(Objet item ) {
        this.item = item;
        // Initialisation des composants visuels
        this.itemLabel = new JLabel(item.toString());
        this.myQuantityLabel = new JLabel();
        this.theirQuantityLabel = new JLabel();
        this.put = new JButton("put 1");
        this.get = new JButton("get 1");

        // Initialisation du panel et mise en place de la disposition
        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 5)); // Une ligne, cinq colonnes
        panel.add(itemLabel); // Affichage du nom de l'objet
        panel.add(theirQuantityLabel); // Affichage de la quantité de l'objet disponible
        panel.add(myQuantityLabel); // Affichage de la quantité de l'objet du joueur
        panel.add(put); // Bouton pour ajouter un objet
        panel.add(get); // Bouton pour retirer un objet

        // Configuration de la taille du panel
        panel.setSize(new Dimension(panel.getWidth(), 75));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Retourne le bouton permettant de retirer un objet de l'inventaire.
     *
     * @return Le bouton "get".
     */
    public JButton getGet() {
        return this.get;
    }

    /**
     * Retourne le bouton permettant d'ajouter un objet à l'inventaire.
     *
     * @return Le bouton "put".
     */
    public JButton getPut() {
        return this.put;
    }

    /**
     * Retourne l'étiquette affichant la quantité d'objet du joueur.
     *
     * @return L'étiquette de la quantité du joueur.
     */
    public JLabel getMyQuantityLabel() {
        return this.myQuantityLabel;
    }

    /**
     * Retourne l'étiquette affichant la quantité d'objet disponible.
     *
     * @return L'étiquette de la quantité disponible.
     */
    public JLabel getTheirQuantityLabel() {
        return this.theirQuantityLabel;
    }

    /**
     * Retourne le panel contenant tous les composants de cette ligne d'inventaire.
     *
     * @return Le panel.
     */
    public JPanel getPanel() {
        return this.panel;
    }

    /**
     * Retourne l'objet associé à cette ligne d'inventaire.
     *
     * @return L'objet de l'inventaire.
     */
    public Objet getItem() {
        return this.item;
    }
}
