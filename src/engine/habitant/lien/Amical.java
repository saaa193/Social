package engine.habitant.lien;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Lien amical entre deux habitants : booste le moral et le social.
 * Le lien peut mourir si le moral des deux habitants est trop bas.
 */
public class Amical extends Liens {

	public Amical(Habitant partenaire, int force) {
		super(partenaire, force);
	}

	@Override
	public void appliquerBonusMental(Habitant proprietaire) {
		double ratio = this.force / 100.0;

		int bonusMoral = (int) (15 * ratio);
		int bonusSocial = (int) (30 * ratio);

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

		// On retourne true si le lien est toujours en vie, false sinon
		return !estMort();
	}
}