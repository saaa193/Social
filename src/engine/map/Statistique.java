package engine.map;

import engine.habitant.Habitant;
import engine.habitant.lien.Liens;

import java.util.List;

/**
 * Classe utilitaire qui calcule les statistiques globales de la population.
 * Utilisée par les dashboards pour afficher les indicateurs en temps réel.
 *
 * Refactorisée pour respecter l'OCP : plus aucun instanceof.
 * On délègue l'identification du type au lien lui-même via getTypeLien().
 */
public class Statistique {

	public static int getPopulationVivante(List<Habitant> habitants) {
		int count = 0;
		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() > 0) count++;
		}
		return count;
	}

	public static int getMoralMoyen(List<Habitant> habitants) {
		if (habitants.isEmpty()) return 0;
		int total = 0;
		for (Habitant h : habitants) {
			total += h.getMoral();
		}
		return total / habitants.size();
	}

	public static int getNombreInfluences(List<Habitant> habitants) {
		int count = 0;
		for (Habitant h : habitants) {
			if (h.getMoral() < 40) count++;
		}
		return count;
	}

	/**
	 * Compte les liens d'un type donné dans toute la population.
	 * Remplace les 3 anciennes méthodes qui utilisaient instanceof.
	 * Pour ajouter un nouveau type de lien, aucune modification ici n'est nécessaire.
	 */
	public static int getNombreLiensParType(List<Habitant> habitants, String type) {
		int count = 0;
		for (Habitant h : habitants) {
			for (Liens l : h.getRelation()) {
				if (l.getTypeLien().equals(type)) count++;
			}
		}
		return count / 2;
	}
}