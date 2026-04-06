package engine.habitant.biais;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Biais negatif : amplifie les mauvaises nouvelles.
 * Profil OCEAN : nevrosisme eleve.
 * Un habitant anxieux percoit les rumeurs comme plus graves.
 */
public class BiaisNegatif implements BiaisCognitif {

	private static final double FACTEUR_AMPLIFICATION = 1.5;

	@Override
	public int filtrerImpact(int impact, float veracite) {
		double facteur = impact < 0 ? FACTEUR_AMPLIFICATION : 1.0;
		return (int) (impact * facteur);
	}
}