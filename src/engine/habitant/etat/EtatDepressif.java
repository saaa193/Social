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
		int malus = 2 + (habitant.getNevrosisme() - 60) / 10;
		if (malus < 2) {
			malus = 2;
		}
		int moral = habitant.getBesoins().getMoral();
		int social = habitant.getBesoins().getSocial();

		if (moral > 10) {
			habitant.getBesoins().setMoral(moral - malus);
		} else {
			habitant.getBesoins().setMoral(moral - 1);
		}
		if (social > 15) {
			habitant.getBesoins().setSocial(social - 1);
		}
	}

	public <T> T accept(EtatVisitor<T> visitor) {
		return visitor.visit(this);
	}
}