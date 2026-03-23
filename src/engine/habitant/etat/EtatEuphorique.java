package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * État Euphorique : bonheur intense et énergie sociale maximale.
 * Touche les extravertis avec un fort réseau social actif.
 * Condition : extraversion > 80 ET social > 80.
 * Ancré dans le Big Five : extraversion = besoin de stimulation sociale.
 */
public class EtatEuphorique implements EtatHabitant {

    @Override
    public void appliquer(Habitant habitant) {
        // Boost social important
        habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 3);
        // Boost moral
        habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 2);
        // L'extraversion augmente légèrement
        habitant.getPsychologie().augmenterExtraversion(1);
    }

    public <T> T accept(EtatVisitor<T> visitor) {
        return visitor.visit(this);
    }
}