package config;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Paramètres globaux — pattern Singleton calqué sur VariableRepository du prof (Tree V1).
 * Constructeur privé, instance unique statique, accès via getInstance().
 */
public class GameConfiguration {

	// L'unique instance — initialisée directement comme le prof
	private static GameConfiguration instance = new GameConfiguration();

	// Constructeur privé — personne ne peut faire new GameConfiguration()
	private GameConfiguration() {}

	// Méthode statique — comme VariableRepository.getInstance() du prof
	public static GameConfiguration getInstance() {
		return instance;
	}

	// Constantes de configuration — restent static final comme avant
	public static final int WINDOW_WIDTH  = 1450;
	public static final int WINDOW_HEIGHT = 835;
	public static final int BLOCK_SIZE    = 12;

	public static final int MENU_RIGHT_WIDTH   = 270;
	public static final int MENU_BOTTOM_HEIGHT = 60;
	public static final int TOP_BAR_HEIGHT     = 50;

	public static final int LINE_COUNT   = (WINDOW_HEIGHT - MENU_BOTTOM_HEIGHT - TOP_BAR_HEIGHT) / BLOCK_SIZE;
	public static final int COLUMN_COUNT = (WINDOW_WIDTH - MENU_RIGHT_WIDTH) / BLOCK_SIZE;

	public static final int GAME_SPEED = 500;

	public static final double BASE_FAIM         = 0.37;
	public static final double BASE_FATIGUE      = 0.42;
	public static final double BASE_SOCIAL       = 0.26;
	public static final double BASE_RECUPERATION = 0.70;
	public static final double OCEAN_IMPACT      = 0.20;
}