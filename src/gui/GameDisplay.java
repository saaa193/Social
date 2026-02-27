package gui;

import java.awt.Graphics;
import java.awt.Color;

import javax.swing.JPanel;
import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.map.Block;
import engine.map.Map;
import engine.process.MobileInterface;


public class GameDisplay extends JPanel {
    private static final long serialVersionUID = 1L;

    private Map map;
    private MobileInterface manager;
    private PaintStrategy paintStrategy = new PaintStrategy();

    public GameDisplay(Map map, MobileInterface manager) {
        this.map = map;
        this.manager = manager;
    }
    public Block getBlockAt(int x, int y) {
        int line = y / GameConfiguration.BLOCK_SIZE;
        int column = x / GameConfiguration.BLOCK_SIZE;
        return map.getBlock(line, column);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintStrategy.paint(map, g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        for (Habitant h : manager.getHabitants()) {
            paintStrategy.paint(h, g);
        }
    }
}