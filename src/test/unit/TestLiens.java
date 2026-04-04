package test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import engine.habitant.Habitant;
import engine.habitant.lien.Amical;
import engine.habitant.lien.Liens;
import engine.map.Block;
import engine.map.Map;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * TestCase sur les liens sociaux.
 * Verifie que la force d'un lien amical evolue
 * correctement et que le clamping fonctionne.
 */
public class TestLiens {

	private Liens lien;

	@Before
	public void initialiser() {
		Map map = new Map(10, 10);
		Block position = map.getBlock(0, 0);
		Habitant partenaire = new Habitant(position, map, "Bob", "Male", 30);
		lien = new Amical(partenaire, 50);
	}

	@Test
	public void testForceInitiale() {
		assertEquals(50, lien.getForce());
	}

	@Test
	public void testLienVivantAvecForce50() {
		assertTrue(!lien.estMort());
	}

	@Test
	public void testLienMortAvecForceZero() {
		lien.setForce(0);
		assertTrue(lien.estMort());
	}

	@Test
	public void testClampingMax() {
		lien.setForce(200);
		assertEquals(100, lien.getForce());
	}

	@Test
	public void testClampingMin() {
		lien.setForce(-10);
		assertEquals(0, lien.getForce());
	}
}