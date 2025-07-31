package main.java.model.util;

/**
 * La classe Couple représente un couple de valeurs génériques.
 * Elle permet de stocker et de manipuler une clé et une valeur de types
 * génériques.
 * Utilisée principalement pour associer une clé à une valeur, comme une entrée
 * dans une map.
 *
 * @param <K> Le type de la clé.
 * @param <V> Le type de la valeur.
 */
public class Couple<K, V> {

    private K key; // La clé de type générique K
    private V value; // La valeur de type générique V

    /**
     * Constructeur pour initialiser un couple avec une clé et une valeur.
     *
     * @param key   La clé du couple.
     * @param value La valeur associée à la clé.
     */
    public Couple(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Retourne la clé du couple.
     *
     * @return La clé du couple.
     */
    public K getKey() {
        return this.key;
    }

    /**
     * Modifie la clé du couple.
     *
     * @param key La nouvelle clé à attribuer au couple.
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * Retourne la valeur du couple.
     *
     * @return La valeur du couple.
     */
    public V getValue() {
        return this.value;
    }

    /**
     * Modifie la valeur du couple.
     *
     * @param value La nouvelle valeur à attribuer au couple.
     */
    public void setValue(V value) {
        this.value = value;
    }

    /**
     * Retourne une représentation textuelle du couple sous la forme "clé = valeur".
     *
     * @return Une chaîne représentant le couple.
     */
    @Override
    public String toString() {
        return key + " = " + value;
    }
}
