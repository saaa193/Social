package engine.habitant.nutrition;

import engine.habitant.besoin.Besoins;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Interface définissant la stratégie de nutrition d'un habitant.
 */
public interface StrategieNutrition {
	void appliquer(Besoins besoins);
}