package engine.habitant.visitor;

import engine.habitant.etat.EtatAnxieux;
import engine.habitant.etat.EtatEpanoui;
import engine.habitant.etat.EtatIsole;
import engine.habitant.etat.EtatStable;

/**
 * Interface Visitor pour les états psychologiques.
 * Miroir exact de TreeVisitor<T> du prof :
 * une méthode visit() par type concret.
 */
public interface EtatVisitor<T> {

    T visit(EtatEpanoui etat);

    T visit(EtatAnxieux etat);

    T visit(EtatIsole etat);

    T visit(EtatStable etat);

}