package main;

import javax.swing.*;

public class Main {

        public static void main(String[] args) {
                JFrame window = new JFrame();
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close button
                window.setResizable(false); // Not Resizable
                window.setTitle("Java 2D Game"); // Screen tittle

                GamePanel gamePanel = new GamePanel();
                window.add(gamePanel);

                window.pack(); // causes the window to  be sized to fit the preferred size and layouts of its subcomponents (=GamePanel)

                window.setLocationRelativeTo(null); // the screen is going to appear in the middle of the screen
                window.setVisible(true); // now we can see

                gamePanel.setupGame(); // call this method before starting the game
                gamePanel.startGameThread(); // Start the game!
        }
}
