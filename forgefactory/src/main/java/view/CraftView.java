package main.java.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import main.java.model.storage.DB;
import main.java.model.util.Objet;
import main.java.model.util.Recipe;

/**
 * La classe CraftView représente la vue d'un panneau de fabrication dans
 * l'interface du jeu.
 * Elle permet d'afficher l'inventaire du joueur et les recettes disponibles
 * pour fabriquer des objets.
 * Pour chaque recette, un bouton de fabrication est proposé à l'utilisateur.
 */
public class CraftView extends JPanel {

    // Composants graphiques
    private final JScrollPane inventoryPane;
    private final JScrollPane craftPane;
    private final JPanel inventorySubPane;
    private final JPanel craftSubPane;

    // Carte contenant les boutons associés à chaque recette de fabrication
    private final HashMap<Recipe, JButton> craftingButtons;

    /**
     * Constructeur de la vue CraftView.
     * Initialise la vue en créant les panneaux et en configurant les composants
     * graphiques.
     *
     */
    public CraftView() {
        // Initialisation des composants graphiques
        craftingButtons = new HashMap<>();
        inventorySubPane = new JPanel();
        craftSubPane = new JPanel();
        inventoryPane = new JScrollPane();
        craftPane = new JScrollPane();

        // Configuration des sous-panneaux avec GridBagLayout
        inventorySubPane.setLayout(new GridBagLayout());
        craftSubPane.setLayout(new GridBagLayout());

        // Créer le panneau de recettes
        craftPane();

        // Attacher les sous-panneaux aux panneaux de défilement
        inventoryPane.setViewportView(inventorySubPane);
        craftPane.setViewportView(craftSubPane);

        // Ajouter les panneaux à la vue principale avec une disposition horizontale
        add(inventoryPane);
        add(craftPane);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        // Revalider et redessiner la vue
        revalidate();
        repaint();
    }

    /**
     * Met à jour l'affichage de l'inventaire du joueur.
     * Chaque objet de l'inventaire est affiché avec sa quantité.
     *
     * @param lines Une carte contenant les objets de l'inventaire et leurs
     *              quantités respectives.
     */
    public void updateInventory(Map<Objet, Integer> lines) {
        inventorySubPane.removeAll(); // Nettoyer l'ancien contenu
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ajuster horizontalement
        gbc.weightx = 1; // Étirement horizontal
        gbc.weighty = 0; // Pas d'étirement vertical

        int i = 0;
        int maxLength = 0 ; 
        // Pour chaque objet de l'inventaire
        for (Map.Entry<Objet, Integer> couple : lines.entrySet()) {
            JPanel line = new JPanel();

            // Afficher le nom de l'objet et sa quantité
            String label = "    " + couple.getKey().getName() + "    " + couple.getValue() ;
            JLabel title = new JLabel(label);
            line.add(title);

            gbc.gridy = i++; // Déplacer à la ligne suivante
            inventorySubPane.add(line, gbc);

            // Définir la taille minimale de chaque ligne
            line.setPreferredSize(new Dimension(Integer.MAX_VALUE, 30));
            
            if (maxLength < label.length())
                maxLength = label.length();
        }
    
        // Forcer la taille du sous-panneau pour activer le défilement
        inventorySubPane.setPreferredSize(new Dimension(maxLength, lines.size() * 25));
        inventorySubPane.revalidate();
        inventorySubPane.repaint();
    }

    /**
     * Crée et affiche les recettes de fabrication dans le panneau de fabrication.
     * Pour chaque recette, un bouton "Craft" est ajouté.
     */
    private void craftPane() {
        craftSubPane.removeAll(); // Nettoyer le panneau de recettes
        GridBagConstraints gbc = new GridBagConstraints();

        // Pour chaque recette dans la base de données
        for (int i = 0; i < DB.recipeMap.length(); i++) {
            Recipe recette = DB.recipeMap.get(i);

            // Composants pour chaque ligne
            JLabel title = new JLabel(recette.toString());
            JButton craftButton = new JButton("Craft");

            // Associer le bouton à la recette dans la carte
            craftingButtons.put(recette, craftButton);

            // Ajouter le label à la ligne
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.7; // 70% pour le label
            gbc.fill = GridBagConstraints.HORIZONTAL;

            craftSubPane.add(title, gbc);

            // Ajouter le bouton de fabrication
            gbc.gridx = 1;
            gbc.weightx = 0.3; // 30% pour le bouton
            gbc.fill = GridBagConstraints.NONE;
            craftSubPane.add(craftButton, gbc);

            // Appliquer un style au bouton
            craftButton.setBorder(new LineBorder(Color.BLACK));
        }

        // Revalider et redessiner le panneau
        craftSubPane.revalidate();
        craftSubPane.repaint();
    }

    /**
     * Retourne la carte des boutons de fabrication associés aux recettes.
     *
     * @return La carte des boutons de fabrication, associée à chaque recette.
     */
    public HashMap<Recipe, JButton> getButtons() {
        return this.craftingButtons;
    }
}
