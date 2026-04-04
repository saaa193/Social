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
			// Récupération nocturne — plus généreuse et garantie
			if (Math.random() < tauxRecuperation) this.fatigue += 3;
			// Récupération minimale garantie (même les insomniaques dorment un peu)
			this.fatigue += 1;
			// On mange moins la nuit mais on ne meurt pas de faim en dormant
			if (Math.random() < 0.40) this.faim += 1;

		} else {
			// Nutrition (Strategy Pattern) — appliquée AVANT la dégradation
			strategieNutrition.appliquer(this);

			if (Math.random() < tauxFaim)    this.faim    -= 1;
			if (Math.random() < tauxFatigue) this.fatigue -= 1;
			if (Math.random() < tauxSocial)  this.social  -= 1;
		}

		// ==========================================================
		// === USURE NATURELLE (Principe d'Entropie du système)   ===
		// ==========================================================
		// Même quand tout va bien, les besoins descendent lentement
		// C'est ce qui empêche les habitants de rester à 100% indéfiniment
		if (Math.random() < 0.05) this.faim    -= 1;  // ~5% de chance par tour
		if (Math.random() < 0.04) this.fatigue -= 1;  // ~4% de chance par tour
		if (Math.random() < 0.03) this.social  -= 1;  // ~3% de chance par tour


		// === MORAL : plus nuancé, avec un vrai mécanisme de remontée ===

		// Compteur de facteurs négatifs (au lieu d'un OR brutal)
		int facteursCritiques = 0;
		if (this.faim    < 30) facteursCritiques++;
		if (this.social  < 30) facteursCritiques++;
		if (this.fatigue < 20) facteursCritiques++;

		if (facteursCritiques >= 2) {
			// Situation grave : 2+ besoins en danger → moral baisse
			if (Math.random() < 0.12) this.moral -= 1;
		} else if (facteursCritiques == 1) {
			// Un seul besoin en danger → moral stagne (pas de baisse)
			// C'est réaliste : avoir faim ne rend pas dépressif tout seul
		} else {
			// Tout va bien → le moral REMONTE naturellement
			if (Math.random() < 0.10) this.moral += 1;
		}

		// Bonus moral si vraiment tout va très bien
		if (this.faim > 70 && this.social > 70 && this.fatigue > 50) {
			if (Math.random() < 0.06) this.moral += 1;
		}

		// === SANTE : la mort ne survient que dans les cas extrêmes ===
		if (this.faim <= 0 && this.moral <= 0) {
			// Les DEUX doivent être à zéro pour risquer de mourir
			// Avant c'était un OR : faim=0 OU moral=0 → trop facile de mourir
			if (Math.random() < 0.15) this.sante -= 1;
		}
	}

	//GETTERS
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

	//SETTERS avec clamping 0-100
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