package config;

public class GameConfiguration {
    // Tes dimensions validées
    public static final int WINDOW_WIDTH = 1450;
    public static final int WINDOW_HEIGHT = 835;

    public static final int BLOCK_SIZE = 15; // Passage en HD

    // Taille des menus (pour que la carte ne les écrase pas)
    public static final int MENU_RIGHT_WIDTH = 250;
    public static final int MENU_BOTTOM_HEIGHT = 120;
    public static final int TOP_BAR_HEIGHT = 50;

    // Le calcul dynamique du prof
    public static final int LINE_COUNT = (WINDOW_HEIGHT - MENU_BOTTOM_HEIGHT - TOP_BAR_HEIGHT) / BLOCK_SIZE;
    public static final int COLUMN_COUNT = (WINDOW_WIDTH - MENU_RIGHT_WIDTH) / BLOCK_SIZE;

    public static final int GAME_SPEED = 500;
}