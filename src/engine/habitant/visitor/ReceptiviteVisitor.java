package engine.habitant.visitor;

import engine.habitant.etat.*;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * ReceptiviteVisitor : module le facteur d'écoute d'un habitant
 * selon son état psychologique courant.
 *
 * Pattern Visitor (GoF) — 4e visiteur du projet.
 * Utilisé dans InformationTransmission pour la formule SIR adaptée.
 *
 * Justification génie logiciel :
 * Le comportement varie selon le TYPE d'état (polymorphisme),
 * sans modifier les classes d'état elles-mêmes (Principe Ouvert/Fermé).
 * Chaque nouvel usage des états (contagion, couleur, diagnostic, réceptivité)
 * s'ajoute comme un nouveau Visitor indépendant.
 */
public class ReceptiviteVisitor implements EtatVisitor<Double> {

    /**
     * Épanoui → réceptivité normale : ouvert au monde, écoute naturelle.
     */
    @Override
    public Double visit(EtatEpanoui etat) {
        return 1.0;
    }

    /**
     * Stable → légèrement filtré : raisonnable, ne se laisse pas
     * emporter par n'importe quelle information.
     */
    @Override
    public Double visit(EtatStable etat) {
        return 0.9;
    }

    /**
     * Euphorique → moins réceptif : trop absorbé par sa propre énergie,
     * moins attentif aux messages extérieurs.
     */
    @Override
    public Double visit(EtatEuphorique etat) {
        return 0.7;
    }

    /**
     * Anxieux → très réceptif (x1.4) : cherche des informations pour
     * se rassurer ou anticiper le danger — phénomène bien documenté
     * en psychologie sociale (confirmation bias sous stress).
     */
    @Override
    public Double visit(EtatAnxieux etat) {
        return 1.4;
    }

    /**
     * Dépressif → sous-réceptif : apathie, perte d'intérêt général,
     * les informations extérieures n'atteignent plus.
     */
    @Override
    public Double visit(EtatDepressif etat) {
        return 0.5;
    }

    /**
     * Burnout → très faible réceptivité : épuisement total,
     * l'habitant n'a plus les ressources cognitives pour traiter
     * de nouvelles informations.
     */
    @Override
    public Double visit(EtatBurnout etat) {
        return 0.3;
    }

    /**
     * Isolé → réceptivité accrue (x1.2) : avide de contact social,
     * toute information reçue prend une valeur amplifiée.
     */
    @Override
    public Double visit(EtatIsole etat) {
        return 1.2;
    }

}
