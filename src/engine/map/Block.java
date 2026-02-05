package engine.map;

public class Block {
    private int line;
    private int column;

    public Block(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }

    public String toString() {
        return "Block [line=" + this.line + ", column=" + this.column + "]";
    }
}