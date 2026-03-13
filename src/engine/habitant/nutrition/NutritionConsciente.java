package engine.habitant.nutrition;

import engine.habitant.besoin.Besoins;

/**
 * Stratégie de nutrition pour les habitants consciencieux.
 * Mange régulièrement et de façon disciplinée.
 */
public class NutritionConsciente implements StrategieNutrition {
    public void appliquer(Besoins besoins) {
        if (Math.random() < 0.70) {
            besoins.setFaim(besoins.getFaim() + 3);
        }
    }
}