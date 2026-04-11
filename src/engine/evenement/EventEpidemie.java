package engine.evenement;

import config.RandomProvider;
import engine.habitant.Habitant;
import engine.habitant.lien.Liens;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Épidémie : se propage de proche en proche via le réseau social.
 * Contrairement aux autres événements qui frappent tout le monde
 * en même temps, l'épidémie utilise les liens sociaux pour se
 * propager — exactement comme une vraie maladie.
 * Visible sur la carte : vague de rouge qui progresse dans le réseau.
 */
public class EventEpidemie implements EvenementSimulation, EventVisitor {

	@Override
	public boolean estConcerne(Habitant h) {
		return h.getPsychologie().estVulnerable()
				|| h.getNevrosisme() > 60;
	}

	@Override
	public void appliquer(Habitant h) {
		h.acceptEvent(this);
		propagerViaLiens(h);
	}

	@Override
	public void visit(Habitant habitant) {
		habitant.getBesoins().setSante(habitant.getBesoins().getSante() - 20);
		habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 25);
		habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 15);
		habitant.getPsychologie().augmenterNevrosisme(5);
	}

	/**
	 * Propage l'épidémie via le réseau de liens sociaux.
	 * Plus le lien est fort, plus la contagion est probable.
	 * Les résilients résistent à la propagation.
	 */
	private void propagerViaLiens(Habitant habitant) {
		List<Liens> relations = habitant.getRelation();
		if (relations == null) return;

		for (Liens lien : relations) {
			Habitant proche = lien.getPartenaire();
			if (proche.getBesoins().getSante() <= 0) continue;

			double probaContagion = lien.getForce() / 100.0 * 0.6;

			if (proche.getPsychologie().estResiliant()) {
				probaContagion *= 0.5;
			}

			if (RandomProvider.getInstance().nextDouble() < probaContagion) {
				proche.getBesoins().setSante(proche.getBesoins().getSante() - 10);
				proche.getBesoins().setFatigue(proche.getBesoins().getFatigue() - 15);
				proche.getBesoins().setMoral(proche.getBesoins().getMoral() - 10);
			}
		}
	}
}