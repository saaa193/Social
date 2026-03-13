package engine.habitant.nutrition;

import engine.habitant.besoin.Besoins;

/**
 * Stratégie de nutrition pour les habitants névrosés.
 * Oublie de manger, faim remonte peu.
 */
public class NutritionNevrosee implements StrategieNutrition {
    public void appliquer(Besoins besoins) {
        if (Math.random() < 0.25) {
            besoins.setFaim(besoins.getFaim() + 1);
        }
    }
}