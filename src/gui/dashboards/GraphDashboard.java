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

/**
 * Classe GraphDashboard : Composant UI responsable de la visualisation statistique.
 * Elle utilise JFreeChart pour afficher la répartition de l'état de la population en temps réel.
 */
public class GraphDashboard extends JPanel {
    private static final long serialVersionUID = 1L;

    // Le dataset est le "modèle" du graphique. Quand on le modifie, le graphique se met à jour.
    private DefaultPieDataset datasetEtats;

    public GraphDashboard() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(GameConfiguration.MENU_RIGHT_WIDTH, 250));

        // Design : une bordure titrée pour rendre l'interface plus professionnelle
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Macro-Analyse : État de la population"
        ));
        setBackground(new Color(245, 245, 245));

        // Initialisation : on crée le dataset avec des valeurs à 0.
        datasetEtats = new DefaultPieDataset();
        datasetEtats.setValue("Décès (Noir)", 0);
        datasetEtats.setValue("Sommeil (Gris)", 0);
        datasetEtats.setValue("Détresse (Rouge)", 0);
        datasetEtats.setValue("Neutre (Orange)", 0);
        datasetEtats.setValue("Bonheur (Violet)", 0);

        // Création du graphique PieChart (Camembert)
        JFreeChart pieChart = ChartFactory.createPieChart("", datasetEtats, true, true, false);
        org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) pieChart.getPlot();

        // Application du code couleur pour assurer la cohérence visuelle avec le reste du jeu (PaintStrategy)
        plot.setSectionPaint("Décès (Noir)", Color.BLACK);
        plot.setSectionPaint("Sommeil (Gris)", Color.GRAY);
        plot.setSectionPaint("Détresse (Rouge)", Color.RED);
        plot.setSectionPaint("Neutre (Orange)", Color.ORANGE);
        plot.setSectionPaint("Bonheur (Violet)", new Color(128, 0, 128));

        pieChart.setBackgroundPaint(new Color(245, 245, 245));
        pieChart.setBorderVisible(false);

        // Intégration du graphique dans un conteneur Swing
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setBackground(new Color(245, 245, 245));
        add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * Méthode de rafraîchissement.
     * Elle parcourt la liste des habitants, applique la logique de classification,
     * et met à jour le dataset pour refléter les nouvelles statistiques.
     */
    public void updateStats(List<Habitant> habitants) {
        int deces = 0, sommeil = 0, detresse = 0, neutre = 0, bonheur = 0;

        // On itère sur la population pour catégoriser chaque habitant.
        // C'est ici qu'on fait le lien entre les valeurs brutes et l'affichage.
        for (Habitant h : habitants) {
            Besoins b = h.getBesoins();
            int sante = b.getSante();
            int fatigue = b.getFatigue();
            int moral = h.getMoral();

            // Logique de classification (le "Business Rules Engine")
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

        // Mise à jour du dataset : JFreeChart détecte le changement et redessine le graphique.
        datasetEtats.setValue("Décès (Noir)", deces);
        datasetEtats.setValue("Sommeil (Gris)", sommeil);
        datasetEtats.setValue("Détresse (Rouge)", detresse);
        datasetEtats.setValue("Neutre (Orange)", neutre);
        datasetEtats.setValue("Bonheur (Violet)", bonheur);
    }
}