package engine;

import engine.map.Block;
import engine.map.Map;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 */
public abstract class MobileElement {

	private Block position;
	protected Map map;
	private boolean nuit;

	public MobileElement(Block position, Map map) {
		this.position = position;
		this.map = map;
	}

	public final void executerTour(boolean estLaNuit) {
		this.nuit = estLaNuit;
		seDeplacer();
		agir(estLaNuit);
	}

	protected boolean estLaNuit() {
		return nuit;
	}

	public Block getPosition() {
		return position;
	}

	public void setPosition(Block position) {
		this.position = position;
	}

	protected abstract void seDeplacer();

	protected abstract void agir(boolean estLaNuit);
}