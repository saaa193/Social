package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * État dépressif : l'habitant perd du moral, du social et de la fatigue
 * à chaque tour. Plus grave que l'état anxieux.
 */
public class EtatDepressif implements EtatHabitant {

	@Override
	public void appliquer(Habitant habitant) {
		// Perte de moral accélérée
		habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 2);
		// Isolement social croissant
		habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() - 2);
		// Épuisement physique lié à la dépression
		habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 1);
	}

	public <T> T accept(EtatVisitor<T> visitor) {
		return visitor.visit(this);
	}
}