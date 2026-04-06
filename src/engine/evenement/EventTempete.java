package engine.evenement;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Tempête Urbaine : événement catastrophique qui frappe
 * toute la population différemment selon leur profil OCEAN.
 * Les névrosés paniquent, les résilients résistent.
 * Effet visible immédiat sur la carte — vague de rouge.
 */
public class EventTempete implements EvenementSimulation, EventVisitor {

	@Override
	public boolean estConcerne(Habitant h) {
		// Tout le monde est concerné par une tempête
		return true;
	}

	@Override
	public void appliquer(Habitant h) {
		h.acceptEvent(this);
	}

	@Override
	public void visit(Habitant habitant) {
		if (habitant.getPsychologie().estVulnerable()) {
			// Vulnérables — panique totale
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 35);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 25);
			habitant.getPsychologie().augmenterNevrosisme(8);
		} else if (habitant.getPsychologie().estResiliant()) {
			// Résilients — résistent bien
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 10);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 5);
		} else {
			// Population normale — impact modéré
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 20);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 15);
			habitant.getPsychologie().augmenterNevrosisme(4);
		}
	}
}