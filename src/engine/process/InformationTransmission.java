package engine.process;

import config.RandomProvider;
import engine.habitant.Habitant;
import engine.habitant.etat.EtatHabitant;
import engine.habitant.lien.Liens;
import engine.habitant.visitor.ContagionVisitor;
import engine.habitant.visitor.ReceptiviteVisitor;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * InformationTransmission : propage une information dans le réseau social.
 * Modèle SIR adapté — formule : virulence × force_lien × réceptivité × état.
 */
public class InformationTransmission {

	private String theme;
	private float virulence;
	private float veracite;

	private final ContagionVisitor contagionVisitor = new ContagionVisitor();
	private final ReceptiviteVisitor receptiviteVisitor = new ReceptiviteVisitor();

	public InformationTransmission(String theme, float virulence, float veracite) {
		this.theme = theme;
		this.virulence = virulence;
		this.veracite = veracite;
	}

	/**
	 * Propage l'information sur toute la population.
	 * Seuls les habitants déjà informés peuvent propager.
	 */
	public void propagerDansReseau(List<Habitant> habitants) {
		for (Habitant porteur : habitants) {
			if (!porteur.estInforme()) continue;
			if (porteur.getBesoins().getSante() <= 0) continue;

			for (Liens lien : porteur.getRelation()) {
				Habitant cible = lien.getPartenaire();
				if (cible.getBesoins().getSante() <= 0) continue;
				if (cible.estInforme()) continue;
				tenterTransmission(porteur, cible, lien.getForce());
			}
		}
	}

	/**
	 * Tente de transmettre l'information du porteur vers la cible.
	 *
	 * @param porteur   habitant qui diffuse l'information
	 * @param cible     habitant qui reçoit l'information
	 * @param forceLien force du lien social entre eux (0-100)
	 */
	private void tenterTransmission(Habitant porteur, Habitant cible, int forceLien) {
		if (cible.estInforme()) return;

		double probabilite = calculerProbabiliteTransmission(porteur, cible, forceLien);

		if (RandomProvider.getInstance().nextDouble() < probabilite) {
			cible.recevoirInformation();

			EtatHabitant etatPorteur = porteur.getPsychologie().determinerEtat(porteur.getBesoins());
			int impact = etatPorteur.accept(contagionVisitor);

			if (impact < 0) {
				double amplification = 1.0 + (cible.getNevrosisme() / 100.0);
				impact = (int) (impact * amplification);
			}

			cible.getBesoins().setMoral(cible.getBesoins().getMoral() + impact);

			if (veracite > 0.6f) {
				cible.getBesoins().setMoral(cible.getBesoins().getMoral() + 15);
				cible.getPsychologie().augmenterOuverture(1);
			} else if (veracite < 0.4f) {
				cible.getBesoins().setMoral(cible.getBesoins().getMoral() - 15);
				cible.getPsychologie().augmenterNevrosisme(2);
				cible.getPsychologie().diminuerAgreabilite(1);
			}
		}
	}

	/**
	 * Calcule la probabilité de transmission selon le modèle SIR adapté.
	 *
	 * @param porteur   l'habitant émetteur
	 * @param cible     l'habitant récepteur
	 * @param forceLien la force du lien social (0-100)
	 * @return probabilité entre 0.0 et 1.0
	 */
	private double calculerProbabiliteTransmission(Habitant porteur, Habitant cible, int forceLien) {
		double facteurVirulence = virulence;
		double facteurLien = forceLien / 100.0;

		double receptivite;
		if (veracite > 0.6f) {
			receptivite = 0.3
					+ 0.4 * (cible.getOuverture() / 100.0)
					+ 0.3 * (cible.getConscience() / 100.0);
		} else if (veracite < 0.4f) {
			receptivite = 0.3
					+ 0.5 * (cible.getNevrosisme() / 100.0)
					- 0.3 * (cible.getConscience() / 100.0);
		} else {
			receptivite = 0.3
					+ 0.3 * (cible.getExtraversion() / 100.0)
					+ 0.2 * (cible.getAgreabilite() / 100.0);
		}
		receptivite = Math.max(0.05, Math.min(0.95, receptivite));

		EtatHabitant etatCible = cible.getPsychologie().determinerEtat(cible.getBesoins());
		double facteurEtat = etatCible.accept(receptiviteVisitor);

		return facteurVirulence * facteurLien * receptivite * facteurEtat;
	}

	public String getTheme() { return theme; }
	public float getVirulence() { return virulence; }
	public float getVeracite() { return veracite; }
}