package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Fenetre de lancement de la simulation.
 * Permet de choisir le nombre d'habitants avant de demarrer.
 */
public class LanceurSimulation extends JFrame {

	private static final long serialVersionUID = 1L;

	private JSlider sliderHabitants = new JSlider(150, 200, 200);
	private JLabel lblValeurHabitants = new JLabel("200 habitants", SwingConstants.CENTER);
	private JButton btnLancer = new JButton("Lancer la simulation");

	private int nbHabitants = 200;

	/**
	 * Construit la fenetre de lancement.
	 */
	public LanceurSimulation() {
		super("Projet SOCIAL");
		init();
		setVisible(true);
	}

	private void init() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0, 20));
		contentPane.setBackground(Color.WHITE);
		setSize(450, 280);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		// Titre
		JLabel lblTitre = new JLabel("Simulation Psychologique d'une Ville", SwingConstants.CENTER);
		lblTitre.setFont(new Font("Arial", Font.BOLD, 16));
		lblTitre.setForeground(new Color(60, 60, 60));
		lblTitre.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));
		contentPane.add(lblTitre, BorderLayout.NORTH);

		// Panneau centre — slider habitants
		JPanel panneauCentre = new JPanel(new BorderLayout(0, 8));
		panneauCentre.setBackground(Color.WHITE);
		panneauCentre.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

		JLabel lblHabitants = new JLabel("Nombre d'habitants");
		lblHabitants.setFont(new Font("Arial", Font.BOLD, 12));
		lblHabitants.setForeground(Color.GRAY);

		sliderHabitants.setBackground(Color.WHITE);
		sliderHabitants.setMajorTickSpacing(10);
		sliderHabitants.setPaintTicks(true);
		sliderHabitants.addChangeListener(new SliderHabitantsAction());

		lblValeurHabitants.setFont(new Font("Arial", Font.PLAIN, 12));
		lblValeurHabitants.setForeground(new Color(80, 60, 180));

		panneauCentre.add(lblHabitants, BorderLayout.NORTH);
		panneauCentre.add(sliderHabitants, BorderLayout.CENTER);
		panneauCentre.add(lblValeurHabitants, BorderLayout.SOUTH);

		contentPane.add(panneauCentre, BorderLayout.CENTER);

		// Bouton lancer
		JPanel panneauBas = new JPanel();
		panneauBas.setBackground(Color.WHITE);
		panneauBas.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

		btnLancer.setFont(new Font("Arial", Font.BOLD, 13));
		btnLancer.addActionListener(new LancerAction());
		panneauBas.add(btnLancer);

		contentPane.add(panneauBas, BorderLayout.SOUTH);
	}

	public int getNbHabitants() {
		return nbHabitants;
	}

	private class SliderHabitantsAction implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			nbHabitants = sliderHabitants.getValue();
			lblValeurHabitants.setText(nbHabitants + " habitants");
		}
	}

	private class LancerAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			new PageChargement(new LancerSimulationAction());
		}
	}

	private class LancerSimulationAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			MainGUI mainGUI = new MainGUI("Projet SOCIAL", nbHabitants);
			Thread gameThread = new Thread(mainGUI);
			gameThread.start();
		}
	}
}