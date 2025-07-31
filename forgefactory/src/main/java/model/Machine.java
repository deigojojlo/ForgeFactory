package main.java.model;

import java.util.HashMap;
import java.util.Map.Entry;
import main.java.model.Enum.BonusMachine;
import main.java.model.Exception.InvalidSaveFormat;
import main.java.model.Interface.Savable;
import main.java.model.storage.Value;
import main.java.model.util.Task;

/**
 * La classe abstraite Machine représente une base commune pour toutes les
 * machines du jeu.
 * Elle hérite de la classe Clickable et offre des propriétés et méthodes
 * communes aux machines.
 */
public abstract class Machine implements Savable {

    /**
     * La tâche actuelle assignée à la machine.
     */
    protected Task task;
    /**
     * Liste des bonus attribués à la machine.
     * Les clés sont des types de bonus (BonusMachine) et les valeurs sont les
     * quantités associées.
     */
    private final HashMap<BonusMachine, Integer> countBonus;
    /**
     * L'inventaire associé à la machine.
     * Permet de stocker des objets ou ressources.
     */
    private final Inventory inventory;
    /**
     * La durabilité maximale de la machine (points de vie maximum).
     */
    private int maxDurability;
    /**
     * La durabilité actuelle de la machine.
     */
    private int durability;
    /**
     * La vitesse supplémentaire attribuée à la machine (sous forme de pourcentage).
     */
    private double extraSpeed;
    /**
     * Indique si la machine est en cours de première configuration.
     */
    private boolean firstConfiguration;
    // Indique si la machine est fragile
    private boolean fragile;
    // Indique si la machine est cassé (ne peut pas avoir ce status si elle n'est
    // pas fragile)
    private boolean breaked;

    /**
     * Constructeur de la classe Machine.
     * Initialise la machine avec un ensemble de bonus, une durabilité par défaut
     * et un inventaire.
     *
     * @param bonus Les bonus attribués à la machine.
     */
    public Machine(HashMap<BonusMachine, Integer> bonus) {
        this.durability = 200;
        this.maxDurability = 200;
        this.extraSpeed = 0;
        this.countBonus = bonus;
        this.firstConfiguration = true;
        this.fragile = false;
        this.breaked = false;
        this.inventory = new Inventory(Value.machineInventorySize);
    }

    /**
     * Crée une liste de bonus contenant un seul type de bonus.
     *
     * @param bonus Le type de bonus à ajouter.
     * @return Une HashMap contenant le bonus spécifié avec une quantité de 1.
     */
    protected static HashMap<BonusMachine, Integer> createBonusList(BonusMachine bonus) {
        HashMap<BonusMachine, Integer> bonusList = new HashMap<>();
        if (bonus != null)
            bonusList.put(bonus, 1);
        return bonusList;
    }

    /**
     * Assigne une tâche à la machine.
     *
     * @param task La tâche à attribuer.
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * Retourne l'inventaire associé à la machine.
     *
     * @return L'inventaire de la machine.
     */
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Augmente la taille de l'inventaire de la machine.
     *
     * @param add La quantité supplémentaire à ajouter à la taille de l'inventaire.
     */
    public void extraSize(int add) {
        this.inventory.increaseCapacity(add);
    }

    /**
     * Augmente la durabilité maximale de la machine.
     *
     * @param add La quantité supplémentaire à ajouter à la durabilité maximale.
     */
    public void extraDurability(int add) {
        this.maxDurability += add;
    }

    /**
     * Ajoute un bonus de vitesse à la machine.
     *
     * @param add Le pourcentage de vitesse supplémentaire à appliquer.
     */
    public void extraSpeed(double add) {
        this.extraSpeed += add;
    }

    /**
     * Détermine si la machine est polyvalente.
     * Cela dépend de si c'est la première configuration ou si un bonus spécifique
     * est présent.
     *
     * @return true si la machine est polyvalente, sinon false.
     */
    public boolean getPolyvalence() {
        if (this.firstConfiguration) {
            this.firstConfiguration = false;
            return true;
        } else {
            return countBonus.getOrDefault(BonusMachine.POLYVALENTE, 0) == 1;
        }
    }

    /**
     * Configure la machine pour qu'elle devienne polyvalente.
     */
    public void setPolytvalente() {
        this.countBonus.put(BonusMachine.POLYVALENTE, 1);
    }

    public int getDurability() {
        return this.durability;
    }

    // --- Gestion de la durabilité ---
    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getMaxDurability() {
        return this.maxDurability;
    }

    public void setMaxDurability(int maxDurability) {
        this.maxDurability = maxDurability;
    }

    public double getExtraSpeed() {
        return this.extraSpeed;
    }

    // --- Gestion de la fragilité ---

    public void setFragile() {
        this.fragile = true;
    }

    public boolean getFragile() {
        return this.fragile;
    }

    public void setBreaked(boolean breaked) {
        this.breaked = breaked;
    }

    public boolean getBreaked() {
        return this.breaked;
    }

    // --- Gestion de l'inventaire ---
    public int getInventorySize() {
        return inventory.getCapacity();
    }

    public int getInventoryCount() {
        return inventory.getCurrentCount();
    }

    // --- Gestion des bonus ---
    public int getNumberOf(BonusMachine bonusMachine) {
        return this.countBonus.getOrDefault(bonusMachine, 0);
    }

    public void addcountBonus(BonusMachine bonusMachine) {
        this.countBonus.put(bonusMachine, 1 + this.countBonus.getOrDefault(bonusMachine, 0));
    }

    public HashMap<BonusMachine, Integer> getCountBonus() {
        return this.countBonus;
    }

    /**
     * Effectue une action spécifique lorsque la machine est cliquée.
     * Cette méthode doit être implémentée dans les classes dérivées.
     *
     * @param frame L'interface graphique dans laquelle l'action est déclenchée.
     */

    /**
     * Sauvegarde l'état actuel de la machine sous forme de chaîne de caractères.
     * Inclut l'inventaire, la durabilité, et les bonus.
     *
     * @return Une chaîne représentant l'état de la machine.
     */
    @Override
    public String save() {
        String bonusSave = "";
        if (!countBonus.isEmpty()) {
            for (Entry<BonusMachine, Integer> bonusMachine : countBonus.entrySet()) {
                bonusSave += bonusMachine.getKey() + ":" + bonusMachine.getValue() + "/";
            }
        } else {
            bonusSave = " ";
        }
        return inventory.save() + "," + this.durability + "," + this.breaked + "," + bonusSave;
    }

    /**
     * Restaure l'état de la machine à partir d'une chaîne de sauvegarde.
     *
     * @param save Une chaîne formatée représentant l'état sauvegardé.
     * @throws InvalidSaveFormat Si le format de la chaîne est invalide.
     */
    @Override
    public void restore(String save) throws InvalidSaveFormat {
        try {
            String[] saveSplited = save.split(",");
            this.inventory.restore(saveSplited[1]); // Restaure l'inventaire.
            this.durability = Integer.parseInt(saveSplited[2]); // Restaure la durabilité.
            this.breaked = saveSplited[3].equals("true") ;
            this.firstConfiguration = false; // Machine déjà configurée.

            // Restaure les bonus s'ils existent.
            if (!saveSplited[4].equals(" ")) {
                String[] bonusStringList = saveSplited[4].split("/");
                for (String bonusString : bonusStringList) {
                    if (!bonusString.equals("")) {
                        String[] paire = bonusString.split(":");
                        BonusMachine b = BonusMachine.valueOf(paire[0]);
                        this.countBonus.put(b, Integer.valueOf(paire[1]));
                    }
                }
            }
        } catch (Exception e) {
            Value.printError("erreur lors du chargement d'une machine de type : "
                    + this.getClass().getName());
            throw new InvalidSaveFormat();
        }
    }
}
