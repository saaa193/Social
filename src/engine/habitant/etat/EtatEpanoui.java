package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * État épanoui : l'habitant est heureux et regagne du social à chaque tour.
 */
public class EtatEpanoui implements EtatHabitant {

	@Override
	public void appliquer(Habitant habitant) {
		habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 2);
	}

	public <T> T accept(EtatVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
