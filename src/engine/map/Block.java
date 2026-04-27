package engine.map;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 */
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

	@Override
	public String toString() {
		return "Block [line=" + this.line + ", column=" + this.column + "]";
	}
}