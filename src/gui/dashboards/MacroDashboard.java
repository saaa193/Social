package gui.dashboards;

import config.GameConfiguration;
import engine.process.MobileElementManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * MacroDashboard : Zone opérationnelle située en bas de l'écran.
 * Contient les paramètres de simulation et les boutons d'action.
 */
public class MacroDashboard extends JPanel {
    private static final long serialVersionUID = 1L;

    // Curseurs de paramètres
    private JSlider sliderResistance = new JSlider(0, 100, 50);
    private JSlider sliderInfluence  = new JSlider(0, 10, 5);

    // Labels de valeurs
    private JLabel valeurResistance = new JLabel("50%");
    private JLabel valeurInfluence  = new JLabel("5/10");

    // Combo événement + bouton information
    private JComboBox<String> comboEvenement = new JComboBox<String>();
    private JButton btnInformation = new JButton("(•) Propager une Information");

    public MacroDashboard() {
        this.setPreferredSize(new Dimension(0, GameConfiguration.MENU_BOTTOM_HEIGHT));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setBackground(Color.LIGHT_GRAY);
        this.setLayout(new BorderLayout());

        // --- Zone gauche : titre + curseurs ---
        JPanel zoneGauche = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        zoneGauche.setBackground(Color.LIGHT_GRAY);

        JLabel titre = new JLabel("⚙ PARAMÈTRES DE SIMULATION");
        titre.setFont(new Font("Arial", Font.BOLD, 11));
        zoneGauche.add(titre);

        zoneGauche.add(new JLabel("Résistance :"));
        sliderResistance.setBackground(Color.LIGHT_GRAY);
        sliderResistance.setPreferredSize(new Dimension(100, 30));
        sliderResistance.addChangeListener(e ->
                valeurResistance.setText(sliderResistance.getValue() + "%")
        );
        zoneGauche.add(sliderResistance);
        zoneGauche.add(valeurResistance);

        zoneGauche.add(new JLabel("Force d'influence :"));
        sliderInfluence.setBackground(Color.LIGHT_GRAY);
        sliderInfluence.setPreferredSize(new Dimension(100, 30));
        sliderInfluence.addChangeListener(e ->
                valeurInfluence.setText(sliderInfluence.getValue() + "/10")
        );
        zoneGauche.add(sliderInfluence);
        zoneGauche.add(valeurInfluence);

        this.add(zoneGauche, BorderLayout.WEST);

        // --- Zone droite : combo + bouton ---
        JPanel zoneDroite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        zoneDroite.setBackground(Color.LIGHT_GRAY);

        // JComboBox = bouton "Déclencher un Événement"
        comboEvenement.addItem("⚡ Déclencher un Événement");
        comboEvenement.addItem("Alerte Météo");
        comboEvenement.addItem("Fête de Quartier");
        comboEvenement.addItem("Offres d'Emploi");
        comboEvenement.addItem("Expo Musée");
        zoneDroite.add(comboEvenement);

        // Bouton Propager une Information
        btnInformation.setBackground(new Color(200, 50, 50));
        btnInformation.setForeground(Color.WHITE);
        btnInformation.addActionListener(e -> ouvrirModalePropagation());
        zoneDroite.add(btnInformation);

        this.add(zoneDroite, BorderLayout.EAST);
    }

    /**
     * Ouvre la modale "Propager une Information".
     */
    private void ouvrirModalePropagation() {
        JDialog modale = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                "(•) Propager une Information", true);
        modale.setLayout(new BorderLayout(10, 10));
        modale.getContentPane().setBackground(Color.WHITE);
        modale.setSize(400, 320);
        modale.setLocationRelativeTo(this);

        // --- Contenu principal ---
        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setBackground(Color.WHITE);
        contenu.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        // Thème de l'information
        JLabel lblTheme = new JLabel("THÈME DE L'INFORMATION");
        lblTheme.setFont(new Font("Arial", Font.BOLD, 11));
        lblTheme.setForeground(Color.GRAY);
        contenu.add(lblTheme);
        contenu.add(Box.createVerticalStrut(5));

        JTextField champTheme = new JTextField("Ex: Fausse rumeur sur la ville...");
        champTheme.setForeground(Color.GRAY);
        champTheme.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        contenu.add(champTheme);
        contenu.add(Box.createVerticalStrut(15));

        // Curseur Véracité
        JPanel panelVeracite = new JPanel(new BorderLayout());
        panelVeracite.setBackground(Color.WHITE);
        JLabel lblVeracite = new JLabel("✅ VÉRACITÉ");
        lblVeracite.setFont(new Font("Arial", Font.BOLD, 11));
        JLabel valVeracite = new JLabel("50%");
        valVeracite.setForeground(new Color(0, 150, 0));
        panelVeracite.add(lblVeracite, BorderLayout.WEST);
        panelVeracite.add(valVeracite, BorderLayout.EAST);
        contenu.add(panelVeracite);

        JSlider sliderVeracite = new JSlider(0, 100, 50);
        sliderVeracite.setBackground(Color.WHITE);
        sliderVeracite.setForeground(new Color(0, 150, 0));
        sliderVeracite.addChangeListener(e ->
                valVeracite.setText(sliderVeracite.getValue() + "%")
        );
        contenu.add(sliderVeracite);
        contenu.add(Box.createVerticalStrut(10));

        // Curseur Virulence
        JPanel panelVirulence = new JPanel(new BorderLayout());
        panelVirulence.setBackground(Color.WHITE);
        JLabel lblVirulence = new JLabel("🔥 VIRULENCE");
        lblVirulence.setFont(new Font("Arial", Font.BOLD, 11));
        JLabel valVirulence = new JLabel("50%");
        valVirulence.setForeground(new Color(200, 50, 50));
        panelVirulence.add(lblVirulence, BorderLayout.WEST);
        panelVirulence.add(valVirulence, BorderLayout.EAST);
        contenu.add(panelVirulence);

        JSlider sliderVirulence = new JSlider(0, 100, 50);
        sliderVirulence.setBackground(Color.WHITE);
        sliderVirulence.addChangeListener(e ->
                valVirulence.setText(sliderVirulence.getValue() + "%")
        );
        contenu.add(sliderVirulence);
        contenu.add(Box.createVerticalStrut(5));

        // Texte explicatif
        JLabel lblExplication = new JLabel("Une virulence élevée augmente la vitesse de propagation mais aussi la panique.");
        lblExplication.setFont(new Font("Arial", Font.ITALIC, 10));
        lblExplication.setForeground(Color.GRAY);
        contenu.add(lblExplication);

        modale.add(contenu, BorderLayout.CENTER);

        // --- Bouton Lancer la Rumeur ---
        JButton btnLancer = new JButton("(•) Lancer la Rumeur");
        btnLancer.setBackground(new Color(220, 80, 100));
        btnLancer.setForeground(Color.WHITE);
        btnLancer.setFont(new Font("Arial", Font.BOLD, 13));
        btnLancer.addActionListener(e -> {
            // Récupère les valeurs et ferme la modale
            modale.dispose();
        });

        JPanel bas = new JPanel();
        bas.setBackground(Color.WHITE);
        bas.add(btnLancer);
        modale.add(bas, BorderLayout.SOUTH);

        modale.setVisible(true);
    }

    // --- ACCESSEURS ---
    public int getResistance() { return sliderResistance.getValue(); }
    public int getInfluence()  { return sliderInfluence.getValue(); }
    public String getEvenementSelectionne() { return (String) comboEvenement.getSelectedItem(); }

    // --- LISTENERS ---
    public void addEvenementListener(ActionListener listener) {
        comboEvenement.addActionListener(listener);
    }

    public void addInformationListener(ActionListener listener) {
        btnInformation.addActionListener(listener);
    }
}