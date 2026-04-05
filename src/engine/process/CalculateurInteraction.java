package engine.process;

import engine.habitant.Habitant;
import engine.habitant.etat.EtatAnxieux;
import engine.habitant.etat.EtatBurnout;
import engine.habitant.etat.EtatDepressif;
import engine.habitant.etat.EtatEpanoui;
import engine.habitant.etat.EtatEuphorique;
import engine.habitant.etat.EtatHabitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * CalculateurInteraction : calcule la probabilite psychologique
 * qu'une interaction sociale se produise entre deux habitants.
 * Remplace la logique spatiale par un algorithme Big Five pur.
 */
public class CalculateurInteraction {

	private static final double POIDS_AGREABILITE   = 0.40;
	private static final double POIDS_COMPATIBILITE = 0.30;
	private static final double POIDS_ETAT          = 0.20;
	private static final double POIDS_HISTORIQUE    = 0.10;

	private static final double PLAFOND_HISTORIQUE  = 0.20;
	private static final double BONUS_ETAT_POSITIF  = 0.20;
	private static final double MALUS_ETAT_NEGATIF  = 0.15;

	/**
	 * Calcule la probabilite d'interaction entre deux habitants.
	 * Algorithme pondere sur 4 facteurs psychologiques Big Five.
	 * Modele de Byrne (1971) : attraction par similarite de personnalite.
	 *
	 * @param h1 le premier habitant
	 * @param h2 le second habitant
	 * @return probabilite entre 0.0 et 1.0
	 */
	public double calculerProbabilite(Habitant h1, Habitant h2) {
		double facteurAgreabilite   = calculerFacteurAgreabilite(h1, h2);
		double facteurCompatibilite = calculerFacteurCompatibilite(h1, h2);
		double facteurEtat          = calculerFacteurEtat(h1);
		double facteurHistorique    = calculerFacteurHistorique(h1, h2);

		double probabilite = (facteurAgreabilite   * POIDS_AGREABILITE)
				+ (facteurCompatibilite * POIDS_COMPATIBILITE)
				+ (facteurEtat          * POIDS_ETAT)
				+ (facteurHistorique    * POIDS_HISTORIQUE);

		return Math.max(0.0, Math.min(1.0, probabilite));
	}

	/**
	 * Facteur 1 : agreabilite moyenne des deux agents.
	 * Plus les deux sont agreables, plus l'interaction est probable.
	 */
	private double calculerFacteurAgreabilite(Habitant h1, Habitant h2) {
		return (h1.getAgreabilite() + h2.getAgreabilite()) / 200.0;
	}

	/**
	 * Facteur 2 : compatibilite globale des profils OCEAN.
	 * Calque sur calculerCompatibiliteAvec() de Psychologie.
	 */
	private double calculerFacteurCompatibilite(Habitant h1, Habitant h2) {
		return h1.getPsychologie().calculerCompatibiliteAvec(h2.getPsychologie()) / 100.0;
	}

	/**
	 * Facteur 3 : etat emotionnel actuel de h1.
	 * Un agent epanoui cherche le contact, un depressif le fuit.
	 */
	private double calculerFacteurEtat(Habitant h1) {
		EtatHabitant etat = h1.getPsychologie().determinerEtat(h1.getBesoins());
		if (etat instanceof EtatEpanoui || etat instanceof EtatEuphorique) {
			return BONUS_ETAT_POSITIF;
		}
		if (etat instanceof EtatDepressif || etat instanceof EtatAnxieux || etat instanceof EtatBurnout) {
			return -MALUS_ETAT_NEGATIF;
		}
		return 0.0;
	}

	/**
	 * Facteur 4 : historique des rencontres passees.
	 * La confiance s'accumule avec le nombre de rencontres.
	 * Plafonne a PLAFOND_HISTORIQUE pour eviter la saturation.
	 */
	private double calculerFacteurHistorique(Habitant h1, Habitant h2) {
		int rencontresMin = Math.min(h1.getNbRencontres(), h2.getNbRencontres());
		double facteur = rencontresMin / 10.0;
		return Math.min(facteur, PLAFOND_HISTORIQUE);
	}
}