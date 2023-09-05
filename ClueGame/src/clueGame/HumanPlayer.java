package clueGame;

import java.awt.Color;
import java.util.ArrayList;

//goes first
public class HumanPlayer extends Player {

	public HumanPlayer(String name, String color, int row, int col) {
		super(name, color, row, col);
	}

	@Override
	protected Solution createSuggestion(Card room, ArrayList<Card> personDeck, ArrayList<Card> weaponDeck) {
		return null;
	}

	@Override
	protected BoardCell selectTarget(int dieRoll) {
		return null;
	}
}
