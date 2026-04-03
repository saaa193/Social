package engine.habitant.lien;

import engine.habitant.Habitant;
/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Lien professionnel entre deux habitants : booste le social
 * et impacte le moral aléatoirement selon l'ambiance au travail.
 */
public class Professionnel extends Liens {

	public Professionnel(Habitant partenaire, int force) {
		super(partenaire, force);
	}

	@Override
	public void appliquerBonusMental(Habitant proprietaire) {
		double ratio = this.force / 100.0;

		// Social → priorité principale du lien professionnel
		int bonusSocial = (int) (20 * ratio);
		proprietaire.getBesoins().setSocial(
				proprietaire.getBesoins().getSocial() + bonusSocial
		);

		// Moral → aléatoire selon "l'humeur" au travail
		if (Math.random() < 0.5) {
			// Bonne journée au boulot
			int bonusMoral = (int) (10 * ratio);
			proprietaire.getBesoins().setMoral(
					proprietaire.getBesoins().getMoral() + bonusMoral
			);
		} else {
			// Mauvaise journée au boulot
			int malusMoral = (int) (8 * ratio);
			proprietaire.getBesoins().setMoral(
					proprietaire.getBesoins().getMoral() - malusMoral
			);
		}
	}

	@Override
	public boolean evoluerForce(Habitant proprietaire) {

		if (proprietaire.getMoral() > 50 && partenaire.getMoral() > 50) {
			// Bonne ambiance → lien se renforce
			setForce(getForce() + 2);
		} else if (proprietaire.getMoral() < 30 || partenaire.getMoral() < 30) {
			// Mauvaise ambiance → lien se dégrade vite
			setForce(getForce() - 4);
		}

		// Le lien pro peut mourir (démission, licenciement...)
		return !estMort();
	}
}