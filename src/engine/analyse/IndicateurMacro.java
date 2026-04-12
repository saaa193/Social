package engine.analyse;

import engine.habitant.Habitant;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Interface Strategy pour les indicateurs macroscopiques.
 * chaque indicateur encapsule son propre algorithme de calcul.
 * Pour ajouter un nouvel indicateur : créer une classe,
 * implémenter cette interface — rien d'autre ne change.
 */
public interface IndicateurMacro {

    /**
     * Calcule la valeur de l'indicateur sur la population.
     * Retourne une valeur entre 0.0 et 1.0.
     */
    double calculer(List<Habitant> habitants);

    /**
     * Retourne le nom lisible de l'indicateur.
     * Utilisé pour l'affichage dans le dashboard.
     */
    String getNom();
}