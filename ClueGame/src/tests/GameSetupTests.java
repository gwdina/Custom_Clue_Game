package tests;

import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import java.util.*;
import clueGame.*;


/* ask about equals() method in Card class
ask if we are testing everything we need to */

public class GameSetupTests {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	private static Card livingroom, bedroom, kitchen, garden, diningroom, atrium, bathroom, office, pool,
		alCapone, kenichiShinoda, pabloEscobar, eddieMcGrath, benjaminSiegel, matteoDenaro,
		pistol, poison, katana, throwingStars, wire, golfClub;
	private static Player player1, player2, player3, player4, player5, player6;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();

		//room cards
		livingroom = new Card("Living Room", CardType.ROOM);
		bedroom = new Card("Bedroom", CardType.ROOM);
		kitchen = new Card("Kitchen", CardType.ROOM);
		garden = new Card("Garden", CardType.ROOM);
		diningroom = new Card("Dining Room", CardType.ROOM);
		atrium = new Card("Atrium", CardType.ROOM);
		bathroom = new Card("Bathroom", CardType.ROOM);
		office = new Card("Office", CardType.ROOM);
		pool = new Card("Pool", CardType.ROOM);
		
		//person cards
		alCapone = new Card("Al Capone", CardType.PERSON);
		kenichiShinoda = new Card("Kenichi Shinoda", CardType.PERSON);
		pabloEscobar = new Card("Pablo Escobar", CardType.PERSON);
		eddieMcGrath = new Card("Eddie McGrath", CardType.PERSON);
		benjaminSiegel = new Card("Benjamin Siegel", CardType.PERSON);
		matteoDenaro = new Card("Matteo Denaro", CardType.PERSON);
		
		//weapon cards
		pistol = new Card("Pistol", CardType.WEAPON);
		poison = new Card("Poison", CardType.WEAPON);
		katana = new Card("Katana", CardType.WEAPON);
		throwingStars = new Card("Throwing Stars", CardType.WEAPON);
		wire = new Card("Wire", CardType.WEAPON);
		golfClub = new Card("Golf Club", CardType.WEAPON);
		
		//player
		player1 = new HumanPlayer("Al Capone", "Cyan", 23, 10);
		player2 = new ComputerPlayer("Kenichi Shinoda", "Yellow", 24, 4);
		player3 = new ComputerPlayer("Pablo Escobar", "Blue", 23, 20);
		player4 = new ComputerPlayer("Eddie McGrath", "Green", 0, 10);
		player5 = new ComputerPlayer("Benjamin Siegel", "Magenta", 23, 29);
		player6 = new ComputerPlayer("Matteo Denaro", "Red", 8, 30);
	}
	
	@Test
	public void testCards() {
		assertEquals("Living Room", livingroom.getCardName());
		assertEquals(CardType.ROOM, atrium.getCardType());
		assertEquals("Pablo Escobar", pabloEscobar.getCardName());
		assertEquals(CardType.PERSON, alCapone.getCardType());
		assertEquals("Katana", katana.getCardName());
		assertEquals(CardType.WEAPON, wire.getCardType());
	}
	
	@Test
	public void testPlayers() {
		assertEquals("Pablo Escobar", player3.getName()); //test computer player
		assertEquals("Al Capone", player1.getName()); //test human player
	}
	
	@Test
	public void testSolutionCards() {
		//test that Answer has a card from each Card Type
		assertEquals(CardType.ROOM, board.getSolution().getRoom().getCardType());
		assertEquals(CardType.PERSON, board.getSolution().getPerson().getCardType());
		assertEquals(CardType.WEAPON, board.getSolution().getWeapon().getCardType());
	}
	
	@Test
	public void testPlayerCards() {
		//test that each player has 3 cards
		assertEquals(3, board.getPlayer().get(0).getPlayerCards().size());
		assertEquals(3, board.getPlayer().get(1).getPlayerCards().size());
		assertEquals(3, board.getPlayer().get(2).getPlayerCards().size());
		assertEquals(3, board.getPlayer().get(3).getPlayerCards().size());
		assertEquals(3, board.getPlayer().get(4).getPlayerCards().size());
		assertEquals(3, board.getPlayer().get(5).getPlayerCards().size());
	}
}
