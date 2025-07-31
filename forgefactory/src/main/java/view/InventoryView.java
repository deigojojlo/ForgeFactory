package main.java.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import main.java.model.util.Objet;


/**
 * Classe InventoryView
 *
 * <p>
 * Cette classe représente une vue graphique pour afficher le contenu d'un
 * inventaire.
 * Elle utilise un JScrollPane pour permettre le défilement des objets lorsque
 * leur nombre dépasse l'espace disponible.
 * </p>
 */
public class InventoryView extends JScrollPane {

    // Panneau contenant les éléments de l'inventaire
    private final JPanel contentPanel;

    /**
     * Constructeur de la classe InventoryView.
     *
     */
    public InventoryView() {
        // Initialisation du panneau principal pour contenir les éléments
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        setViewportView(contentPanel);

        // Configuration des propriétés du JScrollPane
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED); // Barre de défilement verticale visible en cas de besoin
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED); // Désactiver la barre de défilement horizontale
        
        revalidate();
        repaint();
    }

    /**
     * Met à jour l'affichage de l'inventaire avec une nouvelle liste d'objets.
     *
     * @param items La liste des objets à afficher avec leur quantité, sous forme de
     *              Map.
     *              Chaque clé représente un objet de type {@link Objet}, et chaque
     *              valeur représente la quantité associée.
     */
    public void updateInventory(Map<Objet, Integer> items) {
        // Réinitialiser le contenu du panneau principal pour afficher les nouveaux
        // éléments
        contentPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ajuster horizontalement
        gbc.weightx = 1; // Étirement horizontal
        gbc.weighty = 0; // Pas d'étirement vertical

        int i = 0;
        int maxLength = 0 ; 
        // Pour chaque objet de l'inventaire
        for (Map.Entry<Objet, Integer> couple : items.entrySet()) {
            JPanel line = new JPanel();

            // Afficher le nom de l'objet et sa quantité
            String label = "    " + couple.getKey().getName() + "    " + couple.getValue();
            JLabel title = new JLabel(label);
            line.add(title);

            gbc.gridy = i++; // Déplacer à la ligne suivante
            contentPanel.add(line, gbc);

            // Définir la taille minimale de chaque ligne
            line.setPreferredSize(new Dimension(Integer.MAX_VALUE, 30));

            if (maxLength < label.length())
            maxLength = label.length();
        }

        // Forcer Swing à recalculer et redessiner les composants
        contentPanel.setPreferredSize(new Dimension(maxLength*10, items.size() * 25));
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
