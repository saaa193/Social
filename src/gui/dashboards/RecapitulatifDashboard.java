package gui.dashboards;

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
 * RecapitulatifDashboard : Fenêtre modale affichant le graphique
 * d'évolution psychologique de la population jour par jour.
 * Affiche 3 courbes : Moral, Névrosisme et Agréabilité.
 * Ancré dans le Big Five : montre comment la psychologie collective
 * évolue au fil du temps selon les interactions et événements.
 */
public class RecapitulatifDashboard extends JDialog {

    private static final long serialVersionUID = 1L;

    public RecapitulatifDashboard(JFrame parent, MobileElementManager manager) {
        super(parent, "Récapitulatif Psychologique", true);
        construire(manager);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Construit le graphique à partir de l'historique du manager.
     */
    private void construire(MobileElementManager manager) {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        List<String> jours = manager.getHistoriqueJours();
        List<Double> nevrosisme = manager.getHistoriqueNevrosisme();
        List<Double> agreabilite = manager.getHistoriqueAgreabilite();
        List<Double> moral = manager.getHistoriqueMoral();

        // Construction du dataset JFreeChart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < jours.size(); i++) {
            String jour = jours.get(i);
            dataset.addValue(moral.get(i), "Moral", jour);
            dataset.addValue(nevrosisme.get(i), "Névrosisme", jour);
            dataset.addValue(agreabilite.get(i), "Agréabilité", jour);
        }

        // Création du graphique en courbes
        JFreeChart chart = ChartFactory.createLineChart(
                "Évolution Psychologique de la Population",
                "Jour",
                "Valeur moyenne (0-100)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Couleurs des courbes
        org.jfree.chart.plot.CategoryPlot plot =
                (org.jfree.chart.plot.CategoryPlot) chart.getPlot();
        org.jfree.chart.renderer.category.LineAndShapeRenderer renderer =
                new org.jfree.chart.renderer.category.LineAndShapeRenderer();

        // Moral → violet
        renderer.setSeriesPaint(0, new Color(128, 0, 128));
        // Névrosisme → rouge
        renderer.setSeriesPaint(1, Color.RED);
        // Agréabilité → vert
        renderer.setSeriesPaint(2, new Color(0, 150, 0));

        plot.setRenderer(renderer);
        chart.setBackgroundPaint(Color.WHITE);

        // Ajout du graphique dans la fenêtre
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(700, 400));
        add(chartPanel, BorderLayout.CENTER);

        // Message si pas encore de données
        if (jours.isEmpty()) {
            JLabel msg = new JLabel("Aucune donnée — lancez la simulation au moins un jour.", JLabel.CENTER);
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

        setMinimumSize(new Dimension(750, 500));
    }

    /**
     * Ferme la fenêtre modale.
     */
    private class FermerAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
}