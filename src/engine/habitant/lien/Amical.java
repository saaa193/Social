package engine.habitant.lien;

import engine.habitant.Habitant;

public class Amical extends Liens {

    public Amical(Habitant partenaire, int force) {
        super(partenaire, force);
    }

    @Override
    public void appliquerBonusMental(Habitant proprietaire) {

        int moralActuel=proprietaire.getBesoins().getMoral();
        proprietaire.getBesoins().setMoral(moralActuel+15);

        int socialActuel=proprietaire.getBesoins().getSocial();
        proprietaire.getBesoins().setSocial(socialActuel+30);
    }
}
