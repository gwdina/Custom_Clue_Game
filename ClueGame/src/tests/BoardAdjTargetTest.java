package tests;

import static org.junit.Assert.*;
import java.util.Set;
import org.junit.jupiter.api.*;
import clueGame.*;

/**
 * Board Adjacency and Target Test
 * 
 * @author Anastasia Velyhko
 * @author Gordon Dina
 *
 */

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms()
	{
		// we want to test a couple of different rooms.
		// First, the office that only has a single door but a secret room
		Set<BoardCell> testList = board.getAdjList(3, 29);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(5, 27)));
		assertTrue(testList.contains(board.getCell(20, 1)));
		
		//testing atrium 
		testList = board.getAdjList(20, 16);
		assertEquals(6, testList.size());
		assertTrue(testList.contains(board.getCell(20, 11)));
		assertTrue(testList.contains(board.getCell(21, 11)));
		assertTrue(testList.contains(board.getCell(20, 20)));
		assertTrue(testList.contains(board.getCell(21, 20)));
		assertTrue(testList.contains(board.getCell(16, 15)));
		assertTrue(testList.contains(board.getCell(16, 16)));
	}

	
	// Ensure door locations include their rooms and also additional walkways
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacencyDoor()
	{
		//testing office down door
		Set<BoardCell> testList = board.getAdjList(15, 0);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(20, 1)));
		assertTrue(testList.contains(board.getCell(15, 1)));
		assertTrue(testList.contains(board.getCell(14, 0)));

		//testing kitchen right door
		testList = board.getAdjList(5, 27);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(3, 29)));
		assertTrue(testList.contains(board.getCell(5, 26)));
		assertTrue(testList.contains(board.getCell(6, 27)));
		
		//testing garden right door
		testList = board.getAdjList(7, 11);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(11, 17)));
		assertTrue(testList.contains(board.getCell(8, 11)));
		assertTrue(testList.contains(board.getCell(7, 10)));
		assertTrue(testList.contains(board.getCell(6, 11)));
		
		//testing atrium down door
		testList = board.getAdjList(16, 15);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(20, 16)));
		assertTrue(testList.contains(board.getCell(16, 14)));
		assertTrue(testList.contains(board.getCell(16, 16)));
		assertTrue(testList.contains(board.getCell(15, 15)));
		
		//testing dinner right door
		testList = board.getAdjList(21, 26);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(21, 25)));
		assertTrue(testList.contains(board.getCell(16, 27)));
		assertTrue(testList.contains(board.getCell(22, 26)));

		//testing bathroom down door
		testList = board.getAdjList(9, 6);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(10, 3)));
		assertTrue(testList.contains(board.getCell(9, 5)));
		assertTrue(testList.contains(board.getCell(9, 7)));
		assertTrue(testList.contains(board.getCell(8, 6)));
		
		//testing patio left door
		testList = board.getAdjList(3, 23);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(2, 17)));
		assertTrue(testList.contains(board.getCell(4, 23)));
		assertTrue(testList.contains(board.getCell(2, 23)));
		assertTrue(testList.contains(board.getCell(3, 24)));
		
		//testing bedroom up door
		testList = board.getAdjList(6, 8);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(3, 4)));
		assertTrue(testList.contains(board.getCell(7, 8)));
		assertTrue(testList.contains(board.getCell(6, 7)));
		assertTrue(testList.contains(board.getCell(6, 9)));
	}
	
	// Test a variety of walkway scenarios
	// These tests are Dark Orange on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on top edge of board
		Set<BoardCell> testList = board.getAdjList(0, 9);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(1, 9)));
		assertTrue(testList.contains(board.getCell(0, 10)));
		
		// Test near a door but not adjacent
		testList = board.getAdjList(13, 0);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(14, 0)));
		assertTrue(testList.contains(board.getCell(13, 1)));

		// Test adjacent to walkways
		testList = board.getAdjList(11, 9);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(10, 9)));
		assertTrue(testList.contains(board.getCell(12, 9)));
		assertTrue(testList.contains(board.getCell(11, 8)));
		assertTrue(testList.contains(board.getCell(11, 10)));

		// Test on right edge
		testList = board.getAdjList(9, 30);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(9, 29)));
		assertTrue(testList.contains(board.getCell(8, 30)));
	}
	
	
	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsInGarden() {
		// test a roll of 1
		board.calcTargets(board.getCell(11, 17), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(7, 11)));
		assertTrue(targets.contains(board.getCell(15, 21)));
		assertTrue(targets.contains(board.getCell(15, 12)));
		assertTrue(targets.contains(board.getCell(7, 22)));
		assertTrue(targets.contains(board.getCell(16, 27)));
		
		// test a roll of 2
		board.calcTargets(board.getCell(11, 17), 2);
		targets= board.getTargets();
		assertEquals(13, targets.size());
		assertTrue(targets.contains(board.getCell(8, 22)));
		assertTrue(targets.contains(board.getCell(16, 27)));
		assertTrue(targets.contains(board.getCell(16, 12)));
	}
	
	@Test
	public void testTargetsInOffice() {
		// test a roll of 1
		board.calcTargets(board.getCell(20, 1), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(15, 0)));
		assertTrue(targets.contains(board.getCell(3, 29)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(20, 1), 3);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 29)));
		assertTrue(targets.contains(board.getCell(14, 1)));	
		assertTrue(targets.contains(board.getCell(15, 2)));
		assertTrue(targets.contains(board.getCell(13, 0)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(20, 1), 4);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(3, 29)));
		assertTrue(targets.contains(board.getCell(15, 3)));	
		assertTrue(targets.contains(board.getCell(14, 2)));	
		assertTrue(targets.contains(board.getCell(13, 1)));	
	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(7, 22), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(11, 17)));
		assertTrue(targets.contains(board.getCell(8, 22)));	
		assertTrue(targets.contains(board.getCell(7, 23)));	
		assertTrue(targets.contains(board.getCell(6, 22)));
		
		// test a roll of 2
		board.calcTargets(board.getCell(7, 22), 2);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(5, 22)));
		assertTrue(targets.contains(board.getCell(7, 24)));
		assertTrue(targets.contains(board.getCell(9, 22)));	
		assertTrue(targets.contains(board.getCell(8, 23)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(7, 22), 4);
		targets= board.getTargets();
		assertEquals(17, targets.size());
		assertTrue(targets.contains(board.getCell(11, 22)));
		assertTrue(targets.contains(board.getCell(6, 25)));
		assertTrue(targets.contains(board.getCell(10, 23)));	
		assertTrue(targets.contains(board.getCell(5, 20)));
		assertTrue(targets.contains(board.getCell(4, 23)));	
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(6, 18), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(5, 18)));
		assertTrue(targets.contains(board.getCell(6, 19)));	
		assertTrue(targets.contains(board.getCell(6, 17)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(6, 18), 3);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(6, 21)));
		assertTrue(targets.contains(board.getCell(5, 20)));
		assertTrue(targets.contains(board.getCell(6, 15)));	
	}

	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(22, 11), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(21, 11)));
		assertTrue(targets.contains(board.getCell(22, 10)));	
		assertTrue(targets.contains(board.getCell(23, 11)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(22, 11), 4);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(19, 10)));
		assertTrue(targets.contains(board.getCell(18, 11)));
		assertTrue(targets.contains(board.getCell(20, 16)));
		assertTrue(targets.contains(board.getCell(23, 10)));
	}
	
	@Test
	public void testTargetsInWalkway3() {
		// test a roll of 1
		board.calcTargets(board.getCell(7, 25), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(7, 26)));
		assertTrue(targets.contains(board.getCell(7, 24)));
		assertTrue(targets.contains(board.getCell(6, 25)));
		assertTrue(targets.contains(board.getCell(8, 25)));
		
		// test a roll of 2
		board.calcTargets(board.getCell(7, 25), 2);
		targets= board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(7, 27)));
		assertTrue(targets.contains(board.getCell(7, 23)));
		assertTrue(targets.contains(board.getCell(5, 25)));
		assertTrue(targets.contains(board.getCell(9, 25)));
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 
		board.getCell(14, 3).setOccupied(true);
		board.getCell(13, 4).setOccupied(true);
		board.getCell(14, 5).setOccupied(true);
		board.calcTargets(board.getCell(14, 4), 4);
		board.getCell(14, 3).setOccupied(false);
		board.getCell(13, 4).setOccupied(false);
		board.getCell(14, 5).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(18, 4)));
		assertTrue(targets.contains(board.getCell(17, 3)));
		assertTrue(targets.contains(board.getCell(15, 1)));	
		assertFalse( targets.contains( board.getCell(13, 7))) ;
		assertFalse( targets.contains( board.getCell(14, 0))) ;
	
		// test a roll of 1
		board.getCell(22, 24).setOccupied(true);
		board.calcTargets(board.getCell(22, 23), 1);
		board.getCell(22, 24).setOccupied(false);
		targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(21, 23)));	
		assertTrue(targets.contains(board.getCell(23, 23)));	
		assertFalse(targets.contains(board.getCell(22, 24)));	
		
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(20, 7).setOccupied(true);
		board.calcTargets(board.getCell(19, 4), 1);
		board.getCell(20, 7).setOccupied(false);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(20, 7)));		
		
		// check leaving a room with a blocked doorway
		board.getCell(4, 13).setOccupied(true);
		board.calcTargets(board.getCell(2, 17), 1);
		board.getCell(4, 13).setOccupied(false);
		targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(3, 23)));
	}
}
