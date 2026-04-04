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
 * Déplacement extraverti : l'habitant se déplace vers une destination
 * personnelle qui change aléatoirement. Il explore la carte mais
 * revient régulièrement vers son domicile.
 * Calqué sur Strategy du prof — comportement variable injecté.
 */
public class DeplacementExtraverti implements StrategieDeplacement {

	private int toursAvantChangement = 0;

	@Override
	public void deplacer(Habitant habitant, Map map) {
		Block pos = habitant.getPosition();
		int ligne = pos.getLine();
		int colonne = pos.getColumn();

		// Zone cible — plus dispersée pour éviter l'entassement
		// L'extraverti cherche une zone sociale, pas un point précis
		int centreL = map.getLineCount() / 2 + (int)(Math.random() * 200) - 100;
		int centreC = map.getColumnCount() / 2 + (int)(Math.random() * 200) - 100;

		int directionLigne = 0;
		if (ligne < centreL) directionLigne = 1;
		else if (ligne > centreL) directionLigne = -1;

		int directionColonne = 0;
		if (colonne < centreC) directionColonne = 1;
		else if (colonne > centreC) directionColonne = -1;

		if (Math.random() < 0.5 && directionLigne != 0) {
			ligne = ligne + directionLigne;
		} else if (directionColonne != 0) {
			colonne = colonne + directionColonne;
		}

		if (ligne < 0) ligne = 0;
		if (ligne >= map.getLineCount()) ligne = map.getLineCount() - 1;
		if (colonne < 0) colonne = 0;
		if (colonne >= map.getColumnCount()) colonne = map.getColumnCount() - 1;

		habitant.setPosition(map.getBlock(ligne, colonne));
	}

	private void avancerVers(Habitant habitant, Block cible, Map map) {
		Block pos = habitant.getPosition();
		int ligne   = pos.getLine();
		int colonne = pos.getColumn();

		// On avance d'un pas dans la bonne direction
		if (ligne < cible.getLine() && ligne + 1 < map.getLineCount()) {
			ligne++;
		} else if (ligne > cible.getLine() && ligne - 1 >= 0) {
			ligne--;
		} else if (colonne < cible.getColumn() && colonne + 1 < map.getColumnCount()) {
			colonne++;
		} else if (colonne > cible.getColumn() && colonne - 1 >= 0) {
			colonne--;
		}

		habitant.setPosition(map.getBlock(ligne, colonne));
	}
}