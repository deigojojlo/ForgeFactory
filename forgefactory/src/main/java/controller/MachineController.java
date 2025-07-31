package main.java.controller;

import main.java.model.Enum.BonusMachine;
import main.java.model.Exception.NotEnoughMoney;
import main.java.model.Exception.NotFound;
import main.java.model.Interface.Clickable;
import main.java.model.Inventory;
import main.java.model.Machine;
import main.java.model.Money;
import main.java.model.storage.Value;
import main.java.model.util.Bonus;
import main.java.view.GUI;
import main.java.view.MachineView;
import main.java.view.OverlayPanel;

public abstract class MachineController implements Clickable {
    private final MachineView view;
    private final Machine model;
    private final Money wallet;
    private final Inventory playerInventory;

    private String description;

    /**
     * Constructeur du contrôleur pour gérer l'interaction entre la vue, le modèle
     * et l'inventaire.
     *
     * @param view            La vue associée à la machine.
     * @param model           Le modèle de la machine.
     * @param playerInventory L'inventaire du joueur.
     * @param wallet          Le porte-monnaie du joueur.
     */
    protected MachineController(MachineView view, Machine model, Inventory playerInventory, Money wallet) {
        this.view = view;
        this.model = model;
        this.wallet = wallet;
        this.playerInventory = playerInventory;

        // Création de la vue de l'inventaire et du panneau d'actions
        view.createInventoryView();
        view.createActionPane();

        // Initialisation des gestionnaires pour l'inventaire et les améliorations
        setInventoryHandler();
        setUpgradeHandler();

        // Initialisation des gestionnaires pour la reparation des machines fragiles
        this.view.getReparation().addActionListener(a -> {
            this.model.setBreaked(false);
            this.view.removeReparation();
            notifyView();
        });

        // Après restoration
        if (this.model.getBreaked())
            this.view.addReparation();

        // Notifie la vue de l'état initial
        notifyView();

        // Applique les bonus associés au modèle
        applyBonus();
    }

    /**
     * Retourne la vue associée à ce contrôleur.
     *
     * @return La vue de la machine.
     */
    protected MachineView getView() {
        return this.view;
    }

    /**
     * Retourne le modèle de la machine associé à ce contrôleur.
     *
     * @return Le modèle de la machine.
     */
    public Machine getModel() {
        return this.model;
    }

    /**
     * Définit une description pour la vue et notifie la vue des changements.
     *
     * @param description La description à afficher dans la vue.
     */
    protected void setDescription(String description) {
        this.description = description;
        notifyView();
    }

    /**
     * Applique les bonus au modèle de la machine en utilisant la classe Bonus.
     */
    protected void applyBonus() {
        Bonus.applyBonus(this.model);
    }

    /**
     * Initialise les gestionnaires d'événements pour les actions liées à
     * l'inventaire.
     * Lorsque l'utilisateur déplace des objets entre l'inventaire du joueur et
     * celui de la machine.
     */
    private void setInventoryHandler() {
        // Pour chaque élément compacté dans la vue, ajoute des listeners pour gérer les
        // actions
        this.view.getInventoryViewCompactors().forEach(e -> {
            // Listener pour récupérer un objet de la machine
            e.getGet().addActionListener(a -> {
                try {
                    model.getInventory().removeItem(e.getItem(), 1); // Retirer l'objet de la machine
                    playerInventory.addItem(e.getItem(), 1); // Ajouter l'objet à l'inventaire du joueur
                    notifyView();
                } catch (NotFound exception) {
                    // Exception ignorée si l'objet n'est pas trouvé
                } finally {
                    notifyView();
                }
            });

            // Listener pour déposer un objet dans la machine
            e.getPut().addActionListener(a -> {
                try {
                    if (model.getInventory().getCurrentCount() == model.getInventory().getCapacity()) // Si l'inventaire
                                                                                                      // est
                        // plein
                        return;
                    playerInventory.removeItem(e.getItem(), 1); // Retirer l'objet de l'inventaire du joueur
                    model.getInventory().addItem(e.getItem(), 1); // Ajouter l'objet à la machine
                } catch (NotFound exception) {
                    // Exception ignorée si l'objet n'est pas trouvé
                } finally {
                    notifyView();
                }
            });
        });
    }

    /**
     * Initialise les gestionnaires d'événements pour les actions d'amélioration de
     * la machine.
     * Chaque bouton d'amélioration est associé à un listener qui tente d'acheter et
     * d'appliquer l'amélioration.
     */
    private void setUpgradeHandler() {
        // Listener pour réparer la machine
        this.view.getRepaireButton().addActionListener(a -> {
            if (model.getDurability() == model.getMaxDurability()) // Ne pas payer pour rien
                return;
            try {
                this.wallet.removeAmount(150); // Retirer l'argent du portefeuille
                this.model.setDurability(this.model.getMaxDurability()); // Réparer la machine
                this.notifyView();
            } catch (NotEnoughMoney exception) {
                Value.printError("Not Enough Money"); // Afficher une erreur si l'argent est insuffisant
            }
        });

        // Listener pour augmenter la vitesse de la machine
        this.view.getSpeedButton().addActionListener(a -> {
            if (this.model.getNumberOf(BonusMachine.SPEED) >= Value.maxSpeed)
                return;
            try {
                this.wallet.removeAmount(150);
                this.model.extraSpeed(0.1); // Augmenter la vitesse de la machine
                this.model.addcountBonus(BonusMachine.SPEED); // Ajouter le bonus
                this.notifyView();
            } catch (NotEnoughMoney exception) {
                Value.printError("Not Enough Money");
            }
        });

        // Listener pour augmenter la taille de la machine
        this.view.getSizeButton().addActionListener(a -> {
            if (this.model.getNumberOf(BonusMachine.XL) >= Value.maxXL)
                return;
            try {
                this.wallet.removeAmount(150);
                this.model.extraSize(100); // Augmenter la taille de l'inventaire de la machine
                this.model.addcountBonus(BonusMachine.XL); // Ajouter le bonus
                this.notifyView();
            } catch (NotEnoughMoney exception) {
                Value.printError("Not Enough Money");
            }
        });

        // Listener pour augmenter la durabilité de la machine
        this.view.getUnbreakingButton().addActionListener(a -> {
            if (this.model.getNumberOf(BonusMachine.UNBREAKING) >= Value.maxUnbreaking)
                return;
            try {
                this.wallet.removeAmount(150);
                this.model.setMaxDurability(this.model.getMaxDurability() + 100); // Augmenter la durabilité maximale
                this.model.addcountBonus(BonusMachine.UNBREAKING);
                this.notifyView();
            } catch (NotEnoughMoney exception) {
                Value.printError("Not Enough Money");
            }
        });

        // Listener pour rendre la machine polyvalente
        this.view.getPolyvalenteButton().addActionListener(a -> {
            if (this.model.getNumberOf(BonusMachine.POLYVALENTE) >= 1) {
                return;
            }
            try {
                this.wallet.removeAmount(150);
                this.model.setPolytvalente(); // Appliquer le bonus polyvalent
                this.notifyView();
                // Ajouter le panneau de configuration
                this.view.addActionsPane();
            } catch (NotEnoughMoney exception) {
                Value.printError("Not Enough Money");
            }
        });
    }

    /**
     * Notifie la vue qu'il y a un changement dans l'état de la machine.
     * Cette méthode met à jour les labels dans la vue avec la description actuelle.
     */
    protected void notifyView() {
        this.view.updateLabels(this.description);
    }

    protected void setReparation() {
        this.view.addReparation();
        this.notifyView();
    }

    /**
     * Gère l'action lorsqu'un utilisateur clique sur un élément de la vue.
     * Cette méthode est appelée lorsque l'utilisateur souhaite interagir avec la
     * machine via l'interface graphique.
     *
     * @param frame La fenêtre principale du jeu.
     */
    @Override
    public void action(GUI frame) {
        this.view.removeActionsPane(); // Supprime le panneau d'actions actuel

        // Si la machine est polyvalente, on ajoute le panneau de configuration
        if (this.model.getPolyvalence()) {
            this.view.addActionsPane();
        }

        notifyView();
        frame.showOverlayPanel(new OverlayPanel(frame, view)); // Afficher un panneau superposé
    }
}
