package engine.habitant.adaptation;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Interface Strategy pour les mecanismes d'adaptation au stress.
 * Calquee sur StrategieNutrition du prof.
 * Chaque personnalite OCEAN developpe sa propre reaction
 * face aux chocs emotionnels — modele de Lazarus (1984).
 */
public interface StrategieAdaptation {

	/**
	 * Applique la reaction au stress selon le profil OCEAN.
	 * Remplace le malus fixe de Traumatisme par une reaction personnalisee.
	 *
	 * @param habitant           l'habitant qui subit le stress
	 * @param resistanceCollective la resistance collective (0 a 100)
	 */
	void appliquer(Habitant habitant, int resistanceCollective);
}