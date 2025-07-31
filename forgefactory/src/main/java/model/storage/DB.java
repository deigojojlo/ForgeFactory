package main.java.model.storage;

import main.java.model.Enum.Item;
import main.java.model.Map.ResourceList;
import main.java.model.util.Couple;
import main.java.model.util.Objet;
import main.java.model.util.Recipe;
import main.java.model.util.RecipeMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Interface qui stock nos informations importantes
 * ListToInt associe Map.List à un id
 * intToList la bijection réciproque de ListToInt
 * item la liste des objet du jeu
 * listToObjet associe Map.List à un objet de item pour voir creer un objet
 * ressource qui contient un Map.List et qui sur un click donne un objet du jeu
 * ObjetToInt associe un objet de item à son id
 * intToObjet sa bijection reciproque
 * RecipeMap la liste de toutes les Recipes faisables dans le jeu
 */
public interface DB {
    // hashmap for Map.List
    HashMap<ResourceList, Integer> ListToInt = new HashMap<ResourceList, Integer>() {
        {
            int id = 0;
            for (ResourceList i : ResourceList.values()) {
                put(i, id++);
            }
        }
    };

    HashMap<Integer, ResourceList> intToList = new HashMap<Integer, ResourceList>() {
        {
            for (Map.Entry<ResourceList, Integer> entry : ListToInt.entrySet()) {
                put(entry.getValue(), entry.getKey());
            }
        }
    };

    // hashMap of objet for inventory, craft, market ... NECESSAIRE: ID DANS ORDRE
    // CROISSANT (DANS L'ORDRE ORDINAL DE ITEM)
    Objet[] item = {
            new Objet(Item.APPLE, 5, 1, 0, 5, 5), // resource
            new Objet(Item.WOOD, 5, 1, 1, 0, 1), // resource
            new Objet(Item.STEEL, 5, 1, 5, 2, 2), // resource
            new Objet(Item.STONE, 5, 1, 2, 0, 3), // resource
            new Objet(Item.PIE, 20, 15), // item
            new Objet(Item.JAM, 20, 15),
            new Objet(Item.WOODAXE, 40, 30),
            new Objet(Item.STONEAXE, 45, 35),
            new Objet(Item.STEELAXE, 50, 40),
            new Objet(Item.INGOT, 15, 10),
            new Objet(Item.STEELBLOCK, 150, 100),
            new Objet(Item.BRICK, 50, 25),
            new Objet(Item.PLANKS, 10, 5),
            new Objet(Item.ROOF, 25, 12),
            new Objet(Item.HOUSE, 150000, 100000),
            new Objet(Item.BERRIES, 5, 1, 2, 1, 1), // resource
    };

    // assoc Map.List to a Objet of item
    HashMap<ResourceList, Objet> listToObjet = new HashMap<ResourceList, Objet>() {
        {
            put(ResourceList.APPLE, item[0]); // la pomme à l'objet pomme
            put(ResourceList.WOOD, item[1]); // le bois à l'objet bois
            put(ResourceList.STEEL, item[2]); // l'acier à l'objet acier
            put(ResourceList.STONE, item[3]); // la pierre à l'objet pierre
            put(ResourceList.BERRIES, item[15]); // les baies à l'objet baies
        }
    };
    HashMap<Object, ResourceList> ObjetToList = new HashMap<>();
    // all item with his id
    HashMap<Objet, Integer> objetToInt = new HashMap<Objet, Integer>() {
        {
            for (Objet o : item) {
                put(o, o.getId());
            }
        }
    };

    HashMap<Integer, Objet> intToObjet = new HashMap<Integer, Objet>() {
        {
            for (Map.Entry<Objet, Integer> entry : objetToInt.entrySet()) {
                put(entry.getValue(), entry.getKey());
            }
        }
    };

    RecipeMap recipeMap = new RecipeMap(
            new Recipe(item[10], 1, 10, new Couple<>(item[9], 9)), // Steelbloc need ingot
            new Recipe(item[9], 1, 1, new Couple<>(item[2], 1)), // ingot need steel
            new Recipe(item[4], 1, 5, new Couple<>(item[0], 5)), // pie need apple
            new Recipe(item[6], 1, 5, new Couple<>(item[1], 6)), // woodaxe new wood
            new Recipe(item[7], 1, 10, new Couple<>(item[1], 2), new Couple<>(item[3], 3)),
            new Recipe(item[8], 1, 15, new Couple<>(item[1], 2), new Couple<>(item[2], 3)),
            new Recipe(item[11], 2, 10, new Couple<>(item[3], 4)),
            new Recipe(item[12], 4, 2, new Couple<>(item[1], 1)),
            new Recipe(item[13], 1, 2, new Couple<>(item[1], 2), new Couple<>(item[3], 2)),
            new Recipe(item[14], 1, 60, new Couple<>(item[11], 100),
                    new Couple<>(item[12], 100),
                    new Couple<>(item[13], 50)));
}