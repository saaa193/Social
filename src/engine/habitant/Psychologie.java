package engine.habitant;

import engine.habitant.besoin.Besoins;
import engine.habitant.deplacement.*;
import engine.habitant.etat.*;
import engine.habitant.nutrition.NutritionConsciente;
import engine.habitant.nutrition.NutritionNevrosee;
import engine.habitant.nutrition.NutritionSociale;
import engine.habitant.nutrition.StrategieNutrition;
import engine.habitant.lien.Liens;
import engine.habitant.biais.BiaisCognitif;
import engine.habitant.biais.BiaisConfirmation;
import engine.habitant.biais.BiaisNegatif;
import engine.habitant.biais.BiaisOptimisme;
import engine.habitant.adaptation.AdaptationEmotionnelle;
import engine.habitant.adaptation.AdaptationEvitement;
import engine.habitant.adaptation.AdaptationRationnelle;
import engine.habitant.adaptation.StrategieAdaptation;

import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 * <p>
 * Psychologie : encapsule le profil Big Five (OCEAN) d'un Habitant.
 * Responsabilité : connaître les traits de personnalité et
 * déterminer l'état psychologique courant.
 */

public class Psychologie {

	private int ouverture;
	private int conscience;
	private int extraversion;
	private int agreabilite;
	private int nevrosisme;

	public Psychologie() {
		this.ouverture = (int) (Math.random() * 101);
		this.conscience = (int) (Math.random() * 101);
		this.extraversion = (int) (Math.random() * 101);
		this.agreabilite = (int) (Math.random() * 101);
		this.nevrosisme = (int) (Math.random() * 101);
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
	 * L'appelant fait juste etat.appliquer(habitant) sans savoir quel état c'est.
	 * Les nouveaux états sont vérifiés en premier car plus graves.
	 */
	public EtatHabitant determinerEtat(Besoins besoins) {

		// Burnout : trop consciencieux, épuisement total
		if (conscience > 80 && besoins.getFatigue() < 10) {
			return new EtatBurnout();
		}

		// Dépressif : moral effondré + névrosisme élevé
		if (besoins.getMoral() < 20 && nevrosisme > 60) {
			return new EtatDepressif();
		}

		// Euphorique : extraverti très heureux socialement
		if (extraversion > 80 && besoins.getSocial() > 80) {
			return new EtatEuphorique();
		}

		// Anxieux : névrosisme élevé + moral fragilisé
		if (nevrosisme > 70 && besoins.getMoral() < 50) {
			return new EtatAnxieux();
		}

		// Isolé : peu de liens sociaux réels (pas juste une jauge basse)
		// Un extraverti avec peu d'amis souffre plus de l'isolement
		if (extraversion > 50 && besoins.getSocial() < 40) {
			return new EtatIsole();
		}

		// Épanoui : moral et social élevés
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

	/**
	 * Retourne le trait Ouverture.
	 */
	public int getOuverture() {
		return ouverture;
	}

	/**
	 * Retourne le trait Conscience.
	 */
	public int getConscience() {
		return conscience;
	}

	/**
	 * Retourne le trait Extraversion.
	 */
	public int getExtraversion() {
		return extraversion;
	}

	/**
	 * Retourne le trait Agreabilite.
	 */
	public int getAgreabilite() {
		return agreabilite;
	}

	/**
	 * Retourne le trait Nevrosisme.
	 */
	public int getNevrosisme() {
		return nevrosisme;
	}

	/**
	 * Retourne la stratégie de nutrition selon le profil OCEAN dominant.
	 * on compare des valeurs métier.
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
	 * Calquee sur determinerStrategieNutrition().
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
	 * Calquee sur determinerStrategieNutrition() du prof.
	 * Modele transactionnel du stress de Lazarus (1984).
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
	 * L'évolution n'est plus fixe à 1 point — elle dépend de l'intensité
	 * des traits et des besoins de l'habitant.
	 */
	public void evoluer(EtatHabitant etat, Besoins besoins) {

		if (etat instanceof EtatEpanoui) {
			int bonus = 1;
			if (agreabilite > 60) {
				bonus = 2;
			}

			if (agreabilite < 80 && Math.random() < 0.50) {
				agreabilite = Math.min(100, agreabilite + bonus);
			}
			if (nevrosisme > 20 && Math.random() < 0.50) {
				nevrosisme = Math.max(0, nevrosisme - 1);
			}
			if (conscience < 85 && Math.random() < 0.40) {
				conscience = Math.min(100, conscience + 1);
			}
			if (ouverture < 85 && Math.random() < 0.40) {
				ouverture = Math.min(100, ouverture + 1);
			}

		} else if (etat instanceof EtatAnxieux) {
			// Plus le moral est bas, plus on se dégrade vite
			int malus = 1;
			if (besoins.getMoral() < 25) {
				malus = 2;
			}
			// La conscience protège contre la dégradation
			if (conscience > 60) {
				malus = Math.max(1, malus - 1);
			}

			nevrosisme = Math.min(100, nevrosisme + malus);
			extraversion = Math.max(0, extraversion - malus);
			conscience = Math.max(0, conscience - 1);
			ouverture = Math.max(0, ouverture - 1);

		} else if (etat instanceof EtatIsole) {
			// Plus le social est bas, plus l'isolement empire
			int malus = 1;
			if (besoins.getSocial() < 20) {
				malus = 2;
			}
			extraversion = Math.max(0, extraversion - malus);
			agreabilite = Math.max(0, agreabilite - 1);
			ouverture = Math.max(0, ouverture - 1);
			conscience = Math.max(0, conscience - 1);

		} else if (etat instanceof EtatDepressif) {
			// La dépression dégrade fortement les traits OCEAN
			// Plus le moral est bas, plus la dégradation est rapide
			int malus = 2;
			if (besoins.getMoral() < 10) {
				malus = 3;
			}
			// Le névrosisme grimpe — la dépression fragilise
			nevrosisme = Math.min(100, nevrosisme + malus);
			// L'extraversion chute — on se replie sur soi
			extraversion = Math.max(0, extraversion - malus);
			// L'agréabilité diminue — on se ferme aux autres
			agreabilite = Math.max(0, agreabilite - 1);
			// L'ouverture diminue — plus de curiosité
			ouverture = Math.max(0, ouverture - 1);

		} else if (etat instanceof EtatBurnout) {
			// Le burnout érode la conscience — paradoxalement
			// Trop de conscience a causé le burnout
			int malus = 2;
			if (besoins.getFatigue() < 5) {
				malus = 3;
			}
			// La conscience chute — l'habitant abandonne sa rigueur
			conscience = Math.max(0, conscience - malus);
			// Le névrosisme monte — l'épuisement fragilise
			nevrosisme = Math.min(100, nevrosisme + 1);
			// L'extraversion chute — plus d'énergie sociale
			extraversion = Math.max(0, extraversion - 1);

		} else if (etat instanceof EtatEuphorique) {
			if (extraversion < 85 && Math.random() < 0.50) {
				extraversion = Math.min(100, extraversion + 1);
			}
			if (agreabilite < 80 && Math.random() < 0.40) {
				agreabilite = Math.min(100, agreabilite + 1);
			}
			if (ouverture < 85 && Math.random() < 0.40) {
				ouverture = Math.min(100, ouverture + 1);
			}
			if (nevrosisme > 15 && Math.random() < 0.40) {
				nevrosisme = Math.max(0, nevrosisme - 1);
			}
		}

		// EtatStable → rien ne change
	}

	/**
	 * Retourne la stratégie de déplacement selon le profil OCEAN.
	 * Même principe que determinerStrategieNutrition().
	 */
	public StrategieDeplacement determinerStrategieDeplacement() {
		// Névrosisme très élevé → anxieux, fuit les autres
		if (nevrosisme > 65) {
			return new DeplacementAnxieux();
		}
		// Extraversion élevée → cherche les autres
		if (extraversion > 65) {
			return new DeplacementExtraverti();
		}
		// Extraversion faible → introverti, bouge peu
		if (extraversion < 35) {
			return new DeplacementIntroverti();
		}
		// Par défaut → déplacement stable aléatoire
		return new DeplacementStable();
	}

	/**
	 * Un habitant est vulnérable si son névrosisme est élevé
	 * et son agréabilité est faible.
	 * Une personne vulnérable absorbe facilement les émotions négatives des autres.
	 */
	public boolean estVulnerable() {
		return nevrosisme > 60 && agreabilite < 40;
	}

	/**
	 * Un habitant est résilient si sa conscience et son agréabilité sont élevées.
	 * Une personne résiliente résiste bien aux chocs émotionnels des autres.
	 */
	public boolean estResiliant() {
		return conscience > 60 && agreabilite > 60;
	}

	/**
	 * Fait évoluer les traits OCEAN selon les liens sociaux de l'habitant.
	 * Les personnes autour de nous influencent notre profil psychologique.
	 * Appelée depuis Habitant.agir() à chaque tour.
	 */
	public void evoluerSelonReseau(List<Liens> relations) {

		// Si l'habitant n'a pas de liens, rien ne se passe
		if (relations.isEmpty()) {
			return;
		}

		// On parcourt tous les liens de l'habitant
		for (Liens lien : relations) {
			Habitant proche = lien.getPartenaire();

			// Plus le lien est fort, plus l'influence est grande
			// Un lien de force 100 donne une influence de 1.0
			// Un lien de force 50 donne une influence de 0.5
			double influence = lien.getForce() / 100.0;

			// Si le proche est très névrosé → notre névrosisme monte légèrement
			if (proche.getNevrosisme() > 70) {
				nevrosisme = Math.min(100, nevrosisme + (int) (influence * 2));
			}

			// Si le proche est très agréable → notre agréabilité monte légèrement
			if (proche.getAgreabilite() > 70) {
				agreabilite = Math.min(100, agreabilite + (int) (influence * 1));
			}

			// Si le proche est très extraverti → notre extraversion monte légèrement
			if (proche.getExtraversion() > 70) {
				extraversion = Math.min(100, extraversion + (int) (influence * 1));
			}
		}
	}

	/**
	 * Augmente l'extraversion d'un point (influence du leader).
	 */
	public void augmenterExtraversion(int valeur) {
		this.extraversion = Math.min(100, this.extraversion + valeur);
	}

	/**
	 * Augmente l'agréabilité d'un point (influence du leader).
	 */
	public void augmenterAgreabilite(int valeur) {
		this.agreabilite = Math.min(100, this.agreabilite + valeur);
	}

	/**
	 * Augmente le névrosisme d'un point (contagion du stress).
	 */
	public void augmenterNevrosisme(int valeur) {
		this.nevrosisme = Math.min(100, this.nevrosisme + valeur);
	}

	/**
	 * Diminue la conscience (burnout professionnel).
	 */
	public void diminuerConscience(int valeur) {
		this.conscience = Math.max(0, this.conscience - valeur);
	}

	/**
	 * Diminue l'ouverture (séquelle traumatique).
	 */
	public void diminuerOuverture(int valeur) {
		this.ouverture = Math.max(0, this.ouverture - valeur);
	}

	/**
	 * Diminue l'agréabilité (séquelle traumatique).
	 */
	public void diminuerAgreabilite(int valeur) {
		this.agreabilite = Math.max(0, this.agreabilite - valeur);
	}

	/**
	 * Augmente l'ouverture (beau temps, expériences positives).
	 */
	public void augmenterOuverture(int valeur) {
		this.ouverture = Math.min(100, this.ouverture + valeur);
	}

	/**
	 * Diminue le névrosisme (événement positif, thérapie).
	 */
	public void diminuerNevrosisme(int valeur) {
		this.nevrosisme = Math.max(0, this.nevrosisme - valeur);
	}

}