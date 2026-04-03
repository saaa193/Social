package engine.habitant.visitor;

import engine.habitant.etat.*;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Calcule la charge émotionnelle qu'un habitant propage à son entourage.
 * Valeur positive = contagion joyeuse, négative = contagion dépressive.
 */
public class ContagionVisitor implements EtatVisitor<Integer> {

	@Override
	public Integer visit(EtatEpanoui etat) {
		// Un habitant épanoui booste le moral de ses proches
		return +5;
	}

	@Override
	public Integer visit(EtatAnxieux etat) {
		// Un anxieux propage son stress autour de lui
		return -8;
	}

	@Override
	public Integer visit(EtatIsole etat) {
		// Un isolé propage un malaise discret
		return -3;
	}

	@Override
	public Integer visit(EtatStable etat) {
		// Stable : aucune contagion
		return 0;
	}

	@Override
	public Integer visit(EtatDepressif etat) {
		// Un dépressif propage un malaise profond
		return -12;
	}

	@Override
	public Integer visit(EtatBurnout etat) {
		// Un burnout propage de l'épuisement
		return -6;
	}

	@Override
	public Integer visit(EtatEuphorique etat) {
		// Un euphorique booste fortement son entourage
		return +10;
	}

}