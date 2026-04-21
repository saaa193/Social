package engine.evenement;

import engine.habitant.Habitant;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Contrat abstrait pour tous les événements de la simulation.
 * Principe Ouvert/Fermé : pour ajouter un événement,
 * on crée une nouvelle classe, on ne touche à rien d'existant.
 */
public interface EvenementSimulation {

    boolean estConcerne(Habitant h);

    void appliquer(Habitant h);

    default void declencher(List<Habitant> habitants) {
        for (Habitant h : habitants) {
            if (estConcerne(h)) {
                appliquer(h);
            }
        }
    }

    default void declencher(List<Habitant> habitants, double multiplicateur) {
        for (Habitant h : habitants) {
            if (estConcerne(h) && h.getBesoins().getSante() > 0) {
                int moralAvant = h.getBesoins().getMoral();
                appliquer(h);
                int delta = h.getBesoins().getMoral() - moralAvant;
                int bonus = (int)(delta * (multiplicateur - 1.0));
                h.getBesoins().setMoral(h.getBesoins().getMoral() + bonus);
            }
        }
    }
}