package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * Interface commune à tous les états psychologiques d'un Habitant.
 *
 * Principe : chaque état sait s'appliquer sur un habitant
 * sans que Habitant.java ait besoin de savoir quel état c'est.
 */

public interface EtatHabitant {

    void appliquer(Habitant habitant);
    <T> T accept(EtatVisitor<T> visitor);

}