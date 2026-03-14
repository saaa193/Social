package engine.evenement;

import engine.habitant.Habitant;

public interface EventVisitor {
    void visit(Habitant habitant);
}
