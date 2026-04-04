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
 * Gestionnaire refactorisé : plus aucun if/else.
 * Délègue entièrement à la Factory et à EvenementSimulation.
 */
public class GestionnaireEvenements {

	public static void declencherEvenement(String nomEvenement, List<Habitant> habitants) {
		EvenementSimulation evenement = EvenementFactory.creer(nomEvenement);
		evenement.declencher(habitants);
	}
}