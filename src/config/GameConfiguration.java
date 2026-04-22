package config;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Paramètres globaux — pattern Singleton calqué sur VariableRepository du prof.
 * Toutes les constantes de calibration sont centralisées ici.
 * Pour ajuster l'équilibre de la simulation : modifier ce seul fichier.
 */
public class GameConfiguration {

	private static GameConfiguration instance = new GameConfiguration();
	private GameConfiguration() {}
	public static GameConfiguration getInstance() { return instance; }

	// Dimensions de la fenêtre
	public static final int WINDOW_WIDTH       = 1450;
	public static final int WINDOW_HEIGHT      = 835;
	public static final int BLOCK_SIZE         = 12;
	public static final int MENU_RIGHT_WIDTH   = 270;
	public static final int MENU_BOTTOM_HEIGHT = 60;
	public static final int TOP_BAR_HEIGHT     = 50;
	public static final int LINE_COUNT   = (WINDOW_HEIGHT - MENU_BOTTOM_HEIGHT - TOP_BAR_HEIGHT) / BLOCK_SIZE;
	public static final int COLUMN_COUNT = (WINDOW_WIDTH - MENU_RIGHT_WIDTH) / BLOCK_SIZE;
	public static final int GAME_SPEED   = 500;

	// Taux de base des besoins (influence OCEAN)
	public static final double BASE_FAIM         = 0.30;  // réduit — faim descend moins vite
	public static final double BASE_FATIGUE      = 0.35;  // réduit — fatigue descend moins vite
	public static final double BASE_SOCIAL       = 0.18;  // réduit — social descend moins vite
	public static final double BASE_RECUPERATION = 0.80;  // augmenté — récupération nocturne meilleure
	public static final double OCEAN_IMPACT      = 0.20;

	// Usure naturelle des besoins
	public static final double USURE_FAIM    = 0.05;  // réduit
	public static final double USURE_FATIGUE = 0.04;  // réduit
	public static final double USURE_SOCIAL  = 0.03;  // réduit — social plus stable

	// Récupération nocturne
	public static final double PROBA_FAIM_NUIT = 0.40;

	// Seuils critiques des besoins
	public static final int SEUIL_FAIM_CRITIQUE    = 20;
	public static final int SEUIL_SOCIAL_CRITIQUE  = 20;
	public static final int SEUIL_FATIGUE_CRITIQUE = 15;

	// Seuils confort des besoins
	public static final int SEUIL_FAIM_BON    = 70;
	public static final int SEUIL_SOCIAL_BON  = 60;  // plus facile à atteindre
	public static final int SEUIL_FATIGUE_BON = 45;  // plus facile à atteindre

	// Probabilités d'évolution du moral
	public static final double PROBA_MORAL_REMONTE = 0.12;  // augmenté — moral remonte plus facilement
	public static final double PROBA_MORAL_BONUS   = 0.08;  // augmenté
	public static final double PROBA_MORAL_BAISSE  = 0.08;  // réduit — moral baisse moins vite

	// Seuil de mort
	public static final double PROBA_SANTE_BAISSE = 0.10;  // réduit — moins de décès

	// Seuils de déplacement
	public static final int    SEUIL_EPUISEMENT_TOTAL        = 10;
	public static final int    SEUIL_FATIGUE_LENTE           = 40;
	public static final int    SEUIL_DEPRESSION_MOUVEMENT    = 20;
	public static final double PROBA_NOCTAMBULE_BOUGE        = 0.50;
	public static final double PROBA_DEPRIME_BOUGE           = 0.50;
	public static final int    SEUIL_NOCTAMBULE_EXTRAVERSION = 70;
	public static final int    SEUIL_NOCTAMBULE_CONSCIENCE   = 30;

	// Déplacement anxieux
	public static final double PROBA_ANXIEUX_BOUGE   = 0.30;
	public static final int    DISTANCE_MAX_DOMICILE = 5;

	// Déplacement extraverti
	public static final int    TOURS_AVANT_CHANGEMENT = 15;
	public static final double PROBA_PAS_ALEATOIRE    = 0.20;

	// Déplacement introverti
	public static final double PROBA_INTROVERTI_BOUGE = 0.55;

	// Contagion émotionnelle
	public static final double FACTEUR_VULNERABLE        = 1.5;
	public static final double FACTEUR_RESILIENT         = 0.5;
	public static final double FACTEUR_RESILIENT_POSITIF = 1.2;

	// Traumatisme
	public static final int TOURS_AVANT_TRAUMATISME = 5;

	// Résistance collective — modèle ressort
	public static final double COEFF_RESISTANCE_COLLECTIVE = 0.08;
}