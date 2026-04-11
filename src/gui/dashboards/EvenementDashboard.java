package gui.dashboards;

import config.GameConfiguration;
import config.RandomProvider;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * EvenementDashboard : affiche l'evenement psychologique en cours.
 * Etat calme : icone grise + "Aucun evenement".
 * Etat actif : nom de l'evenement + habitants affectes + impact OCEAN.
 */
public class EvenementDashboard extends JPanel {

	private static final long serialVersionUID = 1L;

	private int minutesRestantes = 0;

	private JLabel lblIcone = new JLabel("🗓", JLabel.CENTER);
	private JLabel lblTitre = new JLabel("AUCUN EVENEMENT", JLabel.CENTER);
	private JLabel lblSousTitre = new JLabel("La ville est calme pour le moment.", JLabel.CENTER);
	private JLabel lblImpact = new JLabel(" ", JLabel.CENTER);

	public EvenementDashboard() {
		setLayout(new BorderLayout(0, 4));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(GameConfiguration.MENU_RIGHT_WIDTH, 110));
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(220, 220, 220)),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)
		));

		lblIcone.setFont(new Font("Arial", Font.PLAIN, 20));
		lblIcone.setForeground(new Color(180, 180, 180));

		lblTitre.setFont(new Font("Arial", Font.BOLD, 12));
		lblTitre.setForeground(new Color(160, 160, 160));

		lblSousTitre.setFont(new Font("Arial", Font.PLAIN, 11));
		lblSousTitre.setForeground(new Color(180, 180, 180));

		lblImpact.setFont(new Font("Arial", Font.BOLD, 11));
		lblImpact.setForeground(new Color(100, 100, 200));

		JPanel centre = new JPanel(new BorderLayout(0, 2));
		centre.setBackground(Color.WHITE);
		centre.add(lblTitre, BorderLayout.NORTH);
		centre.add(lblSousTitre, BorderLayout.CENTER);
		centre.add(lblImpact, BorderLayout.SOUTH);

		add(lblIcone, BorderLayout.NORTH);
		add(centre, BorderLayout.CENTER);
	}

	/**
	 * Active l'affichage d'un evenement avec son impact OCEAN.
	 *
	 * @param nomEvenement le nom de l'evenement declenche
	 * @param nbAffectes   le nombre d'habitants concernes
	 */
	public void afficherEvenement(String nomEvenement, int nbAffectes) {
		minutesRestantes = getDureeAleatoire(nomEvenement);

		lblIcone.setText("⚡");
		lblIcone.setForeground(new Color(255, 160, 0));

		lblTitre.setText(nomEvenement.toUpperCase());
		lblTitre.setForeground(new Color(60, 60, 60));

		lblSousTitre.setText(nbAffectes + " habitants affectes");
		lblSousTitre.setForeground(new Color(120, 120, 120));

		lblImpact.setText(getImpactOCEAN(nomEvenement));
	}

	/**
	 * Met a jour l'affichage a chaque tour.
	 * Retour a l'etat calme quand minutesRestantes atteint zero.
	 */
	public void nextTour(List<engine.habitant.Habitant> habitants) {
		if (minutesRestantes <= 0) return;

		minutesRestantes--;

		int nbInformes = 0;
		for (engine.habitant.Habitant h : habitants) {
			if (h.estInforme()) nbInformes++;
		}

		if (nbInformes > 0) {
			lblSousTitre.setText(nbInformes + " habitants affectés");
		}

		if (minutesRestantes <= 0) {
			reinitialiser();
		}
	}

	/**
	 * Remet le panneau dans l'etat "aucun evenement".
	 */
	private void reinitialiser() {
		lblIcone.setText("🗓");
		lblIcone.setForeground(new Color(180, 180, 180));

		lblTitre.setText("AUCUN EVENEMENT");
		lblTitre.setForeground(new Color(160, 160, 160));

		lblSousTitre.setText("La ville est calme pour le moment.");
		lblSousTitre.setForeground(new Color(180, 180, 180));

		lblImpact.setText(" ");
	}

	/**
	 * Retourne une duree aleatoire en tours selon l'evenement.
	 * Chaque evenement a sa propre fourchette de duree — coherente
	 * avec son impact psychologique sur la population.
	 * 10 minutes par tour dans la simulation.
	 *
	 * @param nomEvenement le nom de l'evenement
	 * @return le nombre de tours d'affichage
	 */
	private int getDureeAleatoire(String nomEvenement) {
		if (nomEvenement.equals("Tempête Urbaine")) {
			return 120 + RandomProvider.getInstance().nextInt(61);
		}
		if (nomEvenement.equals("Festival de Quartier")) {
			return 180 + RandomProvider.getInstance().nextInt(61);
		}
		if (nomEvenement.equals("Crise Économique")) {
			return 240 + RandomProvider.getInstance().nextInt(121);
		}
		if (nomEvenement.equals("Semaine Culturelle")) {
			return 180 + RandomProvider.getInstance().nextInt(61);
		}
		if (nomEvenement.equals("Épidémie")) {
			return 240 + RandomProvider.getInstance().nextInt(121);
		}
		return 120;
	}

	/**
	 * Retourne l'impact OCEAN selon l'evenement.
	 * Les fleches refletent exactement ce que visit() fait
	 * dans chaque classe Event*.
	 */
	private String getImpactOCEAN(String nomEvenement) {
		if (nomEvenement.equals("Tempête Urbaine")) {
			return "Nevrosisme ↑   Moral ↓";
		}
		if (nomEvenement.equals("Festival de Quartier")) {
			return "Social ↑   Extraversion ↑";
		}
		if (nomEvenement.equals("Crise Économique")) {
			return "Moral ↓   Nevrosisme ↑";
		}
		if (nomEvenement.equals("Semaine Culturelle")) {
			return "Ouverture ↑   Moral ↑";
		}
		if (nomEvenement.equals("Épidémie")) {
			return "Sante ↓   Moral ↓";
		}
		return " ";
	}
}