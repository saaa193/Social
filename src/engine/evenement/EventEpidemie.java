package engine.evenement;

import config.RandomProvider;
import engine.habitant.Habitant;
import engine.habitant.biais.BiaisCognitif;
import engine.habitant.lien.Liens;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Epidemie : le plus destructeur des evenements. Touche toute la population.
 * Seul evenement qui reduit la sante. Se propage via les liens sociaux.
 */
public class EventEpidemie implements EvenementSimulation, EventVisitor {

	@Override
	public boolean estConcerne(Habitant h) {
		return true;
	}

	@Override
	public void appliquer(Habitant h) {
		h.acceptEvent(this);
		propagerViaLiens(h);
	}

	@Override
	public void visit(Habitant habitant) {
		BiaisCognitif biais = habitant.getPsychologie().determinerBiais();

		if (habitant.getPsychologie().estVulnerable()) {
			int impact = biais.filtrerImpact(-30, 0.5f);
			habitant.getBesoins().setSante(habitant.getBesoins().getSante() - 20);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 25);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + impact);
			habitant.getPsychologie().augmenterNevrosisme(5);
		} else if (habitant.getPsychologie().estResiliant()) {
			int impact = biais.filtrerImpact(-10, 0.5f);
			habitant.getBesoins().setSante(habitant.getBesoins().getSante() - 5);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 10);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + impact);
		} else {
			int impact = biais.filtrerImpact(-20, 0.5f);
			habitant.getBesoins().setSante(habitant.getBesoins().getSante() - 12);
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 18);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + impact);
			habitant.getPsychologie().augmenterNevrosisme(3);
		}
	}

	/**
	 * Propage l'epidemie via les liens sociaux.
	 * Plus le lien est fort, plus la contagion est probable.
	 */
	private void propagerViaLiens(Habitant habitant) {
		List<Liens> relations = habitant.getRelation();
		if (relations == null) return;

		for (Liens lien : relations) {
			Habitant proche = lien.getPartenaire();
			if (proche.getBesoins().getSante() <= 0) continue;

			double probaContagion = lien.getForce() / 100.0 * 0.8;

			if (proche.getPsychologie().estResiliant()) {
				probaContagion *= 0.5;
			}

			if (RandomProvider.getInstance().nextDouble() < probaContagion) {
				proche.getBesoins().setSante(proche.getBesoins().getSante() - 8);
				proche.getBesoins().setFatigue(proche.getBesoins().getFatigue() - 10);
				proche.getBesoins().setMoral(proche.getBesoins().getMoral() - 10);
			}
		}
	}
}