package engine.analyse;

import engine.habitant.Habitant;
import engine.habitant.lien.Liens;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Indicateur de cohésion sociale.
 * Mesure le ratio entre le nombre réel de liens
 * et le nombre maximum possible dans la population.
 * 0.0 = population atomisée, 1.0 = tout le monde se connaît.
 */
public class CohesionSociale implements IndicateurMacro {

    @Override
    public double calculer(List<Habitant> habitants) {
        if (habitants.size() < 2) return 0.0;

        // Nombre réel de liens dans toute la population
        int liensReels = 0;
        for (Habitant h : habitants) {
            if (h.getRelation() != null) {
                liensReels += h.getRelation().size();
            }
        }
        // Chaque lien est compté deux fois (A→B et B→A)
        liensReels = liensReels / 2;

        // Nombre maximum possible : n*(n-1)/2
        int n = habitants.size();
        int liensMax = (n * (n - 1)) / 2;

        return (double) liensReels / liensMax;
    }

    @Override
    public String getNom() {
        return "Cohésion sociale";
    }
}