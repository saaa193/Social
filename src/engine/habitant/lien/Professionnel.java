package engine.habitant.lien;

import engine.habitant.Habitant;

public class Professionnel extends Liens {

    public Professionnel(Habitant partenaire, int force) {
        super(partenaire, force);
    }

    @Override
    public void appliquerBonusMental(Habitant proprietaire) {
        //Les collegues augmentent la vie social
        int socialActuel = proprietaire.getBesoins().getSocial();
        proprietaire.getBesoins().setSocial(socialActuel + 15);

        //Et le travail c'est fatiguant
        int fatigueActuelle = proprietaire.getBesoins().getFatigue();
        proprietaire.getBesoins().setFatigue(fatigueActuelle - 10);
    }
}
