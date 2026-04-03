package engine.habitant.nutrition;

import engine.habitant.besoin.Besoins;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Nutrition sociale : l'habitant mange mieux quand son social est élevé.
 */
public class NutritionSociale implements StrategieNutrition {
	public void appliquer(Besoins besoins) {
		if (besoins.getSocial() > 50) {
			besoins.setFaim(besoins.getFaim() + 4);
		} else {
			besoins.setFaim(besoins.getFaim() + 1);
		}
	}
}