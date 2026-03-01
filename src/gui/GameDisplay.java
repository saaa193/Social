package gui;

import java.awt.Graphics;
import java.awt.Color;

import javax.swing.JPanel;
import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.map.Block;
import engine.map.Map;
import engine.process.MobileInterface;

/**
 * GameDisplay : Composant Swing responsable du rendu graphique.
 * Il utilise une stratégie de dessin (PaintStrategy) pour séparer la logique de rendu de la logique de contrôle.
 */
public class GameDisplay extends JPanel {
    private static final long serialVersionUID = 1L;

    private Map map;
    private MobileInterface manager;

    // Pattern Strategy : la logique de dessin est déléguée à cet objet
    private PaintStrategy paintStrategy = new PaintStrategy();

    public GameDisplay(Map map, MobileInterface manager) {
        this.map = map;
        this.manager = manager;
    }

    /**
     * Méthode utilitaire pour convertir les coordonnées écran (pixels)
     * en coordonnées logiques (ligne/colonne de la carte).
     */
    public Block getBlockAt(int x, int y) {
        int line = y / GameConfiguration.BLOCK_SIZE;
        int column = x / GameConfiguration.BLOCK_SIZE;
        return map.getBlock(line, column);
    }

    /**
     * Méthode principale de rendu graphique.
     * Appelle automatiquement le cycle de peinture de Swing.
     */
    @Override
    public void paintComponent(Graphics g) {
        // Appelle le nettoyage standard de Swing
        super.paintComponent(g);
        g.fillRect(0, 0, getWidth(), getHeight());


        // 1. Dessin de la carte (le décor)
        paintStrategy.paint(map, g);

        g.setColor(Color.WHITE);

        // 2. Dessin des habitants (les éléments mobiles)
        for (Habitant h : manager.getHabitants()) {
            paintStrategy.paint(h, g);
        }
    }
}