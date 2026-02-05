package engine.map;

/**
 * Classe Map adaptée pour le projet SOCIAL.
 * Gère la grille de la ville et les limites du terrain.
 */
public class Map {
    private Block[][] blocks;
    private int lineCount;
    private int columnCount;

    public Map(int lineCount, int columnCount) {
        init(lineCount, columnCount);

        for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                // Initialise chaque case de la ville
                blocks[lineIndex][columnIndex] = new Block(lineIndex, columnIndex);
            }
        }
    }

    private void init(int lineCount, int columnCount) {
        this.lineCount = lineCount;
        this.columnCount = columnCount;
        blocks = new Block[lineCount][columnCount];
    }

    // --- Getters standards ---
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

    // --- Gestion des bordures (utile pour empêcher les gens de sortir de la ville) ---
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

    public boolean isOnBorder(Block block) {
        return isOnTop(block) || isOnBottom(block) || isOnLeftBorder(block) || isOnRightBorder(block);
    }
}
