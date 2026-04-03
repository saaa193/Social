package engine.habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 * <p>
 * Traumatisme : représente une séquelle psychologique permanente.
 * Se déclenche quand un habitant reste dans un état négatif trop longtemps.
 * Les séquelles sont permanentes — le profil OCEAN ne revient jamais
 * complètement à son état initial.
 * Ancré dans le Big Five : les expériences difficiles modifient
 * durablement les traits de personnalité.
 */
public class Traumatisme {

	/**
	 * Applique les séquelles permanentes sur le profil OCEAN de l'habitant.
	 * Un résilient subit des séquelles réduites de moitié.
	 */
	public void appliquer(Habitant habitant) {

		// Séquelles de base
		int malus = 10;

		// Un résilient résiste mieux au traumatisme
		if (habitant.getPsychologie().estResiliant()) {
			malus = 5;
		}

		// Le névrosisme augmente définitivement
		habitant.getPsychologie().augmenterNevrosisme(malus);

		// L'agréabilité diminue — l'habitant se ferme aux autres
		habitant.getPsychologie().diminuerAgreabilite(malus / 2);

		// L'ouverture diminue — l'habitant se méfie du monde
		habitant.getPsychologie().diminuerOuverture(malus / 2);
	}
}