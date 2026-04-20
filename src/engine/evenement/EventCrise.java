package engine.evenement;

import engine.habitant.Habitant;
import engine.habitant.biais.BiaisCognitif;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Crise Economique : stress modere sur toute la population.
 * Moins fort que la Tempete. Reversible — ne touche pas a la sante.
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
		BiaisCognitif biais = habitant.getPsychologie().determinerBiais();

		if (habitant.getConscience() > 65) {
			int impact = biais.filtrerImpact(-15, 0.5f);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + impact);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 10);
		} else if (habitant.getNevrosisme() > 60) {
			int impact = biais.filtrerImpact(-25, 0.5f);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + impact);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 15);
			habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() - 10);
			habitant.getPsychologie().augmenterNevrosisme(3);
		} else {
			int impact = biais.filtrerImpact(-20, 0.5f);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + impact);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 10);
			habitant.getPsychologie().augmenterNevrosisme(2);
		}
	}
}