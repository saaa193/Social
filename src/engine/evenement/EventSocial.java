package engine.evenement;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Événement social : fête de quartier, rassemblement.
 * Critère OCEAN : extraversion > 40.
 */
public class EventSocial implements EvenementSimulation, EventVisitor {

	@Override
	public boolean estConcerne(Habitant h) {
		return h.getExtraversion() > 40;
	}

	@Override
	public void appliquer(Habitant h) {
		h.acceptEvent(this);
	}

	@Override
	public void visit(Habitant habitant) {
		if (habitant.getNevrosisme() > 60) {
			habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 10);
			habitant.getPsychologie().augmenterAgreabilite(2);
		} else {
			habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 20);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 15);
			habitant.getPsychologie().augmenterAgreabilite(3);
			habitant.getPsychologie().augmenterExtraversion(3);
		}
	}
}