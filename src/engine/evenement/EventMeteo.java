package engine.evenement;

import engine.habitant.Habitant;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Événement météo : affecte la fatigue, le moral et les traits OCEAN.
 * Implémente EvenementSimulation : encapsule son propre critère OCEAN.
 * Implémente EventVisitor : applique l'effet via le double-dispatch.
 */
public class EventMeteo implements EvenementSimulation, EventVisitor {

	private boolean mauvaisTemps;

	public EventMeteo(boolean mauvaisTemps) {
		this.mauvaisTemps = mauvaisTemps;
	}

	@Override
	public boolean estConcerne(Habitant h) {
		if (mauvaisTemps) {
			return h.getNevrosisme() > 50;
		}
		return true;
	}

	@Override
	public void appliquer(Habitant h) {
		h.acceptEvent(this);
	}

	@Override
	public void visit(Habitant habitant) {
		if (mauvaisTemps) {
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 2);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 3);
			habitant.getPsychologie().augmenterNevrosisme(3);
		} else {
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 2);
			habitant.getPsychologie().augmenterOuverture(2);
		}
	}
}