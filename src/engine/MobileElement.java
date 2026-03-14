package engine;

import engine.map.Block;
import engine.map.Map;

public abstract class MobileElement {

    private Block position;
    protected Map map; // ← La map est nécessaire pour se déplacer

    public MobileElement(Block position, Map map) {
        this.position = position;
        this.map = map;
    }

    public Block getPosition() { return position; }
    public void setPosition(Block position) { this.position = position; }

    /** Template Method : squelette du tour, ordre garanti par 'final' */
    public final void executerTour(boolean estLaNuit) {
        seDeplacer();
        agir(estLaNuit);
    }

    protected abstract void seDeplacer();
    protected abstract void agir(boolean estLaNuit);
}