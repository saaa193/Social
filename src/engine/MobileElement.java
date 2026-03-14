package engine;

import engine.map.Block;

/**
 * MobileElement : Classe de base abstraite.
 * Elle implémente le Design Pattern "Template Method" pour standardiser
 * le cycle de vie de tous les éléments mobiles de la simulation.
 */
public abstract class MobileElement {

    private Block position;

    public MobileElement(Block position) {
        this.position = position;
    }

    public Block getPosition() {
        return position;
    }

    public void setPosition(Block position) {
        this.position = position;
    }

    // =========================================================
    // LE PATTERN TEMPLATE METHOD (Exigé par le cours)
    // =========================================================

    /**
     * C'est le "squelette" de l'algorithme d'un tour de jeu.
     * Le mot-clé 'final' est crucial : il empêche les classes filles (comme Habitant)
     * de changer cet ordre strict (d'abord on se déplace, ensuite on agit).
     */
    public final void executerTour(boolean estLaNuit) {
        seDeplacer();
        agir(estLaNuit);
    }

    // Méthodes abstraites que les classes filles (Habitant) DOIVENT obligatoirement implémenter.
    // C'est ce qui crée le "contrat".

    protected abstract void seDeplacer();

    protected abstract void agir(boolean estLaNuit);
}