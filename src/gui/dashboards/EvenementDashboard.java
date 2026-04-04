package gui.dashboards;

import config.GameConfiguration;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

	private JLabel lblIcone    = new JLabel("🗓", JLabel.CENTER);
	private JLabel lblTitre    = new JLabel("AUCUN EVENEMENT", JLabel.CENTER);
	private JLabel lblSousTitre = new JLabel("La ville est calme pour le moment.", JLabel.CENTER);
	private JLabel lblImpact   = new JLabel(" ", JLabel.CENTER);

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
	 * Decremente les minutes restantes de 10 a chaque tour.
	 * Chaque tour = 10 minutes simulees.
	 * Retour a l'etat calme quand minutesRestantes atteint zero.
	 */
	public void nextTour() {
		if (minutesRestantes > 0) {
			minutesRestantes -= 10;
			if (minutesRestantes <= 0) {
				minutesRestantes = 0;
				reinitialiser();
			}
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
		if (nomEvenement.equals("Offres d'Emploi")) {
			// ~30 sec reelles a vitesse x1
			return 500 + (int) (Math.random() * 201);
		}
		if (nomEvenement.equals("Alerte Meteo")) {
			// ~45 sec reelles a vitesse x1
			return 700 + (int) (Math.random() * 201);
		}
		if (nomEvenement.equals("Expo Musee")) {
			// ~1 min reelle a vitesse x1
			return 900 + (int) (Math.random() * 201);
		}
		if (nomEvenement.equals("Fete de Quartier")) {
			// ~1 min 30 sec reelles a vitesse x1
			return 1100 + (int) (Math.random() * 201);
		}
		return 500;
	}

	/**
	 * Retourne l'impact OCEAN selon l'evenement.
	 * Les fleches refletent exactement ce que visit() fait
	 * dans chaque classe Event*.
	 */
	private String getImpactOCEAN(String nomEvenement) {
		if (nomEvenement.equals("Alerte Meteo")) {
			return "Nevrosisme \u2191   Moral \u2193";
		}
		if (nomEvenement.equals("Fete de Quartier")) {
			return "Social \u2191   Agreabilite \u2191";
		}
		if (nomEvenement.equals("Offres d'Emploi")) {
			return "Moral \u2191   Nevrosisme \u2193";
		}
		if (nomEvenement.equals("Expo Musee")) {
			return "Ouverture \u2191   Moral \u2191";
		}
		return " ";
	}
}