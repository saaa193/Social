package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Page de chargement affichee avant le lancement de la simulation.
 * La barre de progression avance automatiquement puis lance MainGUI.
 */
public class PageChargement extends JFrame {

	private static final long serialVersionUID = 1L;

	private JProgressBar barreProgression = new JProgressBar(0, 100);
	private JLabel lblStatut = new JLabel("Chargement de la simulation...", SwingConstants.CENTER);
	private int progression = 0;
	private ActionListener suite;

	/**
	 * Construit la page de chargement et demarre la progression.
	 *
	 * @param suite l'action a executer quand le chargement est termine
	 */
	public PageChargement(ActionListener suite) {
		super("Projet SOCIAL");
		this.suite = suite;
		init();
		setVisible(true);
	}

	private void init() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(Color.WHITE);
		setSize(500, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		JPanel panneauCentre = new JPanel(new BorderLayout());
		panneauCentre.setBackground(Color.WHITE);
		panneauCentre.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));

		JLabel lblTitre = new JLabel("Simulation Psychologique d'une Ville", SwingConstants.CENTER);
		lblTitre.setFont(new Font("Arial", Font.BOLD, 18));
		lblTitre.setForeground(new Color(60, 60, 60));
		panneauCentre.add(lblTitre, BorderLayout.CENTER);

		JPanel panneauBas = new JPanel(new BorderLayout(0, 8));
		panneauBas.setBackground(Color.WHITE);
		panneauBas.setBorder(BorderFactory.createEmptyBorder(0, 40, 30, 40));

		barreProgression.setStringPainted(false);
		barreProgression.setForeground(new Color(80, 60, 180));
		barreProgression.setBackground(new Color(220, 220, 220));

		lblStatut.setFont(new Font("Arial", Font.PLAIN, 12));
		lblStatut.setForeground(Color.GRAY);

		panneauBas.add(barreProgression, BorderLayout.CENTER);
		panneauBas.add(lblStatut, BorderLayout.SOUTH);

		getContentPane().add(panneauCentre, BorderLayout.CENTER);
		getContentPane().add(panneauBas, BorderLayout.SOUTH);

		Timer timer = new Timer(30, new ProgressionAction());
		timer.start();
	}

	/**
	 * Avance la barre de progression et lance la simulation quand elle est pleine.
	 */
	private class ProgressionAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			progression += 2;
			barreProgression.setValue(progression);

			if (progression >= 100) {
				Timer timer = (Timer) e.getSource();
				timer.stop();
				dispose();
				suite.actionPerformed(e);
			}
		}
	}
}