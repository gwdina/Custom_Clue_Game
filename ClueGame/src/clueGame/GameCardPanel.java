package clueGame;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class GameCardPanel extends JPanel {
	JPanel peoplePanel, roomPanel, weaponPanel;
	Color color;
	Board board;
	private Map<Card, Color> seenCardsText = new HashMap<Card, Color>();
	private Color[] otherPlayers = {new Color(255, 108, 108), Color.white, new Color(255, 255, 63), 
			new Color(116, 255, 101), new Color(255, 165, 86),  Color.lightGray};
	// player colors: red, white, yellow, green, orange, light gray.  

	public GameCardPanel() {
		setLayout(new GridLayout(3,1)); // create grid for main panel
		setBorder(new TitledBorder (new EtchedBorder(), "Known Cards")); //create border and title for panel
		board = Board.getInstance();
		// create each type card panels
		peoplePanel = new JPanel();
		roomPanel = new JPanel();
		weaponPanel = new JPanel();

		// title, border and grid for panels
		peoplePanel.setBorder(new TitledBorder (new EtchedBorder(), "People")); 
		peoplePanel.setLayout(new GridLayout(0, 1)); // create grid for people panel

		roomPanel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		roomPanel.setLayout(new GridLayout(0, 1));

		weaponPanel.setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));
		weaponPanel.setLayout(new GridLayout(0, 1));  

		Player human = Board.getInstance().getPlayer().get(0);

		updatePanels(human);
	}

	public void updatePanels(Player human) {
		// creates the people panel
		updatePanel(peoplePanel, human);
		// creates the room panel
		updatePanel(roomPanel, human);
		// creates the weapon panel
		updatePanel(weaponPanel, human);
		revalidate(); // refreshes card panel
	}

	private void updatePanel(JPanel panel, Player human) {
		CardType typeOfCard = null;
		// determines what card to look for from the type of panel
		if (panel == peoplePanel) typeOfCard = CardType.PERSON;
		else if (panel == roomPanel) typeOfCard = CardType.ROOM;
		else if (panel == weaponPanel) typeOfCard = CardType.WEAPON;

		panel.removeAll();
		// add the fields to go into the panel using the updated seen card data
		add(panel);   // causes swing to either add or read the entire panel and recalculate it

		JLabel handLabel = new JLabel("In Hand:"); // create hand label
		JLabel seenLabel = new JLabel("Seen:"); // create seen label

		panel.add(handLabel); // needs to be added first before adding in the text fields

		boolean contains = false; // used to check if hand/seen contains the type, if not "none" will be used
		// goes over each card in the players hand
		contains = getHand(panel, human, typeOfCard, contains);
		// only works if there is no person card type in players hand
		noneInHand(panel, contains);
		panel.add(seenLabel); // needs to be added first before adding in the text fields
		contains = false;

		Color disprovePlayerColor = null;

		for(Player k: board.getPlayer()) {
			// goes over each card seen by the human player
			for (Card s: human.getSeenCards()) {
				JTextField seenText = new JTextField(); // create seen text field
				// adds seen person cards to panel if card is found
				if ((k.getPlayerCards().contains(s)) && (s.getCardType() == typeOfCard)) {
					seenText.setText(s.getCardName()); // text for the panel
					// colourizes the seen card to another player color
					disprovePlayerColor = k.getColor();
					if (!seenCardsText.containsKey(s)) {
						seenCardsText.put(s, color);
					}

					seenText.setBackground(seenCardsText.get(s));
					panel.add(seenText); // adds text boxes to panel
					//panel.repaint();
					contains = true;
				}
			}
			noneInHand(panel, contains);
		}
	}

	private void noneInHand(JPanel panel, boolean contains) {
		if (!contains) {
			JTextField noneInHand = new JTextField();
			noneInHand.setText("None"); // text for the panel
			noneInHand.setEditable(false);
			panel.add(noneInHand); // add text box to panel
		}
	}

	private boolean getHand(JPanel panel, Player human, CardType typeOfCard, boolean contains) {
		for (int i = 0; i < human.getPlayerCards().size(); i++) {
			JTextField handText = new JTextField(); //create hand text field
			handText.setEditable(false);

			// adds hand person cards to panel if card is found
			if (human.getPlayerCards().get(i).getCardType() == typeOfCard) {
				handText.setText(human.getPlayerCards().get(i).getCardName()); // text for the panel
				handText.setBackground(human.getColor()); // text box colors by players set color
				panel.add(handText); // adds text boxes to panel
				contains = true;
			}	
		}
		return contains;
	}
	public void setColor(Color color) {
		this.color = color;
	}
}
