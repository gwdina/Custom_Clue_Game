package clueGame;

import java.awt.*;
import javax.swing.*;

import clueGame.Board.MovePlayerListener;

public class ClueGame extends JFrame {
	private ClueGame gui;
	GameControlPanel controlPanel;
	GameCardPanel cardPanel;
	Board board;

	public ClueGame() {
		gui = this;
		board = Board.getInstance();
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();

		controlPanel = new GameControlPanel();
		cardPanel = new GameCardPanel();
		board.setControlPanel(controlPanel);
		board.setCardPanel(cardPanel);
		setTitle("Clue Game - CSCI306"); //game title
		setSize(1100, 920);  // size the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		JOptionPane.showMessageDialog(null, "You are " + board.getPlayer().get(0).getName() 
				+ ". Can you find the solution before the Computer players?", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
		createLayout();
	}

	//draw each panel
	private void createLayout() {
		add(board, BorderLayout.CENTER);
		add(cardPanel, BorderLayout.EAST);
		add(controlPanel, BorderLayout.SOUTH);
	}

	//show game window
	public static void main(String[] args) {
		ClueGame gui = new ClueGame();  // create the panel
		gui.setVisible(true); // make it visible;
	}
}
