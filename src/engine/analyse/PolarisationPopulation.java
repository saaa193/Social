package engine.analyse;

import engine.habitant.Habitant;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Indicateur de polarisation de la population.
 * Mesure la variance des traits OCEAN dans la population.
 * Haute variance = population polarisée (groupes distincts).
 * Faible variance = population homogène (tout le monde se ressemble).
 *
 * Implémente IndicateurMacro — pattern Strategy :
 * s'intègre automatiquement dans AnalyseurPopulation
 * sans modifier aucune classe existante.
 */
public class PolarisationPopulation implements IndicateurMacro {

	@Override
	public double calculer(List<Habitant> habitants) {
		if (habitants.size() < 2) return 0.0;

		double varianceOuverture = calculerVariance(habitants, "ouverture");
		double varianceConscience = calculerVariance(habitants, "conscience");
		double varianceExtraversion = calculerVariance(habitants, "extraversion");
		double varianceAgreabilite = calculerVariance(habitants, "agreabilite");
		double varianceNevrosisme = calculerVariance(habitants, "nevrosisme");

		double varianceMoyenne = (varianceOuverture + varianceConscience
				+ varianceExtraversion + varianceAgreabilite
				+ varianceNevrosisme) / 5.0;

		return Math.min(1.0, varianceMoyenne / 2500.0);
	}

	private double calculerVariance(List<Habitant> habitants, String trait) {
		double somme = 0;
		int count = 0;
		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() > 0) {
				somme += getValeurTrait(h, trait);
				count++;
			}
		}
		if (count == 0) return 0.0;
		double moyenne = somme / count;

		double variance = 0;
		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() > 0) {
				double diff = getValeurTrait(h, trait) - moyenne;
				variance += diff * diff;
			}
		}
		return variance / count;
	}

	private int getValeurTrait(Habitant h, String trait) {
		switch (trait) {
			case "ouverture": return h.getOuverture();
			case "conscience": return h.getConscience();
			case "extraversion": return h.getExtraversion();
			case "agreabilite": return h.getAgreabilite();
			case "nevrosisme": return h.getNevrosisme();
			default: return 0;
		}
	}

	@Override
	public String getNom() {
		return "Polarisation OCEAN";
	}
}