package engine.habitant.deplacement;

import engine.habitant.Habitant;
import engine.map.Map;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Interface définissant la stratégie de déplacement d'un habitant.
 */
public interface StrategieDeplacement {
	void deplacer(Habitant habitant, Map map);
}