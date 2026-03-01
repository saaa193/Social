package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.habitant.besoin.Besoins;
import engine.map.Block;
import engine.map.Map;
import engine.process.GameBuilder;
import engine.process.MobileInterface;

import gui.dashboards.ControlDashboard;
import gui.dashboards.InspectorDashboard;
import gui.dashboards.MacroDashboard;
import gui.dashboards.GraphDashboard;

/**
 * MainGUI : La classe principale qui orchestre tout le programme.
 * Elle implémente Runnable car elle gère la boucle de jeu
 * dans un Thread séparé pour ne pas bloquer l'interface graphique.
 */
public class MainGUI extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

    // --- Composants de structure (Modèle et Moteur) ---
    private Map map;
    private MobileInterface manager;
    private GameDisplay dashboard;

    private boolean stop = true;
    private MainGUI instance = this;
    private int speed = GameConfiguration.GAME_SPEED;

    // Variable d'état : Permet de basculer entre vue "Moyenne" et vue "Habitant"
    private Habitant habitantSelectionne = null;

    // --- Composants de l'interface (Vue) ---
    private ControlDashboard control;
    private InspectorDashboard inspector;
    private MacroDashboard macro;
    private GraphDashboard graph;

    public MainGUI(String title) {
        super(title);
        init();
    }

    /**
     * Initialisation de la structure : Utilisation du BorderLayout pour
     * organiser l'espace visuel efficacement.
     */
    private void init() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // 1. Barre de contrôle (Haut)
        control = new ControlDashboard();
        control.addStartStopListener(new StartStopAction());
        control.addAccelererListener(new SpeedAction());
        contentPane.add(BorderLayout.NORTH, control);

        // 2. Panneau latéral droit : Graphiques et Inspecteur
        JPanel rightPanel = new JPanel(new BorderLayout(0, 15));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        graph = new GraphDashboard();
        rightPanel.add(graph, BorderLayout.NORTH);

        inspector = new InspectorDashboard();
        rightPanel.add(inspector, BorderLayout.CENTER);

        contentPane.add(rightPanel, BorderLayout.EAST);

        // 3. Initialisation Moteur & Carte
        map = GameBuilder.buildMap();
        manager = GameBuilder.buildInitMobile(map);
        dashboard = new GameDisplay(map, manager);

        // Gestion des interactions souris
        MouseControls mouseControls = new MouseControls();
        dashboard.addMouseListener(mouseControls);

        // 4. Zone opérationnelle (Bas)
        macro = new MacroDashboard();
        contentPane.add(macro, BorderLayout.SOUTH);

        // 5. Zone principale : la Carte
        int mapWidth = GameConfiguration.COLUMN_COUNT * GameConfiguration.BLOCK_SIZE;
        int mapHeight = GameConfiguration.LINE_COUNT * GameConfiguration.BLOCK_SIZE;
        dashboard.setPreferredSize(new Dimension(mapWidth, mapHeight));
        contentPane.add(dashboard, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack(); // Ajuste la fenêtre au contenu
        setLocationRelativeTo(null); // Centre la fenêtre
        setVisible(true);
        setResizable(false);
    }

    /**
     * La "Game Loop" : Le cœur de l'animation.
     * Cette boucle s'exécute en continu tant que le jeu n'est pas en pause.
     */
    @Override
    public void run() {
        while (!stop) {
            try {
                Thread.sleep(speed); // Contrôle de la vitesse de simulation
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            // A. Mise à jour logique
            manager.nextRound();
            dashboard.repaint(); // Demande à Swing de redessiner

            // B. Mise à jour de l'affichage (Date/Période)
            control.setLblHorloge(manager.getHorloge().getHeureActuelle());
            control.setPeriodeText(manager.getHorloge().estWeekend() ? "WEEKEND" : "SEMAINE");

            // C. Mise à jour des Dashboards
            graph.updateStats(manager.getHabitants());

            // Gestion intelligente de l'inspecteur : Moyenne ou Individu
            if (habitantSelectionne == null) {
                inspector.updateAverages(manager.getHabitants());
            } else {
                Besoins b = habitantSelectionne.getBesoins();
                inspector.setInfos(habitantSelectionne.getPrenom(), habitantSelectionne.getSexe(), "" + habitantSelectionne.getAge());
                inspector.setJauges(b.getFaim(), b.getFatigue(), b.getSocial(), b.getSante(), b.getMoral());
            }
        }
    }

    // --- Pattern "Listener" pour gérer les clics ---

    private class StartStopAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!stop) {
                stop = true;
                control.setBtnStartStopText("▶");
                dashboard.repaint();
            } else {
                stop = false;
                control.setBtnStartStopText("⏸");
                Thread gameThread = new Thread(instance); // Nouveau thread pour la boucle
                gameThread.start();
            }
        }
    }

    private class SpeedAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Logique de cycle de vitesse (x1 -> x2 -> x5 -> x10 -> x1)
            if (speed == GameConfiguration.GAME_SPEED) {
                speed = GameConfiguration.GAME_SPEED / 2;
                control.setBtnVitesseText("Vitesse: x2");
            }
            else if (speed == GameConfiguration.GAME_SPEED / 2) {
                speed = GameConfiguration.GAME_SPEED / 5;
                control.setBtnVitesseText("Vitesse: x5");
            }
            else if (speed == GameConfiguration.GAME_SPEED / 5){
                speed = GameConfiguration.GAME_SPEED / 10;
                control.setBtnVitesseText("Vitesse: x10");
            }
            else {
                speed = GameConfiguration.GAME_SPEED;
                control.setBtnVitesseText("Vitesse: x1");
            }
        }
    }

    private class MouseControls implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            // Conversion des pixels en coordonnées de la Grille
            Block position = dashboard.getBlockAt(e.getX(), e.getY());
            habitantSelectionne = manager.getHabitant(position.getLine(), position.getColumn());
        }

        // Méthodes requises par MouseListener mais non utilisées
        @Override public void mousePressed(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}
    }
}