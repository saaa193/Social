package test.unit;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import engine.habitant.Habitant;
import engine.map.Block;
import engine.map.Map;
import engine.process.CalculateurInteraction;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * TestCase sur CalculateurInteraction.
 * Verifie que la probabilite d'interaction est bien
 * calculee selon les profils OCEAN des habitants.
 */
public class TestCalculateurInteraction {

	private CalculateurInteraction calculateur;
	private Habitant h1;
	private Habitant h2;

	@Before
	public void initialiser() {
		Map map = new Map(10, 10);
		Block position = map.getBlock(0, 0);
		calculateur = new CalculateurInteraction();
		h1 = new Habitant(position, map, "Alice", "Female", 25);
		h2 = new Habitant(position, map, "Bob", "Male", 30);
	}

	@Test
	public void testProbabiliteEntre0Et1() {
		double probabilite = calculateur.calculerProbabilite(h1, h2);
		assertTrue(probabilite >= 0.0 && probabilite <= 1.0);
	}

	@Test
	public void testProbabilitePositive() {
		double probabilite = calculateur.calculerProbabilite(h1, h2);
		assertTrue(probabilite > 0.0);
	}

	@Test
	public void testProbabiliteSymetrique() {
		double prob1 = calculateur.calculerProbabilite(h1, h2);
		double prob2 = calculateur.calculerProbabilite(h2, h1);
		assertTrue(Math.abs(prob1 - prob2) < 0.10);
	}
}