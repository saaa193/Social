package engine.evenement;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Événement météo : affecte la fatigue, le moral et les traits OCEAN.
 */
public class EventMeteo implements EventVisitor {

	private boolean mauvaisTemps;

	public EventMeteo(boolean mauvaisTemps) {
		this.mauvaisTemps = mauvaisTemps;
	}

	@Override
	public void visit(Habitant habitant) {
		if (mauvaisTemps) {
			// Effet immédiat sur les besoins
			habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 2);
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 3);
			// Effet durable sur OCEAN — le névrosisme augmente
			habitant.getPsychologie().augmenterNevrosisme(3);
		} else {
			// Beau temps → moral remonte
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 2);
			// Effet durable — l'ouverture augmente
			habitant.getPsychologie().augmenterOuverture(2);
		}
	}
}