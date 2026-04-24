package engine.process;

import engine.evenement.EvenementFactory;
import engine.evenement.EvenementSimulation;
import engine.habitant.Habitant;
import java.util.List;
import log.LoggerUtility;
import org.apache.log4j.Logger;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Gestionnaire : declenche un evenement sur la population.
 * La force d'influence amplifie l'impact moral de l'evenement.
 * Delegue entierement a la Factory et a EvenementSimulation.
 */
public class GestionnaireEvenements {
	private static final Logger logger = LoggerUtility.getLogger(GestionnaireEvenements.class, "text");


	/**
	 * Declenche un evenement sur la population.
	 * La force d'influence amplifie l'effet moral sur les habitants concernes.
	 * Retourne le nombre d'habitants concernes.
	 *
	 * @param nomEvenement   le nom de l'evenement a declencher
	 * @param habitants      la liste complete des habitants
	 * @param forceInfluence la force d'influence du slider (0 a 10)
	 * @return le nombre d'habitants affectes
	 */
	public static int declencherEvenement(String nomEvenement, List<Habitant> habitants, int forceInfluence) {

		EvenementSimulation evenement = EvenementFactory.creer(nomEvenement);

		// Force 0  → multiplicateur 0.2 (effet quasi nul)
		// Force 5  → multiplicateur 1.35 (effet normal)
		// Force 10 → multiplicateur 2.5 (effet dévastateur)
		double multiplicateur = 0.2 + (forceInfluence / 10.0) * 2.3;

		evenement.declencher(habitants, multiplicateur);

		int nbAffectes = 0;
		for (Habitant h : habitants) {
			if (evenement.estConcerne(h)) nbAffectes++;
		}
		logger.info("Evenement declenche : " + nomEvenement + " — " + nbAffectes + " habitants affectes");
		return nbAffectes;

	}
}