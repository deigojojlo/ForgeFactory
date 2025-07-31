package main.java.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Classe HomeView
 *
 * <p>
 * Cette classe représente l'écran d'accueil d'une application ou d'un jeu.
 * Elle propose des boutons pour lancer une nouvelle partie, continuer, accéder
 * aux paramètres ou quitter l'application.
 * </p>
 */
public class HomeView extends JPanel {

    // Boutons de l'interface
    private final JButton newGameButton;
    private final JButton continueButton;
    private final JButton settingsButton;
    private final JButton quitButton;

    /**
     * Constructeur de la classe HomeView.
     *
     * <p>
     * Initialise l'écran d'accueil avec une disposition en grille contenant
     * quatre boutons : "Nouvelle partie", "Continuer", "Paramètres" et "Quitter".
     * </p>
     */
    public HomeView() {
        // Panneau principal avec une disposition en grille (2 lignes, 2 colonnes)
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10)); // Espacement de 10px entre les boutons

        // Initialisation des boutons
        newGameButton = new JButton("Nouvelle partie");
        continueButton = new JButton("Continuer");
        settingsButton = new JButton("Paramètres");
        quitButton = new JButton("Quitter");

        // Ajout des boutons au panneau
        panel.add(newGameButton);
        panel.add(continueButton);
        panel.add(settingsButton);
        panel.add(quitButton);

        // Personnalisation de l'apparence des boutons
        styleButton(newGameButton, new Color(45, 140, 240), new Color(30, 120, 220), Color.WHITE);
        styleButton(continueButton, Color.GRAY, Color.DARK_GRAY, Color.WHITE);
        styleButton(settingsButton, Color.GRAY, Color.DARK_GRAY, Color.WHITE);
        styleButton(quitButton, Color.RED, Color.RED, Color.WHITE);

        // Définir la taille par défaut pour tous les boutons
        Dimension buttonSize = new Dimension(200, 100);
        setButtonSize(newGameButton, buttonSize);
        setButtonSize(continueButton, buttonSize);
        setButtonSize(settingsButton, buttonSize);
        setButtonSize(quitButton, buttonSize);

        // Ajout du panneau au composant principal
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    /**
     * Applique un style cohérent à un bouton.
     *
     * @param button      Le bouton à styliser.
     * @param bgColor     La couleur de fond.
     * @param borderColor La couleur de la bordure.
     * @param fgColor     La couleur du texte.
     */
    private void styleButton(JButton button, Color bgColor, Color borderColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false); // Désactive l'indicateur de focus
        button.setBorder(BorderFactory.createLineBorder(borderColor, 2)); // Ajoute une bordure colorée
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change le curseur pour un curseur "main"
    }

    /**
     * Définit la taille d'un bouton.
     *
     * @param button Le bouton à redimensionner.
     * @param size   La dimension souhaitée.
     */
    private void setButtonSize(JButton button, Dimension size) {
        button.setPreferredSize(size);
    }

    // Getters pour permettre au contrôleur d'accéder aux boutons
    public JButton getNewGameButton() {
        return newGameButton;
    }

    public JButton getContinueButton() {
        return continueButton;
    }

    public JButton getSettingsButton() {
        return settingsButton;
    }

    public JButton getQuitButton() {
        return quitButton;
    }
}
