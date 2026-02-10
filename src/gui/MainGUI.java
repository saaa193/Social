package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import config.GameConfiguration;
import engine.habitant.Habitants;
import engine.map.Block;
import engine.map.Map;
import engine.process.GameBuilder;
import engine.process.MobileInterface;

public class MainGUI extends JFrame implements Runnable {

    private static final Dimension IDEAL_MAIN_DIMENSION = new Dimension(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);
    private static final long serialVersionUID = 1L;

    private Map map;

    private final static Dimension preferredSize = new Dimension(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);

    private MobileInterface manager;

    private GameDisplay dashboard;

    private boolean stop = true;
    private MainGUI instance = this;
    private int speed = GameConfiguration.GAME_SPEED;

    private int currentSpeed = GameConfiguration.GAME_SPEED;

    private JLabel lblHorloge = new JLabel("Horloge");
    private JButton btnStartStop = new JButton("Start");
    private JButton btnAccelerer = new JButton("Accelerer");

    private JLabel nomLabel = new JLabel("Nom");
    private JLabel sexeLabel = new JLabel("Sexe");
    private JLabel ageLabel = new JLabel("Age");

    private JPanel control = new JPanel();

    public MainGUI(String title) {
        super(title);
        init();
    }

    private void init() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        control.setLayout(new FlowLayout(FlowLayout.CENTER));
        control.add(lblHorloge);

        btnStartStop.addActionListener(new StartStopAction());
        control.add(btnStartStop);

        btnAccelerer.addActionListener(new SpeedAction());
        control.add(btnAccelerer);

        contentPane.add(BorderLayout.NORTH, control);

        JTextField textField = new JTextField("Cliquez sur un habitant pour voir ses infos");
        contentPane.add(textField, BorderLayout.SOUTH);

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
        setPreferredSize(preferredSize);
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

            // Ensure that the chronometer is not stopped during the iteration.
            if (!stop) {
                dashboard.repaint();
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
            } else {
                speed = GameConfiguration.GAME_SPEED;
            }
        }
    }

    private class MouseControls implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            Block position = dashboard.getBlockAt(x, y);
            Habitants h = manager.getHabitant(position.getLine(),position.getColumn());
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
