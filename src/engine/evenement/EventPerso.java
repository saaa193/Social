package engine.evenement;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Événement personnel : offres d'emploi, bonne nouvelle.
 * Critère OCEAN : conscience > 50.
 */
public class EventPerso implements EvenementSimulation, EventVisitor {

	@Override
	public boolean estConcerne(Habitant h) {
		return h.getConscience() > 50;
	}

	@Override
	public void appliquer(Habitant h) {
		h.acceptEvent(this);
	}

	@Override
	public void visit(Habitant habitant) {
		habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 20);
		habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 10);
		habitant.getPsychologie().augmenterOuverture(3);
		habitant.getPsychologie().augmenterAgreabilite(2);
		habitant.getPsychologie().diminuerNevrosisme(5);
	}
}