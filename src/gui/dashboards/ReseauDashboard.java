package gui.dashboards;

import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.map.Statistique;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * ReseauDashboard : Affiche la densité du réseau social.
 * Montre le nombre de liens Famille, Travail et Amis
 * avec des barres colorées et des boutons pour filtrer l'affichage.
 */
public class ReseauDashboard extends JPanel {

    private static final long serialVersionUID = 1L;

    // Barres de progression pour chaque type de lien
    private JProgressBar barreFamille = new JProgressBar(0, 200);
    private JProgressBar barreTravail = new JProgressBar(0, 200);
    private JProgressBar barreAmis    = new JProgressBar(0, 200);

    // Labels pour afficher le nombre de liens
    private JLabel nombreFamille = new JLabel("0");
    private JLabel nombreTravail = new JLabel("0");
    private JLabel nombreAmis    = new JLabel("0");

    // Boutons œil pour activer/désactiver l'affichage des liens
    private JToggleButton btnFamille = new JToggleButton("O");
    private JToggleButton btnTravail = new JToggleButton("O");
    private JToggleButton btnAmis    = new JToggleButton("O");

    public ReseauDashboard() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Densité du réseau"
        ));
        setPreferredSize(new Dimension(GameConfiguration.MENU_RIGHT_WIDTH, 160));

        // Panel principal avec les 3 lignes
        JPanel contenu = new JPanel(new GridLayout(3, 1, 0, 8));
        contenu.setBackground(Color.WHITE);
        contenu.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        // Ajout des 3 lignes
        contenu.add(creerLigneLien("FAMILLE", barreFamille, nombreFamille, btnFamille, new Color(220, 50, 100)));
        contenu.add(creerLigneLien("TRAVAIL", barreTravail, nombreTravail, btnTravail, new Color(50, 100, 220)));
        contenu.add(creerLigneLien("AMIS",    barreAmis,    nombreAmis,    btnAmis,    new Color(50, 180, 50)));

        add(contenu, BorderLayout.CENTER);

        // Les boutons sont activés par défaut
        btnFamille.setSelected(true);
        btnTravail.setSelected(true);
        btnAmis.setSelected(true);
    }

    /**
     * Crée une ligne avec : label, barre colorée, nombre, bouton œil.
     */
    private JPanel creerLigneLien(String nom, JProgressBar barre, JLabel nombre, JToggleButton btn, Color couleur) {
        JPanel ligne = new JPanel(new BorderLayout(5, 0));
        ligne.setBackground(Color.WHITE);

        // Label du type de lien
        JLabel label = new JLabel(nom);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setForeground(couleur);
        label.setPreferredSize(new Dimension(60, 20));
        ligne.add(label, BorderLayout.WEST);

        // Barre colorée
        barre.setForeground(couleur);
        barre.setBackground(new Color(230, 230, 230));
        barre.setStringPainted(false);
        ligne.add(barre, BorderLayout.CENTER);

        // Panel droite : nombre + bouton oeil
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        droite.setBackground(Color.WHITE);

        nombre.setFont(new Font("Arial", Font.BOLD, 11));
        nombre.setForeground(Color.GRAY);
        droite.add(nombre);

        // Style du bouton oeil
        btn.setPreferredSize(new Dimension(30, 25));
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        btn.setFocusPainted(false);
        droite.add(btn);

        ligne.add(droite, BorderLayout.EAST);

        return ligne;
    }

    /**
     * Met à jour les barres et les nombres à chaque tour.
     * Appelée depuis MainGUI.run()
     */
    public void updateReseau(List<Habitant> habitants) {
        if (habitants == null || habitants.isEmpty()) return;

        // Récupération des nombres depuis Statistique
        int nbFamille = Statistique.getNombreLiensFamiliaux(habitants);
        int nbTravail = Statistique.getNombreLiensProfessionnels(habitants);
        int nbAmis    = Statistique.getNombreLiensAmicaux(habitants);

        // Mise à jour des barres
        barreFamille.setValue(Math.min(nbFamille, 200));
        barreTravail.setValue(Math.min(nbTravail, 200));
        barreAmis.setValue(Math.min(nbAmis, 200));

        // Mise à jour des nombres
        nombreFamille.setText(String.valueOf(nbFamille));
        nombreTravail.setText(String.valueOf(nbTravail));
        nombreAmis.setText(String.valueOf(nbAmis));

        // Mise à jour visuelle du bouton oeil
        if (btnFamille.isSelected()) {
            btnFamille.setText("O");
        } else {
            btnFamille.setText("X");
        }

        if (btnTravail.isSelected()) {
            btnTravail.setText("O");
        } else {
            btnTravail.setText("X");
        }

        if (btnAmis.isSelected()) {
            btnAmis.setText("O");
        } else {
            btnAmis.setText("X");
        }
    }

    public boolean afficherFamille() { return btnFamille.isSelected(); }
    public boolean afficherTravail() { return btnTravail.isSelected(); }
    public boolean afficherAmis() { return btnAmis.isSelected(); }
}