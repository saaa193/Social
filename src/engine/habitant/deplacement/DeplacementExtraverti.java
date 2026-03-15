package engine.habitant.deplacement;

import engine.habitant.Habitant;
import engine.map.Block;
import engine.map.Map;

/**
 * Déplacement extraverti : l'habitant se dirige vers le centre de la carte
 * mais avec un peu d'aléatoire pour éviter les regroupements massifs.
 * Extraversion élevée → cherche les interactions sociales.
 */
public class DeplacementExtraverti implements StrategieDeplacement {

    @Override
    public void deplacer(Habitant habitant, Map map) {
        Block pos = habitant.getPosition();
        int ligne = pos.getLine();
        int colonne = pos.getColumn();

        // Zone cible
        int centreL = map.getLineCount() / 2 + (int)(Math.random() * 110) - 55;
        int centreC = map.getColumnCount() / 2 + (int)(Math.random() * 110) - 55;

        // Direction vers la zone cible
        int directionLigne = 0;
        if (ligne < centreL) {
            directionLigne = 1;
        } else if (ligne > centreL) {
            directionLigne = -1;
        }

        int directionColonne = 0;
        if (colonne < centreC) {
            directionColonne = 1;
        } else if (colonne > centreC) {
            directionColonne = -1;
        }

        // Déplacement vertical ou horizontal
        if (Math.random() < 0.5 && directionLigne != 0) {
            ligne = ligne + directionLigne;
        } else if (directionColonne != 0) {
            colonne = colonne + directionColonne;
        }

        // Vérification des bords AVANT de setPosition
        if (ligne < 0) ligne = 0;
        if (ligne >= map.getLineCount()) ligne = map.getLineCount() - 1;
        if (colonne < 0) colonne = 0;
        if (colonne >= map.getColumnCount()) colonne = map.getColumnCount() - 1;

        habitant.setPosition(map.getBlock(ligne, colonne));
    }
}