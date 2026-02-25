package engine.habitant.lien;

import engine.habitant.Habitant;

public abstract class Liens {
    protected Habitant partenaire;
    protected int force;

    public Liens(Habitant partenaire, int force) {
        this.partenaire = partenaire;
        this.force = force;
    }

    public Habitant getPartenaire() {
        return partenaire;
    }
    public int getForce() {
        return force;
    }

    public abstract void appliquerBonusMental(Habitant proprietaire);
}
