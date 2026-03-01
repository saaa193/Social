package gui.dashboards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * ControlDashboard : Le tableau de bord principal.
 * Il regroupe les informations de temps, météo et les contrôles de la simulation.
 * Utilisation de BorderLayout pour une répartition propre des zones de l'interface.
 */
public class ControlDashboard extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel titre = new JLabel("Projet SOCIAL ");
    private JLabel lblHorloge = new JLabel("Lancement");
    private JLabel lblPeriode = new JLabel("SEMAINE");
    private JLabel lblMeteo = new JLabel("☀ Soleil");

    private JButton btnStartStop = new JButton("▶");
    private JButton btnVitesse = new JButton("Vitesse: x1");

    public ControlDashboard() {
        // Mise en page BorderLayout : idéal pour une barre d'outils
        this.setLayout(new BorderLayout());
        this.setBackground(Color.LIGHT_GRAY);

        // Zone Ouest : le titre
        this.add(titre, BorderLayout.WEST);

        // Zone Centre : les infos dynamiques (date, période, météo)
        JPanel centre = new JPanel();
        centre.setBackground(Color.LIGHT_GRAY);
        centre.add(lblHorloge);

        lblPeriode.setForeground(new Color(100, 100, 255)); // Touche de couleur pour le design
        centre.add(new JLabel(" | "));
        centre.add(lblPeriode);
        centre.add(new JLabel(" | "));
        centre.add(lblMeteo);

        this.add(centre, BorderLayout.CENTER);

        // Zone Est : les contrôles (Start/Stop et Vitesse)
        JPanel droite = new JPanel();
        droite.setBackground(Color.LIGHT_GRAY);
        droite.add(btnStartStop);
        droite.add(btnVitesse);
        this.add(droite, BorderLayout.EAST);
    }

    // --- API DE MISE À JOUR ---
    // Au lieu de rendre les labels publics, on expose des méthodes "set".
    // Cela permet au Controller de mettre à jour l'IHM sans connaître les détails techniques.

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

    // --- GESTION DES ÉVÉNEMENTS ---
    // Pattern de délégation : on transmet les listeners au Controller.
    public void addStartStopListener(ActionListener listener) {
        btnStartStop.addActionListener(listener);
    }

    public void addAccelererListener(ActionListener listener) {
        btnVitesse.addActionListener(listener);
    }
}