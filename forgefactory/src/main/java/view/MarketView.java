package main.java.view;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import main.java.controller.MarketController;
import main.java.model.Enum.BonusMachine;
import main.java.model.util.Objet;

/**
 * Cette classe représente l'interface utilisateur du marché.
 * Elle gère l'affichage des objets disponibles à l'achat et à la vente,
 * ainsi que des structures ("factories" et "harvesters") que l'utilisateur peut
 * acheter.
 *
 * 
 * La classe est responsable de la mise à jour des quantités affichées et de la
 * gestion des actions d'achat/vente
 * en coordonnant avec le MarketController.
 * 
 */
public class MarketView extends JPanel {
    private final HashMap<Integer, JLabel> quantityLabels; // Associe chaque Objet à son JLabel
    private final HashMap<JButton, BonusMachine> factoryButtons;
    private final HashMap<JButton, BonusMachine> harvesterButtons;

    private final JTabbedPane tabbedPane;
    private final JPanel itemsPanel;
    private final JPanel structuresPanel;
    private final JScrollPane scrollPane;

    private final BonusMachine[] bonusTypes = { null, BonusMachine.XL, BonusMachine.SPEED, BonusMachine.UNBREAKING,
            BonusMachine.FRAGILE };

    /**
     * Constructeur pour initialiser la vue du marché.
     */
    public MarketView() {
        this.quantityLabels = new HashMap<>();
        this.factoryButtons = new HashMap<>();
        this.harvesterButtons = new HashMap<>();

        setLayout(new GridLayout(1, 2, 10, 0)); // Deux colonnes : Achat / Vente

        this.tabbedPane = new JTabbedPane();
        this.itemsPanel = new JPanel();
        this.structuresPanel = new JPanel();

        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        structuresPanel.setLayout(new BoxLayout(structuresPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(itemsPanel);

        scrollPane.setPreferredSize(new Dimension(getWidth() - 20, getHeight() - 200));
        tabbedPane.setPreferredSize(new Dimension(getWidth() - 20, getHeight() - 200));

        tabbedPane.add("Marché", scrollPane);
        tabbedPane.add("Structures", structuresPanel);
        add(tabbedPane);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Configure les panneaux pour afficher les options d'achat des structures
     * ("factories" et "harvesters").
     *
     * 
     */
    public void setStructurePanel() {
        JPanel content = new JPanel();
        JScrollPane contentScrollPane = new JScrollPane(content);
        structuresPanel.add(contentScrollPane);

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        for (BonusMachine bonus : bonusTypes) {
            String label = bonus == null ? "" : (bonus + " ");

            JPanel factoryPanel = new JPanel(new GridLayout(1, 3));
            JLabel factoryLabel = new JLabel(label + "Factory");
            JButton factoryButton = new JButton("Buy -" + (price(label)));
            factoryButtons.put(factoryButton, bonus);

            factoryPanel.add(factoryLabel);
            factoryPanel.add(factoryButton);
            content.add(factoryPanel);

            JPanel harvesterPanel = new JPanel(new GridLayout(1, 2));
            JLabel harvesterLabel = new JLabel(label + "Harvester");
            JButton harvesterButton = new JButton("Buy -" + (price(label)));
            harvesterButtons.put(harvesterButton, bonus);

            harvesterPanel.add(harvesterLabel);
            harvesterPanel.add(harvesterButton);
            content.add(harvesterPanel);

            harvesterPanel.setBackground(Color.white); // Fond blanc pour le bouton
            harvesterPanel.setFocusable(false); // Empêche le focus sur le bouton
            harvesterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75)); // Définir une taille maximale pour le
                                                                                 // bouton
            factoryPanel.setBackground(Color.white); // Fond blanc pour le bouton
            factoryPanel.setFocusable(false); // Empêche le focus sur le bouton
            factoryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75)); // Définir une taille maximale pour le
                                                                               // bouton
        }
        content.add(new JPanel());
    }

    /**
     * Retourne le prix de la machine associé
     * 
     * @param name le nom de l'amelioration
     */
    private int price(String name) {
        if (name.equals("FRAGILE "))
            return 75;
        if (name.equals(""))
            return 150;
        return 250;
    }

    /**
     * Retourne les boutons associés aux factories.
     *
     * @return une HashMap associant les boutons aux types de BonusMachine.
     */
    public HashMap<JButton, BonusMachine> getFactoryButtons() {
        return this.factoryButtons;
    }

    /**
     * Retourne les boutons associés aux harvesters.
     *
     * @return une HashMap associant les boutons aux types de BonusMachine.
     */
    public HashMap<JButton, BonusMachine> getHarvesterButtons() {
        return this.harvesterButtons;
    }

    /**
     * Met à jour le panneau des objets disponibles à l'achat et à la vente.
     *
     * @param items      une liste des objets disponibles.
     * @param controller le MarketController responsable des actions d'achat et de
     *                   vente.
     */
    public void setItemsPanel(List<Objet> items, MarketController controller) {
        itemsPanel.removeAll();

        for (Objet item : items) {
            JPanel itemPanel = new JPanel(new GridLayout(0, 4));
            JLabel itemLabel = new JLabel(item.getName());
            JLabel quantityLabel = new JLabel("Vous en avez : " + controller.getQuantity(item));
            JButton buyButton = new JButton("Buy -" + item.getBuy());
            JButton sellButton = new JButton("Sell " + item.getSell());

            buyButton.addActionListener(e -> controller.handleBuy(item));
            sellButton.addActionListener(e -> controller.handleSell(item));

            quantityLabels.put(item.getId(), quantityLabel);

            itemPanel.add(itemLabel);
            itemPanel.add(quantityLabel);
            itemPanel.add(buyButton);
            itemPanel.add(sellButton);

            itemsPanel.add(itemPanel);
        }

        itemsPanel.add(new JPanel()); // Nécessaire pour que tout le contenu soit visible

        int itemHeight = 30;
        int totalHeight = (items.size() + 10) * itemHeight;
        itemsPanel.setPreferredSize(new Dimension(scrollPane.getWidth() - 20, totalHeight));
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    /**
     * Met à jour l'affichage de la quantité d'un objet donné.
     *
     * @param itemId      l'identifiant de l'objet.
     * @param newQuantity la nouvelle quantité à afficher.
     */
    public void updateItemQuantity(int itemId, int newQuantity) {
        JLabel label = quantityLabels.get(itemId);
        if (label != null) {
            label.setText("Vous en avez : " + newQuantity);
        } else {
            System.err.println("Erreur : Aucun JLabel trouvé pour l'ID " + itemId);
        }
    }
}
