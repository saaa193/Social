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
 * Déplacement stable : mouvement aléatoire dans les 4 directions
 * pour un habitant psychologiquement stable.
 */
public class DeplacementStable implements StrategieDeplacement {

	@Override
	public void deplacer(Habitant habitant, Map map) {
		// Choix aléatoire d'une direction (0=haut, 1=bas, 2=gauche, 3=droite)
		int direction = (int) (Math.random() * 4);

		Block pos = habitant.getPosition();
		int ligne = pos.getLine();
		int colonne = pos.getColumn();

		// On vérifie les bords avant de bouger
		if (direction == 0 && !map.isOnTop(pos)) {
			ligne--;
		} else if (direction == 1 && !map.isOnBottom(pos)) {
			ligne++;
		} else if (direction == 2 && !map.isOnLeftBorder(pos)) {
			colonne--;
		} else if (direction == 3 && !map.isOnRightBorder(pos)) {
			colonne++;
		}

		habitant.setPosition(map.getBlock(ligne, colonne));
	}
}