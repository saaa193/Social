package test.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import engine.evenement.EvenementFactory;
import engine.evenement.EvenementSimulation;
import engine.evenement.EventCulturel;
import engine.evenement.EventMeteo;
import engine.evenement.EventPerso;
import engine.evenement.EventSocial;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * TestCase sur la Factory et les evenements.
 * Verifie que la Factory produit les bons types
 * et que le polymorphisme fonctionne correctement.
 */
public class TestEvenement {

	@Test
	public void testFactoryAlerteMeteo() {
		EvenementSimulation e = EvenementFactory.creer("Alerte Meteo");
		assertNotNull(e);
		assertTrue(e instanceof EventMeteo);
	}

	@Test
	public void testFactoryFeteQuartier() {
		EvenementSimulation e = EvenementFactory.creer("Fete de Quartier");
		assertNotNull(e);
		assertTrue(e instanceof EventSocial);
	}

	@Test
	public void testFactoryOffresEmploi() {
		EvenementSimulation e = EvenementFactory.creer("Offres d'Emploi");
		assertNotNull(e);
		assertTrue(e instanceof EventPerso);
	}

	@Test
	public void testFactoryExpoMusee() {
		EvenementSimulation e = EvenementFactory.creer("Expo Musee");
		assertNotNull(e);
		assertTrue(e instanceof EventCulturel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFactoryEvenementInconnu() {
		EvenementFactory.creer("Evenement inexistant");
	}
}