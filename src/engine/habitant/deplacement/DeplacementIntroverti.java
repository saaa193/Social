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
 * Déplacement introverti : bouge peu, reste dans sa zone.
 * Toutes les constantes viennent de GameConfiguration.
 */
public class DeplacementIntroverti implements StrategieDeplacement {

	@Override
	public void deplacer(Habitant habitant, Map map) {
		if (RandomProvider.getInstance().nextDouble() > GameConfiguration.PROBA_INTROVERTI_BOUGE) return;

		Block pos = habitant.getPosition();
		int ligne = pos.getLine();
		int colonne = pos.getColumn();

		int direction = RandomProvider.getInstance().nextInt(4);

		if (direction == 0 && !map.isOnTop(pos)) ligne--;
		else if (direction == 1 && !map.isOnBottom(pos)) ligne++;
		else if (direction == 2 && !map.isOnLeftBorder(pos)) colonne--;
		else if (direction == 3 && !map.isOnRightBorder(pos)) colonne++;

		habitant.setPosition(map.getBlock(ligne, colonne));
	}
}