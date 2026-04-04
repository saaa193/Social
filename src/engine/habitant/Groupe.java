package engine.habitant;

import java.util.ArrayList;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 * <p>
 * Groupe : Représente un groupe social d'habitants.
 * Un groupe se forme naturellement quand plusieurs habitants
 * sont connectés entre eux via des liens sociaux.
 * Le leader est l'habitant avec l'extraversion + agréabilité les plus élevées.
 */
public class Groupe {

	private List<Habitant> membres = new ArrayList<Habitant>();
	private Habitant leader;

	public Groupe() {
	}

	/**
	 * Ajoute un membre au groupe et recalcule le leader.
	 */
	public void ajouterMembre(Habitant habitant) {
		if (!membres.contains(habitant)) {
			membres.add(habitant);
			actualiserLeader();
		}
	}

	/**
	 * Détermine le leader du groupe.
	 * Le leader est celui qui a le score extraversion + agréabilité le plus élevé.
	 * Ancré dans le Big Five : l'extraversion = influence sociale naturelle.
	 */
	private void actualiserLeader() {
		if (membres.isEmpty()) return;

		Habitant nouveauLeader = membres.get(0);
		int scoreMax = 0;

		for (Habitant h : membres) {
			int score = h.getExtraversion() + h.getAgreabilite();
			if (score > scoreMax) {
				scoreMax = score;
				nouveauLeader = h;
			}
		}

		this.leader = nouveauLeader;
	}

	/**
	 * Le leader influence les traits OCEAN des membres selon la force d'influence.
	 * Appelee a chaque tour depuis GestionnaireGroupes.
	 *
	 * @param forceInfluence la force d'influence du leader (0 a 10)
	 */
	public void appliquerInfluenceLeader(int forceInfluence) {
		if (leader == null || membres.size() < 2) return;

		for (Habitant membre : membres) {
			if (membre == leader) continue;

			// Influence psychologique OCEAN — modulee par le slider
			double probabilite = forceInfluence / 10.0 * 0.20;
			if (Math.random() < probabilite) {
				if (leader.getExtraversion() > 70) {
					membre.getPsychologie().augmenterExtraversion(1);
				}
				if (leader.getAgreabilite() > 70) {
					membre.getPsychologie().augmenterAgreabilite(1);
				}
				if (leader.getNevrosisme() > 70) {
					membre.getPsychologie().augmenterNevrosisme(1);
				}
			}

			// Deplacement autour du leader — inchange
			int ligne = leader.getPosition().getLine();
			int colonne = leader.getPosition().getColumn();

			int direction = (int) (Math.random() * 4);

			if (direction == 0 && !membre.getMap().isOnTop(leader.getPosition())) {
				ligne--;
			} else if (direction == 1 && !membre.getMap().isOnBottom(leader.getPosition())) {
				ligne++;
			} else if (direction == 2 && !membre.getMap().isOnLeftBorder(leader.getPosition())) {
				colonne--;
			} else if (direction == 3 && !membre.getMap().isOnRightBorder(leader.getPosition())) {
				colonne++;
			}

			membre.setPosition(membre.getMap().getBlock(ligne, colonne));
		}
	}

	public List<Habitant> getMembres() {
		return membres;
	}

	public Habitant getLeader() {
		return leader;
	}

	public int getTaille() {
		return membres.size();
	}
}