package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * État stable : aucun effet particulier appliqué sur l'habitant.
 */
public class EtatStable implements EtatHabitant {

	@Override
	public void appliquer(Habitant habitant) {
		// Rien : l'habitant est stable
	}

	public <T> T accept(EtatVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
