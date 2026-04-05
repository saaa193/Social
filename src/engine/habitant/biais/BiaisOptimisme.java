package engine.habitant.biais;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Biais optimiste : minimise les mauvaises nouvelles.
 * Profil OCEAN : agreabilite elevee.
 * Un habitant agreable relativise les rumeurs negatives.
 */
public class BiaisOptimisme implements BiaisCognitif {

	@Override
	public int filtrerImpact(int impact, float veracite) {
		if (impact < 0) {
			return (int) (impact * 0.5);
		}
		return impact;
	}
}