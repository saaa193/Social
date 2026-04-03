package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * État euphorique : l'habitant extraverti gagne du social, du moral
 * et de l'extraversion à chaque tour.
 */
public class EtatEuphorique implements EtatHabitant {

	@Override
	public void appliquer(Habitant habitant) {
		// Boost social important
		habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 3);
		// Boost moral
		habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 2);
		// L'extraversion augmente légèrement
		habitant.getPsychologie().augmenterExtraversion(1);
	}

	public <T> T accept(EtatVisitor<T> visitor) {
		return visitor.visit(this);
	}
}