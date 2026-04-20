package engine.evenement;

import engine.habitant.Habitant;
import engine.habitant.biais.BiaisCognitif;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Semaine Culturelle : boost moral visible immediatement.
 * Moins fort que le Festival. Les ouverts en profitent davantage.
 */
public class EventCulturel implements EvenementSimulation, EventVisitor {

	@Override
	public boolean estConcerne(Habitant h) {
		return h.getOuverture() > 40;
	}

	@Override
	public void appliquer(Habitant h) {
		h.acceptEvent(this);
	}

	@Override
	public void visit(Habitant habitant) {
		BiaisCognitif biais = habitant.getPsychologie().determinerBiais();

		if (habitant.getOuverture() > 65) {
			int impact = biais.filtrerImpact(25, 1.0f);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + impact);
			habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 20);
			habitant.getPsychologie().augmenterOuverture(3);
			habitant.getPsychologie().diminuerNevrosisme(3);
		} else {
			int impact = biais.filtrerImpact(15, 1.0f);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + impact);
			habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 10);
			habitant.getPsychologie().augmenterOuverture(1);
		}
	}
}