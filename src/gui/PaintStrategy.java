package gui;

import java.awt.Graphics;
import engine.habitant.Habitant;
import engine.map.Map;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Interface Strategy pour le rendu visuel — calquée sur ColorStrategy du prof.
 * Définit le contrat de dessin sans imposer une implémentation.
 * Ainsi, on peut changer le rendu sans toucher à GameDisplay.
 */
public interface PaintStrategy {

    void paint(Map map, Graphics graphics);

    void paint(Habitant habitant, Graphics graphics);

    void setFiltres(boolean famille, boolean travail, boolean amis);
}