package clueGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Suggestion extends JDialog{
	JComboBox<String> personBox, weaponBox;
	JTextField roomBox;
	JLabel roomLabel, personLabel, weaponLabel;
	JButton submitButton, cancelButton;
	Board board;
	GameControlPanel controlPanel;
	GameCardPanel cardPanel;
	Player player;
	String room, person, weapon;
	
	public Suggestion() {
		setTitle("Make a Suggestion");
		setSize(260, 150);
		setLayout(new GridLayout(4, 2));
		board = Board.getInstance();
		player = board.getPlayer().get(0);
		
		roomLabel = new JLabel("Room");
		personLabel = new JLabel("Person");
		weaponLabel = new JLabel("Weapon");
		
		roomBox = new JTextField();
		personBox = new JComboBox<String>();
		weaponBox = new JComboBox<String>();
		
		submitButton = new JButton("Submit");
		cancelButton = new JButton("Cancel");
		
		roomBox.setEditable(false);
		
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
			public void actionPerformed(ActionEvent e)
			{
				controlPanel.setGuess(roomBox.getText() + ", " + personBox.getSelectedItem().toString() + 
						", " + weaponBox.getSelectedItem().toString());
				
				Card roomCard = board.getCard(roomBox.getText(), CardType.ROOM);
				Card personCard = board.getCard(personBox.getSelectedItem().toString(), CardType.PERSON);
				Card weaponCard = board.getCard(weaponBox.getSelectedItem().toString(), CardType.WEAPON);
				
				Solution suggestion = new Solution(roomCard, personCard, weaponCard);
				notPlayer(suggestion);
				Card result = board.handleSuggestion(player, suggestion); // seeing of suggestion holds true
				setResult(result); 
				setVisible(false);
			}

			private void notPlayer(Solution suggestion) {
				for(Player k: board.getPlayer()) {
					// fix issue if player suggests themselves
					if(k.getName() == suggestion.getPerson().getCardName()) {
						k.updateRow(player.getRow());
						k.updateCol(player.getCol());
						k.setDragged(true);
					}
				}
			}

			private void setResult(Card result) {
				if(result != null) {
					player.updateSeen(result); // add card shown to the computer players seen card set for future use

					Color disprovePlayerColor = null; // for setting the background color for GuessResult
					
					// looks at each player's hand to see which one disproved the suggestion in order to get their color
					for(Player k: board.getPlayer()) {
						if(k.getPlayerCards().contains(result)) disprovePlayerColor = k.getColor();
						cardPanel.setColor(disprovePlayerColor);
					}
					controlPanel.setGuessResult(result.getCardName(), disprovePlayerColor);
					cardPanel.updatePanels(player);
				}
				else controlPanel.setGuessResult("Suggestion was not disproven", null);
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
		roomBox.setText(board.getRoomCard().getCardName());
	}
	
	public void setControlPanel(GameControlPanel controlPanel) { 
		this.controlPanel = controlPanel;
	}
	
	public void setCardPanel(GameCardPanel cardPanel) { 
		this.cardPanel = cardPanel;
	}
}
