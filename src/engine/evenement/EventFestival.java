package engine.evenement;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Festival de Quartier : joie collective mais avec contrepartie.
 * Les extravertis adorent mais s'épuisent.
 * Les introvertis subissent la foule.
 * Effet visible : moral monte puis fatigue crée une descente le lendemain.
 */
public class EventFestival implements EvenementSimulation, EventVisitor {

    @Override
    public boolean estConcerne(Habitant h) {
        return true;
    }

    @Override
    public void appliquer(Habitant h) {
        h.acceptEvent(this);
    }

    @Override
    public void visit(Habitant habitant) {
        if (habitant.getExtraversion() > 60) {
            // Extravertis — profitent pleinement mais s'épuisent
            habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 25);
            habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() + 20);
            // Contrepartie : épuisement important
            habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 25);
            habitant.getPsychologie().augmenterExtraversion(3);
        } else if (habitant.getExtraversion() < 35) {
            // Introvertis — subissent la foule
            habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 10);
            habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 20);
            habitant.getPsychologie().augmenterNevrosisme(3);
        } else {
            // Profil neutre — léger bénéfice
            habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() + 10);
            habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 10);
        }
    }
}