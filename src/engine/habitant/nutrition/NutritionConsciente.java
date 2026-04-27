package engine.habitant.nutrition;

import config.RandomProvider;
import engine.habitant.besoin.Besoins;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Nutrition consciencieuse : l'habitant mange regulierement et de facon disciplinee.
 */
public class NutritionConsciente implements StrategieNutrition {

	/**
	 * Augmente la faim regulierement avec une forte probabilite.
	 */
	@Override
	public void appliquer(Besoins besoins) {
		if (RandomProvider.getInstance().nextDouble() < 0.70) {
			besoins.setFaim(besoins.getFaim() + 3);
		}
	}
}