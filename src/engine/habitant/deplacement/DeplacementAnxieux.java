package engine.habitant.deplacement;

import engine.habitant.Habitant;
import engine.map.Block;
import engine.map.Map;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Déplacement anxieux : l'habitant névrosé fuit les autres
 * et se dirige vers les bords de la carte.
 */
public class DeplacementAnxieux implements StrategieDeplacement {

	@Override
	public void deplacer(Habitant habitant, Map map) {
		Block pos = habitant.getPosition();
		int ligne = pos.getLine();
		int colonne = pos.getColumn();

		// L'anxieux essaie de s'éloigner du centre de la carte
		// Il cherche un coin isolé
		int centreL = map.getLineCount() / 2;
		int centreC = map.getColumnCount() / 2;

		// On calcule la direction qui s'éloigne du centre
		int directionLigne = (ligne < centreL) ? -1 : 1;
		int directionColonne = (colonne < centreC) ? -1 : 1;

		// On choisit aléatoirement si on bouge en ligne ou en colonne
		if (Math.random() < 0.5) {
			// Mouvement vertical — fuit vers le bord
			int nouvelleLigne = ligne + directionLigne;
			if (nouvelleLigne >= 0 && nouvelleLigne < map.getLineCount()) {
				ligne = nouvelleLigne;
			}
		} else {
			// Mouvement horizontal — fuit vers le bord
			int nouvelleColonne = colonne + directionColonne;
			if (nouvelleColonne >= 0 && nouvelleColonne < map.getColumnCount()) {
				colonne = nouvelleColonne;
			}
		}

		habitant.setPosition(map.getBlock(ligne, colonne));
	}
}