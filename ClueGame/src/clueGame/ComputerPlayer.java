package clueGame;

import java.awt.Color;
import java.util.*;


public class ComputerPlayer extends Player {
	private Board board = Board.getInstance();

	public ComputerPlayer(String name, String color, int row, int col) {
		super(name, color, row, col);
		for(Card i: getPlayerCards()) {
			updateSeen(i);
		}
	}

	//Given a room, the computer player will create a suggestion composed of that room, 
	//a weapon and player from those cards the computer player has not seen.
	public Solution createSuggestion(Card room, ArrayList<Card> personDeck, ArrayList<Card> weaponDeck) { 
		Card person = null, weapon = null;
		BoardCell cell = board.getCell(super.getRow(), super.getCol());

		if (!(cell.getWalkway() || cell.getUnused())) {
			//will randomly grab a person card not in seen cards
			person = getCard(personDeck, person);
			//will randomly grab a weapon card not in seen cards
			weapon = getCard(weaponDeck, weapon);
			return new Solution(room, person, weapon); 
		}
		return null;
	}

	private Card getCard(ArrayList<Card> personDeck, Card person) {
		while (getSeenCards().contains(person) || person == null) {
			Random randPerson = new Random();
			int randomIndexPerson = randPerson.nextInt(personDeck.size());
			person = personDeck.get(randomIndexPerson);
		}
		return person;
	}

	// This is another simplified (i.e. read brain dead) AI, in which the computer 
	//player selects that location he or she wishes to move to from the target list. 
	public BoardCell selectTarget(int movement) { 
		board.calcTargets(board.getCell(super.getRow(), super.getCol()), movement);
		Set<BoardCell> targets= board.getTargets();
		if (targets.size() == 0) return null;

		for (BoardCell location: targets) {
			//if room and not seen, then go to location
			if(!(location.getWalkway() || location.getUnused()) && !getSeenCards().contains(location)) {
				targets.clear();
				return location;
			}
		}

		//if no room can be found then a random target will be chosen
		BoardCell[] randomTarget = targets.toArray(new BoardCell[0]);
		Random randLocation = new Random();
		int randomIndexLocation = randLocation.nextInt(randomTarget.length);
		targets.clear();
		return randomTarget[randomIndexLocation];
	}
}

