package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * État isolé : l'habitant extraverti sans réseau social perd du moral
 * proportionnellement à son extraversion.
 */

public class EtatIsole implements EtatHabitant {

	@Override
	public void appliquer(Habitant habitant) {
		int malus = 1 + (habitant.getExtraversion() - 70) / 10;
		if (malus < 1) {
			malus = 1;
		}
		habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - malus);
	}

	public <T> T accept(EtatVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
