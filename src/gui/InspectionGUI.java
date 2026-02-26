package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import engine.habitant.Habitant;
import engine.habitant.besoin.Besoins;

// On utilise JDialog pour créer une fenêtre Modale (Pop-up par dessus le jeu)
public class InspectionGUI extends JDialog {

    private static final long serialVersionUID = 1L;

    public InspectionGUI(JFrame parent, Habitant h) {
        // Le "true" à la fin bloque les clics sur la fenêtre principale tant que celle-ci est ouverte
        super(parent, "Inspection Habitant", false);

        setSize(550, 450);
        setLocationRelativeTo(parent); // Centre la fenêtre par rapport au jeu
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // --- EN-TÊTE BLEU (Comme sur le PDF) ---
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(new Color(65, 105, 225)); // Bleu Royal
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel nameLabel = new JLabel(h.getPrenom(), SwingConstants.LEFT);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel subLabel = new JLabel("Statut : Actif  •  " + h.getAge() + " ans  •  " + h.getSexe(), SwingConstants.LEFT);
        subLabel.setForeground(Color.WHITE);
        subLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        header.add(nameLabel);
        header.add(subLabel);
        add(header, BorderLayout.NORTH);

        // --- CORPS (2 Colonnes : OCEAN et Besoins) ---
        JPanel body = new JPanel(new GridLayout(1, 2, 30, 0));
        body.setBackground(Color.WHITE);
        body.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Colonne de Gauche : OCEAN
        JPanel oceanPanel = new JPanel(new GridLayout(11, 1));
        oceanPanel.setBackground(Color.WHITE);
        JLabel oceanTitle = new JLabel("PSYCHOLOGIE (OCEAN)", SwingConstants.CENTER);
        oceanTitle.setFont(new Font("Arial", Font.BOLD, 12));
        oceanPanel.add(oceanTitle);
        oceanPanel.add(new JSeparator());

        addBar(oceanPanel, "Ouverture", h.getOuverture(), new Color(138, 43, 226)); // Violet
        addBar(oceanPanel, "Conscience", h.getConscience(), new Color(138, 43, 226));
        addBar(oceanPanel, "Extraversion", h.getExtraversion(), new Color(138, 43, 226));
        addBar(oceanPanel, "Agréabilité", h.getAgreabilite(), new Color(138, 43, 226));
        addBar(oceanPanel, "Névrosisme", h.getNevrosisme(), new Color(138, 43, 226));
        body.add(oceanPanel);

        // Colonne de Droite : BESOINS VITAUX
        JPanel needsPanel = new JPanel(new GridLayout(11, 1));
        needsPanel.setBackground(Color.WHITE);
        JLabel needsTitle = new JLabel("BESOINS VITAUX", SwingConstants.CENTER);
        needsTitle.setFont(new Font("Arial", Font.BOLD, 12));
        needsPanel.add(needsTitle);
        needsPanel.add(new JSeparator());

        Besoins b = h.getBesoins();
        addBar(needsPanel, "Faim", b.getFaim(), Color.ORANGE);
        addBar(needsPanel, "Sommeil", b.getFatigue(), new Color(100, 149, 237));
        addBar(needsPanel, "Social", b.getSocial(), Color.YELLOW);
        addBar(needsPanel, "Santé", b.getSante(), Color.GREEN);
        addBar(needsPanel, "Moral", b.getMoral(), Color.MAGENTA);
        body.add(needsPanel);

        add(body, BorderLayout.CENTER);
    }

    // Méthode pour ajouter une jauge facilement
    private void addBar(JPanel panel, String title, int value, Color color) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(label);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(value);
        bar.setStringPainted(true);
        bar.setForeground(color);
        panel.add(bar);
    }
}