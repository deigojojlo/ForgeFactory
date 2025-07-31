package main.java.model;

import java.util.HashMap;
import main.java.model.Enum.BonusMachine;
import main.java.model.Exception.InvalidSaveFormat;
import main.java.model.storage.DB;
import main.java.model.storage.Value;

/**
 * La classe Harvester représente une machine de récolte dans le jeu.
 * Elle hérite de la classe Machine et fournit des fonctionnalités spécifiques
 * pour la récolte de ressources.
 */
public class Harvester extends Machine {

    /**
     * La ressource que cette machine récolte.
     * Utilise un élément de la liste des ressources définie dans Map.List.
     */
    private Map.ResourceList resource;

    /**
     * Constructeur de Harvester avec des coordonnées et une liste de bonus.
     *
     * @param resource La ressource que la machine va récolter.
     * @param bonus    Une HashMap contenant les bonus de la machine, avec les types
     *                 de bonus comme clé et leurs valeurs associées.
     */
    public Harvester(Map.ResourceList resource, HashMap<BonusMachine, Integer> bonus) {
        super(bonus); // Appelle le constructeur de la classe Machine.
        this.resource = resource;
    }

    /**
     * Constructeur de Harvester avec une ressource et un bonus spécifique.
     *
     * @param resource La ressource que la machine va récolter.
     * @param bonus    Un bonus spécifique à appliquer à la machine.
     */
    public Harvester(Map.ResourceList resource, BonusMachine bonus) {
        this(resource, createBonusList(bonus)); // Transforme le bonus en HashMap.
    }

    public Harvester(Map.ResourceList resource) {
        this(resource, new HashMap<>());
    }

    /**
     * Retourne la ressource que la machine est configurée pour récolter.
     *
     * @return La ressource actuellement attribuée.
     */
    public Map.ResourceList getResource() {
        return this.resource;
    }

    /**
     * Définit la ressource que la machine doit récolter.
     *
     * @param resource La ressource à attribuer à la machine.
     */
    public void setResource(Map.ResourceList resource) {
        this.resource = resource;
    }

    /**
     * Sauvegarde l'état de la machine sous forme de chaîne de caractères.
     * Inclut les informations de la classe parente et la ressource actuelle.
     *
     * @return Une chaîne de caractères représentant l'état de la machine.
     */
    @Override
    public String save() {
        // Ajoute la représentation de la ressource à l'état sauvegardé.
        return super.save() + "," + DB.ListToInt.get(resource);
    }

    /**
     * Restaure l'état de la machine à partir d'une chaîne de caractères
     * sauvegardée.
     *
     * @param s Une chaîne de caractères formatée représentant l'état sauvegardé.
     * @throws InvalidSaveFormat Si le format de la chaîne est invalide.
     */
    @Override
    public void restore(String s) throws InvalidSaveFormat {
        try {
            // Divise la chaîne de sauvegarde en un tableau d'éléments.
            String[] harvesterString = s.split(",");

            // Restaure l'inventaire et les bonus en utilisant la classe parente.
            super.restore(s);

            // Restaure la ressource actuelle en utilisant la dernière partie de la chaîne.
            this.resource = DB.intToList.get(Integer.valueOf(harvesterString[harvesterString.length - 1]));
        } catch (Exception e) {
            Value.printError(
                    "erreur lors du chargement d'un collecteur de ressource \nRessource associé : " + resource);
            // Lance une exception en cas de format invalide.
            throw new InvalidSaveFormat();
        }
    }
}
