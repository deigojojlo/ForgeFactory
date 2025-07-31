package main.java.model.util;

import main.java.model.Enum.BonusMachine;
import main.java.model.Machine;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * La classe Bonus applique des bonus à une machine en fonction des différents
 * bonus qui lui sont attribués.
 * Les bonus incluent des améliorations de taille d'inventaire, de vitesse, de
 * durabilité, et un statut fragile.
 */
public class Bonus {

    /**
     * Applique les bonus à la machine donnée.
     * Les bonus sont extraits de la liste des bonus de la machine et sont appliqués
     * aux différentes propriétés de la machine.
     *
     * @param machine La machine à laquelle appliquer les bonus.
     */
    public static void applyBonus(Machine machine) {
        // Compteurs de bonus
        int inventoryBonus = 0; // Bonus d'inventaire (taille)
        int speedBonus = 0; // Bonus de vitesse
        int unbreakingBonus = 0; // Bonus de durabilité (unbreaking)
        boolean fragileBonus = false; // Indicateur de bonus fragile

        // Récupère la liste des bonus attribués à la machine
        HashMap<BonusMachine, Integer> bonusList = machine.getCountBonus();

        // Parcours des bonus pour les appliquer
        for (Entry<BonusMachine, Integer> entry : bonusList.entrySet()) {
            switch (entry.getKey()) {
                case XL:
                    // Applique le bonus d'inventaire
                    inventoryBonus = entry.getValue();
                    break;
                case SPEED:
                    // Applique le bonus de vitesse
                    speedBonus = entry.getValue();
                    break;
                case UNBREAKING:
                    // Applique le bonus de durabilité
                    unbreakingBonus = entry.getValue();
                    break;
                case FRAGILE:
                    // Applique le bonus fragile
                    fragileBonus = true;
                    break;
                default:
                    break;
            }
        }

        // Applique les bonus à la machine
        machine.extraSize(100 * inventoryBonus); // Augmente la taille d'inventaire
        machine.extraSpeed(0.1 * speedBonus); // Augmente la vitesse
        machine.extraDurability(100 * unbreakingBonus); // Augmente la durabilité
        if (fragileBonus) {
            machine.setFragile(); // Applique le bonus fragile
        }
    }
}
