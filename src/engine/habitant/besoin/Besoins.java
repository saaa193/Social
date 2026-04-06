package engine.habitant.besoin;

import config.GameConfiguration;
import engine.habitant.nutrition.StrategieNutrition;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Représente les besoins vitaux d'un habitant.
 * Toutes les constantes viennent de GameConfiguration — Singleton.
 */
public class Besoins {

	private int faim    = 100;
	private int moral   = 100;
	private int fatigue = 100;
	private int sante   = 100;
	private int social  = 100;
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
			if (Math.random() < GameConfiguration.PROBA_FAIM_NUIT) this.faim += 1;

		} else {
			strategieNutrition.appliquer(this);
			if (Math.random() < tauxFaim)    this.faim    -= 1;
			if (Math.random() < tauxFatigue) this.fatigue -= 1;
			if (Math.random() < tauxSocial)  this.social  -= 1;
		}

		// Usure naturelle — entropie du système
		if (Math.random() < GameConfiguration.USURE_FAIM)    this.faim    -= 1;
		if (Math.random() < GameConfiguration.USURE_FATIGUE) this.fatigue -= 1;
		if (Math.random() < GameConfiguration.USURE_SOCIAL)  this.social  -= 1;

		// Moral
		int facteursCritiques = 0;
		if (this.faim    < GameConfiguration.SEUIL_FAIM_CRITIQUE)    facteursCritiques++;
		if (this.social  < GameConfiguration.SEUIL_SOCIAL_CRITIQUE)  facteursCritiques++;
		if (this.fatigue < GameConfiguration.SEUIL_FATIGUE_CRITIQUE) facteursCritiques++;

		if (facteursCritiques >= 2) {
			if (Math.random() < GameConfiguration.PROBA_MORAL_BAISSE) this.moral -= 1;
		} else if (facteursCritiques == 0) {
			if (Math.random() < GameConfiguration.PROBA_MORAL_REMONTE) this.moral += 1;
		}

		if (this.faim    > GameConfiguration.SEUIL_FAIM_BON
				&& this.social  > GameConfiguration.SEUIL_SOCIAL_BON
				&& this.fatigue > GameConfiguration.SEUIL_FATIGUE_BON) {
			if (Math.random() < GameConfiguration.PROBA_MORAL_BONUS) this.moral += 1;
		}

		// Santé
		if (this.faim <= 0 && this.moral <= 0) {
			if (Math.random() < GameConfiguration.PROBA_SANTE_BAISSE) this.sante -= 1;
		}
	}

	public int getFaim()    { return faim; }
	public int getMoral()   { return moral; }
	public int getFatigue() { return fatigue; }
	public int getSante()   { return sante; }
	public int getSocial()  { return social; }

	public void setFaim(int faim)       { this.faim    = Math.max(0, Math.min(100, faim)); }
	public void setMoral(int moral)     { this.moral   = Math.max(0, Math.min(100, moral)); }
	public void setFatigue(int fatigue) { this.fatigue = Math.max(0, Math.min(100, fatigue)); }
	public void setSante(int sante)     { this.sante   = Math.max(0, Math.min(100, sante)); }
	public void setSocial(int social)   { this.social  = Math.max(0, Math.min(100, social)); }

	public void setStrategieNutrition(StrategieNutrition strategie) {
		this.strategieNutrition = strategie;
	}
}