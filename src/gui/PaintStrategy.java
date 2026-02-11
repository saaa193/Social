package gui;

import java.awt.Color;
import java.awt.Graphics;

import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.habitant.Habitant;

public class PaintStrategy {

    // Dessine le terrain (Damier)
    public void paint(Map map, Graphics graphics) {
        int blockSize = GameConfiguration.BLOCK_SIZE;
        Block[][] blocks = map.getBlocks();

        for (int lineIndex = 0; lineIndex < map.getLineCount(); lineIndex++) {
            for (int columnIndex = 0; columnIndex < map.getColumnCount(); columnIndex++) {
                Block block = blocks[lineIndex][columnIndex];

                // Un damier gris clair / blanc pour mieux voir
                if ((lineIndex + columnIndex) % 2 == 0) {
                    graphics.setColor(Color.LIGHT_GRAY);
                    graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
                }
            }
        }
    }

    // Dessine UN habitant (appelé par la boucle dans GameDisplay)
    public void paint(Habitant habitant, Graphics graphics) {
        Block position = habitant.getPosition();
        int blockSize = GameConfiguration.BLOCK_SIZE;

        int y = position.getLine();
        int x = position.getColumn();

        // Le rond rouge (le corps)
        graphics.setColor(Color.ORANGE);
        graphics.fillOval(x * blockSize, y * blockSize, blockSize, blockSize);

        // Un petit contour noir pour faire propre (optionnel)
        graphics.setColor(Color.BLACK);
        graphics.drawOval(x * blockSize, y * blockSize, blockSize, blockSize);
    }
}