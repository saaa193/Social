package engine.habitant;

import config.RandomProvider;
import engine.habitant.adaptation.AdaptationEmotionnelle;
import engine.habitant.adaptation.AdaptationEvitement;
import engine.habitant.adaptation.AdaptationRationnelle;
import engine.habitant.adaptation.StrategieAdaptation;
import engine.habitant.biais.BiaisCognitif;
import engine.habitant.biais.BiaisConfirmation;
import engine.habitant.biais.BiaisNegatif;
import engine.habitant.biais.BiaisOptimisme;
import engine.habitant.besoin.Besoins;
import engine.habitant.deplacement.*;
import engine.habitant.etat.*;
import engine.habitant.lien.Liens;
import engine.habitant.nutrition.NutritionConsciente;
import engine.habitant.nutrition.NutritionNevrosee;
import engine.habitant.nutrition.NutritionSociale;
import engine.habitant.nutrition.StrategieNutrition;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Psychologie : encapsule le profil Big Five (OCEAN) d'un Habitant.
 * Détermine l'état psychologique courant selon les traits de personnalité.
 */
public class Psychologie {

	private int ouverture;
	private int conscience;
	private int extraversion;
	private int agreabilite;
	private int nevrosisme;

	public Psychologie() {
		this.ouverture = RandomProvider.getInstance().nextInt(101);
		this.conscience = RandomProvider.getInstance().nextInt(101);
		this.extraversion = RandomProvider.getInstance().nextInt(101);
		this.agreabilite = RandomProvider.getInstance().nextInt(101);
		this.nevrosisme = RandomProvider.getInstance().nextInt(101);
	}

	/**
	 * Constructeur avec parametres OCEAN explicites.
	 * Utilise pour les tests unitaires et les scenarios predéfinis.
	 *
	 * @param ouverture    trait Ouverture (0 a 100)
	 * @param conscience   trait Conscience (0 a 100)
	 * @param extraversion trait Extraversion (0 a 100)
	 * @param agreabilite  trait Agreabilite (0 a 100)
	 * @param nevrosisme   trait Nevrosisme (0 a 100)
	 */
	public Psychologie(int ouverture, int conscience, int extraversion, int agreabilite, int nevrosisme) {
		this.ouverture = ouverture;
		this.conscience = conscience;
		this.extraversion = extraversion;
		this.agreabilite = agreabilite;
		this.nevrosisme = nevrosisme;
	}

	/**
	 * Détermine l'état psychologique dominant de l'habitant.
	 * Retourne un objet EtatHabitant polymorphique.
	 */
	public EtatHabitant determinerEtat(Besoins besoins) {
		if (conscience > 80 && besoins.getFatigue() < 10) {
			return new EtatBurnout();
		}
		if (besoins.getMoral() < 20 && nevrosisme > 60) {
			return new EtatDepressif();
		}
		if (nevrosisme > 70 && besoins.getMoral() < 50) {
			return new EtatAnxieux();
		}
		if (extraversion > 50 && besoins.getSocial() < 40) {
			return new EtatIsole();
		}
		if (extraversion > 80 && besoins.getSocial() > 80) {
			return new EtatEuphorique();
		}
		if (besoins.getMoral() > 70 && besoins.getSocial() > 60) {
			return new EtatEpanoui();
		}
		return new EtatStable();
	}

	/**
	 * Calcule la compatibilité OCEAN entre deux profils psychologiques.
	 */
	public int calculerCompatibiliteAvec(Psychologie autre) {
		double baseAgreabilite = (this.agreabilite + autre.agreabilite) / 2.0;
		double diffOuverture = Math.abs(this.ouverture - autre.ouverture);
		double bonusOuverture = (100 - diffOuverture) / 2.0;
		int force = (int) ((baseAgreabilite * 0.6) + (bonusOuverture * 0.4));
		return Math.max(10, Math.min(80, force));
	}

	public int getOuverture() { return ouverture; }
	public int getConscience() { return conscience; }
	public int getExtraversion() { return extraversion; }
	public int getAgreabilite() { return agreabilite; }
	public int getNevrosisme() { return nevrosisme; }

	/**
	 * Retourne la stratégie de nutrition selon le profil OCEAN dominant.
	 */
	public StrategieNutrition determinerStrategieNutrition() {
		if (conscience > 65) {
			return new NutritionConsciente();
		}
		if (nevrosisme > 65) {
			return new NutritionNevrosee();
		}
		return new NutritionSociale();
	}

	/**
	 * Retourne le biais cognitif selon le profil OCEAN dominant.
	 */
	public BiaisCognitif determinerBiais() {
		if (nevrosisme > 65) {
			return new BiaisNegatif();
		}
		if (agreabilite > 65) {
			return new BiaisOptimisme();
		}
		return new BiaisConfirmation();
	}

	/**
	 * Retourne la strategie d'adaptation au stress selon le profil OCEAN.
	 */
	public StrategieAdaptation determinerStrategieAdaptation() {
		if (conscience > 65) {
			return new AdaptationRationnelle();
		}
		if (agreabilite > 65) {
			return new AdaptationEmotionnelle();
		}
		return new AdaptationEvitement();
	}

	/**
	 * Fait évoluer les traits OCEAN selon l'état psychologique actuel.
	 */
	public void evoluer(EtatHabitant etat, Besoins besoins) {
		if (etat instanceof EtatEpanoui) {
			int bonus = 1;
			if (agreabilite > 60) {
				bonus = 2;
			}
			if (agreabilite < 80 && RandomProvider.getInstance().nextDouble() < 0.50) {
				agreabilite = Math.min(100, agreabilite + bonus);
			}
			if (nevrosisme > 20 && RandomProvider.getInstance().nextDouble() < 0.50) {
				nevrosisme = Math.max(0, nevrosisme - 1);
			}
			if (conscience < 85 && RandomProvider.getInstance().nextDouble() < 0.40) {
				conscience = Math.min(100, conscience + 1);
			}
			if (ouverture < 85 && RandomProvider.getInstance().nextDouble() < 0.40) {
				ouverture = Math.min(100, ouverture + 1);
			}

		} else if (etat instanceof EtatAnxieux) {
			int malus = 1;
			if (besoins.getMoral() < 25) {
				malus = 2;
			}
			if (conscience > 60) {
				malus = Math.max(1, malus - 1);
			}
			nevrosisme = Math.min(100, nevrosisme + malus);
			extraversion = Math.max(0, extraversion - malus);
			conscience = Math.max(0, conscience - 1);
			ouverture = Math.max(0, ouverture - 1);

		} else if (etat instanceof EtatIsole) {
			int malus = 1;
			if (besoins.getSocial() < 20) {
				malus = 2;
			}
			extraversion = Math.max(0, extraversion - malus);
			agreabilite = Math.max(0, agreabilite - 1);
			ouverture = Math.max(0, ouverture - 1);
			conscience = Math.max(0, conscience - 1);

		} else if (etat instanceof EtatDepressif) {
			int malus = 2;
			if (besoins.getMoral() < 10) {
				malus = 3;
			}
			nevrosisme = Math.min(100, nevrosisme + malus);
			extraversion = Math.max(0, extraversion - malus);
			agreabilite = Math.max(0, agreabilite - 1);
			ouverture = Math.max(0, ouverture - 1);

		} else if (etat instanceof EtatBurnout) {
			int malus = 2;
			if (besoins.getFatigue() < 5) {
				malus = 3;
			}
			conscience = Math.max(0, conscience - malus);
			nevrosisme = Math.min(100, nevrosisme + 1);
			extraversion = Math.max(0, extraversion - 1);

		} else if (etat instanceof EtatEuphorique) {
			if (extraversion < 85 && RandomProvider.getInstance().nextDouble() < 0.50) {
				extraversion = Math.min(100, extraversion + 1);
			}
			if (agreabilite < 80 && RandomProvider.getInstance().nextDouble() < 0.40) {
				agreabilite = Math.min(100, agreabilite + 1);
			}
			if (ouverture < 85 && RandomProvider.getInstance().nextDouble() < 0.40) {
				ouverture = Math.min(100, ouverture + 1);
			}
			if (nevrosisme > 15 && RandomProvider.getInstance().nextDouble() < 0.40) {
				nevrosisme = Math.max(0, nevrosisme - 1);
			}
		}
	}

	/**
	 * Retourne la stratégie de déplacement selon le profil OCEAN.
	 */
	public StrategieDeplacement determinerStrategieDeplacement() {
		if (nevrosisme > 65) {
			return new DeplacementAnxieux();
		}
		if (extraversion > 65) {
			return new DeplacementExtraverti();
		}
		if (extraversion < 35) {
			return new DeplacementIntroverti();
		}
		return new DeplacementStable();
	}

	/**
	 * Un habitant est vulnérable si son névrosisme est élevé et son agréabilité faible.
	 */
	public boolean estVulnerable() {
		return nevrosisme > 60 && agreabilite < 40;
	}

	/**
	 * Un habitant est résilient si sa conscience et son agréabilité sont élevées.
	 */
	public boolean estResiliant() {
		return conscience > 60 && agreabilite > 60;
	}

	/**
	 * Fait évoluer les traits OCEAN selon les liens sociaux de l'habitant.
	 */
	public void evoluerSelonReseau(List<Liens> relations) {
		if (relations.isEmpty()) {
			return;
		}
		for (Liens lien : relations) {
			Habitant proche = lien.getPartenaire();
			double influence = lien.getForce() / 100.0;
			if (proche.getNevrosisme() > 70) {
				nevrosisme = Math.min(100, nevrosisme + (int) (influence * 2));
			}
			if (proche.getAgreabilite() > 70) {
				agreabilite = Math.min(100, agreabilite + (int) (influence * 1));
			}
			if (proche.getExtraversion() > 70) {
				extraversion = Math.min(100, extraversion + (int) (influence * 1));
			}
		}
	}

	/**
	 * Augmente l'extraversion.
	 */
	public void augmenterExtraversion(int valeur) {
		this.extraversion = Math.min(100, this.extraversion + valeur);
	}

	/**
	 * Augmente l'agréabilité.
	 */
	public void augmenterAgreabilite(int valeur) {
		this.agreabilite = Math.min(100, this.agreabilite + valeur);
	}

	/**
	 * Augmente le névrosisme.
	 */
	public void augmenterNevrosisme(int valeur) {
		this.nevrosisme = Math.min(100, this.nevrosisme + valeur);
	}

	/**
	 * Diminue la conscience.
	 */
	public void diminuerConscience(int valeur) {
		this.conscience = Math.max(0, this.conscience - valeur);
	}

	/**
	 * Diminue l'ouverture.
	 */
	public void diminuerOuverture(int valeur) {
		this.ouverture = Math.max(0, this.ouverture - valeur);
	}

	/**
	 * Diminue l'agréabilité.
	 */
	public void diminuerAgreabilite(int valeur) {
		this.agreabilite = Math.max(0, this.agreabilite - valeur);
	}

	/**
	 * Augmente l'ouverture.
	 */
	public void augmenterOuverture(int valeur) {
		this.ouverture = Math.min(100, this.ouverture + valeur);
	}

	/**
	 * Diminue le névrosisme.
	 */
	public void diminuerNevrosisme(int valeur) {
		this.nevrosisme = Math.max(0, this.nevrosisme - valeur);
	}
}