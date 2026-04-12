package gui.dashboards;

import config.GameConfiguration;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * MacroDashboard : Zone opérationnelle située en bas de l'écran.
 * Contient les paramètres de simulation et les boutons d'action.
 */
public class MacroDashboard extends JPanel {
	private static final long serialVersionUID = 1L;

	// Curseurs de paramètres
	private JSlider sliderResistance = new JSlider(0, 100, 50);
	private JSlider sliderInfluence = new JSlider(0, 10, 5);

	// Labels de valeurs
	private JLabel valeurResistance = new JLabel("50%");
	private JLabel valeurInfluence = new JLabel("5/10");

	// Combo événement + bouton information
	private JButton btnRecapitulatif = new JButton("📊 Récapitulatif");
	private JComboBox<String> comboEvenement = new JComboBox<String>();
	private JButton btnInformation = new JButton("(•) Propager une Information");
	private JButton btnAide = new JButton("?");

	// Listener pour connecter le bouton rumeur à MainGUI
	private ActionListener informationListener = null;

	private String theme = "";
	private float virulence = 0.5f;
	private float veracite = 0.5f;

	public MacroDashboard() {
		this.setPreferredSize(new Dimension(0, GameConfiguration.MENU_BOTTOM_HEIGHT));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setBackground(Color.LIGHT_GRAY);
		this.setLayout(new BorderLayout());

		//Zone gauche : titre + curseurs
		JPanel zoneGauche = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		zoneGauche.setBackground(Color.LIGHT_GRAY);

		JLabel titre = new JLabel("PARAMETRES DE SIMULATION");
		titre.setFont(new Font("Arial", Font.BOLD, 11));
		btnAide.setFont(new Font("Arial", Font.BOLD, 11));
		btnAide.addActionListener(new BtnAideAction());
		zoneGauche.add(titre);
		zoneGauche.add(btnAide);

		zoneGauche.add(new JLabel("Resistance :"));
		sliderResistance.setBackground(Color.LIGHT_GRAY);
		sliderResistance.setPreferredSize(new Dimension(100, 30));
		sliderResistance.addChangeListener(new SliderResistanceAction());
		zoneGauche.add(sliderResistance);
		zoneGauche.add(valeurResistance);

		zoneGauche.add(new JLabel("Force d'influence :"));
		sliderInfluence.setBackground(Color.LIGHT_GRAY);
		sliderInfluence.setPreferredSize(new Dimension(100, 30));
		sliderInfluence.addChangeListener(new SliderInfluenceAction());
		zoneGauche.add(sliderInfluence);
		zoneGauche.add(valeurInfluence);

		this.add(zoneGauche, BorderLayout.WEST);

		//Zone droite : combo + bouton
		JPanel zoneDroite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		zoneDroite.setBackground(Color.LIGHT_GRAY);
		btnRecapitulatif.setBackground(new Color(50, 100, 200));
		btnRecapitulatif.setForeground(Color.WHITE);
		btnRecapitulatif.setOpaque(true);
		btnRecapitulatif.setBorderPainted(false);
		comboEvenement.addItem("Declencher un Evenement");
		comboEvenement.addItem("Tempête Urbaine");
		comboEvenement.addItem("Festival de Quartier");
		comboEvenement.addItem("Crise Économique");
		comboEvenement.addItem("Semaine Culturelle");
		comboEvenement.addItem("Épidémie");
		zoneDroite.add(comboEvenement);

		btnInformation.setBackground(new Color(200, 50, 50));
		btnInformation.setForeground(Color.WHITE);
		btnInformation.setOpaque(true);
		btnInformation.setBorderPainted(false);
		btnInformation.addActionListener(new BtnInformationAction());
		zoneDroite.add(btnInformation);
		zoneDroite.add(btnRecapitulatif);

		this.add(zoneDroite, BorderLayout.EAST);
	}

	/**
	 * Ouvre la modale "Propager une Information".
	 */
	private void ouvrirModalePropagation() {
		JDialog modale = new JDialog(
				(JFrame) SwingUtilities.getWindowAncestor(this),
				"Propager une Information",
				true
		);
		modale.setLayout(new BorderLayout(10, 10));
		modale.getContentPane().setBackground(Color.WHITE);
		modale.setSize(400, 320);
		modale.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
		//Contenu principal
		JPanel contenu = new JPanel();
		contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
		contenu.setBackground(Color.WHITE);
		contenu.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

		// Theme
		JLabel lblTheme = new JLabel("THEME DE L'INFORMATION");
		lblTheme.setFont(new Font("Arial", Font.BOLD, 11));
		lblTheme.setForeground(Color.GRAY);
		lblTheme.setHorizontalAlignment(JLabel.CENTER);
		JButton btnAideInfo = new JButton("?");
		btnAideInfo.setFont(new Font("Arial", Font.BOLD, 11));
		btnAideInfo.addActionListener(new BtnAideInformationAction(btnAideInfo));

		JPanel panelTitre = new JPanel(new BorderLayout());
		panelTitre.setBackground(Color.WHITE);
		panelTitre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

		JPanel panelBtnAide = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		panelBtnAide.setBackground(Color.WHITE);
		panelBtnAide.add(btnAideInfo);

		panelTitre.add(lblTheme, BorderLayout.CENTER);
		panelTitre.add(panelBtnAide, BorderLayout.EAST);
		contenu.add(panelTitre);
		contenu.add(Box.createVerticalStrut(5));

		JTextField champTheme = new JTextField("Ex: Fausse rumeur sur la ville...");
		champTheme.setForeground(Color.GRAY);
		champTheme.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		contenu.add(champTheme);
		contenu.add(Box.createVerticalStrut(15));

		// Curseur Veracite
		JPanel panelVeracite = new JPanel(new BorderLayout());
		panelVeracite.setBackground(Color.WHITE);
		JLabel lblVeracite = new JLabel("VERACITE");
		lblVeracite.setFont(new Font("Arial", Font.BOLD, 11));
		JLabel valVeracite = new JLabel("50%");
		valVeracite.setForeground(new Color(0, 150, 0));
		panelVeracite.add(lblVeracite, BorderLayout.WEST);
		panelVeracite.add(valVeracite, BorderLayout.EAST);
		contenu.add(panelVeracite);

		JSlider sliderVeracite = new JSlider(0, 100, 50);
		sliderVeracite.setBackground(Color.WHITE);
		sliderVeracite.addChangeListener(new SliderVeraciteAction(valVeracite));
		contenu.add(sliderVeracite);
		contenu.add(Box.createVerticalStrut(10));

		//Curseur Virulence
		JPanel panelVirulence = new JPanel(new BorderLayout());
		panelVirulence.setBackground(Color.WHITE);
		JLabel lblVirulence = new JLabel("VIRULENCE");
		lblVirulence.setFont(new Font("Arial", Font.BOLD, 11));
		JLabel valVirulence = new JLabel("50%");
		valVirulence.setForeground(new Color(200, 50, 50));
		panelVirulence.add(lblVirulence, BorderLayout.WEST);
		panelVirulence.add(valVirulence, BorderLayout.EAST);
		contenu.add(panelVirulence);

		JSlider sliderVirulence = new JSlider(0, 100, 50);
		sliderVirulence.setBackground(Color.WHITE);
		sliderVirulence.addChangeListener(new SliderVirulenceAction(valVirulence));
		contenu.add(sliderVirulence);

		modale.add(contenu, BorderLayout.CENTER);

		//Bouton Lancer
		JButton btnLancer = new JButton("Lancer la Rumeur");
		btnLancer.setBackground(new Color(220, 80, 100));
		btnLancer.setForeground(Color.WHITE);
		btnLancer.setFont(new Font("Arial", Font.BOLD, 13));
		btnLancer.addActionListener(new BtnLancerAction(
				modale, champTheme, sliderVirulence, sliderVeracite
		));

		JPanel bas = new JPanel();
		bas.setBackground(Color.WHITE);
		bas.add(btnLancer);
		modale.add(bas, BorderLayout.SOUTH);

		modale.setVisible(true);
	}

	private class SliderResistanceAction implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			valeurResistance.setText(sliderResistance.getValue() + "%");
		}
	}

	private class SliderInfluenceAction implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			valeurInfluence.setText(sliderInfluence.getValue() + "/10");
		}
	}

	private class SliderVeraciteAction implements ChangeListener {
		private JLabel label;

		public SliderVeraciteAction(JLabel label) {
			this.label = label;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			label.setText(source.getValue() + "%");
		}
	}

	private class SliderVirulenceAction implements ChangeListener {
		private JLabel label;

		public SliderVirulenceAction(JLabel label) {
			this.label = label;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			label.setText(source.getValue() + "%");
		}
	}

	private class BtnAideInformationAction implements ActionListener {
		private JButton source;

		public BtnAideInformationAction(JButton source) {
			this.source = source;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog aide = new JDialog(
					SwingUtilities.getWindowAncestor(source),
					"Aide — Propagation d'information",
					Dialog.ModalityType.APPLICATION_MODAL
			);
			aide.setLayout(new BorderLayout(10, 10));
			aide.getContentPane().setBackground(Color.WHITE);
			aide.setSize(500, 250);
			aide.setLocationRelativeTo(SwingUtilities.getWindowAncestor(source));

			JPanel contenu = new JPanel();
			contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
			contenu.setBackground(Color.WHITE);
			contenu.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

			JLabel lblVeraciteTitre = new JLabel("Véracité (0% à 100%)");
			lblVeraciteTitre.setFont(new Font("Arial", Font.BOLD, 12));
			contenu.add(lblVeraciteTitre);
			contenu.add(Box.createVerticalStrut(5));

			JLabel lblVeracite1 = new JLabel("0% → rumeur, se propage mieux chez les habitants névrosés");
			JLabel lblVeracite2 = new JLabel("50% → information neutre, se propage chez les extravertis et agréables");
			JLabel lblVeracite3 = new JLabel("100% → fait avéré, mieux accepté par les habitants ouverts et consciencieux");
			lblVeracite1.setFont(new Font("Arial", Font.PLAIN, 11));
			lblVeracite2.setFont(new Font("Arial", Font.PLAIN, 11));
			lblVeracite3.setFont(new Font("Arial", Font.PLAIN, 11));
			contenu.add(lblVeracite1);
			contenu.add(lblVeracite2);
			contenu.add(lblVeracite3);
			contenu.add(Box.createVerticalStrut(15));

			JLabel lblVirulenceTitre = new JLabel("Virulence (0% à 100%)");
			lblVirulenceTitre.setFont(new Font("Arial", Font.BOLD, 12));
			contenu.add(lblVirulenceTitre);
			contenu.add(Box.createVerticalStrut(5));

			JLabel lblVirulence1 = new JLabel("0% → propagation très lente, peu d'habitants touchés");
			JLabel lblVirulence2 = new JLabel("50% → propagation modérée");
			JLabel lblVirulence3 = new JLabel("100% → propagation rapide de proche en proche via les liens sociaux");
			lblVirulence1.setFont(new Font("Arial", Font.PLAIN, 11));
			lblVirulence2.setFont(new Font("Arial", Font.PLAIN, 11));
			lblVirulence3.setFont(new Font("Arial", Font.PLAIN, 11));
			contenu.add(lblVirulence1);
			contenu.add(lblVirulence2);
			contenu.add(lblVirulence3);

			aide.add(contenu, BorderLayout.CENTER);

			JButton btnFermer = new JButton("Fermer");
			btnFermer.addActionListener(new FermerAideAction(aide));
			JPanel bas = new JPanel();
			bas.setBackground(Color.WHITE);
			bas.add(btnFermer);
			aide.add(bas, BorderLayout.SOUTH);

			aide.setVisible(true);
		}
	}

	private class BtnAideAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog aide = new JDialog(
					(JFrame) SwingUtilities.getWindowAncestor(btnAide),
					"Aide — Paramètres de simulation",
					true
			);
			aide.setLayout(new BorderLayout(10, 10));
			aide.getContentPane().setBackground(Color.WHITE);
			aide.setSize(500, 280);
			aide.setLocationRelativeTo(SwingUtilities.getWindowAncestor(btnAide));

			JPanel contenu = new JPanel();
			contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
			contenu.setBackground(Color.WHITE);
			contenu.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

			JLabel lblResistanceTitre = new JLabel("Slider Résistance (0-100%)");
			lblResistanceTitre.setFont(new Font("Arial", Font.BOLD, 12));
			contenu.add(lblResistanceTitre);
			contenu.add(Box.createVerticalStrut(5));

			JLabel lblResistance1 = new JLabel("Résistance à 0% → moral baisse de 2 points pour tous");
			JLabel lblResistance2 = new JLabel("Résistance à 50% → aucun effet (neutre)");
			JLabel lblResistance3 = new JLabel("Résistance à 100% → moral monte de 2 points pour tous");
			lblResistance1.setFont(new Font("Arial", Font.PLAIN, 11));
			lblResistance2.setFont(new Font("Arial", Font.PLAIN, 11));
			lblResistance3.setFont(new Font("Arial", Font.PLAIN, 11));
			contenu.add(lblResistance1);
			contenu.add(lblResistance2);
			contenu.add(lblResistance3);
			contenu.add(Box.createVerticalStrut(15));

			JLabel lblInfluenceTitre = new JLabel("Slider Force d'influence (0-10)");
			lblInfluenceTitre.setFont(new Font("Arial", Font.BOLD, 12));
			contenu.add(lblInfluenceTitre);
			contenu.add(Box.createVerticalStrut(5));

			JLabel lblInfluence1 = new JLabel("Force à 0 → événement normal");
			JLabel lblInfluence2 = new JLabel("Force à 10 → événement touche 2x plus d'habitants avec un impact doublé");
			lblInfluence1.setFont(new Font("Arial", Font.PLAIN, 11));
			lblInfluence2.setFont(new Font("Arial", Font.PLAIN, 11));
			contenu.add(lblInfluence1);
			contenu.add(lblInfluence2);

			aide.add(contenu, BorderLayout.CENTER);

			JButton btnFermer = new JButton("Fermer");
			btnFermer.addActionListener(new FermerAideAction(aide));
			JPanel bas = new JPanel();
			bas.setBackground(Color.WHITE);
			bas.add(btnFermer);
			aide.add(bas, BorderLayout.SOUTH);

			aide.setVisible(true);
		}
	}

	private class FermerAideAction implements ActionListener {
		private JDialog aide;

		public FermerAideAction(JDialog aide) {
			this.aide = aide;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			aide.dispose();
		}
	}

	private class BtnInformationAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ouvrirModalePropagation();
		}
	}

	private class BtnLancerAction implements ActionListener {
		private JDialog modale;
		private JTextField champTheme;
		private JSlider sliderVirulence;
		private JSlider sliderVeracite;

		public BtnLancerAction(JDialog modale, JTextField champTheme,
							   JSlider sliderVirulence, JSlider sliderVeracite) {
			this.modale = modale;
			this.champTheme = champTheme;
			this.sliderVirulence = sliderVirulence;
			this.sliderVeracite = sliderVeracite;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			theme = champTheme.getText();
			virulence = sliderVirulence.getValue() / 100.0f;
			veracite = sliderVeracite.getValue() / 100.0f;

			if (informationListener != null) {
				informationListener.actionPerformed(e);
			}

			modale.dispose();
		}
	}

	public int getResistance() { return sliderResistance.getValue(); }
	public int getInfluence() { return sliderInfluence.getValue(); }
	public String getEvenementSelectionne() { return (String) comboEvenement.getSelectedItem(); }
	public String getTheme() { return theme; }
	public float getVirulence() { return virulence; }
	public float getVeracite() { return veracite; }

	public void addEvenementListener(ActionListener listener) {
		comboEvenement.addActionListener(listener);
	}

	public void addInformationListener(ActionListener listener) {
		this.informationListener = listener;
	}

	public void addRecapitulatifListener(ActionListener listener) {
		btnRecapitulatif.addActionListener(listener);
	}}