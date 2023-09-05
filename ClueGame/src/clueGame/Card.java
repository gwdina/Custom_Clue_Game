package clueGame;
import clueGame.*;

public class Card {
	private String cardName; 
	private CardType cardType;

	public Card(String cardName, CardType cardType) {
		super();
		this.cardName = cardName;
		this.cardType = cardType;
	}
	
	public String getCardName() { return cardName; }
	public CardType getCardType() { return cardType; }

	public boolean equals(Card target) {
		if (target.getCardName() == cardName) return true; 
		return false;
	}
}

