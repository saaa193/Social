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
 * Déplacement anxieux : l'habitant reste proche de son domicile.
 * Il bouge peu et revient toujours vers sa position de départ.
 */
public class DeplacementAnxieux implements StrategieDeplacement {

	@Override
	public void deplacer(Habitant habitant, Map map) {
		// 70% du temps il reste sur place
		if (Math.random() > 0.30) return;

		// Sinon petit déplacement aléatoire mais limité
		Block pos = habitant.getPosition();
		Block domicile = habitant.getDomicile();
		int ligne   = pos.getLine();
		int colonne = pos.getColumn();

		// Si trop loin du domicile (> 5 cases) → retour domicile
		int distLigne   = Math.abs(ligne - domicile.getLine());
		int distColonne = Math.abs(colonne - domicile.getColumn());

		if (distLigne > 5 || distColonne > 5) {
			// Retour vers le domicile
			if (ligne < domicile.getLine()) ligne++;
			else if (ligne > domicile.getLine()) ligne--;
			if (colonne < domicile.getColumn()) colonne++;
			else if (colonne > domicile.getColumn()) colonne--;
		} else {
			// Petit mouvement aléatoire
			int direction = (int)(Math.random() * 4);
			if (direction == 0 && ligne > 0) ligne--;
			else if (direction == 1 && ligne < map.getLineCount() - 1) ligne++;
			else if (direction == 2 && colonne > 0) colonne--;
			else if (direction == 3 && colonne < map.getColumnCount() - 1) colonne++;
		}

		habitant.setPosition(map.getBlock(ligne, colonne));
	}
}