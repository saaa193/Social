package gui;

import java.awt.Color;
import java.awt.Graphics;

import engine.habitant.lien.Familial;
import engine.habitant.lien.Liens;
import config.GameConfiguration;
import engine.habitant.lien.Professionnel;
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
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
                }
            }
        }
    }

    public void paint(Habitant habitant, Graphics graphics) {
        Block position = habitant.getPosition();
        int blockSize = GameConfiguration.BLOCK_SIZE;

        int x1 = position.getColumn() * blockSize + blockSize / 2;
        int y1 = position.getLine() * blockSize + blockSize / 2;

        if (habitant.getRelation() != null) {
            for (Liens lien : habitant.getRelation()) {
                Habitant ami = lien.getPartenaire();

                // On choisit la couleur du trait selon le type de lien
                if (lien instanceof Familial) {
                    graphics.setColor(Color.PINK); // Rose pour Famille
                } else if (lien instanceof Professionnel) {
                    graphics.setColor(Color.BLUE); // Bleu pour Travail
                } else {
                    graphics.setColor(Color.GREEN); // Vert pour Amis
                }


                Block posAmi = ami.getPosition();
                int x2 = posAmi.getColumn() * blockSize + blockSize / 2;
                int y2 = posAmi.getLine() * blockSize + blockSize / 2;

                // On dessine la ligne entre les deux potes
                graphics.drawLine(x1, y1, x2, y2);

            }

        }
            int moral = habitant.getMoral();
            int fatigue = habitant.getBesoins().getFatigue();
            int sante = habitant.getBesoins().getSante();

            if (sante <= 0) {
                graphics.setColor(Color.black); // Décès
            } else if (fatigue < 20) {
                graphics.setColor(Color.GRAY); // Sommeil
            } else if (moral < 30) {
                graphics.setColor(Color.RED); // Détresse
            } else if (moral < 70) {
                graphics.setColor(Color.ORANGE); // Neutre
            } else {
                graphics.setColor(new Color(128, 0, 128)); //Bonheur
            }


            int y = position.getLine();
            int x = position.getColumn();

            graphics.fillOval(x * blockSize, y * blockSize, blockSize, blockSize);

            graphics.setColor(Color.BLACK);
            graphics.drawOval(x * blockSize, y * blockSize, blockSize, blockSize);
        }
    }
