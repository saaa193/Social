package engine.habitant.lien;

import engine.habitant.Habitant;

public abstract class Liens {

    protected Habitant partenaire;
    protected int force;

    public Liens(Habitant partenaire, int force) {
        this.partenaire = partenaire;
        this.setForce(force); // setter pour sécuriser dès la création
    }

    public Habitant getPartenaire() { return partenaire; }
    public int getForce() { return force; }

    // Clamping : la force reste toujours entre 0 et 100
    public void setForce(int force) {
        this.force = Math.max(0, Math.min(100, force));
    }

    public abstract void appliquerBonusMental(Habitant proprietaire);

    /**
     * Fait évoluer la force du lien selon l'état des habitants.
     * @return true = lien vivant | false = lien mort à supprimer
     */
    public abstract boolean evoluerForce(Habitant proprietaire);

    // Utilitaire pour le nettoyage dans Habitant
    public boolean estMort() {
        return this.force <= 0;
    }
}