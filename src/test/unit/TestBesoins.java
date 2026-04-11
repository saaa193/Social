package test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import engine.habitant.besoin.Besoins;
import engine.habitant.nutrition.StrategieNutrition;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * TestCase sur la classe Besoins.
 * Vérifie que les besoins biologiques évoluent correctement.
 */
public class TestBesoins {

	private Besoins besoins;

	private class StrategieVide implements StrategieNutrition {
		@Override
		public void appliquer(Besoins b) {}
	}

	@Before
	public void initialiser() {
		besoins = new Besoins(new StrategieVide());
	}

	@Test
	public void testMoralInitialEntre0Et100() {
		int moral = besoins.getMoral();
		assertTrue("Le moral initial doit être entre 0 et 100",
				moral >= 0 && moral <= 100);
	}

	@Test
	public void testSetMoralClampingMax() {
		besoins.setMoral(200);
		assertEquals("Le moral ne doit pas dépasser 100", 100, besoins.getMoral());
	}

	@Test
	public void testSetMoralClampingMin() {
		besoins.setMoral(-50);
		assertEquals("Le moral ne doit pas descendre sous 0", 0, besoins.getMoral());
	}

	@Test
	public void testSanteInitialeMax() {
		assertEquals("La santé initiale doit être 100", 100, besoins.getSante());
	}

	@Test
	public void testSetSanteNegatifDonneZero() {
		besoins.setSante(-10);
		assertEquals("La santé ne doit pas être négative", 0, besoins.getSante());
	}
}