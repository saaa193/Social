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
		int moral = habitant.getBesoins().getMoral();
		int social = habitant.getBesoins().getSocial();

		// Perte de moral ralentie quand on est déjà très bas
		// Un dépressif à moral=5 ne chute pas aussi vite qu'à moral=18
		// C'est le concept de "plancher psychologique" — on touche un fond
		if (moral > 10) {
			habitant.getBesoins().setMoral(moral - 2);
		} else {
			habitant.getBesoins().setMoral(moral - 1);
		}

		// Isolement social — mais ralenti aussi
		if (social > 15) {
			habitant.getBesoins().setSocial(social - 1);
		}

		// PAS de perte de fatigue ici
		// Un dépressif dort PLUS, pas moins — c'est médicalement correct
	}

	public <T> T accept(EtatVisitor<T> visitor) {
		return visitor.visit(this);
	}
}