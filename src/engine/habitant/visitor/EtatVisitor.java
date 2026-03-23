package engine.habitant.visitor;

import engine.habitant.etat.*;

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

    T visit(EtatDepressif etat);

    T visit(EtatBurnout etat);

    T visit(EtatEuphorique etat);

}