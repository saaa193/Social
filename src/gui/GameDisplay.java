package gui;

import java.awt.Graphics;
import java.awt.Color;

import javax.swing.JPanel;

import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.map.Block;
import engine.map.Map;
import engine.process.MobileInterface;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * GameDisplay : Composant Swing responsable du rendu graphique.
 * Il utilise une stratégie de dessin (PaintStrategy) pour séparer la logique de rendu de la logique de contrôle.
 */
public class GameDisplay extends JPanel {
	private static final long serialVersionUID = 1L;

	private Map map;
	private MobileInterface manager;

	// Pattern Strategy : la logique de dessin est déléguée à cet objet
	PaintStrategy paintStrategy = new PaintStrategyDefaut();

	public GameDisplay(Map map, MobileInterface manager) {
		this.map = map;
		this.manager = manager;
	}

	/**
	 * Méthode utilitaire pour convertir les coordonnées écran (pixels)
	 * en coordonnées logiques (ligne/colonne de la carte).
	 */
	public Block getBlockAt(int x, int y) {
		int line = y / GameConfiguration.BLOCK_SIZE;
		int column = x / GameConfiguration.BLOCK_SIZE;
		return map.getBlock(line, column);
	}

	/**
	 * Méthode principale de rendu graphique.
	 * Appelle automatiquement le cycle de peinture de Swing.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		paintStrategy.paint(map, g);

		for (Habitant h : manager.getHabitants()) {
			paintStrategy.paint(h, g);
		}
	}

	/**
	 * Retourne la stratégie de dessin pour modifier les filtres.
	 */
	public PaintStrategy getPaintStrategy() {
		return paintStrategy;
	}
}