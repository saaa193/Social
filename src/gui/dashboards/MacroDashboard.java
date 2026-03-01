package gui.dashboards;

import config.GameConfiguration;

import javax.swing.*;
import java.awt.*;

/**
 * MacroDashboard : Zone opérationnelle située en bas de l'écran.
 * Elle est conçue comme un conteneur extensible pour accueillir les futurs
 * contrôles globaux de la simulation (boutons d'action, filtres, outils).
 */
public class MacroDashboard extends JPanel {
    private static final long serialVersionUID = 1L;

    public MacroDashboard(){
        // On utilise la configuration pour définir la hauteur, assurant une cohérence avec le reste du design
        this.setPreferredSize(new Dimension(0, GameConfiguration.MENU_BOTTOM_HEIGHT));

        // Bordure simple pour délimiter clairement la zone d'action par rapport à la carte
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setBackground(Color.LIGHT_GRAY);

        // Label de placeholder : permet d'identifier visuellement la zone durant le développement
        this.add(new JLabel("--- ZONE OPÉRATIONNELLE ---"));
    }
}