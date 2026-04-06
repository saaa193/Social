package gui.dashboards;

import config.GameConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 * <p>
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
		zoneGauche.add(titre);

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
		modale.setLocationRelativeTo(this);

		//Contenu principal
		JPanel contenu = new JPanel();
		contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
		contenu.setBackground(Color.WHITE);
		contenu.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

		// Theme
		JLabel lblTheme = new JLabel("THEME DE L'INFORMATION");
		lblTheme.setFont(new Font("Arial", Font.BOLD, 11));
		lblTheme.setForeground(Color.GRAY);
		contenu.add(lblTheme);
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

	private class SliderResistanceAction implements javax.swing.event.ChangeListener {
		@Override
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			valeurResistance.setText(sliderResistance.getValue() + "%");
		}
	}

	private class SliderInfluenceAction implements javax.swing.event.ChangeListener {
		@Override
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			valeurInfluence.setText(sliderInfluence.getValue() + "/10");
		}
	}

	private class SliderVeraciteAction implements javax.swing.event.ChangeListener {
		private JLabel label;

		public SliderVeraciteAction(JLabel label) {
			this.label = label;
		}

		@Override
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			label.setText(source.getValue() + "%");
		}
	}

	private class SliderVirulenceAction implements javax.swing.event.ChangeListener {
		private JLabel label;

		public SliderVirulenceAction(JLabel label) {
			this.label = label;
		}

		@Override
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			label.setText(source.getValue() + "%");
		}
	}

	private class BtnInformationAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ouvrirModalePropagation();
		}
	}

	/**
	 * Action du bouton "Lancer la Rumeur" :
	 * Récupère les valeurs et notifie MainGUI via le listener.
	 */
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
			// On récupère les valeurs saisies
			theme = champTheme.getText();
			virulence = sliderVirulence.getValue() / 100.0f;
			veracite = sliderVeracite.getValue() / 100.0f;

			// On notifie MainGUI que la rumeur est lancée
			if (informationListener != null) {
				informationListener.actionPerformed(e);
			}

			modale.dispose();
		}
	}

	/**
	 * Retourne la valeur du slider Resistance (0 a 100).
	 */
	public int getResistance() {
		return sliderResistance.getValue();
	}

	/**
	 * Retourne la valeur du slider Force d'influence (0 a 10).
	 */
	public int getInfluence() {
		return sliderInfluence.getValue();
	}

	/**
	 * Retourne l'evenement selectionne dans le combo.
	 */
	public String getEvenementSelectionne() {
		return (String) comboEvenement.getSelectedItem();
	}

	/**
	 * Retourne le theme de l'information a propager.
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * Retourne la virulence de l'information (0.0 a 1.0).
	 */
	public float getVirulence() {
		return virulence;
	}

	/**
	 * Retourne la veracite de l'information (0.0 a 1.0).
	 */
	public float getVeracite() {
		return veracite;
	}

	/**
	 * Connecte le combo evenement a MainGUI.
	 */
	public void addEvenementListener(ActionListener listener) {
		comboEvenement.addActionListener(listener);
	}

	/**
	 * Connecte le bouton Information a MainGUI.
	 */
	public void addInformationListener(ActionListener listener) {
		this.informationListener = listener;
	}

	/**
	 * Connecte le bouton Récapitulatif à MainGUI.
	 */
	public void addRecapitulatifListener(ActionListener listener) {
		btnRecapitulatif.addActionListener(listener);
	}
}