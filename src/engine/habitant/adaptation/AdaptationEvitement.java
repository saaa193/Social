package engine.habitant.adaptation;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Adaptation par evitement : l'habitant fuit et s'isole.
 * Profil OCEAN : nevrosisme eleve.
 * Le traumatisme reduit fortement l'extraversion et l'agreabilite.
 */
public class AdaptationEvitement implements StrategieAdaptation {

	private static final double FACTEUR_AMPLIFICATION = 1.3;

	@Override
	public void appliquer(Habitant habitant, int resistanceCollective) {
		int malus = 10 - (resistanceCollective / 15);
		if (malus < 3) {
			malus = 3;
		}
		int malusAmplifie = (int) (malus * FACTEUR_AMPLIFICATION);
		habitant.getPsychologie().augmenterNevrosisme(malusAmplifie);
		habitant.getPsychologie().diminuerAgreabilite(malus / 2);
		habitant.getPsychologie().diminuerOuverture(malus / 2);
	}
}