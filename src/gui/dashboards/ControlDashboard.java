package gui.dashboards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ControlDashboard extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel titre = new JLabel("Projet SOCIAL ");
    private JLabel lblHorloge = new JLabel("Lancement");

    private JLabel lblPeriode = new JLabel("SEMAINE");

    private JLabel lblMeteo = new JLabel("☀ Soleil");

    private JButton btnStartStop = new JButton("▶");
    private JButton btnVitesse = new JButton("Vitesse: x1");

    public ControlDashboard() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.LIGHT_GRAY);

        //A gauche, seulement le titre
        this.add(titre, BorderLayout.WEST);

        //Les informations du centre (date, heure, période, météo)
        JPanel centre = new JPanel();
        centre.setBackground(Color.LIGHT_GRAY);
        centre.add(lblHorloge);

        lblPeriode.setForeground(new Color(100, 100, 255));
        centre.add(new JLabel(" | "));
        centre.add(lblPeriode);
        centre.add(new JLabel(" | "));
        centre.add(lblMeteo);

        this.add(centre, BorderLayout.CENTER);

        //A droite la vitesse et le bouton StartStop
        JPanel droite = new JPanel();
        droite.setBackground(Color.LIGHT_GRAY);
        droite.add(btnStartStop);
        droite.add(btnVitesse);
        this.add(droite, BorderLayout.EAST);
    }

    public void setLblHorloge(String texte){
        lblHorloge.setText(texte);
    }
    public void setPeriodeText(String texte){
        lblPeriode.setText(texte);
    }
    public void setBtnVitesseText(String texte){
        btnVitesse.setText(texte);
    }
    public void setBtnStartStopText(String texte){
        btnStartStop.setText(texte);
    }

    public void addStartStopListener(ActionListener listener) {
        btnStartStop.addActionListener(listener);
    }

    public void addAccelererListener(ActionListener listener) {
        btnVitesse.addActionListener(listener);
    }

}
