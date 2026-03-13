package engine.habitant.visitor;

import engine.habitant.etat.EtatAnxieux;
import engine.habitant.etat.EtatEpanoui;
import engine.habitant.etat.EtatIsole;
import engine.habitant.etat.EtatStable;

/**
 * ContagionVisitor : calcule la charge émotionnelle qu'un habitant
 * propage à son entourage via ses liens sociaux.
 * Valeur positive = contagion joyeuse, négative = contagion dépressive.
 */
public class ContagionVisitor implements EtatVisitor<Integer> {

    @Override
    public Integer visit(EtatEpanoui etat) {
        // Un habitant épanoui booste le moral de ses proches
        return +5;
    }

    @Override
    public Integer visit(EtatAnxieux etat) {
        // Un anxieux propage son stress autour de lui
        return -8;
    }

    @Override
    public Integer visit(EtatIsole etat) {
        // Un isolé propage un malaise discret
        return -3;
    }

    @Override
    public Integer visit(EtatStable etat) {
        // Stable : aucune contagion
        return 0;
    }

}