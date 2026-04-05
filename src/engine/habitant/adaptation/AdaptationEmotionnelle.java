package engine.habitant.adaptation;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Adaptation emotionnelle : l'habitant cherche du soutien social.
 * Profil OCEAN : agreabilite elevee.
 * Le traumatisme reduit moins le moral mais augmente le besoin social.
 */
public class AdaptationEmotionnelle implements StrategieAdaptation {

	private static final double FACTEUR_REDUCTION = 0.6;

	@Override
	public void appliquer(Habitant habitant, int resistanceCollective) {
		int malus = 10 - (resistanceCollective / 15);
		if (malus < 3) {
			malus = 3;
		}
		int malusReduit = (int) (malus * FACTEUR_REDUCTION);
		habitant.getPsychologie().augmenterNevrosisme(malusReduit);
		habitant.getPsychologie().diminuerAgreabilite(1);
		habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() - malusReduit);
	}
}