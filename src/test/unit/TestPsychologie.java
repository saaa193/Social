package test.unit;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import engine.habitant.Psychologie;
import engine.habitant.besoin.Besoins;
import engine.habitant.etat.EtatAnxieux;
import engine.habitant.etat.EtatBurnout;
import engine.habitant.etat.EtatDepressif;
import engine.habitant.etat.EtatHabitant;
import engine.habitant.etat.EtatStable;
import engine.habitant.nutrition.NutritionSociale;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * TestCase sur la classe Psychologie.
 * Verifie que determinerEtat() retourne le bon etat
 * selon le profil OCEAN et les besoins de l'habitant.
 */
public class TestPsychologie {

	private Besoins besoins;

	@Before
	public void initialiser() {
		besoins = new Besoins(new NutritionSociale());
	}

	@Test
	public void testEtatDepressif() {
		Psychologie psychologie = new Psychologie(50, 50, 50, 50, 70);
		besoins.setMoral(15);
		EtatHabitant etat = psychologie.determinerEtat(besoins);
		assertTrue(etat instanceof EtatDepressif);
	}

	@Test
	public void testEtatBurnout() {
		Psychologie psychologie = new Psychologie(50, 85, 50, 50, 30);
		besoins.setFatigue(5);
		EtatHabitant etat = psychologie.determinerEtat(besoins);
		assertTrue(etat instanceof EtatBurnout);
	}

	@Test
	public void testEtatAnxieux() {
		Psychologie psychologie = new Psychologie(50, 50, 50, 50, 75);
		besoins.setMoral(40);
		EtatHabitant etat = psychologie.determinerEtat(besoins);
		assertTrue(etat instanceof EtatAnxieux);
	}

	@Test
	public void testEtatStable() {
		Psychologie psychologie = new Psychologie(50, 50, 50, 50, 50);
		besoins.setMoral(50);
		besoins.setSocial(50);
		EtatHabitant etat = psychologie.determinerEtat(besoins);
		assertTrue(etat instanceof EtatStable);
	}
}