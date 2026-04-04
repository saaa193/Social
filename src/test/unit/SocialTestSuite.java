package test.unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Suite de tests unitaires — calquée sur TreeTestSuite du prof.
 * Regroupe tous les TestCase du projet en un seul point d'entrée.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestBesoins.class, TestEvenement.class})
public class SocialTestSuite {

}