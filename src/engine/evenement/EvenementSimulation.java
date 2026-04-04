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
}