package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Interface commune à tous les états psychologiques d'un habitant.
 */
public interface EtatHabitant {

	void appliquer(Habitant habitant);

	<T> T accept(EtatVisitor<T> visitor);

}