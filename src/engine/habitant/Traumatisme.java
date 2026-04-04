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
	 * Applique les sequelles permanentes sur le profil OCEAN de l'habitant.
	 * Le malus est module par la resistance collective de la population.
	 * Un resiliant subit des sequelles reduites de moitie.
	 *
	 * @param habitant   l'habitant qui subit le traumatisme
	 * @param resistance la resistance collective (0 a 100)
	 */
	public void appliquer(Habitant habitant, int resistance) {
		// La resistance reduit le malus de base — entre 3 et 10
		int malus = 10 - (resistance / 15);
		if (malus < 3) {
			malus = 3;
		}

		if (habitant.getPsychologie().estResiliant()) {
			malus = malus / 2;
		}

		habitant.getPsychologie().augmenterNevrosisme(malus);
		habitant.getPsychologie().diminuerAgreabilite(malus / 2);
		habitant.getPsychologie().diminuerOuverture(malus / 2);
	}
}