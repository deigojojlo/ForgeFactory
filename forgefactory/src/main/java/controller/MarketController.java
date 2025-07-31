package main.java.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import main.java.model.Enum.BonusMachine;
import main.java.model.Exception.NotEnoughMoney;
import main.java.model.Exception.NotFound;
import main.java.model.Factory;
import main.java.model.Harvester;
import main.java.model.Interface.Clickable;
import main.java.model.Player;
import main.java.model.storage.DB;
import main.java.model.storage.Value;
import main.java.model.util.Objet;
import main.java.view.GUI;
import main.java.view.MarketView;
import main.java.view.OverlayPanel;

public class MarketController implements Clickable {
    private final Player player; // Le modèle représentant le joueur
    private final MarketView view; // La vue du marché

    /**
     * Constructeur du contrôleur du marché.
     * Ce contrôleur gère la logique de l'interface du marché, où le joueur peut
     * acheter et vendre des objets, ainsi
     * que gérer les machines comme les usines et les moissonneuses.
     *
     * @param player Le modèle du joueur.
     */
    public MarketController(Player player) {
        this.player = player;
        this.view = new MarketView();

        // Initialise la vue avec les éléments du modèle
        List<Objet> items = getItems();
        view.setItemsPanel(items, this); // Configure le panneau des items
        view.setStructurePanel(); // Configure le panneau de structures (machines)
        setFactoryHandle(); // Gère les actions des boutons de fabrication
        setHarvesterHandle(); // Gère les actions des boutons de moissonneuse
    }

    /**
     * Récupère la liste des objets disponibles dans la base de données.
     *
     * @return Une liste des objets disponibles.
     */
    public List<Objet> getItems() {
        LinkedList<Objet> result = new LinkedList<>();
        Collections.addAll(result, DB.item); // Ajoute tous les objets disponibles dans la liste
        return result;
    }

    /**
     * Récupère la quantité d'un objet spécifique dans l'inventaire du joueur.
     *
     * @param item L'objet pour lequel récupérer la quantité.
     * @return La quantité d'objet dans l'inventaire du joueur.
     */
    public int getQuantity(Objet item) {
        return player.getInventory().getQuantityOf(item); // Retourne le nombre d'objets dans l'inventaire
    }

    /**
     * Gère l'achat d'une usine avec un bonus donné.
     * Le prix de l'usine varie selon les bonus appliqués.
     *
     * @param bonusList Le bonus à appliquer à l'usine, ou null si aucun bonus.
     */
    public void handleFactory(BonusMachine bonusList) {
        try {
            // Calcul du prix en fonction des bonus
            int price = 150; // Prix de base
            if (bonusList != null && bonusList != BonusMachine.FRAGILE)
                price += 100; // Augmente le prix si un bonus est présent
            else
                price /= 2; // Si le bonus est fragilité, réduit le prix de moitié

            // Retrait de l'argent et achat de l'usine
            Value.player.getWallet().removeAmount(price);
            if (bonusList == null) {
                player.placeNewMachine(new Factory()); // Usine sans bonus
            } else {
                player.placeNewMachine(new Factory(bonusList)); // Usine avec bonus
            }
            Value.frame.hideOverlayPanel(); // Cache le panneau de l'overlay
        } catch (NotEnoughMoney e) {
            Value.printError("Not enough money"); // Message d'erreur en cas de manque d'argent
        }
    }

    /**
     * Gère l'achat d'une moissonneuse avec un bonus donné.
     *
     * @param bonus Le bonus à appliquer à la moissonneuse, ou null si aucun bonus.
     */
    public void handleHarvester(BonusMachine bonus) {
        try {
            // Retrait de l'argent et achat de la moissonneuse
            Value.player.getWallet().removeAmount(150 + ((bonus == null) ? 0 : 100));
            if (bonus == null) {
                player.placeNewMachine(new Harvester(null)); // Moissonneuse sans bonus
            } else {
                player.placeNewMachine(new Harvester(null, bonus)); // Moissonneuse avec bonus
            }
            Value.frame.hideOverlayPanel(); // Cache le panneau de l'overlay
        } catch (NotEnoughMoney e) {
            Value.printError("Not enough money");
        }
    }

    /**
     * Gère l'achat d'un objet par le joueur et met à jour la vue.
     *
     * @param item L'objet à acheter.
     */
    public void handleBuy(Objet item) {
        try {
            player.buyItem(item); // Achat de l'objet
            notifyView(item); // Met à jour la vue avec la nouvelle quantité
        } catch (NotEnoughMoney e) {
            Value.printError("Not enough money");
        }
    }

    /**
     * Gère la vente d'un objet par le joueur et met à jour la vue.
     *
     * @param item L'objet à vendre.
     */
    public void handleSell(Objet item) {
        try {
            player.sellItem(item); // Vente de l'objet
            notifyView(item); // Met à jour la vue avec la nouvelle quantité
        } catch (NotFound e) {

        }
    }

    /**
     * Associe les boutons d'actions de fabrication à leurs gestionnaires
     * d'événements.
     */
    public void setFactoryHandle() {
        for (HashMap.Entry<JButton, BonusMachine> entry : view.getFactoryButtons().entrySet()) {
            entry.getKey().addActionListener(e -> {
                handleFactory(entry.getValue()); // Gère l'achat de l'usine avec bonus
            });
        }
    }

    /**
     * Associe les boutons d'actions de moissonneuse à leurs gestionnaires
     * d'événements.
     */
    public void setHarvesterHandle() {
        for (HashMap.Entry<JButton, BonusMachine> entry : view.getHarvesterButtons().entrySet()) {
            entry.getKey().addActionListener(e -> {
                handleHarvester(entry.getValue()); // Gère l'achat de la moissonneuse avec bonus
            });
        }
    }

    /**
     * Met à jour la vue avec la nouvelle quantité d'un objet dans l'inventaire du
     * joueur.
     *
     * @param item L'objet dont la quantité a changé.
     */
    private void notifyView(Objet item) {
        int newQuantity = player.getInventory().getQuantityOf(item); // Nouvelle quantité d'objet
        view.updateItemQuantity(item.getId(), newQuantity); // Met à jour la vue avec la nouvelle quantité
    }

    /**
     * Met à jour la vue avec la nouvelle quantité des objets dans l'inventaire du
     * joueur.
     *
     */
    public void updateView() {
        for (Objet item : DB.item) {
            int newQuantity = player.getInventory().getQuantityOf(item); // Nouvelle quantité d'objet
            view.updateItemQuantity(item.getId(), newQuantity); // Met à jour la vue avec la nouvelle quantité
        }
    }

    /**
     * Displays the market overlay when the market is clicked.
     *
     * @param frame The main game frame.
     */
    @Override
    public void action(GUI frame) {
        updateView();
        frame.showOverlayPanel(new OverlayPanel(frame, this.view));
    }
}
