package engine.habitant.deplacement;

import engine.habitant.Habitant;
import engine.map.Map;

/**
 * Interface Strategy : définit comment un habitant se déplace
 * selon son profil psychologique OCEAN.
 * Même principe que StrategieNutrition.
 */
public interface StrategieDeplacement {
    void deplacer(Habitant habitant, Map map);
}