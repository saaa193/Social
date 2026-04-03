package engine.habitant.lien;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Lien familial entre deux habitants : booste le moral et le social.
 * Le lien familial ne meurt jamais, sa force minimale est 10.
 */
public class Familial extends Liens {

	public Familial(Habitant partenaire, int force) {
		super(partenaire, force);
	}

	@Override
	public void appliquerBonusMental(Habitant proprietaire) {
		double ratio = this.force / 100.0;

		int bonusMoral = (int) (25 * ratio);
		int bonusSocial = (int) (15 * ratio);

		proprietaire.getBesoins().setMoral(proprietaire.getBesoins().getMoral() + bonusMoral);
		proprietaire.getBesoins().setSocial(proprietaire.getBesoins().getSocial() + bonusSocial);
	}

	@Override
	public boolean evoluerForce(Habitant proprietaire) {

		if (proprietaire.getMoral() > 50 && partenaire.getMoral() > 50) {
			// Les deux vont bien → lien se renforce doucement
			setForce(getForce() + 1);
		} else if (proprietaire.getMoral() < 30 || partenaire.getMoral() < 30) {
			// L'un va mal → dégradation très faible (famille = stable)
			setForce(getForce() - 1);
		}

		// Un lien familial ne meurt JAMAIS → on le bloque à 10 minimum
		if (this.force < 10) setForce(10);

		return true; // toujours vivant
	}
}