package clueGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.*;

public class Accusation extends JDialog{
	JComboBox<String> roomBox, personBox, weaponBox;
	JLabel roomLabel, personLabel, weaponLabel;
	JButton submitButton, cancelButton;
	Board board;
	
	public Accusation() {
		setTitle("Make an Accusation");
		setSize(260, 150);
		setLayout(new GridLayout(4, 2));
		board = Board.getInstance();
		
		roomLabel = new JLabel("Room");
		personLabel = new JLabel("Person");
		weaponLabel = new JLabel("Weapon");
		
		roomBox = new JComboBox<String>();
		personBox = new JComboBox<String>();
		weaponBox = new JComboBox<String>();
		
		submitButton = new JButton("Submit");
		cancelButton = new JButton("Cancel");
		
		addRoomCard();
		addPersonCard();
		addWeaponCard();
		
		add(roomLabel);
		add(roomBox);
		add(personLabel);
		add(personBox);
		add(weaponLabel);
		add(weaponBox);
		
		cancelButton.addActionListener(e -> setVisible(false));
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean accusation = board.checkAccusation(board.getCard(roomBox.getSelectedItem().toString(), CardType.ROOM), 
						board.getCard(personBox.getSelectedItem().toString(), CardType.PERSON), 
						board.getCard(weaponBox.getSelectedItem().toString(), CardType.WEAPON));
				if (accusation == true) {
					JOptionPane.showMessageDialog(null, "Correct. You win!", "Game end", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}
				else {
					JOptionPane.showMessageDialog(null, "WRONG. You lose.", "Game end", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
		});
		add(submitButton);
		add(cancelButton);
	}

	private void addWeaponCard() {
		for (int i = 0; i < board.getWeaponDeck().size(); i++) {
			weaponBox.addItem(board.getWeaponDeck().get(i).getCardName());
		}
	}

	private void addPersonCard() {
		for (int i = 0; i < board.getPersonDeck().size(); i++) {
			personBox.addItem(board.getPersonDeck().get(i).getCardName());
		}
	}

	private void addRoomCard() {
		for (int i = 0; i < board.getRoomDeck().size(); i++) {
			roomBox.addItem(board.getRoomDeck().get(i).getCardName());
		}
	}
}
