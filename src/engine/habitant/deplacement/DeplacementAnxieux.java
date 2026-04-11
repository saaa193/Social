package engine.habitant.deplacement;

import config.GameConfiguration;
import config.RandomProvider;
import engine.habitant.Habitant;
import engine.map.Block;
import engine.map.Map;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Déplacement anxieux : reste proche de son domicile.
 * Toutes les constantes viennent de GameConfiguration.
 */
public class DeplacementAnxieux implements StrategieDeplacement {

	@Override
	public void deplacer(Habitant habitant, Map map) {
		if (RandomProvider.getInstance().nextDouble() > GameConfiguration.PROBA_ANXIEUX_BOUGE) return;

		Block pos = habitant.getPosition();
		Block domicile = habitant.getDomicile();
		int ligne = pos.getLine();
		int colonne = pos.getColumn();

		int distLigne = Math.abs(ligne - domicile.getLine());
		int distColonne = Math.abs(colonne - domicile.getColumn());

		if (distLigne > GameConfiguration.DISTANCE_MAX_DOMICILE
				|| distColonne > GameConfiguration.DISTANCE_MAX_DOMICILE) {
			if (ligne < domicile.getLine()) ligne++;
			else if (ligne > domicile.getLine()) ligne--;
			if (colonne < domicile.getColumn()) colonne++;
			else if (colonne > domicile.getColumn()) colonne--;
		} else {
			int direction = RandomProvider.getInstance().nextInt(4);
			if (direction == 0 && ligne > 0) ligne--;
			else if (direction == 1 && ligne < map.getLineCount() - 1) ligne++;
			else if (direction == 2 && colonne > 0) colonne--;
			else if (direction == 3 && colonne < map.getColumnCount() - 1) colonne++;
		}

		habitant.setPosition(map.getBlock(ligne, colonne));
	}
}