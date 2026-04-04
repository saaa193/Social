package test.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import engine.evenement.EvenementFactory;
import engine.evenement.EvenementSimulation;
import engine.evenement.EventMeteo;
import engine.evenement.EventSocial;
import engine.evenement.EventPerso;
import engine.evenement.EventCulturel;

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
    public void testFactoryAlerteMeteo() {
        EvenementSimulation e = EvenementFactory.creer("Alerte Météo");
        assertNotNull("La factory ne doit pas retourner null", e);
        assertTrue("Alerte Météo doit produire un EventMeteo",
                e instanceof EventMeteo);
    }

    @Test
    public void testFactoryFeteQuartier() {
        EvenementSimulation e = EvenementFactory.creer("Fête de Quartier");
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
        EvenementSimulation e = EvenementFactory.creer("Expo Musée");
        assertNotNull(e);
        assertTrue(e instanceof EventCulturel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryEvenementInconnu() {
        // Doit lever une exception — comportement de robustesse
        EvenementFactory.creer("Événement inexistant");
    }
}