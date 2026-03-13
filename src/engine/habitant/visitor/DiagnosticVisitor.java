package engine.habitant.visitor;

import engine.habitant.etat.EtatAnxieux;
import engine.habitant.etat.EtatEpanoui;
import engine.habitant.etat.EtatIsole;
import engine.habitant.etat.EtatStable;

/**
 * DiagnosticVisitor : retourne le label d'état affiché dans
 * la fenêtre "Inspection Citoyen".
 */
public class DiagnosticVisitor implements EtatVisitor<String> {

    @Override
    public String visit(EtatEpanoui etat) {
        return "ÉPANOUI";
    }

    @Override
    public String visit(EtatAnxieux etat) {
        return "ANXIEUX";
    }

    @Override
    public String visit(EtatIsole etat) {
        return "DÉTRESSE";
    }

    @Override
    public String visit(EtatStable etat) {
        return "STABLE";
    }

}