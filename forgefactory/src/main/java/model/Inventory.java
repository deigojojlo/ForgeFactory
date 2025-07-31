package main.java.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import main.java.model.Exception.NotFound;
import main.java.model.Interface.Savable;
import main.java.model.storage.DB;
import main.java.model.util.Objet;

/**
 * Inventory class represents the player's inventory.
 * It provides methods to manage the inventory by adding, removing, and
 * retrieving items.
 */
public class Inventory implements Savable {
    private final Map<Objet, Integer> items = new HashMap<>(); // Stores items and their quantities.
    private int capacity; // Maximum capacity of the inventory.
    private int currentCount; // Current total count of items in the inventory.

    /**
     * Constructs an Inventory with the specified size.
     *
     * @param capacity The maximum capacity of the inventory.
     */
    public Inventory(int capacity) {
        this.capacity = capacity;
        this.currentCount = 0;
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item     The item to add.
     * @param quantity The quantity of the item to add.
     */
    public void addItem(Objet item, int quantity) {
        if (quantity < 0) throw  new RuntimeException("Illegal quantity");
        int newQuantity = items.getOrDefault(item, 0) + quantity ;
        items.put(item, newQuantity < 0 ? Integer.MAX_VALUE : newQuantity);
        currentCount += quantity;
    }

    /**
     * Removes an item from the inventory.
     *
     * @param item     The item to remove.
     * @param quantity The quantity of the item to remove.
     * @throws NotFound If the item is not found or insufficient quantity exists in
     *                  the inventory.
     */
    public void removeItem(Objet item, int quantity) throws NotFound {
        if (!items.containsKey(item)) {
            throw new NotFound("The requested item is not in the inventory.");
        }
        int currentQuantity = items.get(item);
        if (currentQuantity < quantity) {
            throw new NotFound("Not enough quantity of the item in the inventory.");
        }
        items.put(item, currentQuantity - quantity);
        currentCount -= quantity;
        if (items.get(item) == 0) {
            items.remove(item);
        }
    }

    /**
     * Retrieves a list of all unique items in the inventory.
     *
     * @return A list of unique items in the inventory.
     */
    public List<Objet> getItems() {
        return new LinkedList<>(items.keySet());
    }

    /**
     * Retrieves the full content of the inventory with quantities.
     *
     * @return A map containing all items and their quantities.
     */
    public Map<Objet, Integer> getInventoryContent() {
        return items;
    }

    /**
     * Gets the quantity of a specific item in the inventory.
     *
     * @param item The item to check.
     * @return The quantity of the item.
     */
    public int getQuantityOf(Objet item) {
        return items.getOrDefault(item, 0);
    }

    /**
     * Gets the maximum capacity of the inventory.
     *
     * @return The inventory's maximum capacity.
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Gets the current total number of items in the inventory.
     *
     * @return The current item count.
     */
    public int getCurrentCount() {
        return this.currentCount;
    }

    /**
     * Increases the inventory's capacity.
     *
     * @param additionalCapacity The amount to increase the inventory's capacity.
     */
    public void increaseCapacity(int additionalCapacity) {
        capacity += additionalCapacity;
    }

    /**
     * Saves the current state of the inventory.
     *
     * @return A serialized string representing the inventory's state.
     */
    @Override
    public String save() {
        StringBuilder savedState = new StringBuilder();
        items.forEach((item, quantity) -> savedState.append(String.format("%s:%d/", DB.objetToInt.get(item).toString(),quantity)));
        return savedState.toString();
    }

    /**
     * Restores the inventory's state from a serialized string.
     *
     * @param serializedState A string representing the saved state of the
     *                        inventory.
     */
    @Override
    public void restore(String serializedState) {
        try {
            String[] pairs = serializedState.split("/");
            for (String pair : pairs) {
                if (!pair.isEmpty()) {
                    String[] association = pair.split(":");
                    int itemId = Integer.parseInt(association[0]);
                    int quantity = Integer.parseInt(association[1]);

                    Objet item = DB.intToObjet.get(itemId);
                    if (item == null) {
                        throw new NotFound("Corrupted item ID in inventory: " + itemId);
                    }
                    addItem(item, quantity);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Corrupted item quantity in inventory: " + e.getMessage());
        } catch (NotFound e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Provides a string representation of the inventory's content.
     *
     * @return A formatted string of all items and their quantities in the
     *         inventory.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Inventory:\n------------\n");
        items.forEach((item, quantity) -> result.append(String.format("%s:%d\n", item.toString(),quantity)));
        return result.toString();
    }
}
