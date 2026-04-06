package test.unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Suite de tests unitaires — calquee sur TreeTestSuite du prof.
 * Regroupe tous les TestCase du projet en un seul point d'entree.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
		TestBesoins.class,
		TestEvenement.class,
		TestPsychologie.class,
		TestTraumatisme.class,
		TestLiens.class,
		TestCalculateurInteraction.class
})
public class SocialTestSuite {

}