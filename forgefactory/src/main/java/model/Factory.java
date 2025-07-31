package main.java.model;

import java.util.HashMap;
import main.java.model.Enum.BonusMachine;
import main.java.model.Exception.InvalidSaveFormat;
import main.java.model.storage.DB;
import main.java.model.util.Recipe;

/**
 * The Factory class represents a specialized machine capable of processing a
 * recipe.
 * It extends the Machine class and provides functionality to save and restore
 * its state.
 * 
 * The "recipe" acts as a reference to a Recipe object, and its value can be
 * updated
 * without changing the reference itself. The class includes mechanisms to
 * handle
 * complex task scheduling and bonus configurations.
 */
public class Factory extends Machine {

    // Reference to a single Recipe object, used for processing tasks.
    private final Recipe[] recipeReference = new Recipe[1];

    /**
     * Constructor for Factory.
     * This initializes the Factory with a set of bonuses and assigns a default
     * recipe.
     * 
     * @param bonuses A map containing the bonus configurations for the Factory.
     */
    public Factory(HashMap<BonusMachine, Integer> bonuses) {
        super(bonuses);
        recipeReference[0] = DB.recipeMap.get(0); // Default recipe assignment.
    }

    /**
     * Constructor for Factory.
     * This initializes the Factory using a single BonusMachine configuration.
     * 
     * @param bonus A single bonus configuration to initialize the Factory.
     */
    public Factory(BonusMachine bonus) {
        this(createBonusList(bonus));
    }

    /**
     * Default constructor for Factory.
     * This initializes the Factory with no bonuses.
     */
    public Factory() {
        this(new HashMap<>());
    }

    /**
     * Gets the current Recipe object associated with the Factory.
     * 
     * @return The current Recipe object.
     */
    public Recipe getRecipe() {
        return recipeReference[0];
    }

    /**
     * Sets a new Recipe object for the Factory.
     * 
     * @param recipe The new Recipe to associate with the Factory.
     */
    public void setRecipe(Recipe recipe) {
        this.recipeReference[0] = recipe;
    }

    /**
     * Saves the current state of the Factory.
     * This includes the state of the Machine and the associated Recipe.
     * 
     * @return A serialized string representing the Factory's state.
     */
    @Override
    public String save() {
        return super.save() + "," + " " + "," + DB.recipeMap.getIndexOf(recipeReference[0]);
    }

    /**
     * Restores the Factory's state from a serialized string.
     * 
     * @param serializedState A string representing the saved state of the Factory.
     * @throws InvalidSaveFormat If the serialized format is invalid.
     */
    @Override
    public void restore(String serializedState) throws InvalidSaveFormat {
        String[] factoryStateParts = serializedState.split(",");

        // Restore the recipe from the serialized state.
        this.recipeReference[0] = DB.recipeMap.get(Integer.parseInt(factoryStateParts[factoryStateParts.length - 1]));

        // Restore the inventory and bonuses using the parent class's restore method.
        super.restore(serializedState);
    }
}
