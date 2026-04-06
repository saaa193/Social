package engine.habitant.deplacement;

import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.map.Block;
import engine.map.Map;
import config.RandomProvider;


/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Déplacement extraverti : destination personnelle qui change
 * périodiquement. Toutes les constantes viennent de GameConfiguration.
 */
public class DeplacementExtraverti implements StrategieDeplacement {

	private int toursAvantChangement =
			RandomProvider.getInstance().nextInt(GameConfiguration.TOURS_AVANT_CHANGEMENT);

	@Override
	public void deplacer(Habitant habitant, Map map) {
		toursAvantChangement--;
		if (toursAvantChangement <= 0) {
			int nouvelleLigne   = RandomProvider.getInstance().nextInt(map.getLineCount());
			int nouvelleColonne = RandomProvider.getInstance().nextInt(map.getColumnCount());
			habitant.setDestination(map.getBlock(nouvelleLigne, nouvelleColonne));
			toursAvantChangement = GameConfiguration.TOURS_AVANT_CHANGEMENT;
		}
		avancerVers(habitant, habitant.getDestination(), map);
	}

	private void avancerVers(Habitant habitant, Block cible, Map map) {
		Block pos = habitant.getPosition();
		int ligne   = pos.getLine();
		int colonne = pos.getColumn();

		if (RandomProvider.getInstance().nextDouble() < GameConfiguration.PROBA_PAS_ALEATOIRE) {
			int direction = RandomProvider.getInstance().nextInt(4);
			if (direction == 0 && ligne > 0) ligne--;
			else if (direction == 1 && ligne < map.getLineCount() - 1) ligne++;
			else if (direction == 2 && colonne > 0) colonne--;
			else if (direction == 3 && colonne < map.getColumnCount() - 1) colonne++;
		} else {
			if (ligne < cible.getLine() && ligne + 1 < map.getLineCount()) ligne++;
			else if (ligne > cible.getLine() && ligne - 1 >= 0) ligne--;
			else if (colonne < cible.getColumn() && colonne + 1 < map.getColumnCount()) colonne++;
			else if (colonne > cible.getColumn() && colonne - 1 >= 0) colonne--;
		}

		habitant.setPosition(map.getBlock(ligne, colonne));
	}
}