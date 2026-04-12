package gui.dashboards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * ControlDashboard : Le tableau de bord principal.
 * Il regroupe les informations de temps, météo et les contrôles de la simulation.
 * Utilisation de BorderLayout pour une répartition propre des zones de l'interface.
 */
public class ControlDashboard extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel titre = new JLabel("Projet SOCIAL ");
	private JLabel lblHorloge = new JLabel("START");
	private JLabel lblPeriode = new JLabel("SEMAINE");
	private JLabel lblMeteo = new JLabel("☀ Soleil");

	private JButton btnStartStop = new JButton("▶");
	private JButton btnVitesse = new JButton("Vitesse: x1");

	private JLabel lblFlashInfo = new JLabel("");

	public ControlDashboard() {
		// Mise en page BorderLayout : idéal pour une barre d'outils
		this.setLayout(new BorderLayout());
		this.setBackground(Color.LIGHT_GRAY);

		// Zone Ouest : le titre
		this.add(titre, BorderLayout.WEST);

		// Zone Centre : les infos dynamiques (date, période, météo)
		JPanel centre = new JPanel();
		centre.setBackground(Color.LIGHT_GRAY);
		centre.add(lblHorloge);

		lblPeriode.setForeground(new Color(100, 100, 255)); // Touche de couleur pour le design
		centre.add(new JLabel(" | "));
		centre.add(lblPeriode);
		centre.add(new JLabel(" | "));
		centre.add(lblMeteo);

		lblFlashInfo.setForeground(Color.RED);
		lblFlashInfo.setVisible(false);
		centre.add(lblFlashInfo);

		this.add(centre, BorderLayout.CENTER);

		// Zone Est : les contrôles (Start/Stop et Vitesse)
		JPanel droite = new JPanel();
		droite.setBackground(Color.LIGHT_GRAY);
		droite.add(btnStartStop);
		droite.add(btnVitesse);
		this.add(droite, BorderLayout.EAST);
	}


	public void setLblHorloge(String texte) {
		lblHorloge.setText(texte);
	}

	public void setPeriodeText(String texte) {
		lblPeriode.setText(texte);
	}

	public void setBtnVitesseText(String texte) {
		btnVitesse.setText(texte);
	}

	public void setBtnStartStopText(String texte) {
		btnStartStop.setText(texte);
	}

	//GESTION DES ÉVÉNEMENTS
	public void addStartStopListener(ActionListener listener) {
		btnStartStop.addActionListener(listener);
	}

	public void addAccelererListener(ActionListener listener) {
		btnVitesse.addActionListener(listener);
	}

	public void setMeteo(boolean mauvaisTemps) {
		if (mauvaisTemps) {
			lblMeteo.setText("🌧 Pluie");
		} else {
			lblMeteo.setText("☀ Soleil");
		}
	}

	/**
	 * Affiche une bannière FLASH INFO temporaire.
	 * Disparaît automatiquement après 3 secondes.
	 */
	public void afficherFlashInfo(String theme) {
		lblFlashInfo.setText("🔴 FLASH INFO : " + theme);
		lblFlashInfo.setVisible(true);
		Timer timer = new Timer(10000, new ResetFlashInfoAction());
		timer.setRepeats(false);
		timer.start();
	}

	/**
	 * Cache la bannière FLASH INFO après 3 secondes.
	 */
	private class ResetFlashInfoAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			lblFlashInfo.setVisible(false);
		}
	}

}