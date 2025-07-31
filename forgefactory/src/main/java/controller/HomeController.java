package main.java.controller;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import main.java.model.Home;
import main.java.model.util.Save;
import main.java.view.GUI;
import main.java.view.HomeView;

public class HomeController {
    private final Home model; // Le modèle représentant l'écran d'accueil
    private final HomeView view; // La vue associée à l'écran d'accueil
    private final GUI frame; // Le cadre principal de l'application (GUI)

    /**
     * Constructeur de HomeController.
     *
     * @param model Le modèle représentant l'écran d'accueil
     * @param view  La vue associée à l'écran d'accueil
     * @param frame Le cadre principal de l'application
     */
    public HomeController(Home model, HomeView view, GUI frame) {
        this.model = model;
        this.view = view;
        this.frame = frame;

        // Initialisation des écouteurs d'événements
        initListeners();
    }

    /**
     * Initialise les écouteurs d'événements pour les boutons de l'écran d'accueil.
     */
    private void initListeners() {
        // Listener pour "Nouvelle partie"
        view.getNewGameButton().addActionListener(e -> {
            model.setNewGame(true); // Mise à jour du modèle (nouvelle partie)
            frame.setGame(false); // Transition vers le jeu 
        });

        // Listener pour "Continuer"
        if (Save.saveExists()) { // Vérifie si une sauvegarde existe
            view.getContinueButton().addActionListener(e -> {
                model.setNewGame(false); // Mise à jour du modèle (partie continue)
                frame.setGame(true); // Transition vers le jeu avec la sauvegarde
            });
        } else {
            view.getContinueButton().setEnabled(false); // Désactive le bouton "Continuer" si aucune sauvegarde n'existe
        }

        // Listener pour "Quitter"
        view.getQuitButton().addActionListener(e -> {
            System.exit(0); // Quitte l'application
        });

        // Listener pour "Paramètres"
        view.getSettingsButton().addActionListener(e-> {
            System.out.println("Paramètres sélectionnés.");
            JPanel panel = new JPanel() ; 
            JLabel label = new JLabel("paramètres");
            JButton button = new JButton("exit");
            label.setPreferredSize(new Dimension(700,75));
            button.addActionListener(i -> System.exit(0));
            panel.add(label);
            panel.add(button);
            panel.setMinimumSize(new Dimension(1920,1080));
            panel.setPreferredSize(new Dimension(1920,1080));
            panel.setMaximumSize(new Dimension(1920,1080));
            frame.addToLayeredPanel(panel);
            panel.repaint();
            panel.setBounds(0, 0, 1920, 1080);
            frame.revalidate();
            frame.repaint();
        });
    }
}
