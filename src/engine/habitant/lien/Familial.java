package engine.habitant.lien;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Lien familial entre deux habitants.
 * Booste le moral en priorite. Ne meurt jamais.
 */
public class Familial extends Liens {

	public Familial(Habitant partenaire, int force) {
		super(partenaire, force);
	}

	@Override
	public String getTypeLien() {
		return "Familial";
	}

	/**
	 * Applique un bonus de moral et de social proportionnel a la force du lien.
	 */
	@Override
	public void appliquerBonusMental(Habitant proprietaire) {
		double ratio = this.force / 100.0;
		int bonusMoral = (int) (2 * ratio);
		int bonusSocial = (int) (3 * ratio);
		proprietaire.getBesoins().setMoral(proprietaire.getBesoins().getMoral() + bonusMoral);
		proprietaire.getBesoins().setSocial(proprietaire.getBesoins().getSocial() + bonusSocial);
	}

	/**
	 * Fait evoluer la force du lien. Un lien familial ne meurt jamais.
	 * Retourne toujours true.
	 */
	@Override
	public boolean evoluerForce(Habitant proprietaire) {
		if (proprietaire.getMoral() > 50 && partenaire.getMoral() > 50) {
			setForce(getForce() + 1);
		} else if (proprietaire.getMoral() < 30 || partenaire.getMoral() < 30) {
			setForce(getForce() - 1);
		}
		if (this.force < 10) setForce(10);
		return true;
	}
}