package main.java.model;

import main.java.model.Exception.NotEnoughMoney;
import main.java.model.Interface.Savable;
import main.java.model.storage.Value;

/**
 * La classe Money représente l'argent du joueur dans le jeu.
 * Elle fournit des méthodes pour gérer l'argent, comme l'ajout, le retrait,
 * et la vérification de la possibilité d'effectuer des achats.
 */
public class Money implements Savable {

    // Montant actuel d'argent du joueur
    private int amount;

    /**
     * Constructeur par défaut. Initialise l'argent à zéro.
     */
    public Money() {
        amount = 0;
    }

    /**
     * Retourne le montant actuel d'argent.
     * 
     * @return Le montant d'argent.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Met à jour le montant d'argent et met à jour l'affichage.
     * 
     * @param newAmount Le nouveau montant d'argent.
     */
    public void setAmount(int newAmount) {
        amount = newAmount;
        Value.game.updateMoneyLabel(this.amount); // Met à jour l'interface utilisateur
    }

    /**
     * Vérifie si un achat est possible en fonction du prix donné.
     * 
     * @param price Le prix de l'article.
     * @return true si l'achat est possible, false sinon.
     */
    public Boolean isPurchasePossible(int price) {
        return (price <= amount);
    }

    /**
     * Ajoute un montant d'argent.
     * 
     * @param amount Le montant à ajouter (doit être positif).
     * @throws AssertionError si le montant est négatif.
     */
    public void addAmount(int amount) {
        assert amount >= 0 : "Le montant à ajouter doit être positif.";
        this.amount += amount;
        Value.game.updateMoneyLabel(this.amount); // Met à jour l'affichage
    }

    /**
     * Retire un montant d'argent.
     * 
     * @param amount Le montant à retirer (doit être négatif ou zéro).
     * @throws NotEnoughMoney si le joueur n'a pas assez d'argent pour effectuer le
     *                        retrait.
     * @throws AssertionError si le montant est positif.
     */
    public void removeAmount(int amount) throws NotEnoughMoney {
        assert amount <= 0 : "Le montant à retirer doit être négatif ou zéro.";
        if (this.amount - amount < 0) {
            throw new NotEnoughMoney(); // Lève une exception si l'argent est insuffisant
        }
        this.amount -= amount;
        Value.game.updateMoneyLabel(this.amount); // Met à jour l'affichage
    }

    /**
     * Sauvegarde l'état actuel de l'argent.
     * 
     * @return Une chaîne de caractères représentant l'argent actuel.
     */
    @Override
    public String save() {
        return toString();
    }

    /**
     * Restaure l'état de l'argent à partir d'une chaîne de caractères sauvegardée.
     * 
     * @param savedState La chaîne représentant l'argent à restaurer.
     */
    @Override
    public void restore(String savedState) {
        amount = Integer.parseInt(savedState);
    }

    /**
     * Retourne une représentation en chaîne de caractères de l'argent actuel.
     * 
     * @return Une chaîne représentant l'argent.
     */
    @Override
    public String toString() {
        return String.valueOf(amount);
    }
}
