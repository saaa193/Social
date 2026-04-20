package engine.evenement;

import engine.habitant.Habitant;
import engine.habitant.biais.BiaisCognitif;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Festival de Quartier : joie collective visible immediatement.
 * Les extravertis profitent pleinement, les introvertis subissent.
 */
public class EventFestival implements EvenementSimulation, EventVisitor {

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

		if (habitant.getExtraversion() > 60) {
			int impact = biais.filtrerImpact(35, 1.0f);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + impact);
			habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 25);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 10);
			habitant.getPsychologie().augmenterExtraversion(2);
		} else if (habitant.getExtraversion() < 35) {
			int impact = biais.filtrerImpact(-8, 1.0f);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + impact);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 10);
			habitant.getPsychologie().augmenterNevrosisme(2);
		} else {
			int impact = biais.filtrerImpact(15, 1.0f);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + impact);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 5);
		}
	}
}