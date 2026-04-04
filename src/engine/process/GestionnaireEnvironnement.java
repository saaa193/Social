package engine.process;

import engine.habitant.Habitant;
import engine.map.Horloge;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * GestionnaireEnvironnement : applique les effets psychologiques
 * de l'environnement sur la population.
 * Gère la météo, le weekend et les phases de la journée.
 * Respecte le principe de responsabilité unique —
 * tout ce qui est "extérieur" à l'habitant est géré ici.
 */
public class GestionnaireEnvironnement {

	private boolean mauvaisTemps = false;
	private int heureAuChangementMeteo = 0;

	/**
	 * Applique tous les effets environnementaux du tour en cours.
	 * Appelée depuis MobileElementManager.nextRound().
	 *
	 * @param habitants    la liste des habitants vivants
	 * @param estLeWeekend vrai si on est en weekend
	 * @param estLaNuit    vrai si on est la nuit
	 * @param horloge      l'horloge de la simulation
	 * @param heure        l'heure actuelle (0-23)
	 */
	public void appliquerEffets(List<Habitant> habitants, boolean estLeWeekend,
	                            boolean estLaNuit, Horloge horloge, int heure) {
		if (estLeWeekend && !estLaNuit) {
			appliquerEffetsWeekend(habitants);
		}

		if (!estLaNuit) {
			appliquerEffetsPhase(habitants, heure);
		}

		appliquerEffetsMeteo(habitants, horloge);
	}

	/**
	 * Applique les effets selon la phase de la journée.
	 * Matin : chacun vaque à ses occupations.
	 * Midi : pause déjeuner, besoin social augmenté.
	 * Après-midi : légère fatigue naturelle.
	 * Soir : extravertis sortent, introvertis récupèrent.
	 *
	 * Approche macroscopique — on ajuste des taux biologiques
	 * sans introduire de classes Infrastructure concrètes.
	 *
	 * @param habitants la liste des habitants vivants
	 * @param heure     l'heure actuelle (0-23)
	 */
	private void appliquerEffetsPhase(List<Habitant> habitants, int heure) {
		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() <= 0) continue;

			if (heure >= 7 && heure < 12) {
				// MATIN : chacun vaque à ses occupations
				// Les consciencieux sont en forme et productifs
				if (h.getConscience() > 60) {
					h.getBesoins().setFatigue(h.getBesoins().getFatigue() + 1);
				}

			} else if (heure >= 12 && heure < 14) {
				// MIDI : pause déjeuner
				// Tout le monde cherche du contact social
				h.getBesoins().setSocial(h.getBesoins().getSocial() + 2);
				h.getBesoins().setFaim(h.getBesoins().getFaim() + 3);

			} else if (heure >= 14 && heure < 18) {
				// APRÈS-MIDI : fatigue naturelle qui monte
				if (Math.random() < 0.3) {
					h.getBesoins().setFatigue(h.getBesoins().getFatigue() - 1);
				}

			} else if (heure >= 18 && heure < 23) {
				// SOIR : comportement selon extraversion
				if (h.getExtraversion() > 60) {
					// Extraverti → sort, cherche du social
					h.getBesoins().setSocial(h.getBesoins().getSocial() + 3);
					h.getBesoins().setMoral(h.getBesoins().getMoral() + 1);
				} else {
					// Introverti → rentre chez lui, récupère
					h.getBesoins().setFatigue(h.getBesoins().getFatigue() + 2);
					h.getBesoins().setMoral(h.getBesoins().getMoral() + 1);
				}
			}
		}
	}

	/**
	 * Applique les effets psychologiques du weekend.
	 */
	private void appliquerEffetsWeekend(List<Habitant> habitants) {
		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() > 0) {
				if (h.getExtraversion() > 60) {
					h.getBesoins().setSocial(h.getBesoins().getSocial() + 2);
					h.getBesoins().setMoral(h.getBesoins().getMoral() + 1);
				}
				if (h.getExtraversion() < 35) {
					h.getBesoins().setFatigue(h.getBesoins().getFatigue() + 2);
					h.getBesoins().setMoral(h.getBesoins().getMoral() + 1);
				}
				if (h.getNevrosisme() > 65) {
					h.getBesoins().setMoral(h.getBesoins().getMoral() - 1);
				}
			}
		}
	}

	/**
	 * Gère le changement de météo à 8h et applique ses effets OCEAN.
	 */
	private void appliquerEffetsMeteo(List<Habitant> habitants, Horloge horloge) {
		if (horloge.getHeureObject().getHeures() == 8 && heureAuChangementMeteo != 8) {
			mauvaisTemps = Math.random() < 0.35;
			heureAuChangementMeteo = 8;

			for (Habitant h : habitants) {
				if (h.getBesoins().getSante() > 0) {
					if (mauvaisTemps) {
						if (h.getNevrosisme() > 60) {
							h.getBesoins().setMoral(h.getBesoins().getMoral() - 3);
							h.getBesoins().setFatigue(h.getBesoins().getFatigue() - 2);
						}
					} else {
						if (h.getOuverture() > 60) {
							h.getBesoins().setMoral(h.getBesoins().getMoral() + 3);
							h.getBesoins().setSocial(h.getBesoins().getSocial() + 2);
						}
					}
				}
			}
		} else if (horloge.getHeureObject().getHeures() != 8) {
			heureAuChangementMeteo = 0;
		}
	}

	/**
	 * Retourne vrai si la météo actuelle est mauvaise.
	 */
	public boolean isMauvaisTemps() {
		return mauvaisTemps;
	}
}