package config;

public class GameConfiguration {
    // Dimensions globales de la fenêtre (en pixels)
    public static final int WINDOW_WIDTH = 1450;
    public static final int WINDOW_HEIGHT = 835;

    // Taille d'un bloc sur la carte
    public static final int BLOCK_SIZE = 12;

    // Paramètres de taille des panneaux de l'interface (UI) pour réserver l'espace
    public static final int MENU_RIGHT_WIDTH = 250;    // Espace pour le panneau de droite (stats)
    public static final int MENU_BOTTOM_HEIGHT = 120;  // Espace pour le panneau du bas (opérations)
    public static final int TOP_BAR_HEIGHT = 50;       // Espace pour la barre de contrôle en haut

    // Calcul automatique de la grille : on soustrait les menus pour que la carte tienne pile poil
    // LINE_COUNT : Nombre de lignes disponibles pour la carte selon la hauteur restante
    public static final int LINE_COUNT = (WINDOW_HEIGHT - MENU_BOTTOM_HEIGHT - TOP_BAR_HEIGHT) / BLOCK_SIZE;

    // COLUMN_COUNT : Nombre de colonnes disponibles pour la carte selon la largeur restante
    public static final int COLUMN_COUNT = (WINDOW_WIDTH - MENU_RIGHT_WIDTH) / BLOCK_SIZE;

    // Vitesse de la boucle de jeu (en millisecondes). 500ms signifie un tour toutes les 0.5s
    public static final int GAME_SPEED = 500;
}