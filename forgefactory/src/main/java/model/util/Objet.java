package main.java.model.util;

import main.java.model.Enum.Item;

/**
 * La classe Objet représente un objet dans le jeu, incluant ses propriétés
 * telles que le nom, l'ID, le prix d'achat et de vente, la durée de récolte,
 * le temps de récupération et la quantité disponible. Cette classe implémente
 * l'interface Cloneable afin de permettre la duplication d'un objet.
 */
public class Objet implements Cloneable {

    private final Item name; // Le nom de l'objet (correspond à un élément de l'énumération Item)
    private final int sell; // Le prix de vente de l'objet
    private final int buy; // Le prix d'achat de l'objet
    private final int id; // L'ID unique de l'objet

    private final int duration; // La durée de récolte de l'objet (si applicable)
    private final int recovery; // Le temps de récupération de l'objet (si applicable)
    private final int quantity; // La quantité d'objet disponible

    /**
     * Constructeur de l'objet avec les informations de base (nom, ID, prix d'achat
     * et de vente).
     * Utilisé pour les objets sans durée de récolte ou récupération.
     *
     * @param name Le nom de l'objet (correspond à un élément de l'énumération
     *             Item).
     * @param buy  Le prix d'achat de l'objet.
     * @param sell Le prix de vente de l'objet.
     */
    public Objet(Item name, int buy, int sell) {
        this.name = name;
        this.sell = sell;
        this.buy = buy;
        this.id = name.ordinal();
        this.duration = 0;
        this.recovery = 0;
        this.quantity = 0;
    }

    /**
     * Constructeur de l'objet avec des informations supplémentaires pour la durée
     * et la récupération. En particulier ce contructeur créer des objet qui sont
     * recoltables sur la carte
     *
     * @param name     Le nom de l'objet.
     * @param buy      Le prix d'achat de l'objet.
     * @param sell     Le prix de vente de l'objet.
     * @param duration La durée de récolte de l'objet.
     * @param recovery Le temps de récupération de l'objet.
     * @param quantity La quantité d'objet disponible.
     */
    public Objet(Item name, int buy, int sell, int duration, int recovery, int quantity) {
        this.name = name;
        this.id = name.ordinal();
        this.buy = buy;
        this.sell = sell;
        this.duration = duration;
        this.quantity = quantity;
        this.recovery = recovery;
    }

    /**
     * Retourne le nom de l'objet.
     *
     * @return Le nom de l'objet.
     */
    public String getName() {
        return this.name.toString();
    }

    /**
     * Retourne l'ID unique de l'objet.
     *
     * @return L'ID de l'objet.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Retourne la durée de récolte de l'objet.
     *
     * @return La durée de récolte.
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Retourne le temps de récupération de l'objet.
     *
     * @return Le temps de récupération.
     */
    public int getRecovery() {
        return this.recovery;
    }

    /**
     * Retourne le prix de vente de l'objet.
     *
     * @return Le prix de vente.
     */
    public int getSell() {
        return this.sell;
    }

    /**
     * Retourne le prix d'achat de l'objet.
     *
     * @return Le prix d'achat.
     */
    public int getBuy() {
        return this.buy;
    }

    /**
     * Retourne la quantité d'objet disponible.
     *
     * @return La quantité d'objet.
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Clone l'objet actuel pour créer une copie indépendante de celui-ci.
     *
     * @return Une nouvelle instance d'objet qui est une copie de l'objet courant.
     */
    @Override
    public Object clone() {
        Objet clonedObjet = null;
        try {
            clonedObjet = (Objet) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clonedObjet;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'objet.
     *
     * @return Le nom de l'objet sous forme de chaîne.
     */
    @Override
    public String toString() {
        return name.toString();
    }

    /**
     * Vérifie si l'objet courant est égal à un autre objet.
     * L'égalité est basée sur le nom de l'objet.
     *
     * @param obj L'objet à comparer.
     * @return true si les objets sont égaux, sinon false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        if (this == obj)
            return true;
        return this.name == ((Objet) obj).name;
    }
}
