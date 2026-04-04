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
 * de l'environnement sur la population (meteo et weekend).
 * Extrait de MobileElementManager pour respecter le principe
 * de responsabilite unique — calque sur BlockManager du prof.
 */
public class GestionnaireEnvironnement {

	private boolean mauvaisTemps = false;
	private int heureAuChangementMeteo = 0;

	/**
	 * Applique les effets environnementaux du tour en cours.
	 * Appelee depuis MobileElementManager.nextRound().
	 *
	 * @param habitants    la liste des habitants vivants
	 * @param estLeWeekend vrai si on est en weekend
	 * @param estLaNuit    vrai si on est la nuit
	 * @param horloge      l'horloge de la simulation
	 */
	public void appliquerEffets(List<Habitant> habitants, boolean estLeWeekend, boolean estLaNuit, Horloge horloge) {
		if (estLeWeekend && !estLaNuit) {
			appliquerEffetsWeekend(habitants);
		}
		appliquerEffetsMeteo(habitants, horloge);
	}

	/**
	 * Applique les effets psychologiques du weekend.
	 * Les extravertis profitent, les introvertis se reposent,
	 * les nevrosiques souffrent davantage.
	 *
	 * @param habitants la liste des habitants vivants
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
	 * Gere le changement de meteo a 8h et applique ses effets OCEAN.
	 * Mauvais temps : impact negatif sur les habitants nevrosiques.
	 * Beau temps : impact positif sur les habitants ouverts.
	 *
	 * @param habitants la liste des habitants vivants
	 * @param horloge   l'horloge de la simulation
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
	 * Retourne vrai si la meteo actuelle est mauvaise.
	 * Utilise par ControlDashboard pour afficher l'icone meteo.
	 */
	public boolean isMauvaisTemps() {
		return mauvaisTemps;
	}
}