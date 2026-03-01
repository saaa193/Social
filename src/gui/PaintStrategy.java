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

/**
 * PaintStrategy : La classe responsable du rendu visuel (Le "Renderer").
 * Elle implémente le Pattern Strategy pour déléguer le dessin des éléments
 * (Map et Habitant) sans polluer leurs classes respectives.
 */
public class PaintStrategy {

    /**
     * Dessine la grille de la carte.
     * Utilise un motif en damier pour une meilleure lisibilité spatiale.
     */
    public void paint(Map map, Graphics graphics) {
        int blockSize = GameConfiguration.BLOCK_SIZE;
        Block[][] blocks = map.getBlocks();

        for (int lineIndex = 0; lineIndex < map.getLineCount(); lineIndex++) {
            for (int columnIndex = 0; columnIndex < map.getColumnCount(); columnIndex++) {
                Block block = blocks[lineIndex][columnIndex];

                // Alternance de couleur pour créer un effet de damier (1 case sur 2)
                if ((lineIndex + columnIndex) % 2 == 0) {
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
                }
            }
        }
    }

    /**
     * Dessine un habitant et ses relations sociales.
     * Le rendu est dynamique : la couleur dépend de l'état biologique/moral.
     */
    public void paint(Habitant habitant, Graphics graphics) {
        Block position = habitant.getPosition();
        int blockSize = GameConfiguration.BLOCK_SIZE;

        // Centre du bloc pour dessiner les lignes de relation
        int x1 = position.getColumn() * blockSize + blockSize / 2;
        int y1 = position.getLine() * blockSize + blockSize / 2;

        // 1. DESSIN DES LIENS SOCIAUX (Dessinés en premier pour être en arrière-plan)
        if (habitant.getRelation() != null) {
            for (Liens lien : habitant.getRelation()) {
                Habitant ami = lien.getPartenaire();

                // Code couleur par type de relation (Polymorphisme des liens)
                if (lien instanceof Familial) {
                    graphics.setColor(Color.PINK); // Famille = Rose
                } else if (lien instanceof Professionnel) {
                    graphics.setColor(Color.BLUE); // Travail = Bleu
                } else {
                    graphics.setColor(Color.GREEN); // Amis = Vert
                }

                Block posAmi = ami.getPosition();
                int x2 = posAmi.getColumn() * blockSize + blockSize / 2;
                int y2 = posAmi.getLine() * blockSize + blockSize / 2;

                // Ligne entre les deux habitants
                graphics.drawLine(x1, y1, x2, y2);
            }
        }

        // 2. DESSIN DE L'HABITANT (Le point de couleur selon son état)
        int moral = habitant.getMoral();
        int fatigue = habitant.getBesoins().getFatigue();
        int sante = habitant.getBesoins().getSante();

        // Système de couleurs dynamiques (Visualisation des besoins)
        if (sante <= 0) {
            graphics.setColor(Color.black); // Décès
        } else if (fatigue < 20) {
            graphics.setColor(Color.GRAY); // Sommeil
        } else if (moral < 30) {
            graphics.setColor(Color.RED); // Détresse
        } else if (moral < 70) {
            graphics.setColor(Color.ORANGE); // Neutre
        } else {
            graphics.setColor(new Color(128, 0, 128)); // Bonheur (Violet)
        }

        int y = position.getLine();
        int x = position.getColumn();

        // Dessin du cercle (Habitant)
        graphics.fillOval(x * blockSize, y * blockSize, blockSize, blockSize);

        // Ajout d'une bordure noire pour un meilleur contraste
        graphics.setColor(Color.BLACK);
        graphics.drawOval(x * blockSize, y * blockSize, blockSize, blockSize);
    }
}