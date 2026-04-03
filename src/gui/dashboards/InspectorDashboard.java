package gui.dashboards;

import javax.swing.*;
import java.awt.*;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 * <p>
 * InspectorDashboard : Le tableau de bord dynamique.
 * Il permet deux modes de visualisation :
 * 1. Micro-analyse : Inspection détaillée d'un habitant cliqué.
 * 2. Macro-analyse : Statistiques moyennes de toute la population.
 */
public class InspectorDashboard extends JPanel {

	// Composants d'information textuelle
	private JLabel nomLabel = new JLabel("Nom : -");
	private JLabel sexeLabel = new JLabel("Sexe : -");
	private JLabel ageLabel = new JLabel("Age : -");

	// Barres de progression (Jauges) pour visualiser les besoins
	private JProgressBar faimBar = new JProgressBar(0, 100);
	private JProgressBar fatigueBar = new JProgressBar(0, 100);
	private JProgressBar socialBar = new JProgressBar(0, 100);
	private JProgressBar santeBar = new JProgressBar(0, 100);
	private JProgressBar moralBar = new JProgressBar(0, 100);


	/**
	 * Méthode utilitaire : Configure le style des jauges uniformément.
	 */
	private void setupBar(String title, JProgressBar bar, Color color) {
		this.add(new JLabel(title));
		bar.setStringPainted(true); // Affiche le pourcentage textuel
		bar.setForeground(color);
		this.add(bar);
	}

	/**
	 * Met à jour les infos textuelles de l'habitant sélectionné.
	 */
	public void setInfos(String nom, String sexe, String age) {
		nomLabel.setText("  Nom : " + nom);
		sexeLabel.setText("  Sexe : " + sexe);
		ageLabel.setText("  Age : " + age + " ans");
	}

	/**
	 * Met à jour les valeurs des jauges pour l'affichage.
	 */
	public void setJauges(int faim, int fatigue, int social, int sante, int moral) {
		faimBar.setValue(faim);
		fatigueBar.setValue(fatigue);
		socialBar.setValue(social);
		santeBar.setValue(sante);
		moralBar.setValue(moral);
	}

	/**
	 * NOUVEAUTÉ : Logique de Macro-Analyse.
	 * Calcule la moyenne de tous les besoins des habitants de la ville.
	 * Cela offre une vue d'ensemble précieuse pour l'utilisateur.
	 */
	public void updateAverages(java.util.List<engine.habitant.Habitant> habitants) {
		if (habitants == null || habitants.isEmpty()) return;

		int totalFaim = 0, totalFatigue = 0, totalSocial = 0, totalSante = 0, totalMoral = 0;
		int totalAge = 0;

		// Calcul de la somme totale
		for (engine.habitant.Habitant h : habitants) {
			totalAge += h.getAge();
			totalFaim += h.getBesoins().getFaim();
			totalFatigue += h.getBesoins().getFatigue();
			totalSocial += h.getBesoins().getSocial();
			totalSante += h.getBesoins().getSante();
			totalMoral += h.getBesoins().getMoral();
		}

		int taille = habitants.size();

		// Affichage du résultat moyen
		setInfos("Moyenne de la ville", "Mixte", "" + (totalAge / taille));

		// Mise à jour visuelle des jauges avec la moyenne
		setJauges(
				totalFaim / taille,
				totalFatigue / taille,
				totalSocial / taille,
				totalSante / taille,
				totalMoral / taille
		);
	}
}