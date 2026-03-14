package engine.map;

import engine.habitant.Habitant;
import engine.habitant.lien.Amical;
import engine.habitant.lien.Familial;
import engine.habitant.lien.Liens;
import engine.habitant.lien.Professionnel;

import java.util.List;

/**
 * Classe utilitaire qui calcule les statistiques globales de la population.
 * Utilisée par les dashboards pour afficher les indicateurs en temps réel.
 */
public class Statistique {

    /**
     * Retourne le nombre total d'habitants vivants.
     */
    public static int getPopulationVivante(List<Habitant> habitants) {
        int count = 0;
        for (Habitant h : habitants) {
            if (h.getBesoins().getSante() > 0) count++;
        }
        return count;
    }

    /**
     * Calcule le moral moyen de toute la population (0-100).
     */
    public static int getMoralMoyen(List<Habitant> habitants) {
        if (habitants.isEmpty()) return 0;
        int total = 0;
        for (Habitant h : habitants) {
            total += h.getMoral();
        }
        return total / habitants.size();
    }

    /**
     * Compte les habitants considérés comme influencés.
     * Un habitant est influencé si son moral est inférieur à 40.
     */
    public static int getNombreInfluences(List<Habitant> habitants) {
        int count = 0;
        for (Habitant h : habitants) {
            if (h.getMoral() < 40) count++;
        }
        return count;
    }

    /**
     * Compte le nombre de liens familiaux dans la population.
     */
    public static int getNombreLiensFamiliaux(List<Habitant> habitants) {
        int count = 0;
        for (Habitant h : habitants) {
            for (Liens l : h.getRelation()) {
                if (l instanceof Familial) count++;
            }
        }
        return count / 2;
    }

    /**
     * Compte le nombre de liens amicaux dans la population.
     */
    public static int getNombreLiensAmicaux(List<Habitant> habitants) {
        int count = 0;
        for (Habitant h : habitants) {
            for (Liens l : h.getRelation()) {
                if (l instanceof Amical) count++;
            }
        }
        return count / 2;
    }

    /**
     * Compte le nombre de liens professionnels dans la population.
     */
    public static int getNombreLiensProfessionnels(List<Habitant> habitants) {
        int count = 0;
        for (Habitant h : habitants) {
            for (Liens l : h.getRelation()) {
                if (l instanceof Professionnel) count++;
            }
        }
        return count / 2;
    }

}