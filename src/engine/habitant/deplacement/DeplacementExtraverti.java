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
 * Déplacement extraverti : chaque habitant a sa propre destination
 * personnelle qui change périodiquement. Évite l'entassement au centre.
 * Calqué sur Strategy du prof — comportement variable injecté.
 */
public class DeplacementExtraverti implements StrategieDeplacement {

	// Désynchronisation : chaque habitant démarre à un moment différent
	private int toursAvantChangement = (int)(Math.random() * 15);

	@Override
	public void deplacer(Habitant habitant, Map map) {
		// Tous les 15 tours, choisit une nouvelle destination personnelle
		toursAvantChangement--;
		if (toursAvantChangement <= 0) {
			int nouvelleLigne   = (int)(Math.random() * map.getLineCount());
			int nouvelleColonne = (int)(Math.random() * map.getColumnCount());
			habitant.setDestination(map.getBlock(nouvelleLigne, nouvelleColonne));
			toursAvantChangement = 15;
		}

		// Avance d'un pas vers SA destination personnelle
		avancerVers(habitant, habitant.getDestination(), map);
	}

	private void avancerVers(Habitant habitant, Block cible, Map map) {
		Block pos = habitant.getPosition();
		int ligne   = pos.getLine();
		int colonne = pos.getColumn();

		// 20% du temps on fait un pas aléatoire pour éviter les groupes
		if (Math.random() < 0.20) {
			int direction = (int)(Math.random() * 4);
			if (direction == 0 && ligne > 0) ligne--;
			else if (direction == 1 && ligne < map.getLineCount() - 1) ligne++;
			else if (direction == 2 && colonne > 0) colonne--;
			else if (direction == 3 && colonne < map.getColumnCount() - 1) colonne++;
		} else {
			// Sinon avance vers la destination personnelle
			if (ligne < cible.getLine() && ligne + 1 < map.getLineCount()) {
				ligne++;
			} else if (ligne > cible.getLine() && ligne - 1 >= 0) {
				ligne--;
			} else if (colonne < cible.getColumn() && colonne + 1 < map.getColumnCount()) {
				colonne++;
			} else if (colonne > cible.getColumn() && colonne - 1 >= 0) {
				colonne--;
			}
		}

		habitant.setPosition(map.getBlock(ligne, colonne));
	}
}