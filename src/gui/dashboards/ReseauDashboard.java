package gui.dashboards;

import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.map.Statistique;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * ReseauDashboard : Affiche la densité du réseau social.
 * Montre le nombre de liens Famille, Travail et Amis
 * avec des barres colorées et des boutons pour filtrer l'affichage.
 */
public class ReseauDashboard extends JPanel {

	private static final long serialVersionUID = 1L;

	private JProgressBar barreFamille = new JProgressBar(0, 200);
	private JProgressBar barreTravail = new JProgressBar(0, 200);
	private JProgressBar barreAmis    = new JProgressBar(0, 200);

	private JLabel nombreFamille = new JLabel("0");
	private JLabel nombreTravail = new JLabel("0");
	private JLabel nombreAmis    = new JLabel("0");

	private JToggleButton btnFamille = new JToggleButton("O");
	private JToggleButton btnTravail = new JToggleButton("O");
	private JToggleButton btnAmis    = new JToggleButton("O");

	public ReseauDashboard() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				"Densité du réseau"
		));
		setPreferredSize(new Dimension(GameConfiguration.MENU_RIGHT_WIDTH, 160));

		JPanel contenu = new JPanel(new GridLayout(3, 1, 0, 8));
		contenu.setBackground(Color.WHITE);
		contenu.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

		contenu.add(creerLigneLien("FAMILLE", barreFamille, nombreFamille, btnFamille, new Color(220, 50, 100)));
		contenu.add(creerLigneLien("TRAVAIL", barreTravail, nombreTravail, btnTravail, new Color(50, 100, 220)));
		contenu.add(creerLigneLien("AMIS", barreAmis, nombreAmis, btnAmis, new Color(50, 180, 50)));

		add(contenu, BorderLayout.CENTER);

		btnFamille.setSelected(true);
		btnTravail.setSelected(true);
		btnAmis.setSelected(true);
	}

	private JPanel creerLigneLien(String nom, JProgressBar barre, JLabel nombre, JToggleButton btn, Color couleur) {
		JPanel ligne = new JPanel(new BorderLayout(5, 0));
		ligne.setBackground(Color.WHITE);

		JLabel label = new JLabel(nom);
		label.setFont(new Font("Arial", Font.BOLD, 11));
		label.setForeground(couleur);
		label.setPreferredSize(new Dimension(60, 20));
		ligne.add(label, BorderLayout.WEST);

		barre.setForeground(couleur);
		barre.setBackground(new Color(230, 230, 230));
		barre.setStringPainted(false);
		ligne.add(barre, BorderLayout.CENTER);

		JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		droite.setBackground(Color.WHITE);

		nombre.setFont(new Font("Arial", Font.BOLD, 11));
		nombre.setForeground(Color.GRAY);
		droite.add(nombre);

		btn.setPreferredSize(new Dimension(30, 25));
		btn.setFont(new Font("Arial", Font.PLAIN, 12));
		btn.setBackground(Color.WHITE);
		btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		btn.setFocusPainted(false);
		droite.add(btn);

		ligne.add(droite, BorderLayout.EAST);
		return ligne;
	}

	/**
	 * Met à jour les barres à chaque tour.
	 * Utilise getNombreLiensParType() — plus aucun instanceof.
	 */
	public void updateReseau(List<Habitant> habitants) {
		if (habitants == null || habitants.isEmpty()) return;

		int nbFamille = Statistique.getNombreLiensParType(habitants, "Familial");
		int nbTravail = Statistique.getNombreLiensParType(habitants, "Professionnel");
		int nbAmis = Statistique.getNombreLiensParType(habitants, "Amical");

		barreFamille.setValue(Math.min(nbFamille, 200));
		barreTravail.setValue(Math.min(nbTravail, 200));
		barreAmis.setValue(Math.min(nbAmis, 200));

		nombreFamille.setText(String.valueOf(nbFamille));
		nombreTravail.setText(String.valueOf(nbTravail));
		nombreAmis.setText(String.valueOf(nbAmis));

		btnFamille.setText(btnFamille.isSelected() ? "O" : "X");
		btnTravail.setText(btnTravail.isSelected() ? "O" : "X");
		btnAmis.setText(btnAmis.isSelected()    ? "O" : "X");
	}

	public boolean afficherFamille() { return btnFamille.isSelected(); }
	public boolean afficherTravail() { return btnTravail.isSelected(); }
	public boolean afficherAmis() { return btnAmis.isSelected(); }
}