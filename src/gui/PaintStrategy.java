package gui;

import java.awt.Color;
import java.awt.Graphics;

import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.habitant.Habitant;

public class PaintStrategy {

    public void paint(Map map, Graphics graphics) {
        int blockSize = GameConfiguration.BLOCK_SIZE;
        Block[][] blocks = map.getBlocks();

        for (int lineIndex = 0; lineIndex < map.getLineCount(); lineIndex++) {
            for (int columnIndex = 0; columnIndex < map.getColumnCount(); columnIndex++) {
                Block block = blocks[lineIndex][columnIndex];

                if ((lineIndex + columnIndex) % 2 == 0) {
                    graphics.setColor(Color.LIGHT_GRAY);
                    graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
                }
            }
        }
    }

    public void paint(Habitant habitant, Graphics graphics) {
        Block position = habitant.getPosition();
        int blockSize = GameConfiguration.BLOCK_SIZE;

        int y = position.getLine();
        int x = position.getColumn();

        int moral = habitant.getMoral();

        if (moral < 30) {
            graphics.setColor(Color.RED); // Triste / En détresse
        } else if (moral > 70) {
            graphics.setColor(Color.MAGENTA); // Heureux
        } else {
            graphics.setColor(Color.ORANGE); // Normal
        }

        graphics.fillOval(x * blockSize, y * blockSize, blockSize, blockSize);

        graphics.setColor(Color.BLACK);
        graphics.drawOval(x * blockSize, y * blockSize, blockSize, blockSize);
    }
}