package engine.habitant.lien;

import engine.habitant.Habitant;

/**
 * Classe représentant une relation familiale.
 * Elle se distingue du lien Amical par une priorité donnée à la stabilité émotionnelle (Moral).
 */
public class Familial extends Liens {

    public Familial(Habitant partenaire, int force) {
        super(partenaire, force);
    }

    /**
     * Applique les effets d'un lien familial sur les besoins de l'habitant.
     * Le bonus de moral est plus élevé (+30) car la famille est une source de réconfort profond.
     */
    @Override
    public void appliquerBonusMental(Habitant proprietaire) {

        // Boost important du moral (+30) : le soutien familial est vital pour le bien-être.
        int moralActuel = proprietaire.getBesoins().getMoral();
        proprietaire.getBesoins().setMoral(moralActuel + 30);

        // Boost social (+20) : un peu moins que l'amitié, car c'est un lien plus durable et moins centré sur l'événementiel.
        int socialActuel = proprietaire.getBesoins().getSocial();
        proprietaire.getBesoins().setSocial(socialActuel + 20);
    }
}