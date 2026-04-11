package engine.process;

import config.GameConfiguration;
import config.RandomProvider;
import engine.habitant.Habitant;
import engine.habitant.lien.Familial;
import engine.map.Block;
import engine.map.Map;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * GameBuilder : isole la logique de création et d'initialisation du jeu.
 * Évite de surcharger MainGUI avec des détails techniques.
 */
public class GameBuilder {

	/**
	 * Construit la carte selon les paramètres de configuration globale.
	 */
	public static Map buildMap() {
		return new Map(GameConfiguration.LINE_COUNT, GameConfiguration.COLUMN_COUNT);
	}

	/**
	 * Initialise le gestionnaire d'éléments mobiles et peuple la carte.
	 * Établit les liens familiaux de base pour créer une structure sociale initiale.
	 */
	public static MobileInterface buildInitMobile(Map map) {
		MobileElementManager manager = new MobileElementManager(map);

		for (int i = 0; i < 200; i++) {
			int line = RandomProvider.getInstance().nextInt(GameConfiguration.LINE_COUNT);
			int column = RandomProvider.getInstance().nextInt(GameConfiguration.COLUMN_COUNT);

			Block position = map.getBlock(line, column);

			String prenom = "Habitant" + i;
			int age = 18 + RandomProvider.getInstance().nextInt(47);
			String sexe = (i % 2 == 0) ? "Homme" : "Femme";

			manager.addHabitant(new Habitant(position, map, prenom, sexe, age));
		}

		List<Habitant> liste = manager.getHabitants();
		for (int i = 0; i < liste.size() - 1; i += 2) {
			Habitant h1 = liste.get(i);
			Habitant h2 = liste.get(i + 1);
			h1.getRelation().add(new Familial(h2, 100));
			h2.getRelation().add(new Familial(h1, 100));
		}

		return manager;
	}
}