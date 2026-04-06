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
		// Réduit de -8 à -3 — le stress se transmet, mais doucement
		return -3;
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
		// Contagion modérée — un dépressif propage un malaise,
		// pas une onde de choc nucléaire
		return -4;
	}

	@Override
	public Integer visit(EtatBurnout etat) {
		// Réduit de -6 à -2 — le burnout est surtout personnel
		return -2;
	}

	@Override
	public Integer visit(EtatEuphorique etat) {
		return +3;
	}

}