package engine.process;

import config.RandomProvider;
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
 */
public class GestionnaireEnvironnement {

	private boolean mauvaisTemps = false;
	private int heureAuChangementMeteo = 0;

	/**
	 * Applique tous les effets environnementaux du tour en cours.
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

	private void appliquerEffetsPhase(List<Habitant> habitants, int heure) {
		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() <= 0) continue;

			if (heure >= 7 && heure < 12) {
				// MATIN
				if (h.getConscience() > 60) {
					h.getBesoins().setFatigue(h.getBesoins().getFatigue() + 1);
				}
			} else if (heure >= 12 && heure < 14) {
				// MIDI
				h.getBesoins().setSocial(h.getBesoins().getSocial() + 1);
				h.getBesoins().setFaim(h.getBesoins().getFaim() + 2);
			} else if (heure >= 14 && heure < 18) {
				// APRES-MIDI
				if (RandomProvider.getInstance().nextDouble() < 0.3) {
					h.getBesoins().setFatigue(h.getBesoins().getFatigue() - 1);
				}
			} else if (heure >= 18 && heure < 23) {
				// SOIR
				if (h.getExtraversion() > 60) {
					h.getBesoins().setSocial(h.getBesoins().getSocial() + 1);
				} else {
					h.getBesoins().setFatigue(h.getBesoins().getFatigue() + 2);
				}
			}
		}
	}

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

	private void appliquerEffetsMeteo(List<Habitant> habitants, Horloge horloge) {
		if (horloge.getHeureObject().getHeures() == 8 && heureAuChangementMeteo != 8) {
			mauvaisTemps = RandomProvider.getInstance().nextDouble() < 0.35;
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