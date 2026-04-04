package engine.evenement;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Événement culturel : expo musée, concert.
 * Critère OCEAN : ouverture > 50.
 * Séparé d'EventPerso car le critère et l'effet sont distincts.
 */
public class EventCulturel implements EvenementSimulation, EventVisitor {

    @Override
    public boolean estConcerne(Habitant h) {
        return h.getOuverture() > 50;
    }

    @Override
    public void appliquer(Habitant h) {
        h.acceptEvent(this);
    }

    @Override
    public void visit(Habitant habitant) {
        habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 18);
        habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 8);
        habitant.getPsychologie().augmenterOuverture(5);
        habitant.getPsychologie().diminuerNevrosisme(3);
    }
}