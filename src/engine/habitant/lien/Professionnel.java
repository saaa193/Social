package engine.habitant.lien;

import config.GameConfiguration;
import config.RandomProvider;
import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Lien professionnel entre deux habitants.
 * Booste le social et impacte le moral aleatoirement.
 */
public class Professionnel extends Liens {

	public Professionnel(Habitant partenaire, int force) {
		super(partenaire, force);
	}

	@Override
	public String getTypeLien() {
		return "Professionnel";
	}

	/**
	 * Applique un bonus social et un impact moral aleatoire selon la journee.
	 */
	@Override
	public void appliquerBonusMental(Habitant proprietaire) {
		double ratio = this.force / 100.0;
		int bonusSocial = (int) (4 * ratio);
		proprietaire.getBesoins().setSocial(
				proprietaire.getBesoins().getSocial() + bonusSocial
		);
		if (RandomProvider.getInstance().nextDouble() < GameConfiguration.PROBA_BONNE_JOURNEE_PRO) {
			int bonusMoral = (int) (2 * ratio);
			proprietaire.getBesoins().setMoral(
					proprietaire.getBesoins().getMoral() + bonusMoral
			);
		} else {
			int malusMoral = (int) (2 * ratio);
			proprietaire.getBesoins().setMoral(
					proprietaire.getBesoins().getMoral() - malusMoral
			);
		}
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
			setForce(getForce() - 4);
		}
		return !estMort();
	}
}