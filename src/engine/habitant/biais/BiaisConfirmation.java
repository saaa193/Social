package engine.habitant.biais;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Biais de confirmation : croit uniquement les informations veridiques.
 * Profil OCEAN : conscience elevee.
 * Un habitant consciencieux ignore les rumeurs peu fiables.
 */
public class BiaisConfirmation implements BiaisCognitif {

	private static final float SEUIL_VERACITE    = 0.60f;
	private static final double FACTEUR_CREDIBLE  = 1.2;
	private static final double FACTEUR_INCREDULE = 0.1;

	@Override
	public int filtrerImpact(int impact, float veracite) {
		double facteur = veracite >= SEUIL_VERACITE ? FACTEUR_CREDIBLE : FACTEUR_INCREDULE;
		return (int) (impact * facteur);
	}
}