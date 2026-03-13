package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * État Épanoui : moral élevé, réseau social actif.
 * L'habitant regagne un peu de social à chaque tour.
 */

public class EtatEpanoui implements EtatHabitant {

    @Override
    public void appliquer(Habitant habitant) {
        habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 2);
    }

    public <T> T accept(EtatVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
