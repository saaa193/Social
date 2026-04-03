package test;

import gui.MainGUI;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 */
public class TestGame {
	public static void main(String[] args) {

		MainGUI gameMainGUI = new MainGUI("Social game");

		Thread gameThread = new Thread(gameMainGUI);
		gameThread.start();
	}
}