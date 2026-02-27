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

public class MainGUI extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

    private Map map;

    private MobileInterface manager;
    private GameDisplay dashboard;

    private boolean stop = true;
    private MainGUI instance = this;
    private int speed = GameConfiguration.GAME_SPEED;
    private int currentSpeed = GameConfiguration.GAME_SPEED;


    private ControlDashboard control;
    private InspectorDashboard inspector;
    private MacroDashboard macro;
    private GraphDashboard graph; // <-- Le nouveau composant graphique


    public MainGUI(String title) {
        super(title);
        init();
    }

    private void init() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // --- UTILISATION DU CONTROL DASHBOARD (HAUT) ---
        control = new ControlDashboard();
        control.addStartStopListener(new StartStopAction());
        control.addAccelererListener(new SpeedAction());
        contentPane.add(BorderLayout.NORTH, control);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 15));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges

        // 2. On met le graphique en haut de cette boîte
        graph = new GraphDashboard();
        rightPanel.add(graph, BorderLayout.NORTH);

        // 3. On met l'inspecteur au centre de cette boîte
        inspector = new InspectorDashboard();
        rightPanel.add(inspector, BorderLayout.CENTER);

        // 4. On place la boîte entière tout à droite de la fenêtre !
        contentPane.add(rightPanel, BorderLayout.EAST);

        // --- MOTEUR ET CARTE ---
        map = GameBuilder.buildMap();
        manager = GameBuilder.buildInitMobile(map);
        dashboard = new GameDisplay(map, manager);

        MouseControls mouseControls = new MouseControls();
        dashboard.addMouseListener(mouseControls);

        // --- TA ZONE DU BAS EST SAUVEGARDÉE ! ---
        macro = new MacroDashboard();
        contentPane.add(macro, BorderLayout.SOUTH);

        // --- CORRECTION DASHBOARD ---
        int mapWidth = GameConfiguration.COLUMN_COUNT * GameConfiguration.BLOCK_SIZE;
        int mapHeight = GameConfiguration.LINE_COUNT * GameConfiguration.BLOCK_SIZE;
        dashboard.setPreferredSize(new Dimension(mapWidth, mapHeight));
        contentPane.add(dashboard, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // --- GESTION FENETRE PROPRE ---
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }


    @Override
    public void run() {
        while (!stop) {
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            manager.nextRound();
            dashboard.repaint();

            control.setLblHorloge(manager.getHorloge().getHeureActuelle());

            if (manager.getHorloge().estWeekend()) {
                control.setPeriodeText("WEEKEND");
            } else {
                control.setPeriodeText("SEMAINE");
            }

            // Mise à jour du graphique en temps réel
            graph.updateStats(manager.getHabitants());
            graph.updateStats(manager.getHabitants());
        }
    }

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
                Thread gameThread = new Thread(instance);
                gameThread.start();
            }
        }
    }

    private class SpeedAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Si on est en x1, on passe en x2
            if (speed == GameConfiguration.GAME_SPEED) {
                speed = GameConfiguration.GAME_SPEED / 2;
                control.setBtnVitesseText("Vitesse: x2");
            }
            // Si on est en x2, on passe en x5
            else if (speed == GameConfiguration.GAME_SPEED / 2) {
                speed = GameConfiguration.GAME_SPEED / 5;
                control.setBtnVitesseText("Vitesse: x5");
            }
            // Si on est en x5, on passe en x10 <-- CORRECTION ICI
            else if (speed == GameConfiguration.GAME_SPEED / 5){
                speed = GameConfiguration.GAME_SPEED / 10; // <-- CORRECTION ICI
                control.setBtnVitesseText("Vitesse: x10");
            }
            // Si on est en x10 (ou autre anomalie), on boucle et on revient à x1
            else {
                speed = GameConfiguration.GAME_SPEED;
                control.setBtnVitesseText("Vitesse: x1");
            }
        }
    }

    private class MouseControls implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            Block position = dashboard.getBlockAt(x, y);

            Habitant h = manager.getHabitant(position.getLine(), position.getColumn());

            if (h != null) {
                inspector.setInfos(h.getPrenom(), h.getSexe(), "" + h.getAge());
                Besoins b = h.getBesoins();
                inspector.setJauges(b.getFaim(), b.getFatigue(), b.getSocial(), b.getSante(), b.getMoral());
            } else {
                inspector.setInfos("-", "-", "-");
                inspector.setJauges(0, 0, 0, 0, 0);
            }
        }

        @Override public void mousePressed(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}
    }
}