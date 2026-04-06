package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * État burnout : l'habitant consciencieux s'épuise et perd du moral
 * et de la conscience à chaque tour.
 */
public class EtatBurnout implements EtatHabitant {

	@Override
	public void appliquer(Habitant habitant) {
		int malus = 2 + (habitant.getConscience() - 80) / 10;
		if (malus < 2) {
			malus = 2;
		}
		habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - malus);
		habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 2);
		habitant.getPsychologie().diminuerConscience(2);
	}

	public <T> T accept(EtatVisitor<T> visitor) {
		return visitor.visit(this);
	}
}