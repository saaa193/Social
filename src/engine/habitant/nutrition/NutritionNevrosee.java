package engine.habitant.nutrition;

import config.RandomProvider;
import engine.habitant.besoin.Besoins;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Nutrition nevrosee : l'habitant oublie de manger, sa faim remonte peu.
 */
public class NutritionNevrosee implements StrategieNutrition {

	/**
	 * Augmente la faim avec une faible probabilite.
	 */
	@Override
	public void appliquer(Besoins besoins) {
		if (RandomProvider.getInstance().nextDouble() < 0.25) {
			besoins.setFaim(besoins.getFaim() + 1);
		}
	}
}