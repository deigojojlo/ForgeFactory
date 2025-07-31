package main.java.model.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import main.java.controller.Timeline;
import main.java.model.Map;
import main.java.model.storage.Value;

/**
 * La classe PathFinder est responsable de la gestion de la navigation du joueur
 * à travers la carte.
 * Elle utilise un algorithme de recherche en largeur (BFS) pour trouver un
 * chemin vers une destination.
 */
public class PathFinder {

    private static Map gameMap;

    /**
     * Initialisation de la carte utilisée pour la navigation.
     * 
     * @param mapArg La carte à utiliser pour les déplacements.
     */
    public static void initialize(Map mapArg) {
        gameMap = mapArg;
    }

    /**
     * Calcule le chemin à suivre pour aller à la position spécifiée (ligne,
     * colonne) en utilisant
     * l'algorithme de recherche en largeur (BFS). Le chemin retourné est une liste
     * de positions à
     * parcourir pour atteindre la destination.
     *
     * @param targetRow La ligne de la destination.
     * @param targetCol La colonne de la destination.
     * @param startRow  La ligne de départ.
     * @param startCol  La colonne de départ.
     * @return Une liste de positions représentant le chemin vers la destination.
     */
    private static PositionWithParent findPath(int targetRow, int targetCol, int startRow, int startCol) {

        boolean[][] visited = new boolean[gameMap.getRows()][gameMap.getCols()]; // Tableau des cases déjà visitées
        Queue<PositionWithParent> queue = new LinkedList<>(); // File d'attente pour l'algorithme BFS

        // Ajout de la position de départ (destination)
        queue.add(new PositionWithParent(targetRow, targetCol, null));
        visited[targetRow][targetCol] = true;

        // Déplacements possibles (haut, bas, gauche, droite)
        int[] rowOffsets = { -1, 1, 0, 0 };
        int[] colOffsets = { 0, 0, -1, 1 };

        // Recherche en largeur (BFS)
        while (!queue.isEmpty()) {
            PositionWithParent current = queue.poll();

            // Si on atteint la position du joueur, on reconstruit le chemin
            if (current.getRow() == startRow && current.getCol() == startCol) {
                return current;
            }

            // Exploration des voisins (haut, bas, gauche, droite)
            for (int i = 0; i < 4; i++) {
                int newRow = current.getRow() + rowOffsets[i];
                int newCol = current.getCol() + colOffsets[i];

                // Si la case est valide et non visitée, on l'ajoute à la file d'attente
                if (gameMap.isAvailable(newRow, newCol) && !visited[newRow][newCol]) {
                    queue.add(new PositionWithParent(newRow, newCol, current));
                    visited[newRow][newCol] = true;
                }
            }
        }

        // Si aucun chemin n'est trouvé, retourne null
        return null;
    }

    /**
     * Vérifie si une position est accessible depuis une autre position.
     *
     * @param startRow  La ligne de départ.
     * @param startCol  La colonne de départ.
     * @param targetRow La ligne de la destination.
     * @param targetCol La colonne de la destination.
     * @return true si la position est accessible, false sinon.
     */
    public static boolean isAccessible(int startRow, int startCol, int targetRow, int targetCol) {
        return findPath(targetRow, targetCol, startRow, startCol) == null;
    }

    /**
     * Déplace le joueur vers la destination en suivant le chemin calculé.
     * Le mouvement se fait par petits pas (1/8 de seconde entre chaque mouvement).
     *
     * @param targetRow La ligne de la destination.
     * @param targetCol La colonne de la destination.
     */
    public static void moveTo(int targetRow, int targetCol) {
        Position playerPosition = Value.player.getPosition(); // Position actuelle du joueur
        ArrayList<PositionWithParent> path = reconstructPath(findPath(targetRow, targetCol, playerPosition.getRow(), playerPosition.getCol()));

        // Ajout des tâches de mouvement dans la timeline
        for (int i = 0; i < path.size(); i++) {
            int index = i;
            // Le joueur se déplace d'un pas à la fois
            Timeline.add(new Task(i / 8.0, () -> Value.player.moveTo(path.get(index).getRow(), path.get(index).getCol())));
        }
    }

    /**
     * Reconstruit le chemin à partir de la position de destination en remontant
     * les parents des positions jusqu'à la position de départ.
     * 
     * @param end La position de fin (destination).
     * @return Une liste de positions représentant le chemin de la destination au
     *         départ.
     */
    private static ArrayList<PositionWithParent> reconstructPath(PositionWithParent end) {
        ArrayList<PositionWithParent> path = new ArrayList<>();
        PositionWithParent current = end;

        // Remontée des positions parentales jusqu'à atteindre le début du chemin
        while (current != null) {
            path.add(current);
            current = current.parent;
        }

        return path;
    }

   /**
 * Vérifie si une position donnée est atteignable à partir de la position de départ.
 * 
 * Cette méthode vérifie si une cellule spécifique (donnée par les paramètres `row` et `col`)
 * peut être atteinte en partant de la position de départ définie par `gameMap.getSpawn()`.
 * L'atteignabilité est vérifiée à travers une recherche récursive des cases voisines.
 *
 * @param row La ligne de la position cible.
 * @param col La colonne de la position cible.
 * @return true si la position est atteignable, false sinon.
 * @throws AssertionError Si le tableau représentant la carte est vide.
 */
public static boolean reachable(int row, int col) {
    assert gameMap.getRepresentativeMap().length != 0;
    Position pos = gameMap.getSpawn();
    boolean[][] grid = new boolean[gameMap.getRepresentativeMap().length][gameMap.getRepresentativeMap()[0].length];
    return reachableBis(pos.getRow(), pos.getCol(), row, col, grid);
}

    /**
     * Fonction récursive qui vérifie si une position est atteignable à partir d'une position de départ.
     * 
     * Cette méthode utilise la récursion pour explorer les cases voisines et déterminer si la position cible
     * peut être atteinte. Elle vérifie les cases dans les quatre directions (haut, bas, gauche, droite) à chaque
     * étape de la recherche.
     *
     * @param rowa La ligne de la position actuelle.
     * @param cola La colonne de la position actuelle.
     * @param rowx La ligne de la position cible.
     * @param colx La colonne de la position cible.
     * @param grid Le tableau de cases déjà explorées, pour éviter les boucles infinies.
     * @return true si la position cible est atteignable, false sinon.
     */
    private static boolean reachableBis(int rowa, int cola, int rowx, int colx, boolean[][] grid) {
        int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        // Condition d'arrêt : vérifie si la position actuelle est adjacent à la position cible
        if ((Math.abs(rowa - rowx) <= 1 && cola == colx) || (Math.abs(cola - colx) <= 1 && rowa == rowx)) {
            return true;
        }

        grid[rowa][cola] = true; // Marque la position actuelle comme explorée

        // Explore les cases voisines dans les quatre directions possibles
        for (int[] direction : DIRECTIONS) {
            int newRowa = rowa + direction[0];
            int newCola = cola + direction[1];
            if (isValid(grid, newRowa, newCola)) {
                if (reachableBis(newRowa, newCola, rowx, colx, grid)) {
                    return true; // Si une direction mène à la cible, retourne true
                }
            }
        }
        return false; // Retourne false si aucune direction ne mène à la cible
    }

    /**
     * Vérifie si une position donnée est valide.
     * 
     * Cette méthode détermine si une position (ligne, colonne) est à l'intérieur des limites de la carte
     * et si la case correspondante est libre (non occupée) et non déjà explorée.
     *
     * @param grid Le tableau des positions explorées.
     * @param row La ligne de la position à vérifier.
     * @param col La colonne de la position à vérifier.
     * @return true si la position est valide (dans les limites, libre et non explorée), false sinon.
     */
    private static boolean isValid(boolean[][] grid, int row, int col) {
        return gameMap.isInBound(row, col) && 
            gameMap.getRepresentativeMap()[row][col] == null && 
            !grid[row][col]; // La case est dans les limites, libre et non explorée
    }

    /**
     * Classe interne représentant une position sur la carte avec une référence à
     * son parent.
     * Cela permet de reconstruire facilement le chemin une fois la destination
     * atteinte.
     */
    private static class PositionWithParent extends Position {
        public PositionWithParent parent;

        /**
         * Constructeur de la classe Position.
         *
         * @param row    La ligne de la position.
         * @param col    La colonne de la position.
         * @param parent La position parent (qui a permis d'atteindre cette position).
         */
        PositionWithParent(int row, int col, PositionWithParent parent) {
            super(row, col);
            this.parent = parent;
        }
    }
}
