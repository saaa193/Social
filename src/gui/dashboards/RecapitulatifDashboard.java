package gui.dashboards;

import engine.analyse.AnalyseurPopulation;
import engine.analyse.IndicateurMacro;
import engine.process.MobileElementManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * RecapitulatifDashboard : Fenêtre modale affichant deux analyses :
 * 1. L'évolution psychologique jour par jour (courbes)
 * 2. Les indicateurs macroscopiques actuels (barres)
 *    calculés via AnalyseurPopulation — pattern Strategy.
 */
public class RecapitulatifDashboard extends JDialog {

	private static final long serialVersionUID = 1L;

	public RecapitulatifDashboard(JFrame parent, MobileElementManager manager) {
		super(parent, "Récapitulatif de la Simulation", true);
		construire(manager);
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	private void construire(MobileElementManager manager) {
		setLayout(new BorderLayout());
		getContentPane().setBackground(Color.WHITE);

		// Panel principal avec deux graphiques empilés
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
		panelPrincipal.setBackground(Color.WHITE);

		// --- GRAPHIQUE 1 : Évolution psychologique ---
		panelPrincipal.add(construireGraphiquePsycho(manager));

		// --- GRAPHIQUE 2 : Indicateurs macroscopiques ---
		panelPrincipal.add(construireGraphiqueIndicateurs(manager));

		add(panelPrincipal, BorderLayout.CENTER);

		// Message si pas encore de données
		if (manager.getHistoriqueJours().isEmpty()) {
			JLabel msg = new JLabel(
					"Aucune donnée — lancez la simulation au moins un jour.",
					JLabel.CENTER
			);
			msg.setForeground(Color.GRAY);
			add(msg, BorderLayout.NORTH);
		}

		// Bouton fermer
		JButton btnFermer = new JButton("Fermer");
		btnFermer.addActionListener(new FermerAction());
		JPanel bas = new JPanel();
		bas.setBackground(Color.WHITE);
		bas.add(btnFermer);
		add(bas, BorderLayout.SOUTH);

		setMinimumSize(new Dimension(750, 800));
	}

	/**
	 * Construit le graphique d'évolution psychologique.
	 * Identique à l'original — aucun changement.
	 */
	private ChartPanel construireGraphiquePsycho(MobileElementManager manager) {
		List<String> jours      = manager.getHistoriqueJours();
		List<Double> nevrosisme = manager.getHistoriqueNevrosisme();
		List<Double> agreabilite = manager.getHistoriqueAgreabilite();
		List<Double> moral      = manager.getHistoriqueMoral();

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < jours.size(); i++) {
			String jour = jours.get(i);
			dataset.addValue(moral.get(i),       "Moral",       jour);
			dataset.addValue(nevrosisme.get(i),  "Névrosisme",  jour);
			dataset.addValue(agreabilite.get(i), "Agréabilité", jour);
		}

		JFreeChart chart = ChartFactory.createLineChart(
				"Évolution Psychologique de la Population",
				"Jour", "Valeur moyenne (0-100)",
				dataset, PlotOrientation.VERTICAL, true, true, false
		);

		org.jfree.chart.plot.CategoryPlot plot =
				(org.jfree.chart.plot.CategoryPlot) chart.getPlot();
		org.jfree.chart.renderer.category.LineAndShapeRenderer renderer =
				new org.jfree.chart.renderer.category.LineAndShapeRenderer();
		renderer.setSeriesPaint(0, new Color(128, 0, 128));
		renderer.setSeriesPaint(1, Color.RED);
		renderer.setSeriesPaint(2, new Color(0, 150, 0));
		plot.setRenderer(renderer);
		plot.getRangeAxis().setRange(0, 100);
		chart.setBackgroundPaint(Color.WHITE);

		ChartPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new Dimension(700, 350));
		return panel;
	}

	/**
	 * Construit le graphique des indicateurs macroscopiques.
	 * Utilise AnalyseurPopulation — pattern Strategy :
	 * chaque indicateur calcule sa propre valeur.
	 */
	private ChartPanel construireGraphiqueIndicateurs(MobileElementManager manager) {
		AnalyseurPopulation analyseur = new AnalyseurPopulation();
		List<IndicateurMacro> indicateurs = analyseur.getIndicateurs();
		List<Double> resultats = analyseur.calculerTous(manager.getHabitants());

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < indicateurs.size(); i++) {
			// Valeur entre 0 et 100 pour l'affichage
			double valeur = resultats.get(i) * 100.0;
			dataset.addValue(valeur, "Valeur", indicateurs.get(i).getNom());
		}

		JFreeChart chart = ChartFactory.createBarChart(
				"Indicateurs Macroscopiques",
				"Indicateur", "Valeur (0-100)",
				dataset, PlotOrientation.HORIZONTAL, false, true, false
		);

		org.jfree.chart.plot.CategoryPlot plot =
				(org.jfree.chart.plot.CategoryPlot) chart.getPlot();
		org.jfree.chart.renderer.category.BarRenderer renderer =
				(org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, new Color(50, 100, 200));
		plot.getRangeAxis().setRange(0, 100);
		chart.setBackgroundPaint(Color.WHITE);

		ChartPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new Dimension(700, 200));
		return panel;
	}

	private class FermerAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
}