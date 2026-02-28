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

    // --- NOUVELLE MÉTHODE : Calcule et affiche la moyenne de toute la ville ---
    public void updateAverages(java.util.List<engine.habitant.Habitant> habitants) {
        if (habitants == null || habitants.isEmpty()) return;

        int totalFaim = 0, totalFatigue = 0, totalSocial = 0, totalSante = 0, totalMoral = 0;
        int totalAge = 0;

        // On additionne les besoins de tout le monde
        for (engine.habitant.Habitant h : habitants) {
            totalAge += h.getAge();
            totalFaim += h.getBesoins().getFaim();
            totalFatigue += h.getBesoins().getFatigue();
            totalSocial += h.getBesoins().getSocial();
            totalSante += h.getBesoins().getSante();
            totalMoral += h.getBesoins().getMoral();
        }

        int taille = habitants.size();

        // On met à jour les textes pour bien montrer que c'est une moyenne
        setInfos("Moyenne de la ville", "Mixte", "" + (totalAge / taille));

        // On divise par le nombre d'habitants pour avoir la moyenne des jauges !
        setJauges(
                totalFaim / taille,
                totalFatigue / taille,
                totalSocial / taille,
                totalSante / taille,
                totalMoral / taille
        );
    }
}