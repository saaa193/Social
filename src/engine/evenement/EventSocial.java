package engine.evenement;

import engine.habitant.Habitant;

/**
 * Événement social : fête de quartier, rassemblement...
 * Booste le social ET modifie durablement les traits OCEAN.
 * Les névrosés en profitent moins — ils sont moins à l'aise en groupe.
 * Ancré dans le Big Five : extraversion = besoin de stimulation sociale.
 */
public class EventSocial implements EventVisitor {

    @Override
    public void visit(Habitant habitant) {
        if (habitant.getNevrosisme() > 60) {
            // Les névrosés profitent peu des événements sociaux
            habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 3);
            // Leur agréabilité monte légèrement quand même
            habitant.getPsychologie().augmenterAgreabilite(1);
        } else {
            // Les autres profitent pleinement
            habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 8);
            habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 3);
            // Effet durable — agréabilité et extraversion augmentent
            habitant.getPsychologie().augmenterAgreabilite(2);
            habitant.getPsychologie().augmenterExtraversion(2);
        }
    }
}