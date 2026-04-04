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
		// L'euphorie booste, mais avec un rendement décroissant
		// Si le social est déjà haut, le bonus est plus faible
		int social = habitant.getBesoins().getSocial();
		int moral = habitant.getBesoins().getMoral();

		if (social < 80) {
			habitant.getBesoins().setSocial(social + 2);
		} else {
			// Déjà très sociable → petit boost seulement
			habitant.getBesoins().setSocial(social + 1);
		}

		if (moral < 85) {
			habitant.getBesoins().setMoral(moral + 1);
		}
		// Au-dessus de 85 → l'euphorie ne booste plus le moral
		// Ça empêche le moral de rester collé à 100
	}

	public <T> T accept(EtatVisitor<T> visitor) {
		return visitor.visit(this);
	}
}