package engine.habitant.visitor;

import engine.habitant.etat.*;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * ReceptiviteVisitor : module le facteur d'écoute d'un habitant
 * selon son état psychologique courant.
 */
public class ReceptiviteVisitor implements EtatVisitor<Double> {

	@Override
	public Double visit(EtatEpanoui etat) {
		return 1.0;
	}

	@Override
	public Double visit(EtatStable etat) {
		return 0.9;
	}

	@Override
	public Double visit(EtatEuphorique etat) {
		return 0.7;
	}

	@Override
	public Double visit(EtatAnxieux etat) {
		return 1.4;
	}

	@Override
	public Double visit(EtatDepressif etat) {
		return 0.5;
	}

	@Override
	public Double visit(EtatBurnout etat) {
		return 0.3;
	}

	@Override
	public Double visit(EtatIsole etat) {
		return 1.2;
	}
}