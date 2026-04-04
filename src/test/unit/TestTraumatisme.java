package test.unit;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import engine.habitant.Habitant;
import engine.habitant.Traumatisme;
import engine.map.Block;
import engine.map.Map;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * TestCase sur la classe Traumatisme.
 * Verifie que le malus OCEAN est bien applique
 * selon la resistance collective.
 */
public class TestTraumatisme {

	private Traumatisme traumatisme;
	private Habitant habitant;

	@Before
	public void initialiser() {
		Map map = new Map(10, 10);
		Block position = map.getBlock(0, 0);
		traumatisme = new Traumatisme();
		habitant = new Habitant(position, map, "Test", "Male", 30);
	}

	@Test
	public void testTraumatismeAugmenteNevrosisme() {
		int nevrosismeAvant = habitant.getNevrosisme();
		traumatisme.appliquer(habitant, 0);
		assertTrue(habitant.getNevrosisme() > nevrosismeAvant);
	}

	@Test
	public void testTraumatismeDiminueAgreabilite() {
		int agreabiliteAvant = habitant.getAgreabilite();
		traumatisme.appliquer(habitant, 0);
		assertTrue(habitant.getAgreabilite() < agreabiliteAvant);
	}

	@Test
	public void testResistanceMaxReduitNevrosisme() {
		int nevrosismeAvant = habitant.getNevrosisme();
		traumatisme.appliquer(habitant, 100);
		int malus = habitant.getNevrosisme() - nevrosismeAvant;
		assertTrue(malus <= 5);
	}
}