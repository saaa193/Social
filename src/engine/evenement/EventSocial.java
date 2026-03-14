package engine.evenement;

import engine.habitant.Habitant;

/**
 * Événement social : fête de quartier, rassemblement...
 * Booste le social des habitants. Les névrosés en profitent moins.
 */
public class EventSocial implements EventVisitor {

    @Override
    public void visit(Habitant habitant) {
        if (habitant.getNevrosisme() > 60) {
            habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 3);
        } else {
            habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 8);
            habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 3);
        }
    }

}