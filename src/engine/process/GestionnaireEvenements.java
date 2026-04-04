package engine.process;

import engine.evenement.EvenementFactory;
import engine.evenement.EvenementSimulation;
import engine.habitant.Habitant;
import java.util.List;

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

		int nbAffectes = 0;
		for (Habitant h : habitants) {
			if (evenement.estConcerne(h)) {
				nbAffectes++;
			}
		}

		evenement.declencher(habitants);

		int bonusMoral = (forceInfluence - 5) * 2;
		for (Habitant h : habitants) {
			if (evenement.estConcerne(h) && h.getBesoins().getSante() > 0) {
				h.getBesoins().setMoral(h.getBesoins().getMoral() + bonusMoral);
			}
		}

		return nbAffectes;
	}
}