package main.java.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Function;
import main.java.controller.FactoryController;
import main.java.controller.HarvesterController;
import main.java.controller.MarketController;
import main.java.model.Exception.InvalidSaveFormat;
import main.java.model.Interface.Clickable;
import main.java.model.Interface.Savable;
import main.java.model.storage.DB;
import main.java.model.storage.Value;
import main.java.model.util.PathFinder;
import main.java.model.util.Position;
import main.java.model.util.Save;
import main.java.view.GUI;

/**
 * La classe {@code Map} représente la carte du jeu, y compris les ressources, les interactions avec le joueur,
 * et les structures comme les points de spawn, les marchés, les usines et les récolteurs.
 * Elle gère la disposition de la zone de jeu et prend en charge la sauvegarde et la restauration de l'état.
 */
public class Map implements Savable {

    /** La matrice représentant les ressources et éléments sur la carte. */
    private Map.ResourceList[][] representativeMap;

    /** Une map associant des positions à des éléments cliquables sur la carte. */
    private HashMap<Position, Clickable> clickableMap;

    /** Le nombre de lignes de la carte. */
    private int rows;

    /** Le nombre de colonnes de la carte. */
    private int cols;

    /** Un multiplicateur pour ajuster la quantité de ressources placées sur la carte. */
    private int resourcesMultiplier;

    /** Le nombre fixe de types de ressources disponibles sur la carte. */
    private final int resourcesCount = 5;

    /** Le joueur associé à cette carte. */
    private final Player player;

    /** Un générateur de nombres aléatoires pour la randomisation des positions et événements. */
    private final Random rand = new Random();

    /** Indicateur indiquant si une machine est en cours de placement sur la carte. */
    private boolean isPlacing = false;

    /** La machine actuellement en cours de placement sur la carte. */
    private Machine toPlace = null;

    /** La position de spawn du joueur sur la carte. */
    private Position spawnPosition;

    /** La position du marché sur la carte. */
    private Position marketPosition;

    /** Le cadre de l'interface utilisateur associé à cette carte. */
    private final GUI frame;

    /**
     * Constructeur de la classe {@code Map} avec les paramètres spécifiés pour le cadre GUI, le joueur, 
     * le nombre de lignes et de colonnes.
     *
     * @param frame  Le cadre de l'interface utilisateur associé à cette carte.
     * @param player Le joueur associé à cette carte.
     * @param rows   Le nombre de lignes de la carte.
     * @param cols   Le nombre de colonnes de la carte.
     */
    public Map(GUI frame, Player player, int rows, int cols) {
        this.frame = frame;
        this.rows = rows;
        this.cols = cols;
        this.player = player;

        representativeMap = new Map.ResourceList[rows][cols];
        resourcesMultiplier = Math.max(2, ((int) Math.floor(Math.sqrt(rows * cols)) / 3)); // pour generation instanttané ne pas dépasser 79
        clickableMap = new HashMap<>();

        PathFinder.initialize(this);
        selectBuilding();
    }

    /**
     * Constructeur de la classe {@code Map} avec les paramètres spécifiés pour le cadre GUI et le joueur,
     * en restaurant l'état à partir d'un fichier de sauvegarde.
     *
     * @param frame  Le cadre de l'interface utilisateur associé à cette carte.
     * @param player Le joueur associé à cette carte.
     */
    public Map(GUI frame, Player player) {
        this.frame = frame;
        this.player = player;
        Save.restore(this);
        rows = representativeMap.length;
        if (rows != 0)
            cols = representativeMap[0].length;
        PathFinder.initialize(this);
    }

    /**
     * Récupère le joueur associé à cette carte.
     *
     * @return L'objet joueur associé à la carte.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Place les bâtiments et les ressources sur la carte à des positions valides et aléatoires.
     * 
     * @return Une liste des positions où les ressources ont été placées.
     */
    private LinkedList<Position> placeBuilding() {
        LinkedList<Position> placedPosition = new LinkedList<>();
        // Place le point de spawn
        spawnPosition = new Position(rand.nextInt(rows), rand.nextInt(cols));
        representativeMap[spawnPosition.getRow()][spawnPosition.getCol()] = Map.ResourceList.SPAWN;

        // Place le marché
        do {
            if (rand.nextInt(2) == 1) {
                int x = rand.nextInt(rows);
                int y = x == 0 || x == rows - 1 ? rand.nextInt(cols) : rand.nextInt(2) == 0 ? 0 : cols - 1;
                marketPosition = new Position(x, y);
            } else {
                int y = rand.nextInt(cols);
                int x = y == 0 || y == cols - 1 ? rand.nextInt(rows) : rand.nextInt(2) == 0 ? 0 : rows - 1;
                marketPosition = new Position(x, y);
            }
        } while (marketPosition.getCol() == spawnPosition.getCol() && marketPosition.getRow() == spawnPosition.getRow());

        representativeMap[marketPosition.getRow()][marketPosition.getCol()] = Map.ResourceList.MARKET;
        clickableMap.put(new Position(marketPosition.getRow(), marketPosition.getCol()), new MarketController(player));

        // Place les ressources
        for (int i = 0; i < resourcesCount * resourcesMultiplier; i++) {
            boolean placed = false;
            while (!placed) {
                int col = rand.nextInt(cols);
                int row = rand.nextInt(rows);
                if (representativeMap[row][col] == null) {
                    if (!reachable(row, col)) {
                        continue;
                    } else {
                        representativeMap[row][col] = Map.ResourceList.NULL;
                        if (otherAccessible(placedPosition)) {
                            placed = true;
                            placedPosition.add(new Position(row, col));
                        } else {
                            representativeMap[row][col] = null;
                        }
                    }
                }
            }
        }
        return placedPosition;
    }

    /**
     * Sélectionne et place les bâtiments et ressources sur la carte.
     * Cette méthode place aléatoirement des ressources (autres que les machines) sur les positions
     * valides de la carte, puis remplit la carte avec des cases représentant de l'herbe (NULL).
     * Elle appelle la méthode {@link #placeBuilding()} pour initialiser les positions des bâtiments.
     */
    private void selectBuilding() {
        LinkedList<Position> placedPosition = placeBuilding();
        for (Map.ResourceList r : Map.ResourceList.values()) {
            for (int i = 0; i < resourcesMultiplier; i++) {
                if (r != Map.ResourceList.NULL && r != Map.ResourceList.MARKET && r != Map.ResourceList.SPAWN && r != Map.ResourceList.FACTORY
                        && r != Map.ResourceList.HARVESTER) {
                    int j = rand.nextInt(placedPosition.size());
                    representativeMap[placedPosition.get(j).getRow()][placedPosition.get(j).getCol()] = r;
                    Position pos = new Position(placedPosition.get(j).getRow(), placedPosition.get(j).getCol());
                    clickableMap.put(pos, new Resources(player, r, pos));
                    placedPosition.remove(j);
                }
            }
        }
    
        // Remplissage de la carte avec la valeur NULL pour les cases vides (herbe)
        for (ResourceList[] is : representativeMap) {
            for (int i = 0; i < is.length; i++) {
                if (is[i] == null)
                    is[i] = ResourceList.NULL;
            }
        }
    }

    /**
     * Effectue l'action associée à un clic sur la case spécifiée.
     * Si la case est vide et n'est pas le point de spawn, une machine ou un déplacement est effectué.
     * Si la case contient un élément cliquable (ressource, machine, etc.), l'action liée à cet élément est exécutée.
     *
     * @param row La ligne de la case cliquée.
     * @param col La colonne de la case cliquée.
     */
    public void action(int row, int col) {
        Clickable c = clickableMap.get(new Position(row, col));
        // Si aucun élément cliquable n'est présent et que la case n'est pas le point de spawn
        if (c == null && representativeMap[row][col] == ResourceList.NULL && (player.getRow() != row || player.getCol() != col)) {
            placeOrMove(row, col);
        } else if (!(Math.abs(player.getRow() - row) <= 1 // Si on n'est pas dans un rayon de 1 autour du joueur
                && Math.abs(player.getCol() - col) <= 1)) {
        } else if (c != null) { // Éviter les NullPointerException
            c.action(this.frame);
        }
    }

    /**
     * Effectue l'action appropriée en fonction de l'état de placement ou de mouvement du joueur.
     * Si une machine est en cours de placement, celle-ci est placée à la position spécifiée,
     * sinon, le joueur tente de se déplacer vers la position cible.
     *
     * @param row La ligne de la case cible.
     * @param col La colonne de la case cible.
     */
    private void placeOrMove(int row, int col) {
        if (isPlacing) {
            place(row, col);
        } else if (player.canMove()) {
            PathFinder.moveTo(row, col);
        }
    }

    /**
     * Place une machine à la position spécifiée.
     * Si la machine à placer est un récolteur, elle est placée uniquement si des ressources sont présentes autour de la position.
     *
     * @param row La ligne de la case où placer la machine.
     * @param col La colonne de la case où placer la machine.
     */
    private void place(int row, int col) {
        if (toPlace instanceof Harvester) {
            LinkedList<ResourceList> resourceArround = getRessourceArround(row, col);
            if (!resourceArround.isEmpty()) {
                placeMachineAfterPosSelection(row, col);
            }
        } else {
            placeMachineAfterPosSelection(row, col);
        }
    }

    /**
     * Récupère les ressources présentes autour de la position spécifiée.
     * Les ressources autour de la case sont retournées si elles font partie des 5 premiers types de ressources.
     *
     * @param row La ligne de la position cible.
     * @param col La colonne de la position cible.
     * @return Une liste des ressources autour de la position spécifiée.
     */
    private LinkedList<ResourceList> getRessourceArround(int row, int col) {
        int[][] possible = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        LinkedList<ResourceList> result = new LinkedList<>();
        for (int[] pos : possible) {
            if (isInBound(row + pos[0], col + pos[1])
                    && representativeMap[row + pos[0]][col + pos[1]].ordinal() <= 4) { // Les 5 premières ressources sont considérées
                result.add(representativeMap[row + pos[0]][col + pos[1]]);
            }
        }
        return result;
    }

    /**
     * Lance le processus de placement d'une nouvelle machine.
     * La machine à placer est définie par l'argument {@code m}.
     *
     * @param m La machine à placer sur la carte.
     */
    public void placeNewMachine(Machine m) {
        isPlacing = true;
        toPlace = m;
    }

    /**
     * Place la machine à la position spécifiée après avoir sélectionné la position.
     * Selon le type de machine, le contrôleur approprié est créé et ajouté à la carte.
     *
     * @param row La ligne de la position où placer la machine.
     * @param col La colonne de la position où placer la machine.
     */
    private void placeMachineAfterPosSelection(int row, int col) {
        Position pos = new Position(row, col);
        Clickable mc;
        if (toPlace instanceof Factory ) {
            representativeMap[row][col] = ResourceList.FACTORY;
            mc = new FactoryController(
                    ((Factory)toPlace),
                    player.getInventory(), player.getWallet());
        } else {
            representativeMap[row][col] = ResourceList.HARVESTER;
            LinkedList<ResourceList> resourceArround = getRessourceArround(row, col);
            toPlace = new Harvester(resourceArround.get(0), toPlace.getCountBonus());
            mc = new HarvesterController(
                    (Harvester) toPlace,
                    player.getInventory(), player.getWallet(),
                    resourceArround.toArray(ResourceList[]::new));
        }
        clickableMap.put(pos, mc);
    
        Value.game.addMachine(row, col, toPlace);
        toPlace = null;
        isPlacing = false;
    
        clickableMap.get(pos).action(this.frame);
    }

    /**
     * Vérifie si une ressource peut être atteinte depuis la position de spawn.
     * Cette méthode utilise le Pathfinder pour vérifier l'accessibilité de la case spécifiée.
     *
     * @param row La ligne de la ressource à vérifier.
     * @param col La colonne de la ressource à vérifier.
     * @return {@code true} si la ressource est accessible depuis le spawn, sinon {@code false}.
     */
    private boolean reachable(int row, int col) {
        return PathFinder.reachable(row, col);
    }

        /**
     * Vérifie si toutes les positions placées sont accessibles depuis le point de spawn.
     * La méthode retourne {@code false} dès qu'une position est inaccessible.
     * Ensuite, elle vérifie si le marché est accessible.
     *
     * @param placedPosition La liste des positions placées à vérifier.
     * @return {@code true} si toutes les positions sont accessibles, sinon {@code false}.
     */
    private boolean otherAccessible(LinkedList<Position> placedPosition) {
        for (Position position : placedPosition) {
            if (!reachable(position.getRow(), position.getCol()))
                return false;
        }
        return reachable(marketPosition.getRow(), marketPosition.getCol());
    }

    // Section de vérification des coordonnées

    /**
     * Vérifie si la case à la position spécifiée est disponible pour être utilisée.
     * La case est considérée comme disponible si elle est dans les limites de la carte
     * et si elle est vide (représentée par Map.ResourceList.NULL) ou s'il s'agit du point de spawn.
     *
     * @param row La ligne de la case à vérifier.
     * @param col La colonne de la case à vérifier.
     * @return {@code true} si la case est disponible, sinon {@code false}.
     */
    public boolean isAvailable(int row, int col) {
        return isInBound(row, col)
                && (representativeMap[row][col] == Map.ResourceList.NULL || representativeMap[row][col] == Map.ResourceList.SPAWN);
    }

    /**
     * Vérifie si les coordonnées spécifiées sont dans les limites de la carte.
     *
     * @param row La ligne à tester.
     * @param col La colonne à tester.
     * @return {@code true} si les coordonnées sont dans les limites de la carte, sinon {@code false}.
     */
    public boolean isInBound(int row, int col) {
        return row >= 0 && col >= 0 && row < rows && col < cols;
    }

    /**
     * Retourne la position du point de spawn sur la carte.
     *
     * @return La position du point de spawn.
     */
    public Position getSpawn() {
        return spawnPosition;
    }

    // Section des getters

    /**
     * Retourne le nombre de lignes de la carte.
     *
     * @return Le nombre de lignes.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Retourne le nombre de colonnes de la carte.
     *
     * @return Le nombre de colonnes.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Retourne la carte des ressources représentée par un tableau de {@link Map.ResourceList}.
     *
     * @return La carte représentative des ressources.
     */
    public Map.ResourceList[][] getRepresentativeMap() {
        return this.representativeMap;
    }

    /**
     * Retourne la liste des clickables
     *
     * @return Lla list des clickables.
     */
    public HashMap<Position, Clickable> getClickableMap(){
        return this.clickableMap;
    }

    /**
     * Indique si l'on est en train de placer une machine.
     *
     * @return {@code true} si une machine est en cours de placement, sinon {@code false}.
     */
    public boolean getIsPlacing() {
        return isPlacing;
    }

    /**
     * Retourne la machine que l'on est en train de placer.
     *
     * @return La machine à placer.
     */
    public Machine getToPlace() {
        return toPlace;
    }

    /**
     * Retourne la position du marché sous forme de tableau de deux entiers représentant la ligne et la colonne.
     *
     * @return Le tableau contenant la ligne et la colonne du marché.
     */
    public Position getMarket() {
        return marketPosition ;
    }

    /**
     * Retourne la représentation en chaîne de caractères de la carte des ressources.
     * Chaque case de la carte est représentée par son nom ou par "0" si elle est vide.
     *
     * @return La chaîne de caractères représentant la carte.
     */
    @Override
    public String toString() {
        String r = "";
        for (Map.ResourceList[] l : representativeMap) {
            for (Map.ResourceList c : l) {
                r += (c == null) ? "0 " : c + " ";
            }
            r += "\n";
        }
        return r;
    }

    /* Section de sauvegarde */

    /**
     * Sauvegarde l'état actuel de la carte sous forme de chaîne de caractères.
     * La carte est convertie en une représentation sous forme de chaînes d'entiers séparées par des virgules,
     * chaque ligne étant séparée par un "/".
     *
     * @return La chaîne représentant la carte à sauvegarder.
     */
    @Override
    public String save() {
        String r = "";
        for (int j = 0; j < representativeMap.length; j++) {
            ResourceList[] l = representativeMap[j];
            for (int i = 0; i < l.length - 1; i++) {
                r += DB.ListToInt.get(l[i]) + ",";
            }
            r += DB.ListToInt.get(l[l.length - 1]);
            if (j != representativeMap.length - 1)
                ;
            r += "/";
        }
        return r;
    }

    /* Section de restauration */

    /**
     * Restaure l'état de la carte à partir d'une chaîne représentant l'état sauvegardé.
     * La chaîne est décodée pour reconstruire la carte, les ressources et les éléments interactifs.
     *
     * @param s La chaîne représentant l'état sauvegardé de la carte.
     */
    @Override
    public void restore(String s) {
        String[] demiTableau = s.split("/");
        String[][] tableau = new String[demiTableau.length][Save.countOccurrences(',', demiTableau[0])];
        for (int i = 0; i < demiTableau.length; i++) {
            tableau[i] = demiTableau[i].split(",");
        }
        Map.ResourceList[][] rm = new Map.ResourceList[tableau.length][tableau[0].length];

        Function<String, Integer> stringToInt = (String e) -> {
            try {
                return Integer.valueOf(e);
            } catch (NumberFormatException ex) {
                return null;
            }
        };

        this.clickableMap = new HashMap<>();
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                rm[i][j] = DB.intToList
                        .get(stringToInt.apply(tableau[i][j]));
                if (rm[i][j] == ResourceList.NULL) {

                } else if (rm[i][j] == ResourceList.SPAWN) {
                    this.spawnPosition = new Position(i, j);
                } else if (rm[i][j] != ResourceList.MARKET && rm[i][j] != ResourceList.FACTORY && rm[i][j] != ResourceList.HARVESTER) {
                    Position pos = new Position(i, j);
                    clickableMap.put(pos, new Resources(player, rm[i][j], pos));
                } else if (rm[i][j] == ResourceList.MARKET)
                    marketPosition =  new Position(i, j) ;
                    clickableMap.put(marketPosition, new MarketController(this.player));
            }
        }
        representativeMap = rm;

        this.rows = representativeMap.length;
        this.cols = representativeMap[0].length;
    }

    /**
     * Restaure une usine à partir d'une chaîne représentant son état sauvegardé.
     * Cette méthode restaure la position, l'inventaire, les améliorations et les spécificités de l'usine.
     *
     * @param s La chaîne représentant l'état sauvegardé de l'usine.
     * @throws InvalidSaveFormat Si le format de sauvegarde est invalide.
     */
    public void restoreFactory(String s) throws InvalidSaveFormat {
        String[] factoryString = s.split(",");
        String[] position = factoryString[0].split(":");
        Position pos = new Position(Integer.parseInt(position[0]), Integer.parseInt(position[1]));
        Factory factory = new Factory();
        factory.restore(s);
        Clickable mc = new FactoryController(factory,
                player.getInventory(), player.getWallet());
        clickableMap.put(pos, mc);
        representativeMap[pos.getRow()][pos.getCol()] = ResourceList.FACTORY;
    }

    /**
     * Restaure un récolteur à partir d'une chaîne représentant son état sauvegardé.
     * Cette méthode restaure la position, les ressources autour et les caractéristiques du récolteur.
     *
     * @param s La chaîne représentant l'état sauvegardé du récolteur.
     * @throws InvalidSaveFormat Si le format de sauvegarde est invalide.
     */
    public void restoreHarvester(String s) throws InvalidSaveFormat {
        String[] factoryString = s.split(",");
        String[] position = factoryString[0].split(":");
        Position pos = new Position(Integer.parseInt(position[0]), Integer.parseInt(position[1]));
        ResourceList resource = DB.intToList.get(Integer.valueOf(factoryString[factoryString.length - 1]));
        Harvester harvester = new Harvester(resource);
        harvester.restore(s);

        LinkedList<ResourceList> resourceArround = getRessourceArround(pos.getRow(), pos.getCol());
        Clickable mc = new HarvesterController(harvester,
                player.getInventory(), player.getWallet(),
                resourceArround.toArray(ResourceList[]::new));
        clickableMap.put(pos, mc);
        representativeMap[pos.getRow()][pos.getCol()] = ResourceList.HARVESTER;
    }

    /**
     * Liste des types de ressources disponibles sur la carte.
     */
    public enum ResourceList {
        APPLE, WOOD, STEEL, STONE, BERRIES, FACTORY, HARVESTER, NULL, MARKET, SPAWN
    }
}

