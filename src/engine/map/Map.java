package engine.map;

import engine.habitant.Habitant;
import java.util.HashMap;

/**
 * Classe Map : Représente la grille de jeu.
 */
public class Map {
    private Block[][] blocks;

    private HashMap<Block, Habitant> habitants = new HashMap<Block, Habitant>();

    private int lineCount;
    private int columnCount;

    public Map(int lineCount, int columnCount) {
        init(lineCount, columnCount);

        for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                blocks[lineIndex][columnIndex] = new Block(lineIndex, columnIndex);
            }
        }
    }

    public HashMap<Block, Habitant> getHabitants() {
        return habitants;
    }

    public void setHabitants(HashMap<Block, Habitant> habitants) {
        this.habitants = habitants;
    }

    private void init(int lineCount, int columnCount) {
        this.lineCount = lineCount;
        this.columnCount = columnCount;
        blocks = new Block[lineCount][columnCount];
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public Block getBlock(int line, int column) {
        return blocks[line][column];
    }

    // --- LOGIQUE DE GESTION DES BORDURES ---
    // Ces méthodes sont essentielles pour la "collision detection" :
    // elles empêchent les habitants de sortir de la grille.

    public boolean isOnTop(Block block) {
        return block.getLine() == 0;
    }
    public boolean isOnBottom(Block block) {
        return block.getLine() == lineCount - 1;
    }

    public boolean isOnLeftBorder(Block block) {
        return block.getColumn() == 0;
    }

    public boolean isOnRightBorder(Block block) {
        return block.getColumn() == columnCount - 1;
    }

    // Méthode pour vérifier si un bloc est sur n'importe quel bord
    public boolean isOnBorder(Block block) {
        return isOnTop(block) || isOnBottom(block) || isOnLeftBorder(block) || isOnRightBorder(block);
    }
}