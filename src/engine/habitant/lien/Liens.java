package engine.habitant.lien;

import engine.habitant.Habitant;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Classe abstraite représentant un lien social entre deux habitants.
 * Le contrat abstrait getTypeLien() permet d'identifier le type sans
 * utiliser instanceof — respect du principe Ouvert/Fermé (OCP).
 */
public abstract class Liens {

	protected Habitant partenaire;
	protected int force;

	public Liens(Habitant partenaire, int force) {
		this.partenaire = partenaire;
		this.setForce(force);
	}

	public Habitant getPartenaire() {
		return partenaire;
	}

	public int getForce() {
		return force;
	}

	// Clamping : la force reste toujours entre 0 et 100
	public void setForce(int force) {
		this.force = Math.max(0, Math.min(100, force));
	}

	public abstract void appliquerBonusMental(Habitant proprietaire);

	/**
	 * Fait évoluer la force du lien selon l'état des habitants.
	 * @return true = lien vivant | false = lien mort à supprimer
	 */
	public abstract boolean evoluerForce(Habitant proprietaire);

	/**
	 * Retourne le type du lien sous forme de String.
	 * Remplace l'usage de instanceof dans Statistique.
	 * Chaque sous-classe déclare son propre type — contrat OCP respecté.
	 */
	public abstract String getTypeLien();

	// Utilitaire pour le nettoyage dans Habitant
	public boolean estMort() {
		return this.force <= 0;
	}
}