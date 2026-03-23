package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * État Burnout : épuisement professionnel.
 * Touche les habitants très consciencieux qui travaillent trop.
 * Condition : fatigue < 10 ET conscience > 80.
 * Ancré dans le Big Five : la conscience élevée pousse à l'excès.
 */
public class EtatBurnout implements EtatHabitant {

    @Override
    public void appliquer(Habitant habitant) {
        // Épuisement total — fatigue chute rapidement
        habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 3);
        // Le moral s'effondre
        habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 2);
        // La conscience diminue — l'habitant perd sa rigueur
        habitant.getPsychologie().diminuerConscience(2);
    }

    public <T> T accept(EtatVisitor<T> visitor) {
        return visitor.visit(this);
    }
}