package engine.habitant.nutrition;

import config.RandomProvider;
import engine.habitant.besoin.Besoins;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Nutrition consciencieuse : l'habitant mange régulièrement et de façon disciplinée.
 */
public class NutritionConsciente implements StrategieNutrition {
	public void appliquer(Besoins besoins) {
		if (RandomProvider.getInstance().nextDouble() < 0.70) {
			besoins.setFaim(besoins.getFaim() + 3);
		}
	}
}