package gui;

import engine.habitant.Habitant;
import engine.habitant.etat.EtatHabitant;
import engine.habitant.visitor.DiagnosticVisitor;

import javax.swing.*;
import java.awt.*;

/**
 * InspectionCitoyenModal : Fenêtre modale d'inspection d'un habitant.
 * S'ouvre au clic sur un habitant dans la carte.
 * Affiche : identité, état psychologique, jauges de besoins, profil OCEAN.
 */
public class InspectionCitoyenModal extends JDialog {

    private static final long serialVersionUID = 1L;
    private DiagnosticVisitor diagnosticVisitor = new DiagnosticVisitor();

    public InspectionCitoyenModal(JFrame parent, Habitant habitant) {
        super(parent, "Inspection Citoyen", true);
        construire(habitant);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Construit le contenu de la fenêtre pour un habitant donné.
     */
    private void construire(Habitant habitant) {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // --- HAUT : Identité + État ---
        JPanel panneauHaut = new JPanel(new BorderLayout());
        panneauHaut.setBackground(new Color(80, 60, 180));
        panneauHaut.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel nomLabel = new JLabel(habitant.getPrenom());
        nomLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nomLabel.setForeground(Color.WHITE);
        panneauHaut.add(nomLabel, BorderLayout.NORTH);

        JPanel infosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        infosPanel.setBackground(new Color(80, 60, 180));
        infosPanel.add(creerBadge(habitant.getSexe()));
        infosPanel.add(creerBadge(habitant.getAge() + " ans"));
        panneauHaut.add(infosPanel, BorderLayout.CENTER);

        // Label état via DiagnosticVisitor
        EtatHabitant etat = habitant.getPsychologie().determinerEtat(habitant.getBesoins());
        String labelEtat = etat.accept(diagnosticVisitor);

        // "ÉTAT ACTUEL" + label état en dessous du nom/age
        JPanel etatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        etatPanel.setBackground(new Color(80, 60, 180));

        JLabel etatTitreLabel = new JLabel("ÉTAT ACTUEL");
        etatTitreLabel.setForeground(new Color(200, 200, 200));
        etatTitreLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        etatPanel.add(etatTitreLabel);

        JLabel etatLabel = new JLabel(labelEtat);
        etatLabel.setForeground(couleurEtat(labelEtat));
        etatLabel.setFont(new Font("Arial", Font.BOLD, 13));
        etatPanel.add(etatLabel);

        etatPanel.setBackground(new Color(245, 245, 245));
        etatPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        // Et changer la couleur du titre en gris
        etatTitreLabel.setForeground(Color.GRAY);

        // Ajouter au layout principal ENTRE le haut et le centre
        JPanel nordPanel = new JPanel(new BorderLayout());
        nordPanel.add(panneauHaut, BorderLayout.NORTH);
        nordPanel.add(etatPanel, BorderLayout.SOUTH);
        add(nordPanel, BorderLayout.NORTH);

        // --- CENTRE : Besoins + OCEAN ---
        JPanel panneauCentre = new JPanel(new GridLayout(1, 2, 10, 0));
        panneauCentre.setBackground(Color.WHITE);
        panneauCentre.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Jauges besoins
        JPanel besoinsPanel = new JPanel(new GridLayout(5, 2, 5, 8));
        besoinsPanel.setBackground(Color.WHITE);
        besoinsPanel.setBorder(BorderFactory.createTitledBorder("Besoins vitaux"));
        ajouterJauge(besoinsPanel, "Faim",    habitant.getBesoins().getFaim(),    new Color(255, 140, 0));
        ajouterJauge(besoinsPanel, "Fatigue", habitant.getBesoins().getFatigue(), new Color(100, 100, 255));
        ajouterJauge(besoinsPanel, "Social",  habitant.getBesoins().getSocial(),  new Color(0, 180, 0));
        ajouterJauge(besoinsPanel, "Santé",   habitant.getBesoins().getSante(),   new Color(200, 0, 0));
        ajouterJauge(besoinsPanel, "Moral",   habitant.getMoral(),                new Color(128, 0, 128));
        panneauCentre.add(besoinsPanel);

        // Profil OCEAN
        JPanel oceanPanel = new JPanel(new GridLayout(5, 2, 5, 8));
        oceanPanel.setBackground(Color.WHITE);
        oceanPanel.setBorder(BorderFactory.createTitledBorder("Psychologie (OCEAN)"));
        ajouterJauge(oceanPanel, "Ouverture",    habitant.getOuverture(),    new Color(70, 130, 180));
        ajouterJauge(oceanPanel, "Conscience",   habitant.getConscience(),   new Color(70, 130, 180));
        ajouterJauge(oceanPanel, "Extraversion", habitant.getExtraversion(), new Color(70, 130, 180));
        ajouterJauge(oceanPanel, "Agréabilité",  habitant.getAgreabilite(),  new Color(70, 130, 180));
        ajouterJauge(oceanPanel, "Névrosisme",   habitant.getNevrosisme(),   new Color(70, 130, 180));
        panneauCentre.add(oceanPanel);

        add(panneauCentre, BorderLayout.CENTER);

        // --- BAS : Bouton fermer ---
        JButton fermerBtn = new JButton("Fermer");
        fermerBtn.addActionListener(e -> dispose());
        JPanel bas = new JPanel();
        bas.setBackground(Color.WHITE);
        bas.add(fermerBtn);
        add(bas, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(500, 400));
    }

    /**
     * Ajoute une jauge avec son label dans un panel.
     */
    private void ajouterJauge(JPanel panel, String titre, int valeur, Color couleur) {
        JLabel label = new JLabel(titre + " :");
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(label);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(valeur);
        bar.setStringPainted(true);
        bar.setForeground(couleur);
        panel.add(bar);
    }

    /**
     * Crée un badge coloré pour l'identité.
     */
    private JLabel creerBadge(String texte) {
        JLabel badge = new JLabel(texte);
        badge.setForeground(new Color(200, 220, 255));
        badge.setFont(new Font("Arial", Font.PLAIN, 12));
        return badge;
    }

    /**
     * Retourne la couleur selon le label d'état.
     */
    private Color couleurEtat(String labelEtat) {
        if (labelEtat.equals("ÉPANOUI"))  return new Color(0, 200, 0);
        if (labelEtat.equals("ANXIEUX"))  return Color.ORANGE;
        if (labelEtat.equals("DÉTRESSE")) return Color.RED;
        return Color.BLACK;
    }

}