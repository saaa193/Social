package engine.habitant.visitor;

import engine.habitant.etat.EtatAnxieux;
import engine.habitant.etat.EtatBurnout;
import engine.habitant.etat.EtatDepressif;
import engine.habitant.etat.EtatEpanoui;
import engine.habitant.etat.EtatEuphorique;
import engine.habitant.etat.EtatIsole;
import engine.habitant.etat.EtatStable;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Retourne le facteur d'interaction sociale selon l'etat psychologique.
 * Utilise dans CalculateurInteraction pour moduler la probabilite
 * qu'un habitant cherche le contact selon son etat emotionnel.
 */
public class FacteurEtatVisitor implements EtatVisitor<Double> {

	private static final double BONUS_ETAT_POSITIF = 0.20;
	private static final double MALUS_ETAT_NEGATIF = 0.15;

	@Override
	public Double visit(EtatEpanoui etat) {
		return BONUS_ETAT_POSITIF;
	}

	@Override
	public Double visit(EtatEuphorique etat) {
		return BONUS_ETAT_POSITIF;
	}

	@Override
	public Double visit(EtatDepressif etat) {
		return -MALUS_ETAT_NEGATIF;
	}

	@Override
	public Double visit(EtatAnxieux etat) {
		return -MALUS_ETAT_NEGATIF;
	}

	@Override
	public Double visit(EtatBurnout etat) {
		return -MALUS_ETAT_NEGATIF;
	}

	@Override
	public Double visit(EtatStable etat) {
		return 0.0;
	}

	@Override
	public Double visit(EtatIsole etat) {
		return 0.0;
	}
}