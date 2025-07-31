package main.java.model;

import main.java.controller.Timeline;
import main.java.model.Exception.NotEnoughMoney;
import main.java.model.Exception.NotFound;
import main.java.model.Interface.Savable;
import main.java.model.storage.DB;
import main.java.model.util.Couple;
import main.java.model.util.Objet;
import main.java.model.util.Position;
import main.java.model.util.Recipe;

/**
 * La classe Player représente un joueur dans le jeu.
 * Elle offre des méthodes pour interagir avec l'environnement du jeu.
 */
public class Player implements Savable {
    private Position position; // La position du joueur sur la carte
    private Map map; // La carte du jeu
    private final Inventory inventory = new Inventory(Integer.MAX_VALUE); // L'inventaire du joueur
    private final Money wallet = new Money(); // Le porte-monnaie du joueur
    private boolean canMove = true; // Indicateur de si le joueur peut se déplacer

    /**
     * Initialise le joueur avec une position, une carte et une interface graphique.
     *
     * @param position La position initiale du joueur.
     * @param map      La carte où se trouve le joueur.
     */
    public void initPlayer(Position position, Map map) {
        this.position = position;
        this.map = map;
    }

    /**
     * Récupère le porte-monnaie du joueur.
     *
     * @return Le porte-monnaie du joueur.
     */
    public Money getWallet() {
        return this.wallet;
    }

    /**
     * Place une nouvelle machine sur la carte.
     *
     * @param machine La machine à placer sur la carte.
     */
    public void placeNewMachine(Machine machine) {
        map.placeNewMachine(machine); // Place la machine sur la carte
    }

    /**
     * Récupère la position du joueur.
     *
     * @return La position du joueur.
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Récupère la ligne de la position du joueur.
     *
     * @return La ligne de la position du joueur.
     */
    public int getRow() {
        return position.getRow();
    }

    /**
     * Récupère la colonne de la position du joueur.
     *
     * @return La colonne de la position du joueur.
     */
    public int getCol() {
        return position.getCol();
    }

    /**
     * Récupère l'inventaire du joueur.
     *
     * @return L'inventaire du joueur.
     */
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Permet au joueur d'acheter un objet.
     *
     * @param item L'objet à acheter.
     * @throws NotEnoughMoney Si le joueur n'a pas assez d'argent pour acheter
     *                        l'objet.
     */
    public void buyItem(Objet item) throws NotEnoughMoney {
        this.wallet.removeAmount(item.getBuy()); // Déduit l'argent du portefeuille du joueur
        this.inventory.addItem(item, 1); // Ajoute l'objet à l'inventaire
    }

    /**
     * Permet au joueur de vendre un objet.
     *
     * @param item L'objet à vendre.
     * @throws NotFound Si l'objet n'est pas trouvé dans l'inventaire.
     */
    public void sellItem(Objet item) throws NotFound {
        this.inventory.removeItem(item, 1); // Retire l'objet de l'inventaire
        this.wallet.addAmount(item.getSell()); // Ajoute l'argent gagné à la vente dans le portefeuille
    }

    /**
     * Définit si le joueur peut se déplacer ou non.
     *
     * @param canMove La valeur à définir pour l'indicateur de mouvement.
     */
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    /**
     * Vérifie si le joueur peut se déplacer.
     *
     * @return true si le joueur peut se déplacer, false sinon.
     */
    public boolean canMove() {
        return this.canMove;
    }

    /**
     * Ajoute un objet à l'inventaire du joueur.
     *
     * @param item     L'objet à ajouter.
     * @param quantity La quantité de l'objet à ajouter.
     */
    public void addItem(Objet item, int quantity) {
        inventory.addItem(item, quantity); // Ajoute l'objet à l'inventaire
    }

    /**
     * Retire un objet de l'inventaire du joueur.
     *
     * @param item     L'objet à retirer.
     * @param quantity La quantité d'objet à retirer.
     * @throws NotFound Si l'objet n'est pas trouvé dans l'inventaire.
     */
    public void removeItem(Objet item, int quantity) throws NotFound {
        inventory.removeItem(item, quantity); // Retire l'objet de l'inventaire
    }

    /**
     * Sauvegarde l'état du joueur.
     *
     * @return L'état sauvegardé sous forme de chaîne de caractères.
     */
    @Override
    public String save() {
        // On ajoute les ingrédients non fabriqués dans l'inventaire du joueur
        for (Objet objet : Timeline.getResultCraftQueue()) {
            Recipe recipe = DB.recipeMap.getByResult(objet);
            for (Couple<Objet, Integer> ingredient : recipe.getIngredients()) {
                // Taille infinie de l'inventaire du joueur donc pas de problème ici
                this.inventory.addItem(ingredient.getKey(), ingredient.getValue());
            }
        }
        return wallet.save() + "\n" + inventory.save(); // Sauvegarde l'argent et l'inventaire
    }

    /**
     * Restaure l'état du joueur à partir d'une chaîne de caractères.
     *
     * @param savedState La chaîne de caractères représentant l'état sauvegardé.
     */
    @Override
    public void restore(String savedState) {
        String[] splittedSaveString = savedState.split(";");
        wallet.restore(splittedSaveString[0]); // Restaure le portefeuille
        // Si l'inventaire sauvegardé n'est pas vide
        if (splittedSaveString.length > 1) {
            inventory.restore(splittedSaveString[1]); // Restaure l'inventaire
        }
    }

    /**
     * Retourne une représentation sous forme de chaîne de l'inventaire du joueur.
     *
     * @return La chaîne représentant l'inventaire du joueur.
     */
    @Override
    public String toString() {
        return inventory.toString(); // Retourne la chaîne représentant l'inventaire
    }
}
