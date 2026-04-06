package test.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import engine.evenement.EvenementFactory;
import engine.evenement.EvenementSimulation;
import engine.evenement.EventTempete;
import engine.evenement.EventFestival;
import engine.evenement.EventCrise;
import engine.evenement.EventCulturel;
import engine.evenement.EventEpidemie;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * TestCase sur la Factory et les événements.
 * Vérifie que la Factory produit les bons types
 * et que le polymorphisme fonctionne correctement.
 */
public class TestEvenement {

    @Test
    public void testFactoryTempete() {
        EvenementSimulation e = EvenementFactory.creer("Tempête Urbaine");
        assertNotNull("La factory ne doit pas retourner null", e);
        assertTrue("Tempête Urbaine doit produire un EventTempete",
                e instanceof EventTempete);
    }

    @Test
    public void testFactoryFestival() {
        EvenementSimulation e = EvenementFactory.creer("Festival de Quartier");
        assertNotNull(e);
        assertTrue(e instanceof EventFestival);
    }

    @Test
    public void testFactoryCrise() {
        EvenementSimulation e = EvenementFactory.creer("Crise Économique");
        assertNotNull(e);
        assertTrue(e instanceof EventCrise);
    }

    @Test
    public void testFactoryCulturel() {
        EvenementSimulation e = EvenementFactory.creer("Semaine Culturelle");
        assertNotNull(e);
        assertTrue(e instanceof EventCulturel);
    }

    @Test
    public void testFactoryEpidemie() {
        EvenementSimulation e = EvenementFactory.creer("Épidémie");
        assertNotNull(e);
        assertTrue(e instanceof EventEpidemie);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryEvenementInconnu() {
        EvenementFactory.creer("Événement inexistant");
    }
}