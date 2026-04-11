package gui;

import java.awt.Color;
import java.awt.Graphics;
import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.habitant.etat.EtatHabitant;
import engine.habitant.lien.Familial;
import engine.habitant.lien.Liens;
import engine.habitant.lien.Professionnel;
import engine.habitant.visitor.CouleurVisitor;
import engine.map.Block;
import engine.map.Map;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Implémentation par défaut de PaintStrategy.
 * Calquée sur TriColoredStrategy du prof dans Tree V2.
 */
public class PaintStrategyDefaut implements PaintStrategy {

	private boolean afficherFamille = true;
	private boolean afficherTravail = true;
	private boolean afficherAmis = true;

	private static final Color COULEUR_INFORME = new Color(0, 0, 0);
	private static final Color COULEUR_INFORME_PHASE1 = new Color(0, 220, 220);
	private int toursDepuisPropagation = 0;

	@Override
	public void setFiltres(boolean famille, boolean travail, boolean amis) {
		this.afficherFamille = famille;
		this.afficherTravail = travail;
		this.afficherAmis = amis;
	}

	@Override
	public void setToursDepuisPropagation(int tours) {
		this.toursDepuisPropagation = tours;
	}

	@Override
	public void paint(Map map, Graphics graphics) {
		int blockSize = GameConfiguration.BLOCK_SIZE;
		Block[][] blocks = map.getBlocks();

		for (int lineIndex = 0; lineIndex < map.getLineCount(); lineIndex++) {
			for (int columnIndex = 0; columnIndex < map.getColumnCount(); columnIndex++) {
				Block block = blocks[lineIndex][columnIndex];
				graphics.setColor(Color.WHITE);
				graphics.fillRect(
						block.getColumn() * blockSize,
						block.getLine() * blockSize,
						blockSize, blockSize);
			}
		}
	}

	@Override
	public void paint(Habitant habitant, Graphics graphics) {
		Block position = habitant.getPosition();
		int blockSize = GameConfiguration.BLOCK_SIZE;

		int x1 = position.getColumn() * blockSize + blockSize / 2;
		int y1 = position.getLine() * blockSize + blockSize / 2;

		if (habitant.getRelation() != null) {
			for (Liens lien : habitant.getRelation()) {
				Habitant ami = lien.getPartenaire();

				if (lien instanceof Familial && !afficherFamille) continue;
				if (lien instanceof Professionnel && !afficherTravail) continue;
				if (!(lien instanceof Familial) && !(lien instanceof Professionnel)
						&& !afficherAmis) continue;

				if (lien instanceof Familial) {
					graphics.setColor(Color.PINK);
				} else if (lien instanceof Professionnel) {
					graphics.setColor(Color.BLUE);
				} else {
					graphics.setColor(Color.GREEN);
				}

				Block posAmi = ami.getPosition();
				int x2 = posAmi.getColumn() * blockSize + blockSize / 2;
				int y2 = posAmi.getLine() * blockSize + blockSize / 2;
				graphics.drawLine(x1, y1, x2, y2);
			}
		}

		int x = position.getColumn();
		int y = position.getLine();

		if (habitant.estInforme()) {
			graphics.setColor(COULEUR_INFORME);
			graphics.drawOval(
					x * blockSize - 5,
					y * blockSize - 5,
					blockSize + 10,
					blockSize + 10
			);
		}

		int sante = habitant.getBesoins().getSante();

		if (sante <= 0) {
			graphics.setColor(Color.BLACK);
		} else if (habitant.estInforme() && toursDepuisPropagation <= 120) {
			graphics.setColor(COULEUR_INFORME_PHASE1);
		} else {
			EtatHabitant etat = habitant.getPsychologie().determinerEtat(habitant.getBesoins());
			Color couleur = etat.accept(new CouleurVisitor());
			graphics.setColor(couleur);
		}

		graphics.fillOval(x * blockSize, y * blockSize, blockSize, blockSize);
		graphics.setColor(Color.BLACK);
		graphics.drawOval(x * blockSize, y * blockSize, blockSize, blockSize);
	}
}