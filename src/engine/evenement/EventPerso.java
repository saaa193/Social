package engine.evenement;

import engine.habitant.Habitant;

/**
 * Événement personnel : anniversaire, bonne nouvelle...
 * Remonte le moral et le social de l'habitant concerné.
 */
public class EventPerso implements EventVisitor {

    @Override
    public void visit(Habitant habitant) {
        habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 10);
        habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 5);
    }

}