package main.java.view;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit ;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import main.java.controller.GameController;
import main.java.controller.HomeController;
import main.java.model.Home;
import main.java.model.storage.Value;

/**
 * La classe GUI représente la fenêtre principale de l'application avec gestion
 * des vues et des superpositions.
 */
public class GUI extends JFrame {
    public boolean isOverlayOn; // Indicateur pour savoir si le panneau superposé est affiché
    private final CardLayout cardLayout; // Layout pour gérer les différentes vues de l'application
    private final JPanel mainPanel; // Panneau principal qui contient les différentes vues
    private JPanel overlayPanel; // Panneau superposé pour afficher des informations supplémentaires
    private final JLayeredPane layeredPane; // Panneau à couches pour gérer les éléments superposés
    private int layer = 0; // Indice de la couche pour ajouter des composants au JLayeredPane
    private GameController gameController; // Contrôleur du jeu

    /**
     * Constructeur de la classe GUI.
     * Initialise la fenêtre et configure l'affichage.
     */
    public GUI() {
        super("Forge Factory"); // Titre de la fenêtre
        Value.frame = this; // Lien avec la fenêtre principale
        cardLayout = new CardLayout(); // Initialisation du CardLayout
        mainPanel = new JPanel(cardLayout); // Initialisation du panneau principal avec le CardLayout

        // Configuration de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Action de fermeture
        setLocationRelativeTo(null); // Centrer la fenêtre
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiser la fenêtre
        setResizable(false); // Empêcher le redimensionnement de la fenêtre
        setUndecorated(true); // Supprimer la barre de titre de la fenêtre

        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("Taille de l'écran : " + screenSize);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
         // Vérifier si le plein écran est supporté
        if (gd.isFullScreenSupported()) {
            // Le mode plein écran est supporté, donc on passe la fenêtre en plein écran
            gd.setFullScreenWindow(this);
        } else {
            // Le mode plein écran n'est pas supporté, utiliser une autre méthode (par exemple, maximiser)
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            System.out.println("Le plein écran n'est pas supporté sur cet appareil.");
        }

        // Créer le panneau JLayeredPane
        layeredPane = new JLayeredPane();
        setSize(screenSize);
        System.out.println("Using fixed screen size: " + screenSize);
        layeredPane.setPreferredSize(screenSize);
        layeredPane.setBounds(0, 0, screenSize.width, screenSize.height);
        add(layeredPane);

        // Ajouter le panneau principal au JLayeredPane
        mainPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight()); // Définir la position et la taille
                                                                                    // du panneau principal
        layeredPane.add(mainPanel, Integer.valueOf(layer++)); // Ajouter le panneau principal à la couche 0

        // Initialiser et ajouter le panneau superposé
        initOverlayPanel();
        layeredPane.add(overlayPanel, Integer.valueOf(layer++)); // Ajouter le panneau superposé à la couche suivante

        // Ajouter les vues au CardLayout
        HomeView homeView = new HomeView(); // Créer la vue d'accueil
        Home homeModel = new Home(); // Créer le modèle d'accueil
        new HomeController(homeModel, homeView, this); // Créer le contrôleur d'accueil
        mainPanel.add(homeView, "Home"); // Ajouter la vue d'accueil au CardLayout

        setVisible(true); // Rendre la fenêtre visible
        revalidate();
        repaint();
    }

    /**
     * Méthode pour ajouter un composant au JLayeredPane
     * 
     * @param a Composant à ajouter
     */
    public void addToLayeredPanel(Component a) {
        layeredPane.add(a, Integer.valueOf(layer++)); // Ajouter un composant au JLayeredPane à une nouvelle couche
    }

    /**
     * Méthode pour retirer un composant du JLayeredPane
     * 
     * @param a Composant à retirer
     */
    public void removeOfLayeredPanel(Component a) {
        layeredPane.remove(a); // Retirer un composant du JLayeredPane
    }

    /**
     * Initialisation du panneau superposé (overlay).
     * Il sera utilisé pour afficher des informations supplémentaires au-dessus de
     * la vue principale.
     */
    private void initOverlayPanel() {
        overlayPanel = new JPanel(); // Créer un nouveau panneau pour le superposé
        overlayPanel.setOpaque(false); // Rendre le panneau transparent
        overlayPanel.setSize(layeredPane.getWidth() / 2, layeredPane.getHeight() / 2); // Définir la taille du panneau
                                                                                       // superposé

        // Positionner le panneau superposé au centre de l'écran
        int x = (layeredPane.getWidth() - overlayPanel.getWidth()) / 2;
        int y = (layeredPane.getHeight() - overlayPanel.getHeight()) / 2;
        overlayPanel.setBounds(x, y, overlayPanel.getWidth(), overlayPanel.getHeight()); // Définir la position du
                                                                                         // panneau superposé

        // Le panneau superposé est invisible par défaut
        overlayPanel.setVisible(false); // Rendre le panneau superposé invisible au départ
    }

    /**
     * Méthode pour basculer vers la vue du jeu.
     * 
     * @param b Booléen qui détermine l'état du jeu
     */
    public void setGame(boolean b) {
        gameController = new GameController(this, b); // Créer un contrôleur de jeu
        mainPanel.add(gameController.getView(), "Game"); // Ajouter la vue du jeu au CardLayout
        cardLayout.show(mainPanel, "Game"); // Afficher la vue du jeu
        gameController.getView().requestFocus(); // Demander le focus sur la vue du jeu
    }

    /**
     * Méthode pour afficher ou masquer le panneau superposé avec l'interface
     * utilisateur des machines, inventaire et craft
     * 
     * @param jp Le composant à afficher dans le panneau superposé
     */
    public void showOverlayPanel(Component jp) {
        isOverlayOn = true; // Indiquer que le panneau superposé est actif
        overlayPanel.removeAll(); // Retirer tous les composants du panneau superposé

        overlayPanel.setPreferredSize(jp.getPreferredSize()); // Définir la taille du panneau superposé en fonction du
                                                              // composant
        overlayPanel.setVisible(false); // Rendre le panneau superposé invisible avant l'ajout
        overlayPanel.setVisible(true); // Rendre le panneau superposé visible
        overlayPanel.add(jp); // Ajouter le composant au panneau superposé
        overlayPanel.repaint(); // Repeindre le panneau pour appliquer les changements
    }

    /**
     * Méthode pour cacher le panneau superposé
     */
    public void hideOverlayPanel() {
        isOverlayOn = false; // Indiquer que le panneau superposé n'est plus actif
        overlayPanel.setVisible(false); // Rendre le panneau superposé invisible
        overlayPanel.repaint(); // Repeindre le panneau pour appliquer les changements
    }

    /**
     * Méthode principale pour lancer l'application
     * 
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new GUI() // Créer une instance de la classe GUI
    );
    }
}
