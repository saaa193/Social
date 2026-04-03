package gui.dashboards;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.habitant.besoin.Besoins;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 * <p>
 * GraphDashboard : Visualisation statistique de la population.
 * Utilise deux graphiques JFreeChart (comme vu en cours) :
 * 1. PieChart → répartition des états (Bonheur, Détresse, etc.)
 * 2. BarChart horizontal → moyennes des besoins vitaux de la population
 */
public class GraphDashboard extends JPanel {
	private static final long serialVersionUID = 1L;

	// Dataset du camembert (états)
	private DefaultPieDataset datasetEtats;

	// Dataset du bar chart horizontal (moyennes des besoins)
	private DefaultCategoryDataset datasetMoyennes;

	public GraphDashboard() {
		setLayout(new BorderLayout(0, 5));
		setPreferredSize(new Dimension(GameConfiguration.MENU_RIGHT_WIDTH, 420));
		setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				"Analyse de la population"
		));
		setBackground(new Color(245, 245, 245));

		// 1. CAMEMBERT — répartition des états
		datasetEtats = new DefaultPieDataset();
		datasetEtats.setValue("Décès", 0);
		datasetEtats.setValue("Sommeil", 0);
		datasetEtats.setValue("Détresse", 0);
		datasetEtats.setValue("Neutre", 0);
		datasetEtats.setValue("Bonheur", 0);

		JFreeChart pieChart = ChartFactory.createPieChart(
				"États", datasetEtats, true, true, false
		);
		org.jfree.chart.plot.PiePlot plot =
				(org.jfree.chart.plot.PiePlot) pieChart.getPlot();

		plot.setSectionPaint("Décès", Color.BLACK);
		plot.setSectionPaint("Sommeil", Color.GRAY);
		plot.setSectionPaint("Détresse", Color.RED);
		plot.setSectionPaint("Neutre", Color.ORANGE);
		plot.setSectionPaint("Bonheur", new Color(128, 0, 128));
		pieChart.setBackgroundPaint(new Color(245, 245, 245));
		pieChart.setBorderVisible(false);

		ChartPanel piePanel = new ChartPanel(pieChart);
		piePanel.setPreferredSize(new Dimension(GameConfiguration.MENU_RIGHT_WIDTH, 200));
		piePanel.setBackground(new Color(245, 245, 245));
		add(piePanel, BorderLayout.NORTH);

		// 2. BAR CHART HORIZONTAL — moyennes des besoins
		datasetMoyennes = new DefaultCategoryDataset();
		datasetMoyennes.setValue(0, "Moyenne", "Faim");
		datasetMoyennes.setValue(0, "Moyenne", "Fatigue");
		datasetMoyennes.setValue(0, "Moyenne", "Social");
		datasetMoyennes.setValue(0, "Moyenne", "Santé");
		datasetMoyennes.setValue(0, "Moyenne", "Moral");

		// PlotOrientation.HORIZONTAL = barres horizontales
		JFreeChart barChart = ChartFactory.createBarChart(
				"Moyennes des besoins",  // titre
				"Besoin",                // axe catégories
				"Valeur (0-100)",        // axe valeurs
				datasetMoyennes,
				PlotOrientation.HORIZONTAL,
				false,
				true,
				false
		);

		barChart.setBackgroundPaint(new Color(245, 245, 245));
		barChart.setBorderVisible(false);

		org.jfree.chart.plot.CategoryPlot barPlot =
				(org.jfree.chart.plot.CategoryPlot) barChart.getPlot();
		org.jfree.chart.renderer.category.BarRenderer renderer =
				(org.jfree.chart.renderer.category.BarRenderer) barPlot.getRenderer();

		renderer.setSeriesPaint(0, new Color(100, 100, 255));

		// Axe des valeurs limité à 0-100
		barPlot.getRangeAxis().setRange(0, 100);

		ChartPanel barPanel = new ChartPanel(barChart);
		barPanel.setPreferredSize(
				new Dimension(GameConfiguration.MENU_RIGHT_WIDTH, 200)
		);
		barPanel.setBackground(new Color(245, 245, 245));
		add(barPanel, BorderLayout.CENTER);
	}

	/**
	 * Mise à jour des deux graphiques à chaque tour.
	 * Appelée depuis MainGUI.run() → graph.updateStats(habitants)
	 */
	public void updateStats(List<Habitant> habitants) {
		if (habitants == null || habitants.isEmpty()) return;

		//Comptage pour le camembert
		int deces = 0, sommeil = 0, detresse = 0, neutre = 0, bonheur = 0;

		//Totaux pour le bar chart
		int totalFaim = 0, totalFatigue = 0,
				totalSocial = 0, totalSante = 0, totalMoral = 0;

		for (Habitant h : habitants) {
			Besoins b = h.getBesoins();

			// Camembert
			if (b.getSante() <= 0) deces++;
			else if (b.getFatigue() < 20) sommeil++;
			else if (h.getMoral() < 30) detresse++;
			else if (h.getMoral() < 70) neutre++;
			else bonheur++;

			// Bar chart — on accumule les totaux
			totalFaim += b.getFaim();
			totalFatigue += b.getFatigue();
			totalSocial += b.getSocial();
			totalSante += b.getSante();
			totalMoral += b.getMoral();
		}

		// Mise à jour camembert
		datasetEtats.setValue("Décès", deces);
		datasetEtats.setValue("Sommeil", sommeil);
		datasetEtats.setValue("Détresse", detresse);
		datasetEtats.setValue("Neutre", neutre);
		datasetEtats.setValue("Bonheur", bonheur);

		// Mise à jour bar chart — moyennes calculées
		int taille = habitants.size();
		datasetMoyennes.setValue(totalFaim / taille, "Moyenne", "Faim");
		datasetMoyennes.setValue(totalFatigue / taille, "Moyenne", "Fatigue");
		datasetMoyennes.setValue(totalSocial / taille, "Moyenne", "Social");
		datasetMoyennes.setValue(totalSante / taille, "Moyenne", "Santé");
		datasetMoyennes.setValue(totalMoral / taille, "Moyenne", "Moral");
	}
}