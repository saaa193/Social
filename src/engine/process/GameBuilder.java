package engine.process;

import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.map.Block;
import engine.map.Map;

public class GameBuilder {

    public static Map buildMap() {
        return new Map(GameConfiguration.LINE_COUNT, GameConfiguration.COLUMN_COUNT);
    }

    public static MobileInterface buildInitMobile(Map map) {
        // CORRECTION 1 : On utilise le type concret "MobileElementManager"
        // pour pouvoir utiliser la méthode .addHabitant()
        MobileElementManager manager = new MobileElementManager(map);

        for (int i = 0; i < 100; i++) { // J'ai mis 20 habitants pour commencer (100 ça fait beaucoup !)

            // Génération de position aléatoire
            int line = (int)(Math.random() * GameConfiguration.LINE_COUNT);
            int column = (int)(Math.random() * GameConfiguration.COLUMN_COUNT);

            Block position = map.getBlock(line, column);

            String prenom = "Habitant" + i;
            int age = 18 + i;
            String sexe = (i % 2 == 0) ? "Male" : "Female";

            // Création de l'habitant
            Habitant h = new Habitant(position, prenom, sexe, age);

            // CORRECTION 2 (LA PLUS IMPORTANTE) :
            // IL FAUT AJOUTER L'HABITANT DANS LA LISTE DU MANAGER !
            // Sans cette ligne, l'habitant est créé puis supprimé aussitôt.
            manager.addHabitant(h);
        }

        return manager;
    }
}