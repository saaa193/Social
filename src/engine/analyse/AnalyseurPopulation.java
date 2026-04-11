package engine.analyse;

import engine.habitant.Habitant;
import java.util.ArrayList;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Analyseur de population : applique tous les indicateurs
 * macroscopiques sur la liste des habitants.
 * Calqué sur le principe du prof dans Tree V2 :
 * on injecte une liste de stratégies et on les applique toutes.
 * Pour ajouter un indicateur : juste l'ajouter dans le constructeur.
 */
public class AnalyseurPopulation {

	private List<IndicateurMacro> indicateurs = new ArrayList<IndicateurMacro>();

	public AnalyseurPopulation() {
		indicateurs.add(new CohesionSociale());
		indicateurs.add(new InequaliteMoral());
		indicateurs.add(new PolarisationPopulation());
	}

	/**
	 * Calcule tous les indicateurs sur la population.
	 * Retourne une liste de résultats dans le même ordre
	 * que la liste des indicateurs.
	 */
	public List<Double> calculerTous(List<Habitant> habitants) {
		List<Double> resultats = new ArrayList<Double>();
		for (IndicateurMacro indicateur : indicateurs) {
			resultats.add(indicateur.calculer(habitants));
		}
		return resultats;
	}

	/**
	 * Retourne les noms des indicateurs pour l'affichage.
	 */
	public List<String> getNoms() {
		List<String> noms = new ArrayList<String>();
		for (IndicateurMacro indicateur : indicateurs) {
			noms.add(indicateur.getNom());
		}
		return noms;
	}

	/**
	 * Retourne la liste des indicateurs.
	 */
	public List<IndicateurMacro> getIndicateurs() {
		return indicateurs;
	}
}