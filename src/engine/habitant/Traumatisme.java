package engine.habitant;

import engine.habitant.adaptation.StrategieAdaptation;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Traumatisme : represente une sequelle psychologique permanente.
 * Se declenche quand un habitant reste dans un etat negatif trop longtemps.
 * Delegue la reaction au stress a la StrategieAdaptation de l'habitant.
 * Modele transactionnel du stress de Lazarus (1984).
 */
public class Traumatisme {

	/**
	 * Applique les sequelles permanentes sur le profil OCEAN de l'habitant.
	 * Delegue a la strategie d'adaptation determinee par le profil OCEAN.
	 * Chaque personnalite reagit differemment au choc emotionnel.
	 *
	 * @param habitant             l'habitant qui subit le traumatisme
	 * @param resistanceCollective la resistance collective (0 a 100)
	 */
	public void appliquer(Habitant habitant, int resistanceCollective) {
		StrategieAdaptation strategie = habitant.getPsychologie().determinerStrategieAdaptation();
		strategie.appliquer(habitant, resistanceCollective);
	}
}