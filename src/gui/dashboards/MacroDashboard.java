package gui.dashboards;

import config.GameConfiguration;

import javax.swing.*;
import java.awt.*;

public class MacroDashboard extends JPanel {
    private static final long serialVersionUID = 1L;

    public MacroDashboard(){
        this.setPreferredSize(new Dimension(0, GameConfiguration.MENU_BOTTOM_HEIGHT));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setBackground(Color.LIGHT_GRAY);
        this.add(new JLabel("--- ZONE OPÉRATIONNELLE ---"));
    }
}
