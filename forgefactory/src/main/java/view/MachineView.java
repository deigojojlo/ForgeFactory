package main.java.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import main.java.model.Enum.BonusMachine;
import main.java.model.Inventory;
import main.java.model.Machine;
import main.java.model.storage.DB;
import main.java.model.storage.Value;
import main.java.model.util.InventoryViewCompactor;
import main.java.model.util.Objet;

/**
 * Classe abstraite représentant la vue d'une machine.
 * 
 * <p>
 * Cette classe gère l'affichage des propriétés de la machine, des inventaires,
 * des améliorations disponibles, et permet l'interaction avec les utilisateurs
 * via une interface graphique.
 * </p>
 */
public abstract class MachineView extends JPanel {

    // Contenu graphique
    private final LinkedList<InventoryViewCompactor> InventoryViewList; // Liste des vues compactées pour les objets
                                                                        // d'inventaire
    private final HashMap<BonusMachine, JLabel> upgradeLabels; // Étiquettes pour afficher les bonus de la machine
    private final JTabbedPane tabbedPane; // Conteneur pour les onglets
    private final JPanel actionsPane; // Onglet pour les actions spécifiques
    private final JPanel descriptionPane; // Onglet pour la description de la machine
    private final JPanel upgradeDescriptionPane; // Onglet dans la description pour les améliorations

    // Boutons d'actions pour les améliorations
    private JButton unbreakingButton;
    private JButton speedButton;
    private JButton sizeButton;
    private JButton repaireButton;
    private JButton polyvalenteButton;

    // Objets du modèle
    private final Machine model; // Modèle associé à la machine
    private final Inventory playerInventory; // Inventaire du joueur

    // Boutton de reparation si la machine est fragile
    private final JButton reparation;

    /**
     * Constructeur principal de MachineView.
     *
     * @param model           instance du modèle Machine associé.
     * @param playerInventory inventaire du joueur.
     */
    public MachineView(Machine model, Inventory playerInventory) {
        this.model = model;
        this.playerInventory = playerInventory;

        setLayout(new BorderLayout());

        this.tabbedPane = new JTabbedPane();
        this.InventoryViewList = new LinkedList<>();
        this.upgradeLabels = new HashMap<>();
        this.actionsPane = new JPanel();
        this.descriptionPane = new JPanel();
        this.upgradeDescriptionPane = new JPanel();

        this.upgradeDescriptionPane.setLayout(new BoxLayout(upgradeDescriptionPane, BoxLayout.Y_AXIS));

        this.descriptionPane.add(new JLabel(this.model.getClass().getSimpleName() + " : "));
        this.upgradeDescriptionPane.add(new JLabel("Amélioration de la machine :"));

        this.reparation = new JButton("Donner un coup de pied pour reparer la machine");

        // Initialisation des onglets
        createDescription();
        createUpgradesPane();

        // Ajouter le JTabbedPane (onglets) à la vue
        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Méthode abstraite pour définir les actions spécifiques à une machine.
     * 
     * <p>
     * Chaque machine peut avoir des actions différentes (définies dans les
     * sous-classes).
     * </p>
     */
    public abstract void createActionPane();

    /**
     * Met à jour les informations de la description de la machine.
     *
     * @param durability    durabilité actuelle de la machine.
     * @param maxDurability durabilité maximale de la machine.
     * @param count         nombre d'objets dans l'inventaire.
     * @param size          capacité maximale de l'inventaire.
     * @param extraSpeed    vitesse supplémentaire.
     * @param speciality    spécialité ou fonctionnalité unique de la machine.
     */
    public void updateDescription(int durability, int maxDurability, int count, int size, double extraSpeed,
            String speciality) {
        // le 0 est la ligne descriptive du bloc
        ((JLabel) descriptionPane.getComponent(1)).setText(" - Durabilité : " + durability + "/" + maxDurability);
        ((JLabel) descriptionPane.getComponent(2)).setText(" - Inventaire : " + count + "/" + size);
        ((JLabel) descriptionPane.getComponent(3)).setText(" - Vitesse supplémentaire : " + extraSpeed);
        ((JLabel) descriptionPane.getComponent(4)).setText(" - " + speciality);
    }

    /**
     * Crée l'onglet Description pour afficher les informations générales sur la
     * machine.
     */
    private void createDescription() {
        descriptionPane.setLayout(new BoxLayout(descriptionPane, BoxLayout.Y_AXIS));

        // Création des étiquettes pour la description
        JLabel durability = new JLabel();
        JLabel countAndSize = new JLabel();
        JLabel extraSpeed = new JLabel();
        JLabel speciality = new JLabel();

        // Ajout des étiquettes au panneau de description
        descriptionPane.add(durability);
        descriptionPane.add(countAndSize);
        descriptionPane.add(extraSpeed);
        descriptionPane.add(speciality);
        descriptionPane.add(this.upgradeDescriptionPane); // ajout des description d'amélioration

        // Ajout du panneau à l'onglet
        tabbedPane.add("Description", descriptionPane);
    }

    /**
     * Crée l'onglet Inventory qui affiche les objets disponibles dans l'inventaire.
     */
    public void createInventoryView() {
        JScrollPane inventory = new JScrollPane();
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        inventory.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inventory.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        for (Objet item : DB.item) {
            InventoryViewCompactor ivc = new InventoryViewCompactor(item);
            InventoryViewList.add(ivc);
            panel.add(ivc.getPanel());
        }

        panel.add(new JPanel()); // pour tout bien afficher

        inventory.setViewportView(panel);
        tabbedPane.add("Inventory", inventory);

        panel.revalidate();
        panel.repaint();
    }

    /**
     * Crée l'onglet Upgrades qui affiche les améliorations disponibles pour la
     * machine.
     */
    public void createUpgradesPane() {
        JScrollPane panel = new JScrollPane();
        panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel upgradesPane = new JPanel();
        upgradesPane.setLayout(new BoxLayout(upgradesPane, BoxLayout.Y_AXIS));

        // Initialisation des boutons pour les améliorations
        unbreakingButton = new JButton("Unbreaking upgrade 0/3 for 150");
        speedButton = new JButton("Speed upgrade 0/3 for 150");
        sizeButton = new JButton("Inventory upgrade 0/3 for 150");
        polyvalenteButton = new JButton("Polyvalente 0/1 for 150");
        repaireButton = new JButton("Repaire for 150");

        // Rendre les boutons aussi larges que possible
        unbreakingButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, unbreakingButton.getPreferredSize().height));
        speedButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, speedButton.getPreferredSize().height));
        sizeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, sizeButton.getPreferredSize().height));
        polyvalenteButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, polyvalenteButton.getPreferredSize().height));
        repaireButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, repaireButton.getPreferredSize().height));

        // Ajout des boutons au panneau
        upgradesPane.add(unbreakingButton);
        upgradesPane.add(speedButton);
        upgradesPane.add(sizeButton);
        upgradesPane.add(polyvalenteButton);
        upgradesPane.add(repaireButton);

        panel.setViewportView(upgradesPane);
        tabbedPane.addTab("Upgrades", panel);
    }

    /**
     * Ajoute l'onglet Configuration pour les actions de la machine.
     */
    public void addActionsPane() {
        tabbedPane.add("Configuration", actionsPane);
        revalidate();
        repaint();
    }

    /**
     * Supprime l'onglet Configuration des actions de la machine.
     */
    public void removeActionsPane() {
        tabbedPane.remove(actionsPane);
        revalidate();
        repaint();
    }

    /**
     * 
     * 
     */
    public void addReparation() {
        this.descriptionPane.add(this.reparation);
    }

    /**
     * 
     * 
     */
    public void removeReparation() {
        this.descriptionPane.remove(this.reparation);
    }

    public JButton getReparation() {
        return this.reparation;
    }

    /**
     * Met à jour les étiquettes et l'état des composants graphiques selon les
     * informations du modèle.
     * @param  description la description spécifique (soit Ressource : , soit Recette : )
     */
    public void updateLabels(String description) {
        this.InventoryViewList.forEach(e -> {
            e.getMyQuantityLabel().setText(model.getInventory().getQuantityOf(e.getItem()) + "");
            e.getTheirQuantityLabel().setText(this.playerInventory.getQuantityOf(e.getItem()) + "");
        });
        updateDescription(model.getDurability(), model.getMaxDurability(), model.getInventoryCount(),
                model.getInventorySize(), model.getExtraSpeed(), description);
        unbreakingButton.setText(
                String.format("Unbreaking upgrade %d/%d for 150", this.model.getNumberOf(BonusMachine.UNBREAKING),
                        Value.maxUnbreaking));
        speedButton.setText(
                String.format("Speed upgrade %d/%d for 150", this.model.getNumberOf(BonusMachine.SPEED),
                        Value.maxSpeed));
        sizeButton.setText(
                String.format("XL upgrade %d/%d for 150", this.model.getNumberOf(BonusMachine.XL), Value.maxXL));
        polyvalenteButton.setText(
                String.format("POLYVALENTE %d/%d for 150", this.model.getNumberOf(BonusMachine.POLYVALENTE), 1));

        // upgrade description
        HashMap<BonusMachine, Integer> bonusMap = model.getCountBonus();

        for (Entry<BonusMachine, Integer> bonusCouple : bonusMap.entrySet()) {
            JLabel query = this.upgradeLabels.get(bonusCouple.getKey());
            if (query == null) {
                JLabel label = new JLabel(" - " + bonusCouple.getKey().toString() + " : " + bonusCouple.getValue());
                this.upgradeDescriptionPane.add(label);
                this.upgradeLabels.put(bonusCouple.getKey(), label);
            } else {
                query.setText(" - " + bonusCouple.getKey().toString() + " : " + bonusCouple.getValue());
            }
        }
        revalidate();
        repaint();
    }

    // Getters pour les boutons d'amélioration
    public JButton getUnbreakingButton() {
        return this.unbreakingButton;
    }

    public JButton getSpeedButton() {
        return this.speedButton;
    }

    public JButton getSizeButton() {
        return this.sizeButton;
    }

    public JButton getRepaireButton() {
        return this.repaireButton;
    }

    public JButton getPolyvalenteButton() {
        return this.polyvalenteButton;
    }

    public JPanel getActionsPane() {
        return this.actionsPane;
    }

    public LinkedList<InventoryViewCompactor> getInventoryViewCompactors() {
        return this.InventoryViewList;
    }
}