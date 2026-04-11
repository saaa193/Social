package engine.analyse;

import engine.habitant.Habitant;
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

		int liensReels = 0;
		for (Habitant h : habitants) {
			if (h.getRelation() != null) {
				liensReels += h.getRelation().size();
			}
		}
		liensReels = liensReels / 2;

		int n = habitants.size();
		int liensMax = (n * (n - 1)) / 2;

		return (double) liensReels / liensMax;
	}

	@Override
	public String getNom() {
		return "Cohésion sociale";
	}
}