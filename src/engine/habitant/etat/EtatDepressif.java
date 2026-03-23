package engine.habitant.etat;

import engine.habitant.Habitant;
import engine.habitant.visitor.EtatVisitor;

/**
 * État Dépressif : moral très bas depuis plusieurs tours.
 * L'habitant perd du moral, du social ET de la fatigue.
 * Plus grave que l'état Anxieux — nécessite une intervention.
 * Ancré dans le Big Five : névrosisme élevé + isolement prolongé.
 */
public class EtatDepressif implements EtatHabitant {

    @Override
    public void appliquer(Habitant habitant) {
        // Perte de moral accélérée
        habitant.getBesoins().setMoral(habitant.getBesoins().getMoral() - 2);
        // Isolement social croissant
        habitant.getBesoins().setSocial(habitant.getBesoins().getSocial() - 2);
        // Épuisement physique lié à la dépression
        habitant.getBesoins().setFatigue(habitant.getBesoins().getFatigue() - 1);
    }

    public <T> T accept(EtatVisitor<T> visitor) {
        return visitor.visit(this);
    }
}