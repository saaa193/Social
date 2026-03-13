package engine.habitant.nutrition;

import engine.habitant.besoin.Besoins;

/**
 * Stratégie de nutrition sociale : mange mieux quand le social est bon.
 * Profil par défaut.
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