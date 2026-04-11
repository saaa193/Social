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
		return +2;
	}

	@Override
	public Integer visit(EtatAnxieux etat) {
		return -3;
	}

	@Override
	public Integer visit(EtatIsole etat) {
		return -3;
	}

	@Override
	public Integer visit(EtatStable etat) {
		return 0;
	}

	@Override
	public Integer visit(EtatDepressif etat) {
		return -4;
	}

	@Override
	public Integer visit(EtatBurnout etat) {
		return -2;
	}

	@Override
	public Integer visit(EtatEuphorique etat) {
		return +3;
	}
}