package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * État par défaut : l'habitant est stable.
 * Aucun effet particulier appliqué.
 */
public class EtatStable implements EtatHabitant {

    @Override
    public void appliquer(Habitant habitant) {
        // Rien : l'habitant est stable
    }

    public <T> T accept(EtatVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
