package engine.process;

import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.map.Block;
import engine.map.Map;
import java.util.List;

/**
 * Classe GameBuilder : C'est notre "Factory"
 * Son rôle est d'isoler toute la logique de création et d'initialisation du jeu.
 * Cela évite de surcharger le MainGUI avec des détails techniques.
 */
public class GameBuilder {

    // Construit la carte en utilisant les paramètres de configuration globale.
    public static Map buildMap() {
        return new Map(GameConfiguration.LINE_COUNT, GameConfiguration.COLUMN_COUNT);
    }

    /**
     * Méthode d'initialisation complète :
     * 1. Crée le gestionnaire d'éléments mobiles.
     * 2. Peuple la carte avec 150 habitants placés aléatoirement.
     * 3. Établit les liens familiaux de base pour créer une structure sociale initiale.
     */
    public static MobileInterface buildInitMobile(Map map) {
        MobileElementManager manager = new MobileElementManager(map);

        // Génération procédurale de 200 habitants
        for (int i = 0; i < 200; i++) {
            // Positionnement aléatoire via les dimensions de la carte
            int line = (int)(Math.random() * GameConfiguration.LINE_COUNT);
            int column = (int)(Math.random() * GameConfiguration.COLUMN_COUNT);

            Block position = map.getBlock(line, column);

            // Création des attributs d'identité
            String prenom = "Habitant" + i;
            int age = 18 + i;
            String sexe = (i % 2 == 0) ? "Male" : "Female";

            Habitant h = new Habitant(position, prenom, sexe, age);

            manager.addHabitant(h);
        }

        // Initialisation de la structure sociale :
        // On parcourt la liste des habitants pour créer des binômes (0 avec 1, 2 avec 3, etc.)
        List<Habitant> liste = manager.getHabitants();
        for (int i = 0; i < liste.size() - 1; i += 2) {
            Habitant h1 = liste.get(i);
            Habitant h2 = liste.get(i + 1);

            // On ajoute une relation de type "Familial" bidirectionnelle.
            // Cela garantit que la simulation ne commence pas avec des agents totalement isolés.
            h1.getRelation().add(new engine.habitant.lien.Familial(h2, 100));
            h2.getRelation().add(new engine.habitant.lien.Familial(h1, 100));
        }

        return manager;
    }
}