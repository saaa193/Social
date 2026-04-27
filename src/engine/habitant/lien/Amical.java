package engine.habitant.lien;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Lien amical entre deux habitants.
 * Booste le social en priorite, le moral en secondaire.
 */
public class Amical extends Liens {

	public Amical(Habitant partenaire, int force) {
		super(partenaire, force);
	}

	@Override
	public String getTypeLien() {
		return "Amical";
	}

	/**
	 * Applique un bonus de moral et de social proportionnel a la force du lien.
	 */
	@Override
	public void appliquerBonusMental(Habitant proprietaire) {
		double ratio = this.force / 100.0;
		int bonusMoral = (int) (3 * ratio);
		int bonusSocial = (int) (5 * ratio);
		proprietaire.getBesoins().setMoral(proprietaire.getBesoins().getMoral() + bonusMoral);
		proprietaire.getBesoins().setSocial(proprietaire.getBesoins().getSocial() + bonusSocial);
	}

	/**
	 * Fait evoluer la force du lien selon le moral des deux habitants.
	 * Retourne false si le lien est mort.
	 */
	@Override
	public boolean evoluerForce(Habitant proprietaire) {
		if (proprietaire.getMoral() > 50 && partenaire.getMoral() > 50) {
			setForce(getForce() + 2);
		} else if (proprietaire.getMoral() < 30 || partenaire.getMoral() < 30) {
			setForce(getForce() - 3);
		}
		return !estMort();
	}
}