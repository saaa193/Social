package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * État Isolé : extraverti sans réseau social.
 * Plus l'habitant est extraverti, plus l'isolement lui fait mal.
 */

public class EtatIsole implements EtatHabitant {

    @Override
    public void appliquer(Habitant habitant) {
        int malus = 1 + (habitant.getExtraversion() / 50);
        habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - malus);
    }

    public <T> T accept(EtatVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
