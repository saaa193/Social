package gui;

import java.awt.BorderLayout;
import java.awt.Color; // <--- AJOUT : Pour la couleur
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout; // <--- AJOUT : Pour aligner les textes verticalement
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory; // <--- AJOUT : Pour faire une bordure noire
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
// import javax.swing.JTextField; // <--- RETRAIT : On vire la barre blanche inutile

import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.map.Block;
import engine.map.Map;
import engine.process.GameBuilder;
import engine.process.MobileInterface;

public class MainGUI extends JFrame implements Runnable {

    // private static final Dimension IDEAL_MAIN_DIMENSION = ... (On s'en sert pas vraiment ici, pas grave)
    private static final long serialVersionUID = 1L;

    private Map map;

    // J'ai un peu élargi la fenetre (+200 en largeur) pour que le panneau de droite rentre bien
    private final static Dimension preferredSize = new Dimension(GameConfiguration.WINDOW_WIDTH + 200, GameConfiguration.WINDOW_HEIGHT);

    private MobileInterface manager;
    private GameDisplay dashboard;

    private boolean stop = true;
    private MainGUI instance = this;
    private int speed = GameConfiguration.GAME_SPEED;
    private int currentSpeed = GameConfiguration.GAME_SPEED;

    private JLabel lblHorloge = new JLabel("Horloge : 00:00");
    private JButton btnStartStop = new JButton("Start");
    private JButton btnAccelerer = new JButton("Accelerer");

    // --- MODIFICATION ICI : On initialise les labels avec du texte vide ou des tirets ---
    private JLabel nomLabel = new JLabel("Nom : -");
    private JLabel sexeLabel = new JLabel("Sexe : -");
    private JLabel ageLabel = new JLabel("Age : -");

    private JPanel control = new JPanel();

    // --- MODIFICATION ICI : Création du panneau latéral pour les infos ---
    private JPanel infoPanel = new JPanel();

    public MainGUI(String title) {
        super(title);
        init();
    }

    private void init() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // 1. LE HAUT (Boutons + Horloge)
        control.setLayout(new FlowLayout(FlowLayout.CENTER));
        control.add(lblHorloge);

        btnStartStop.addActionListener(new StartStopAction());
        control.add(btnStartStop);

        btnAccelerer.addActionListener(new SpeedAction());
        control.add(btnAccelerer);

        contentPane.add(BorderLayout.NORTH, control);

        // --- MODIFICATION ICI : CONFIGURATION DU PANNEAU DE DROITE ---
        infoPanel.setLayout(new GridLayout(10, 1)); // 10 lignes pour espacer
        infoPanel.setPreferredSize(new Dimension(200, 0)); // Largeur fixe de 200px
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Petit cadre noir autour
        infoPanel.setBackground(Color.LIGHT_GRAY); // Fond gris pour bien voir la zone

        // On ajoute un titre et tes labels dans ce panneau
        infoPanel.add(new JLabel("   --- INFOS HABITANT ---"));
        infoPanel.add(nomLabel);
        infoPanel.add(sexeLabel);
        infoPanel.add(ageLabel);

        // On place ce panneau à DROITE (EAST)
        contentPane.add(infoPanel, BorderLayout.EAST);
        // -------------------------------------------------------------

        // JTextField textField = new JTextField(...) <--- RETRAIT : On supprime la barre blanche en bas

        map = GameBuilder.buildMap();
        manager = GameBuilder.buildInitMobile(map);
        dashboard = new GameDisplay(map, manager);

        MouseControls mouseControls = new MouseControls();
        dashboard.addMouseListener(mouseControls);

        dashboard.setPreferredSize(preferredSize);
        contentPane.add(dashboard, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        // setPreferredSize(preferredSize); // Pas besoin de le mettre 2 fois
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

            // 1. Calcul du moteur
            manager.nextRound();

            // 2. Mise à jour de l'affichage (Animation)
            if (!stop) {
                dashboard.repaint(); // Redessine la carte

                // --- AJOUT POUR L'HORLOGE (Tu l'avais déjà, c'est bien) ---
                lblHorloge.setText(manager.getHorloge().getHeureActuelle());
            }
        }
    }

    private class StartStopAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!stop) {
                stop = true;
                btnStartStop.setText(" Start ");
            } else {
                stop = false;
                btnStartStop.setText(" Pause ");
                Thread gameThread = new Thread(instance);
                gameThread.start();
            }
        }
    }

    private class SpeedAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (speed > 100) {
                speed -= 100;
                // Petit bonus visuel pour savoir
                btnAccelerer.setText("Vitesse: Rapide");
            } else {
                speed = GameConfiguration.GAME_SPEED;
                btnAccelerer.setText("Vitesse: Normale");
            }
        }
    }

    private class MouseControls implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            // 1. On trouve la case cliquée
            Block position = dashboard.getBlockAt(x, y);

            // 2. On demande au manager
            Habitant h = manager.getHabitant(position.getLine(), position.getColumn());

            // 3. Mise à jour des labels DANS LE PANNEAU DE DROITE
            if (h != null) {
                nomLabel.setText("  Nom : " + h.getPrenom());
                sexeLabel.setText("  Sexe : " + h.getSexe());
                ageLabel.setText("  Age : " + h.getAge() + " ans");
                System.out.println("Clic sur : " + h.getPrenom());
            } else {
                nomLabel.setText("  Nom : -");
                sexeLabel.setText("  Sexe : -");
                ageLabel.setText("  Age : -");
            }
        }

        @Override public void mousePressed(MouseEvent e) { }
        @Override public void mouseReleased(MouseEvent e) { }
        @Override public void mouseEntered(MouseEvent e) { }
        @Override public void mouseExited(MouseEvent e) { }
    }
}