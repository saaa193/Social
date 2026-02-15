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
        MobileElementManager manager = new MobileElementManager(map);

        for (int i = 0; i < 150; i++) {

            int line = (int)(Math.random() * GameConfiguration.LINE_COUNT);
            int column = (int)(Math.random() * GameConfiguration.COLUMN_COUNT);

            Block position = map.getBlock(line, column);

            String prenom = "Habitant" + i;
            int age = 18 + i;
            String sexe = (i % 2 == 0) ? "Male" : "Female";

            Habitant h = new Habitant(position, prenom, sexe, age);

            manager.addHabitant(h);
        }

        return manager;
    }
}