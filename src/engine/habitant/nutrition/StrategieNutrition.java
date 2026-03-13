package engine.habitant.nutrition;

import engine.habitant.besoin.Besoins;

/**
 * Interface Strategy : définit comment un habitant gère sa faim
 * selon son profil OCEAN.
 */
public interface StrategieNutrition {
    void appliquer(Besoins besoins);
}