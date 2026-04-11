package engine.habitant.visitor;

import engine.habitant.etat.*;
import java.awt.Color;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Visitor de couleur : retourne la couleur associée
 * à chaque état psychologique pour le rendu graphique.
 */
public class CouleurVisitor implements EtatVisitor<Color> {

	@Override
	public Color visit(EtatEpanoui etat) {
		return new Color(128, 0, 128);
	}

	@Override
	public Color visit(EtatEuphorique etat) {
		return new Color(200, 0, 200);
	}

	@Override
	public Color visit(EtatStable etat) {
		return Color.ORANGE;
	}

	@Override
	public Color visit(EtatAnxieux etat) {
		return new Color(255, 140, 0);
	}

	@Override
	public Color visit(EtatIsole etat) {
		return new Color(100, 100, 150);
	}

	@Override
	public Color visit(EtatDepressif etat) {
		return Color.RED;
	}

	@Override
	public Color visit(EtatBurnout etat) {
		return new Color(80, 0, 0);
	}
}