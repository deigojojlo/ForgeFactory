package main.java.model.util;

/**
 * La classe Recipe représente une recette de fabrication.
 * Elle permet de stocker toutes les informations nécessaires à la création d'un
 * objet à partir de ses ingrédients,
 * ainsi que le temps nécessaire pour réaliser cette recette.
 *
 * Attributs :
 * - result : l'objet qui sera le résultat de la recette.
 * - ingredients : la liste des ingrédients et leurs quantités respectives
 * nécessaires pour la recette.
 * - time : le temps nécessaire pour effectuer la recette, en secondes.
 * - isInstant : un booléen qui est vrai si le temps est égal à 0, indiquant que
 * la recette est instantanée.
 */
public class Recipe {
    // Tableau des ingrédients et de leurs quantités nécessaires pour la recette
    private final Couple<Objet, Integer>[] ingredients;

    // L'objet qui sera créé après l'exécution de la recette
    private final Objet result;

    // La quantité de l'objet résultat produit par la recette
    private final int resultQuantity;

    // Le temps nécessaire pour fabriquer le résultat (en secondes)
    private final int time;

    /**
     * Constructeur de la classe Recipe.
     * Ce constructeur permet d'initialiser les informations de la recette.
     *
     * @param result         Le résultat de la recette (l'objet créé après le
     *                       craft).
     * @param resultQuantity La quantité de l'objet créé.
     * @param time           Le temps nécessaire pour réaliser la recette (en
     *                       secondes).
     * @param ingredient     Un nombre variable d'objets Couple qui représentent les
     *                       ingrédients et leurs quantités.
     */
    @SafeVarargs
    public Recipe(Objet result, int resultQuantity, int time, Couple<Objet, Integer>... ingredient) {
        this.result = result;
        this.resultQuantity = resultQuantity;
        this.time = time;
        this.ingredients = ingredient;
    }

    /**
     * Retourne le résultat de la recette, c'est-à-dire l'objet créé après le craft.
     *
     * @return L'objet résultat de la recette.
     */
    public Objet getResult() {
        return this.result;
    }

    /**
     * Retourne le nombre d'ingrédients de la recette.
     *
     * @return la somme des ingrédients.
     */
    public int sum() {
        int total = 0 ; 
        for (Couple<Objet,Integer> couple : ingredients) {
            total += couple.getValue();
        }
        return total ; 
    }

    /**
     * Retourne la quantité de l'objet résultat produit par la recette.
     *
     * @return La quantité d'objet résultat.
     */
    public int getResultQuantity() {
        return this.resultQuantity;
    }

    /**
     * Retourne la liste des ingrédients avec leurs quantités respectives.
     *
     * @return Un tableau de Couple représentant les ingrédients et leurs quantités.
     */
    public Couple<Objet, Integer>[] getIngredients() {
        return this.ingredients;
    }

    /**
     * Retourne le temps nécessaire pour réaliser la recette (en secondes).
     *
     * @return Le temps nécessaire pour le craft.
     */
    public int getTime() {
        return time;
    }

    /**
     * Méthode utilitaire qui génère un nombre donné d'espaces pour le formatage de
     * la chaîne de caractères.
     *
     * @param n Le nombre d'espaces à générer.
     * @return Une chaîne de caractères composée de 'n' espaces.
     */
    private String generateSpaces(int n) {
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < n; i++) {
            spaces.append(" ");
        }
        return spaces.toString();
    }

    /**
     * Retourne une représentation sous forme de chaîne de la recette.
     * Cette représentation inclut le résultat, les ingrédients et leurs quantités,
     * ainsi que le temps nécessaire pour réaliser la recette.
     *
     * @return La chaîne de caractères représentant la recette.
     */
    @Override
    public String toString() {
        StringBuilder recipeString = new StringBuilder();

        // Informations sur le résultat
        recipeString.append(result.toString())
                .append(" x")
                .append(resultQuantity)
                .append(generateSpaces(9 - result.toString().length()))
                .append(" <=");

        // Informations sur les ingrédients
        for (Couple<Objet, Integer> ingredient : ingredients) {
            recipeString.append("  ")
                    .append(ingredient.getKey())
                    .append(generateSpaces(10 - ingredient.getKey().toString().length()))
                    .append(" x")
                    .append(ingredient.getValue())
                    .append(" ");
        }

        // Informations sur le temps de fabrication
        recipeString.append(", en ")
                .append(time)
                .append("s\n");

        return recipeString.toString();
    }
}
