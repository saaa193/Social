package engine.habitant.lien;

import engine.habitant.Habitant;

/**
 * Lien professionnel entre deux habitants.
 * Booste le social, et impacte le moral aléatoirement (bonne/mauvaise journée).
 * Peut mourir si l'ambiance se dégrade trop.
 */
public class Professionnel extends Liens {

	public Professionnel(Habitant partenaire, int force) {
		super(partenaire, force);
	}

	@Override
	public String getTypeLien() {
		return "Professionnel";
	}

	@Override
	public void appliquerBonusMental(Habitant proprietaire) {
		double ratio = this.force / 100.0;

		int bonusSocial = (int) (20 * ratio);
		proprietaire.getBesoins().setSocial(
				proprietaire.getBesoins().getSocial() + bonusSocial
		);

		// Moral aléatoire selon l'ambiance au travail
		if (Math.random() < 0.5) {
			int bonusMoral = (int) (10 * ratio);
			proprietaire.getBesoins().setMoral(
					proprietaire.getBesoins().getMoral() + bonusMoral
			);
		} else {
			int malusMoral = (int) (8 * ratio);
			proprietaire.getBesoins().setMoral(
					proprietaire.getBesoins().getMoral() - malusMoral
			);
		}
	}

	@Override
	public boolean evoluerForce(Habitant proprietaire) {
		if (proprietaire.getMoral() > 50 && partenaire.getMoral() > 50) {
			setForce(getForce() + 2);
		} else if (proprietaire.getMoral() < 30 || partenaire.getMoral() < 30) {
			setForce(getForce() - 4);
		}
		return !estMort();
	}
}