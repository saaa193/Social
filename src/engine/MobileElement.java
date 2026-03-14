package engine;

import engine.map.Block;
import engine.map.Map;

// MobileElement.java
public abstract class MobileElement {

    private Block position;
    protected Map map;
    private boolean nuit; // ← attribut d'état du cycle jour/nuit

    public MobileElement(Block position, Map map) {
        this.position = position;
        this.map = map;
    }

    public final void executerTour(boolean estLaNuit) {
        this.nuit = estLaNuit; // ← on mémorise AVANT d'appeler seDeplacer
        seDeplacer();
        agir(estLaNuit);
    }

    // Accesseur protégé pour que Habitant puisse lire l'état
    protected boolean estLaNuit() { return nuit; }

    public Block getPosition() { return position; }
    public void setPosition(Block position) { this.position = position; }

    protected abstract void seDeplacer();
    protected abstract void agir(boolean estLaNuit);
}