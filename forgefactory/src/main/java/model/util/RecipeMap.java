package main.java.model.util;

/**
 * La classe RecipeMap permet de stocker une collection de recettes et d'offrir
 * des méthodes pour y accéder,
 * obtenir la longueur du tableau de recettes, récupérer des recettes par index,
 * par référence d'objet ou par résultat.
 */
public class RecipeMap {

    // Tableau qui contient toutes les recettes de la map
    private final Recipe[] recipes;

    /**
     * Constructeur de la classe RecipeMap.
     * Ce constructeur permet d'initialiser la map de recettes avec un ensemble de
     * recettes.
     *
     * @param recipes Une série de recettes à ajouter à la map.
     */
    public RecipeMap(Recipe... recipes) {
        this.recipes = recipes;
    }

    /**
     * Retourne le nombre total de recettes dans la map.
     *
     * @return La longueur du tableau de recettes.
     */
    public int length() {
        return recipes.length;
    }

    /**
     * Retourne la recette à l'index spécifié.
     * Si l'index est invalide (en dehors des limites), une exception sera capturée
     * et affichée.
     *
     * @param index L'index de la recette à récupérer.
     * @return La recette à l'index spécifié, ou null si l'index est invalide.
     */
    public Recipe get(int index) {
        try {
            return recipes[index];
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retourne l'index de la recette spécifiée dans la map.
     * Si la recette n'est pas trouvée, -1 sera retourné.
     *
     * @param recipe La recette dont l'index doit être trouvé.
     * @return L'index de la recette, ou -1 si la recette n'existe pas dans la map.
     */
    public int getIndexOf(Recipe recipe) {
        for (int i = 0; i < recipes.length; i++) {
            if (recipes[i] == recipe) {
                return i; // Retourne l'index de la recette
            }
        }
        return -1; // La recette n'a pas été trouvée
    }

    /**
     * Retourne la recette dont le résultat correspond à l'objet passé en paramètre.
     * Si aucune recette n'a cet objet comme résultat, la méthode retourne null.
     *
     * @param result L'objet dont on cherche la recette correspondante.
     * @return La recette dont le résultat est l'objet spécifié, ou null si aucune
     *         recette ne correspond.
     */
    public Recipe getByResult(Objet result) {
        for (int i = 0; i < recipes.length; i++) {
            if (recipes[i].getResult() == result) {
                return recipes[i]; // Retourne la recette correspondante
            }
        }
        return null; // Aucun résultat trouvé
    }

    /**
     * Retourne une représentation sous forme de chaîne de tous les éléments de la
     * map de recettes.
     * Cette méthode concatène la représentation de chaque recette.
     *
     * @return Une chaîne représentant toutes les recettes de la map.
     */
    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder();
        for (Recipe recipe : recipes) {
            resultString.append(recipe.toString());
        }
        return resultString.toString();
    }
}
