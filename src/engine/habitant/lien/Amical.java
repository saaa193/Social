package engine.habitant.lien;

import engine.habitant.Habitant;

/**
 * Lien amical entre deux habitants.
 * Booste le social en priorité, le moral en secondaire.
 * Peut mourir si le moral des deux habitants s'effondre.
 */
public class Amical extends Liens {

	public Amical(Habitant partenaire, int force) {
		super(partenaire, force);
	}

	@Override
	public String getTypeLien() {
		return "Amical";
	}

	@Override
	public void appliquerBonusMental(Habitant proprietaire) {
		double ratio = this.force / 100.0;
		int bonusMoral = (int) (3 * ratio);
		int bonusSocial = (int) (5 * ratio);
		proprietaire.getBesoins().setMoral(proprietaire.getBesoins().getMoral() + bonusMoral);
		proprietaire.getBesoins().setSocial(proprietaire.getBesoins().getSocial() + bonusSocial);
	}

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