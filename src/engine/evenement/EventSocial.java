package engine.evenement;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Événement social : booste le social et modifie les traits OCEAN.
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