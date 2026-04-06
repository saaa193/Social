package engine.habitant.visitor;

import engine.habitant.etat.*;
import java.awt.Color;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Visitor de couleur — 3ème usage du pattern Visitor.
 * Calqué sur TreeVisitor<T> du prof : générique, une méthode
 * visit() par état concret. Retourne la couleur associée
 * à chaque état psychologique pour le rendu graphique.
 *
 * Avant : couleur basée sur des jauges brutes (moral, fatigue)
 * Après : couleur basée sur l'état psychologique réel — cohérent
 * avec le pattern État déjà en place.
 */
public class CouleurVisitor implements EtatVisitor<Color> {

    @Override
    public Color visit(EtatEpanoui etat) {
        return new Color(128, 0, 128);   // Violet — épanouissement
    }

    @Override
    public Color visit(EtatEuphorique etat) {
        return new Color(200, 0, 200);   // Violet vif — euphorie
    }

    @Override
    public Color visit(EtatStable etat) {
        return Color.ORANGE;             // Orange — stabilité
    }

    @Override
    public Color visit(EtatAnxieux etat) {
        return new Color(255, 140, 0);   // Orange foncé — anxiété
    }

    @Override
    public Color visit(EtatIsole etat) {
        return new Color(100, 100, 150); // Bleu-gris — isolement
    }

    @Override
    public Color visit(EtatDepressif etat) {
        return Color.RED;                // Rouge — dépression
    }

    @Override
    public Color visit(EtatBurnout etat) {
        return new Color(80, 0, 0);      // Rouge sombre — burnout
    }
}