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
 *
 * GraphDashboard : Visualisation statistique de la population.
 * Utilise deux graphiques JFreeChart (comme vu en cours) :
 * 1. PieChart → répartition des états psychologiques réels
 * 2. BarChart horizontal → moyennes des besoins vitaux
 */
public class GraphDashboard extends JPanel {
	private static final long serialVersionUID = 1L;

	private DefaultPieDataset datasetEtats;
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
		datasetEtats.setValue("Décès",      0);
		datasetEtats.setValue("Sommeil",    0);
		datasetEtats.setValue("Épanoui",    0);
		datasetEtats.setValue("Euphorique", 0);
		datasetEtats.setValue("Stable",     0);
		datasetEtats.setValue("Anxieux",    0);
		datasetEtats.setValue("Isolé",      0);
		datasetEtats.setValue("Dépressif",  0);
		datasetEtats.setValue("Burnout",    0);

		JFreeChart pieChart = ChartFactory.createPieChart(
				"États", datasetEtats, false, true, false
		);

		org.jfree.chart.plot.PiePlot plot =
				(org.jfree.chart.plot.PiePlot) pieChart.getPlot();

		// Couleurs par état — cohérentes avec CouleurVisitor
		plot.setSectionPaint("Décès",      Color.BLACK);
		plot.setSectionPaint("Sommeil",    Color.GRAY);
		plot.setSectionPaint("Épanoui",    new Color(128, 0, 128));
		plot.setSectionPaint("Euphorique", new Color(200, 0, 200));
		plot.setSectionPaint("Stable",     Color.ORANGE);
		plot.setSectionPaint("Anxieux",    new Color(255, 140, 0));
		plot.setSectionPaint("Isolé",      new Color(100, 100, 150));
		plot.setSectionPaint("Dépressif",  Color.RED);
		plot.setSectionPaint("Burnout",    new Color(80, 0, 0));

		// Labels directement sur les parts — plus besoin de légende
		plot.setLabelGenerator(
				new org.jfree.chart.labels.StandardPieSectionLabelGenerator("{0}")
		);

		// Masquer les sections à 0 pour ne pas polluer le camembert
		plot.setIgnoreZeroValues(true);

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

		JFreeChart barChart = ChartFactory.createBarChart(
				"Moyennes des besoins",
				"Besoin",
				"Valeur (0-100)",
				datasetMoyennes,
				PlotOrientation.HORIZONTAL,
				false, true, false
		);

		barChart.setBackgroundPaint(new Color(245, 245, 245));
		barChart.setBorderVisible(false);

		org.jfree.chart.plot.CategoryPlot barPlot =
				(org.jfree.chart.plot.CategoryPlot) barChart.getPlot();
		org.jfree.chart.renderer.category.BarRenderer renderer =
				(org.jfree.chart.renderer.category.BarRenderer) barPlot.getRenderer();
		renderer.setSeriesPaint(0, new Color(100, 100, 255));
		barPlot.getRangeAxis().setRange(0, 100);

		ChartPanel barPanel = new ChartPanel(barChart);
		barPanel.setPreferredSize(new Dimension(GameConfiguration.MENU_RIGHT_WIDTH, 200));
		barPanel.setBackground(new Color(245, 245, 245));
		add(barPanel, BorderLayout.CENTER);
	}

	public void updateStats(List<Habitant> habitants) {
		if (habitants == null || habitants.isEmpty()) return;

		int deces = 0, sommeil = 0, epanoui = 0, euphorique = 0;
		int stable = 0, anxieux = 0, isole = 0, depressif = 0, burnout = 0;
		int totalFaim = 0, totalFatigue = 0,
				totalSocial = 0, totalSante = 0, totalMoral = 0;

		for (Habitant h : habitants) {
			engine.habitant.besoin.Besoins b = h.getBesoins();

			if (b.getSante() <= 0) {
				deces++;
			} else if (b.getFatigue() < 20) {
				sommeil++;
			} else {
				engine.habitant.etat.EtatHabitant etat =
						h.getPsychologie().determinerEtat(b);

				if (etat instanceof engine.habitant.etat.EtatEpanoui)        epanoui++;
				else if (etat instanceof engine.habitant.etat.EtatEuphorique) euphorique++;
				else if (etat instanceof engine.habitant.etat.EtatStable)     stable++;
				else if (etat instanceof engine.habitant.etat.EtatAnxieux)    anxieux++;
				else if (etat instanceof engine.habitant.etat.EtatIsole)      isole++;
				else if (etat instanceof engine.habitant.etat.EtatDepressif)  depressif++;
				else if (etat instanceof engine.habitant.etat.EtatBurnout)    burnout++;
			}

			totalFaim    += b.getFaim();
			totalFatigue += b.getFatigue();
			totalSocial  += b.getSocial();
			totalSante   += b.getSante();
			totalMoral   += b.getMoral();
		}

		datasetEtats.setValue("Décès",      deces);
		datasetEtats.setValue("Sommeil",    sommeil);
		datasetEtats.setValue("Épanoui",    epanoui);
		datasetEtats.setValue("Euphorique", euphorique);
		datasetEtats.setValue("Stable",     stable);
		datasetEtats.setValue("Anxieux",    anxieux);
		datasetEtats.setValue("Isolé",      isole);
		datasetEtats.setValue("Dépressif",  depressif);
		datasetEtats.setValue("Burnout",    burnout);

		int taille = habitants.size();
		datasetMoyennes.setValue(totalFaim    / taille, "Moyenne", "Faim");
		datasetMoyennes.setValue(totalFatigue / taille, "Moyenne", "Fatigue");
		datasetMoyennes.setValue(totalSocial  / taille, "Moyenne", "Social");
		datasetMoyennes.setValue(totalSante   / taille, "Moyenne", "Santé");
		datasetMoyennes.setValue(totalMoral   / taille, "Moyenne", "Moral");
	}
}