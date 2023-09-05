package clueGame;

//who did it
public class Solution {
	private Card room;
	private Card person;
	private Card weapon;
	
	public Solution(Card room, Card person, Card weapon) {
		super();
		this.room = room;
		this.person = person;
		this.weapon = weapon;
	}
	
	public Card getRoom() { return this.room; }
	public Card getPerson() { return this.person; }
	public Card getWeapon() { return this.weapon; }
}
