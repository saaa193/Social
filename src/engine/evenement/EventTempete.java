package engine.evenement;

import engine.habitant.Habitant;
import engine.habitant.biais.BiaisCognitif;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Tempête Urbaine : impact fort et immédiat sur toute la population.
 * Visible en 2-3 tours. Réversible — ne touche pas à la santé.
 */
public class EventTempete implements EvenementSimulation, EventVisitor {

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
		BiaisCognitif biais = habitant.getPsychologie().determinerBiais();

		if (habitant.getPsychologie().estVulnerable()) {
			int impact = biais.filtrerImpact(-45, 0.5f);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + impact);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 30);
			habitant.getPsychologie().augmenterNevrosisme(5);
		} else if (habitant.getPsychologie().estResiliant()) {
			int impact = biais.filtrerImpact(-15, 0.5f);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + impact);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 10);
		} else {
			int impact = biais.filtrerImpact(-30, 0.5f);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + impact);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 20);
			habitant.getPsychologie().augmenterNevrosisme(3);
		}
	}
}