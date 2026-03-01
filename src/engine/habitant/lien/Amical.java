package engine.habitant.lien;

import engine.habitant.Habitant;

/**
 * Cette classe gère spécifiquement les relations de type "Amitié".
 * Elle hérite de la classe abstraite Liens.
 */
public class Amical extends Liens {

    // On utilise le constructeur de la classe parente (Liens)
    // pour stocker qui est l'ami et quelle est la force du lien.
    public Amical(Habitant partenaire, int force) {
        super(partenaire, force);
    }

    /**
     * Applique les bénéfices d'une relation amicale sur l'habitant.
     * Quand deux amis interagissent, cela a un impact positif sur leur état mental.
     */
    @Override
    public void appliquerBonusMental(Habitant proprietaire) {

        // Boost du moral : On récupère la valeur actuelle et on ajoute +15.
        // Cela simule le bien-être procuré par une interaction amicale.
        int moralActuel=proprietaire.getBesoins().getMoral();
        proprietaire.getBesoins().setMoral(moralActuel+15);

        // Boost du social : On ajoute +30.
        // Le besoin social monte plus vite que le moral, car c'est le besoin principal comblé par l'amitié.
        int socialActuel=proprietaire.getBesoins().getSocial();
        proprietaire.getBesoins().setSocial(socialActuel+30);
    }
}