package engine.evenement;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Événement personnel : remonte le moral et modifie les traits OCEAN.
 */
public class EventPerso implements EventVisitor {

	@Override
	public void visit(Habitant habitant) {
		// Effet immédiat sur les besoins
		habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 10);
		habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 5);
		// Effet durable sur OCEAN
		// Une bonne nouvelle renforce la conscience et l'ouverture
		habitant.getPsychologie().augmenterOuverture(2);
		habitant.getPsychologie().augmenterAgreabilite(1);
		// Le névrosisme baisse — on se sent mieux dans sa peau
		habitant.getPsychologie().diminuerNevrosisme(2);
	}
}