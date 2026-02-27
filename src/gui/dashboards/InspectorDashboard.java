package gui.dashboards;

import config.GameConfiguration;

import javax.swing.*;
import java.awt.*;

public class InspectorDashboard extends JPanel {

    private JLabel nomLabel = new JLabel("Nom : -");
    private JLabel sexeLabel = new JLabel("Sexe : -");
    private JLabel ageLabel = new JLabel("Age : -");

    private JProgressBar faimBar = new JProgressBar(0, 100);
    private JProgressBar fatigueBar = new JProgressBar(0, 100);
    private JProgressBar socialBar = new JProgressBar(0, 100);
    private JProgressBar santeBar = new JProgressBar(0, 100);
    private JProgressBar moralBar = new JProgressBar(0, 100);

    public InspectorDashboard() {
        this.setLayout(new GridLayout(15, 1));
        this.setPreferredSize(new Dimension(GameConfiguration.MENU_RIGHT_WIDTH, 0));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setBackground(Color.LIGHT_GRAY);

        this.add(new JLabel("   --- INFOS HABITANT ---"));
        this.add(nomLabel);
        this.add(sexeLabel);
        this.add(ageLabel);
        this.add(new JLabel("   --- BESOINS ---"));

        setupBar(" Faim :", faimBar, Color.ORANGE);
        setupBar(" Fatigue :", fatigueBar, new Color(100, 149, 237));
        setupBar(" Social :", socialBar, Color.YELLOW);
        setupBar(" Santé :", santeBar, Color.GREEN);
        setupBar(" Moral :", moralBar, Color.MAGENTA);
    }

    private void setupBar(String title, JProgressBar bar, Color color) {
        this.add(new JLabel(title));
        bar.setStringPainted(true);
        bar.setForeground(color);
        this.add(bar);
    }

    public void setInfos(String nom, String sexe, String age) {
        nomLabel.setText("  Nom : " + nom);
        sexeLabel.setText("  Sexe : " + sexe);
        ageLabel.setText("  Age : " + age + " ans");
    }

    public void setJauges(int faim, int fatigue, int social, int sante, int moral) {
        faimBar.setValue(faim);
        fatigueBar.setValue(fatigue);
        socialBar.setValue(social);
        santeBar.setValue(sante);
        moralBar.setValue(moral);
    }
}