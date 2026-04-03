package engine.habitant.nutrition;

import engine.habitant.besoin.Besoins;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Nutrition névrosée : l'habitant oublie de manger, sa faim remonte peu.
 */
public class NutritionNevrosee implements StrategieNutrition {
	public void appliquer(Besoins besoins) {
		if (Math.random() < 0.25) {
			besoins.setFaim(besoins.getFaim() + 1);
		}
	}
}