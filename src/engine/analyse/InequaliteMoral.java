package engine.analyse;

import engine.habitant.Habitant;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Indicateur d'inégalité du moral — inspiré de l'indice de Gini.
 * Mesure si le moral est uniformément distribué ou s'il y a
 * de grandes disparités dans la population.
 * 0.0 = tout le monde a le même moral (égalité parfaite)
 * 1.0 = inégalité maximale (certains très heureux, d'autres pas)
 * Une ville en crise a une forte inégalité.
 */
public class InequaliteMoral implements IndicateurMacro {

    @Override
    public double calculer(List<Habitant> habitants) {
        if (habitants.isEmpty()) return 0.0;

        // On récupère les moraux des habitants vivants
        List<Integer> moraux = new java.util.ArrayList<Integer>();
        for (Habitant h : habitants) {
            if (h.getBesoins().getSante() > 0) {
                moraux.add(h.getMoral());
            }
        }

        if (moraux.size() < 2) return 0.0;

        // Calcul de la moyenne
        double somme = 0;
        for (int m : moraux) somme += m;
        double moyenne = somme / moraux.size();

        if (moyenne == 0) return 0.0;

        // Calcul de l'indice de Gini simplifié
        // Somme des différences absolues entre chaque paire
        double sommeDiff = 0;
        for (int m1 : moraux) {
            for (int m2 : moraux) {
                sommeDiff += Math.abs(m1 - m2);
            }
        }

        double gini = sommeDiff / (2.0 * moraux.size() * moraux.size() * moyenne);
        return Math.min(1.0, gini);
    }

    @Override
    public String getNom() {
        return "Inégalité du moral";
    }
}