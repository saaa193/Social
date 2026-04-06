package engine.habitant.biais;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Interface Strategy pour les biais cognitifs des habitants.
 * Calquee sur StrategieNutrition du prof :
 * chaque biais encapsule son propre algorithme de filtrage.
 */
public interface BiaisCognitif {

	/**
	 * Filtre l'impact psychologique d'une information
	 * selon le biais cognitif de l'habitant.
	 *
	 * @param impact   l'impact brut calcule avant filtrage
	 * @param veracite la veracite de l'information (0.0 a 1.0)
	 * @return l'impact module par le biais cognitif
	 */
	int filtrerImpact(int impact, float veracite);
}