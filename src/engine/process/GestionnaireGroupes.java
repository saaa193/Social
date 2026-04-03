package engine.process;

import engine.habitant.Groupe;
import engine.habitant.Habitant;
import engine.habitant.lien.Liens;

import java.util.ArrayList;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 * <p>
 * GestionnaireGroupes : Détecte et gère les groupes sociaux
 * qui se forment naturellement dans la population.
 * Un groupe existe quand 3 habitants ou plus se connaissent entre eux.
 * Appelé à chaque tour depuis MobileElementManager.
 */
public class GestionnaireGroupes {

	private List<Groupe> groupes = new ArrayList<Groupe>();

	/**
	 * Reconstruit tous les groupes à partir du réseau social actuel.
	 * Un groupe = au moins 3 habitants connectés entre eux.
	 * Appelée à chaque tour.
	 */
	public void actualiserGroupes(List<Habitant> habitants) {
		groupes.clear();

		for (Habitant h : habitants) {
			if (h.getBesoins().getSante() <= 0) continue;

			// Maximum 4 groupes sur la carte
			if (groupes.size() >= 4) break;

			// Seuils très stricts — seulement les profils très sociaux
			if (h.getAgreabilite() < 80) continue;
			if (h.getExtraversion() < 75) continue;

			List<Habitant> voisins = getVoisins(h);

			// Minimum 5 voisins compatibles
			if (voisins.size() >= 5) {
				Groupe groupe = new Groupe();
				groupe.ajouterMembre(h);

				for (Habitant voisin : voisins) {
					// Voisin doit être très agréable aussi
					if (voisin.getAgreabilite() > 75) {
						groupe.ajouterMembre(voisin);
					}
				}

				if (groupe.getTaille() >= 5 && !groupeExisteDeja(groupe)) {
					groupes.add(groupe);
				}
			}
		}
	}

	/**
	 * Retourne la liste des habitants directement connectés à h.
	 */
	private List<Habitant> getVoisins(Habitant h) {
		List<Habitant> voisins = new ArrayList<Habitant>();
		for (Liens l : h.getRelation()) {
			voisins.add(l.getPartenaire());
		}
		return voisins;
	}

	/**
	 * Vérifie qu'un groupe avec les mêmes membres n'existe pas déjà.
	 * Evite les doublons dans la liste des groupes.
	 */
	private boolean groupeExisteDeja(Groupe nouveauGroupe) {
		for (Groupe g : groupes) {
			if (g.getMembres().containsAll(nouveauGroupe.getMembres())
					&& nouveauGroupe.getMembres().containsAll(g.getMembres())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Applique l'influence du leader sur chaque groupe.
	 * Appelée à chaque tour après actualiserGroupes().
	 */
	public void appliquerInfluences() {
		for (Groupe g : groupes) {
			g.appliquerInfluenceLeader();
		}
	}

	// ACCESSEURS
	public List<Groupe> getGroupes() {
		return groupes;
	}

	/**
	 * Retourne le nombre total de groupes actifs.
	 */
	public int getNombreGroupes() {
		return groupes.size();
	}
}