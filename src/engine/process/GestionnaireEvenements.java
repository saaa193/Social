package engine.process;

import engine.evenement.EventMeteo;
import engine.evenement.EventPerso;
import engine.evenement.EventSocial;
import engine.habitant.Habitant;

import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 * <p>
 * Classe utilitaire qui déclenche les événements sur la population.
 * Seuls les habitants concernés par le profil OCEAN sont affectés.
 */
public class GestionnaireEvenements {

	/**
	 * Déclenche un événement sur les habitants concernés selon leur profil OCEAN.
	 *
	 * @param nomEvenement le nom de l'événement sélectionné dans le combo
	 * @param habitants    la liste complète des habitants
	 */
	public static void declencherEvenement(String nomEvenement, List<Habitant> habitants) {
		for (Habitant h : habitants) {
			if (nomEvenement.equals("Alerte Météo")) {
				// Les névrosés sont plus sensibles à la mauvaise météo
				if (h.getNevrosisme() > 50) {
					h.acceptEvent(new EventMeteo(true));
				}
			} else if (nomEvenement.equals("Fête de Quartier")) {
				// Les extravertis profitent des événements sociaux
				if (h.getExtraversion() > 40) {
					h.acceptEvent(new EventSocial());
				}
			} else if (nomEvenement.equals("Offres d'Emploi")) {
				// Les consciencieux sont motivés par les opportunités
				if (h.getConscience() > 50) {
					h.acceptEvent(new EventPerso());
				}
			} else if (nomEvenement.equals("Expo Musée")) {
				// Les habitants ouverts d'esprit apprécient la culture
				if (h.getOuverture() > 50) {
					h.acceptEvent(new EventPerso());
				}
			}
		}
	}

}