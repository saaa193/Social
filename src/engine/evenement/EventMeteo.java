package engine.evenement;

import engine.habitant.Habitant;

/**
 * Événement météo : affecte la fatigue et le moral selon le temps.
 */
public class EventMeteo implements EventVisitor {

    private boolean mauvaisTemps;

    public EventMeteo(boolean mauvaisTemps) {
        this.mauvaisTemps = mauvaisTemps;
    }

    @Override
    public void visit(Habitant habitant) {
        if (mauvaisTemps) {
            habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 2);
            habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 1);
        } else {
            habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 2);
        }
    }

}