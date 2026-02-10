package test;

import gui.MainGUI;

public class TestGame {
    public static void main(String[] args) {

        MainGUI gameMainGUI = new MainGUI("Social game");

        Thread gameThread = new Thread(gameMainGUI);
        gameThread.start();
    }
}