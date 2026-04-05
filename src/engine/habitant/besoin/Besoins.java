package engine.habitant.besoin;

import engine.habitant.nutrition.StrategieNutrition;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Représente les besoins vitaux d'un habitant.
 */
public class Besoins {

	private int faim = 100;
	private int moral = 100;
	private int fatigue = 100;
	private int sante = 100;
	private int social = 100;
	private StrategieNutrition strategieNutrition;

	public Besoins(StrategieNutrition strategie) {
		this.strategieNutrition = strategie;
	}

	public void vivre(boolean estLaNuit, double tauxFaim,
					  double tauxFatigue, double tauxSocial,
					  double tauxRecuperation) {

		if (estLaNuit) {
			if (Math.random() < tauxRecuperation) this.fatigue += 3;
			this.fatigue += 1;
			if (Math.random() < 0.40) this.faim += 1;

		} else {
			strategieNutrition.appliquer(this);

			if (Math.random() < tauxFaim)    this.faim    -= 1;
			if (Math.random() < tauxFatigue) this.fatigue -= 1;
			if (Math.random() < tauxSocial)  this.social  -= 1;
		}

		// Usure naturelle — les besoins descendent lentement meme quand tout va bien
		if (Math.random() < 0.05) this.faim    -= 1;
		if (Math.random() < 0.04) this.fatigue -= 1;
		if (Math.random() < 0.03) this.social  -= 1;

		// Moral — mecanisme nuance avec facteurs critiques
		int facteursCritiques = 0;
		if (this.faim    < 30) facteursCritiques++;
		if (this.social  < 30) facteursCritiques++;
		if (this.fatigue < 20) facteursCritiques++;

		if (facteursCritiques >= 2) {
			if (Math.random() < 0.30) this.moral -= 1;
		} else if (facteursCritiques == 1) {
			// moral stagne
		} else {
			if (Math.random() < 0.04) this.moral += 1;
		}

		// Usure naturelle du moral
		if (Math.random() < 0.08) this.moral -= 1;

		// Bonus moral si vraiment tout va tres bien
		if (this.faim > 70 && this.social > 70 && this.fatigue > 50) {
			if (Math.random() < 0.03) this.moral += 1;
		}

		// Sante — la mort ne survient que dans les cas extremes
		if (this.faim <= 0 && this.moral <= 0) {
			if (Math.random() < 0.15) this.sante -= 1;
		}

		this.faim    = Math.min(100, this.faim);
		this.fatigue = Math.min(100, this.fatigue);
		this.social  = Math.min(100, this.social);
		this.moral   = Math.min(100, this.moral);
	}

	public int getFaim() {
		return faim;
	}

	public int getMoral() {
		return moral;
	}

	public int getFatigue() {
		return fatigue;
	}

	public int getSante() {
		return sante;
	}

	public int getSocial() {
		return social;
	}

	public void setFaim(int faim) {
		this.faim = Math.max(0, Math.min(100, faim));
	}

	public void setMoral(int moral) {
		this.moral = Math.max(0, Math.min(100, moral));
	}

	public void setFatigue(int fatigue) {
		this.fatigue = Math.max(0, Math.min(100, fatigue));
	}

	public void setSante(int sante) {
		this.sante = Math.max(0, Math.min(100, sante));
	}

	public void setSocial(int social) {
		this.social = Math.max(0, Math.min(100, social));
	}

	/**
	 * Met à jour la stratégie de nutrition selon l'évolution du profil OCEAN.
	 */
	public void setStrategieNutrition(StrategieNutrition strategie) {
		this.strategieNutrition = strategie;
	}
}