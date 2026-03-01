package engine.habitant.lien;

import engine.habitant.Habitant;

/**
 * Classe représentant une relation professionnelle.
 * Contrairement aux liens amicaux ou familiaux, ce lien introduit une notion de compromis.
 */
public class Professionnel extends Liens {

    public Professionnel(Habitant partenaire, int force) {
        super(partenaire, force);
    }

    /**
     * Applique les effets du travail sur les besoins de l'habitant.
     * Le travail comble le besoin social, mais consomme de l'énergie physique.
     */
    @Override
    public void appliquerBonusMental(Habitant proprietaire) {
        // Gain social (+15) : L'interaction avec des collègues répond au besoin de contact humain.
        int socialActuel = proprietaire.getBesoins().getSocial();
        proprietaire.getBesoins().setSocial(socialActuel + 15);

        // Perte d'énergie (-10) : On simule la fatigue liée à l'activité professionnelle.
        // Cela oblige l'habitant à gérer son temps entre vie sociale et repos.
        int fatigueActuelle = proprietaire.getBesoins().getFatigue();
        proprietaire.getBesoins().setFatigue(fatigueActuelle - 10);
    }
}