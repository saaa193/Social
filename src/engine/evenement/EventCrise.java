package engine.evenement;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Crise Économique : stress généralisé sur toute la population.
 * Les consciencieux résistent mieux car ils sont organisés.
 * Les névrosés s'effondrent rapidement.
 * Augmente la polarisation — inégalités visibles dans le dashboard.
 */
public class EventCrise implements EvenementSimulation, EventVisitor {

	@Override
	public boolean estConcerne(Habitant h) {
		return true;
	}

	@Override
	public void appliquer(Habitant h) {
		h.acceptEvent(this);
	}

	@Override
	public void visit(Habitant habitant) {
		if (habitant.getConscience() > 65) {
			// Consciencieux — organisés, résistent mieux
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 10);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 10);
		} else if (habitant.getNevrosisme() > 60) {
			// Névrosés — s'effondrent sous la pression
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 35);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 20);
			habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() - 15);
			habitant.getPsychologie().augmenterNevrosisme(6);
		} else {
			// Population normale — impact fort
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 20);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 15);
			habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() - 10);
			habitant.getPsychologie().augmenterNevrosisme(3);
		}
	}
}