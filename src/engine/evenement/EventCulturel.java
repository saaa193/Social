package engine.evenement;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Semaine Culturelle : effect lent sur l'ouverture d'esprit.
 * Seuls les habitants ouverts en profitent vraiment.
 * Les fermés d'esprit sont indifférents voire agacés.
 */
public class EventCulturel implements EvenementSimulation, EventVisitor {

	@Override
	public boolean estConcerne(Habitant h) {
		return h.getOuverture() > 40;
	}

	@Override
	public void appliquer(Habitant h) {
		h.acceptEvent(this);
	}

	@Override
	public void visit(Habitant habitant) {
		if (habitant.getOuverture() > 65) {
			// Très ouverts — profitent pleinement
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 20);
			habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 15);
			habitant.getPsychologie().augmenterOuverture(5);
			habitant.getPsychologie().diminuerNevrosisme(4);
		} else {
			// Moyennement ouverts — bénéfice modéré
			habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 10);
			habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 8);
			habitant.getPsychologie().augmenterOuverture(2);
		}
	}
}