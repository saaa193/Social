package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.map.Block;
import engine.map.Map;
import engine.process.GameBuilder;
import engine.process.GestionnaireEvenements;
import engine.process.MobileElementManager;
import engine.process.MobileInterface;

import gui.dashboards.*;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 * <p>
 * MainGUI : La classe principale qui orchestre tout le programme.
 * Elle implémente Runnable car elle gère la boucle de jeu
 * dans un Thread séparé pour ne pas bloquer l'interface graphique.
 */
public class MainGUI extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	// Composants de structure (Modèle et Moteur)
	private Map map;
	private MobileInterface manager;
	private GameDisplay dashboard;

	private boolean stop = true;
	private MainGUI instance = this;
	private int speed = GameConfiguration.GAME_SPEED;

	// Composants de l'interface (Vue)
	private ControlDashboard control;
	private MacroDashboard macro;
	private GraphDashboard graph;

	private ReseauDashboard reseau;

	private EvenementDashboard evenementDashboard;

	public MainGUI(String title) {
		super(title);
		init();
	}

	/**
	 * Initialisation de la structure : Utilisation du BorderLayout pour
	 * organiser l'espace visuel efficacement.
	 */
	private void init() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// 1. Barre de contrôle (Haut)
		control = new ControlDashboard();
		control.addStartStopListener(new StartStopAction());
		control.addAccelererListener(new SpeedAction());
		contentPane.add(BorderLayout.NORTH, control);

		// 2. Panneau latéral droit : Graphiques, Inspecteur et Réseau
		evenementDashboard = new EvenementDashboard();

		JPanel rightPanel = new JPanel(new BorderLayout(0, 5));
		rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		graph = new GraphDashboard();
		reseau = new ReseauDashboard();

		JPanel graphEtReseau = new JPanel(new BorderLayout(0, 5));
		graphEtReseau.add(graph, BorderLayout.NORTH);
		graphEtReseau.add(reseau, BorderLayout.SOUTH);

		rightPanel.add(evenementDashboard, BorderLayout.NORTH);
		rightPanel.add(graphEtReseau, BorderLayout.CENTER);

		contentPane.add(rightPanel, BorderLayout.EAST);

		// 3. Initialisation Moteur & Carte
		map = GameBuilder.buildMap();
		manager = GameBuilder.buildInitMobile(map);
		dashboard = new GameDisplay(map, manager);

		// Gestion des interactions souris
		MouseControls mouseControls = new MouseControls();
		dashboard.addMouseListener(mouseControls);

		// 4. Zone opérationnelle (Bas)
		macro = new MacroDashboard();
		contentPane.add(macro, BorderLayout.SOUTH);

		// 5. Zone principale : la Carte
		int mapWidth = GameConfiguration.COLUMN_COUNT * GameConfiguration.BLOCK_SIZE;
		int mapHeight = GameConfiguration.LINE_COUNT * GameConfiguration.BLOCK_SIZE;
		dashboard.setPreferredSize(new Dimension(mapWidth, mapHeight));
		contentPane.add(dashboard, BorderLayout.CENTER);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);

		macro.addEvenementListener(new EvenementAction());

		macro.addInformationListener(new InformationAction());

		macro.addRecapitulatifListener(new RecapitulatifAction());
	}

	/**
	 * La "Game Loop" : Le cœur de l'animation.
	 * Cette boucle s'exécute en continu tant que le jeu n'est pas en pause.
	 */
	@Override
	public void run() {
		while (!stop) {
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			manager.nextRound();
			mettreAJourAffichage();
		}
	}

	/**
	 * Met a jour tous les composants graphiques apres chaque tour.
	 * Calquee sur updateValues() du prof dans ChronometerGUI.
	 */
	private void mettreAJourAffichage() {
		dashboard.repaint();
		control.setLblHorloge(manager.getHorloge().getHeureActuelle());
		control.setPeriodeText(manager.getHorloge().estWeekend() ? "WEEKEND" : "SEMAINE");
		graph.updateStats(manager.getHabitants());
		reseau.updateReseau(manager.getHabitants());
		dashboard.getPaintStrategy().setFiltres(
				reseau.afficherFamille(),
				reseau.afficherTravail(),
				reseau.afficherAmis()
		);
		MobileElementManager mem = (MobileElementManager) manager;
		control.setMeteo(mem.isMauvaisTemps());
		evenementDashboard.nextTour();
		mem.setParametres(macro.getResistance(), macro.getInfluence());
	}

	//Pattern "Listener" pour gérer les clics
	private class StartStopAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!stop) {
				stop = true;
				control.setBtnStartStopText("▶");
				dashboard.repaint();
			} else {
				stop = false;
				control.setBtnStartStopText("⏸");
				Thread gameThread = new Thread(instance);
				gameThread.start();
			}
		}
	}

	private class SpeedAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Logique de cycle de vitesse (x1 -> x2 -> x5 -> x10 -> x1)
			if (speed == GameConfiguration.GAME_SPEED) {
				speed = GameConfiguration.GAME_SPEED / 2;
				control.setBtnVitesseText("Vitesse: x2");
			} else if (speed == GameConfiguration.GAME_SPEED / 2) {
				speed = GameConfiguration.GAME_SPEED / 5;
				control.setBtnVitesseText("Vitesse: x5");
			} else if (speed == GameConfiguration.GAME_SPEED / 5) {
				speed = GameConfiguration.GAME_SPEED / 10;
				control.setBtnVitesseText("Vitesse: x10");
			} else {
				speed = GameConfiguration.GAME_SPEED;
				control.setBtnVitesseText("Vitesse: x1");
			}
		}
	}

	private class EvenementAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String choix = macro.getEvenementSelectionne();
			if (choix != null && !choix.equals("Declencher un Evenement")) {
				MobileElementManager mem = (MobileElementManager) manager;
				int nbAffectes = GestionnaireEvenements.declencherEvenement(choix, manager.getHabitants(), mem.getForceInfluence());
				evenementDashboard.afficherEvenement(choix, nbAffectes);
			}
		}
	}

	private class MouseControls implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			// Conversion des pixels en coordonnées de la Grille
			Block position = dashboard.getBlockAt(e.getX(), e.getY());
			Habitant habitant = manager.getHabitant(position.getLine(), position.getColumn());

			if (habitant != null) {
				// Ouvre la fenêtre modale d'inspection
				new InspectionCitoyenModal(instance, habitant);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}

	private class InformationAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MobileElementManager mem = (MobileElementManager) manager;
			mem.lancerInformation(
					macro.getTheme(),
					macro.getVirulence(),
					macro.getVeracite()
			);
			// Affiche le flash info en haut de l'écran
			control.afficherFlashInfo(macro.getTheme());
		}
	}

	/**
	 * Ouvre le dashboard récapitulatif psychologique.
	 */
	private class RecapitulatifAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MobileElementManager mem = (MobileElementManager) manager;
			new RecapitulatifDashboard(instance, mem);
		}
	}
}