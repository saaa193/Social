package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * État anxieux : l'habitant névrosé perd du social et du moral à chaque tour.
 */

public class EtatAnxieux implements EtatHabitant {

	@Override
	public void appliquer(Habitant habitant) {
		habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() - 1);
		habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 1);
	}

	public <T> T accept(EtatVisitor<T> visitor) {
		return visitor.visit(this);
	}

}
