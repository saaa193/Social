package config;

public class GameConfiguration {
    public static final int WINDOW_WIDTH = 1920;
    public static final int WINDOW_HEIGHT = 1080;

    // LE PASSAGE EN HD : On réduit les blocs de 25 à 15.
    // Ça va rendre la ville beaucoup plus grande et précise !
    public static final int BLOCK_SIZE = 15;

    // On définit la largeur de ton panneau de droite
    public static final int MENU_WIDTH = 200;

    // LA CORRECTION : La grille s'arrête avant le menu !
    public static final int LINE_COUNT = WINDOW_HEIGHT / BLOCK_SIZE;
    public static final int COLUMN_COUNT = (WINDOW_WIDTH - MENU_WIDTH) / BLOCK_SIZE;

    public static final int GAME_SPEED = 500;
}