package engine.habitant.lien;

import engine.habitant.Habitant;

/**
 * Lien familial entre deux habitants.
 * Booste le moral en priorité. Ne meurt jamais (force min = 10).
 */
public class Familial extends Liens {

	public Familial(Habitant partenaire, int force) {
		super(partenaire, force);
	}

	@Override
	public String getTypeLien() {
		return "Familial";
	}

	@Override
	public void appliquerBonusMental(Habitant proprietaire) {
		double ratio = this.force / 100.0;
		int bonusMoral = (int) (2 * ratio);
		int bonusSocial = (int) (3 * ratio);
		proprietaire.getBesoins().setMoral(proprietaire.getBesoins().getMoral() + bonusMoral);
		proprietaire.getBesoins().setSocial(proprietaire.getBesoins().getSocial() + bonusSocial);
	}
	@Override
	public boolean evoluerForce(Habitant proprietaire) {
		if (proprietaire.getMoral() > 50 && partenaire.getMoral() > 50) {
			setForce(getForce() + 1);
		} else if (proprietaire.getMoral() < 30 || partenaire.getMoral() < 30) {
			setForce(getForce() - 1);
		}

		// Un lien familial ne meurt jamais
		if (this.force < 10) setForce(10);
		return true;
	}
}