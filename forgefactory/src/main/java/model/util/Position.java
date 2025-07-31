package main.java.model.util;

import java.util.Objects;

/**
 * La classe Pos représente une position sur une carte, définie par une ligne et
 * une colonne.
 * Elle offre des méthodes pour accéder et modifier ces coordonnées ainsi que
 * des méthodes
 * utiles pour comparer et afficher la position.
 */
public class Position extends Couple<Integer, Integer> {
    /**
     * Constructeur de la classe Pos. Initialise la position avec les coordonnées
     * spécifiées.
     *
     * @param row La ligne de la position.
     * @param col La colonne de la position.
     */
    public Position(int row, int col) {
        super(row,col);
    }

    /**
     * Compare cette position avec une autre pour vérifier si elles sont égales.
     * Deux positions sont égales si elles ont la même ligne et la même colonne.
     *
     * @param obj L'objet à comparer avec cette position.
     * @return true si les positions sont égales, false sinon.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Vérifie si c'est la même référence
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false; // Vérifie si l'objet est de la même classe
        }
        Position autre = (Position) obj; // Cast de l'objet en Pos
        return Objects.equals(autre.getKey(), this.getKey()) && Objects.equals(this.getValue(), autre.getValue()); // Compare les coordonnées
    }

    /**
     * Calcule le code de hachage pour cette position. Cela permet à cette classe
     * de fonctionner correctement dans des structures de données comme HashMap ou
     * HashSet.
     *
     * @return Le code de hachage basé sur les coordonnées (ligne, colonne).
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.getKey(), super.getValue()); // Utilise un hachage basé sur les coordonnées
    }

    /**
     * Renvoie une représentation sous forme de chaîne de caractères de la position.
     * Le format est "row,col", où "row" est la ligne et "col" est la colonne.
     *
     * @return La chaîne représentant la position (ligne, colonne).
     */
    @Override
    public String toString() {
        return super.getKey() + "," + super.getValue(); // Renvoie la chaîne de caractères représentant la position
    }

    /**
     * Retourne la ligne de la position.
     *
     * @return La ligne de la position.
     */
    public int getRow() {
        return super.getKey();
    }

    /**
     * Modifie la ligne de la position.
     *
     * @param row La nouvelle ligne de la position.
     */
    public void setRow(int row) {
        super.setKey(row);
    }

    /**
     * Retourne la colonne de la position.
     *
     * @return La colonne de la position.
     */
    public int getCol() {
        return super.getValue();
    }

    /**
     * Modifie la colonne de la position.
     *
     * @param col La nouvelle colonne de la position.
     */
    public void setCol(int col) {
        super.setValue(col);
    }
}
