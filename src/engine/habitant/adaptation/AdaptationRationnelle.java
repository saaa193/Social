package engine.habitant.adaptation;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Adaptation rationnelle : l'habitant analyse et structure sa reaction.
 * Profil OCEAN : conscience elevee.
 * Le traumatisme reduit moins le moral mais erode la conscience.
 */
public class AdaptationRationnelle implements StrategieAdaptation {

	private static final double FACTEUR_REDUCTION = 0.5;

	@Override
	public void appliquer(Habitant habitant, int resistanceCollective) {
		int malus = 10 - (resistanceCollective / 15);
		if (malus < 3) {
			malus = 3;
		}
		int malusReduit = (int) (malus * FACTEUR_REDUCTION);
		habitant.getPsychologie().augmenterNevrosisme(malusReduit);
		habitant.getPsychologie().diminuerConscience(malusReduit / 2);
		habitant.getPsychologie().diminuerOuverture(1);
	}
}