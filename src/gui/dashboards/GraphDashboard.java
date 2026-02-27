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
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.plot.PiePlot;

import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.habitant.besoin.Besoins;

public class GraphDashboard extends JPanel {
    private static final long serialVersionUID = 1L;
    private DefaultPieDataset datasetEtats;

    public GraphDashboard() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(GameConfiguration.MENU_RIGHT_WIDTH, 250));

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Macro-Analyse : État de la population"
        ));
        setBackground(new Color(245, 245, 245));

        // On prépare les 5 catégories basées sur PaintStrategy
        datasetEtats = new DefaultPieDataset();
        datasetEtats.setValue("Décès (Noir)", 0);
        datasetEtats.setValue("Sommeil (Gris)", 0);
        datasetEtats.setValue("Détresse (Rouge)", 0);
        datasetEtats.setValue("Neutre (Orange)", 0);
        datasetEtats.setValue("Bonheur (Violet)", 0);

        JFreeChart pieChart = ChartFactory.createPieChart("", datasetEtats, true, true, false);
        org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) pieChart.getPlot();

        plot.setSectionPaint("Décès (Noir)", Color.BLACK);
        plot.setSectionPaint("Sommeil (Gris)", Color.GRAY);
        plot.setSectionPaint("Détresse (Rouge)", Color.RED);
        plot.setSectionPaint("Neutre (Orange)", Color.ORANGE);
        plot.setSectionPaint("Bonheur (Violet)", new Color(128, 0, 128));
        pieChart.setBackgroundPaint(new Color(245, 245, 245));
        pieChart.setBorderVisible(false);

        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setBackground(new Color(245, 245, 245));
        add(chartPanel, BorderLayout.CENTER);
    }

    public void updateStats(List<Habitant> habitants) {
        int deces = 0, sommeil = 0, detresse = 0, neutre = 0, bonheur = 0;

        for (Habitant h : habitants) {
            Besoins b = h.getBesoins();
            int sante = b.getSante();
            int fatigue = b.getFatigue();
            int moral = h.getMoral();

            // La même logique stricte que ton PaintStrategy !
            if (sante <= 0) {
                deces++;
            } else if (fatigue < 20) {
                sommeil++;
            } else if (moral < 30) {
                detresse++;
            } else if (moral < 70) {
                neutre++;
            } else {
                bonheur++;
            }
        }

        // Mise à jour du camembert
        datasetEtats.setValue("Décès (Noir)", deces);
        datasetEtats.setValue("Sommeil (Gris)", sommeil);
        datasetEtats.setValue("Détresse (Rouge)", detresse);
        datasetEtats.setValue("Neutre (Orange)", neutre);
        datasetEtats.setValue("Bonheur (Violet)", bonheur);
    }
}