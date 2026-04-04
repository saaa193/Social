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
 * Déplacement introverti : l'habitant introverti bouge peu
 * et reste dans sa zone la plupart du temps.
 */
public class DeplacementIntroverti implements StrategieDeplacement {

	@Override
	public void deplacer(Habitant habitant, Map map) {
		// L'introverti bouge 60% du temps (au lieu de 30%)
		// Il bouge, mais lentement et sans chercher les autres
		if (Math.random() > 0.55) {
			return;
		}

		// Quand il bouge, c'est un petit déplacement aléatoire
		Block pos = habitant.getPosition();
		int ligne = pos.getLine();
		int colonne = pos.getColumn();

		int direction = (int) (Math.random() * 4);

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