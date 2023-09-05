package tests;

/*
 * This program tests that config files are loaded properly.
 */

// Doing a static import allows me to write assertEquals rather than
// Assert.assertEquals
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.jupiter.api.*;

import clueGame.*;

public class FileInitTests {
	// Constants that I will use to test whether the file was loaded correctly
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 25;
	public static final int NUM_COLUMNS = 31;

	// NOTE: I made Board static because I only want to set it up one
	// time (using @BeforeAll), no need to do setup before each test.
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

	@Test
	public void testRoomLabels() {
		// To ensure data is correctly loaded, test retrieving for every rooms
		// from the hash, including the first and last in the file and a few others
		assertEquals("Living Room", board.getRoom('L').getName() );
		assertEquals("Bedroom", board.getRoom('B').getName() );
		assertEquals("Kitchen", board.getRoom('K').getName() );
		assertEquals("Garden", board.getRoom('G').getName() );
		assertEquals("Dining Room", board.getRoom('D').getName() );
		assertEquals("Atrium", board.getRoom('A').getName() );
		assertEquals("Bathroom", board.getRoom('T').getName() );
		assertEquals("Office", board.getRoom('O').getName() );
		assertEquals("Pool", board.getRoom('P').getName() );
	}

	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

	// Test a doorway in each direction (RIGHT/LEFT/UP/DOWN), plus
	// three cells that are not a doorway, 2 are walkways and 1 is unused.
	// These cells are dark blue on the planning spreadsheet
	@Test
	public void FourDoorDirections() {	
		// door to pool, left and up
		BoardCell pool = board.getCell(3, 23); 
		assertTrue(pool.isDoorway());
		assertEquals(DoorDirection.LEFT, pool.getDoorDirection());
		pool = board.getCell(4, 13); // door to pool
		assertTrue(pool.isDoorway());
		assertEquals(DoorDirection.UP, pool.getDoorDirection());
		
		// doors to Garden, up right left 
		BoardCell garden = board.getCell(15, 12);
		assertTrue(garden.isDoorway());
		assertEquals(DoorDirection.UP, garden.getDoorDirection());
		garden = board.getCell(15, 21);
		assertTrue(garden.isDoorway());
		assertEquals(DoorDirection.UP, garden.getDoorDirection());
		garden = board.getCell(7, 11);
		assertTrue(garden.isDoorway());
		assertEquals(DoorDirection.RIGHT, garden.getDoorDirection());
		garden = board.getCell(7, 22);
		assertTrue(garden.isDoorway());
		assertEquals(DoorDirection.LEFT, garden.getDoorDirection());
		
		// single door to Kitchen Room, right
		BoardCell kitchen = board.getCell(5, 27);
		assertTrue(kitchen.isDoorway());
		assertEquals(DoorDirection.RIGHT, kitchen.getDoorDirection());
		
		//doors to Atrium, has many doors directions down, left and right
		BoardCell atrium = board.getCell(16, 15);
		assertTrue(atrium.isDoorway());
		assertEquals(DoorDirection.DOWN, atrium.getDoorDirection());
		atrium = board.getCell(16, 16); 
		assertTrue(atrium.isDoorway());
		assertEquals(DoorDirection.DOWN, atrium.getDoorDirection());
		atrium = board.getCell(20, 20);
		assertTrue(atrium.isDoorway());
		assertEquals(DoorDirection.LEFT, atrium.getDoorDirection());
		atrium = board.getCell(21, 20); 
		assertTrue(atrium.isDoorway());
		assertEquals(DoorDirection.LEFT, atrium.getDoorDirection());
		atrium = board.getCell(20, 11);
		assertTrue(atrium.isDoorway());
		assertEquals(DoorDirection.RIGHT, atrium.getDoorDirection());
		atrium = board.getCell(21, 11); 
		assertTrue(atrium.isDoorway());
		assertEquals(DoorDirection.RIGHT, atrium.getDoorDirection());
		
		// Test that walkways are not doors
		BoardCell nonDoors = board.getCell(11, 10); // only next to other walkways
		assertFalse(nonDoors.isDoorway());
		nonDoors = board.getCell(23, 29);//next to unused and room
		assertFalse(nonDoors.isDoorway());
		nonDoors = board.getCell(6, 7);// right next to a door
		assertFalse(nonDoors.isDoorway());
	}

	

	// Test that we have the correct number of doors
	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(20, numDoors);
	}

	// Test a few room cells to ensure the room initial is correct.
	@Test
	public void testRooms() {
		// just test a standard room location
		BoardCell cellAtrium = board.getCell( 23, 15);
		Room roomAtrium = board.getRoom( cellAtrium ) ;
		assertTrue( roomAtrium != null );
		assertEquals( roomAtrium.getName(), "Atrium" ) ;
		assertFalse( cellAtrium.isLabel() );
		assertFalse( cellAtrium.isRoomCenter() ) ;
		assertFalse( cellAtrium.isDoorway()) ;

		// this is a label cell to test
		BoardCell cellGarden = board.getCell(9, 17);
		Room roomGarden = board.getRoom( cellGarden ) ;
		assertTrue( roomGarden != null );
		assertEquals( roomGarden.getName(), "Garden" ) ;
		assertTrue( cellGarden.isLabel() );
		assertTrue( roomGarden.getLabelCell() == cellGarden );

		// this is a room center cell to test
		BoardCell cellDR = board.getCell(16, 27);
		Room roomDR = board.getRoom( cellDR ) ;
		assertTrue( roomDR != null );
		assertEquals( roomDR.getName(), "Dining Room" ) ;
		assertTrue( cellDR.isRoomCenter() );
		assertTrue( roomDR.getCenterCell() == cellDR );
		
		// this is a secret passage test
		BoardCell cellOffice = board.getCell(22, 0);
		Room roomOffice = board.getRoom( cellOffice ) ;
		assertTrue( roomOffice != null );
		assertEquals( roomOffice.getName(), "Office" ) ;
		assertTrue( cellOffice.getSecretPassage() == 'K' );
		
		// test a walkway
		BoardCell cellWalk = board.getCell(18, 20);
		Room roomWalk = board.getRoom( cellWalk ) ;
		// Note for our purposes, walkways and closets are rooms
		assertTrue( roomWalk != null );
		assertEquals( roomWalk.getName(), "Walkway" ) ;
		assertFalse( cellWalk.isRoomCenter() );
		assertFalse( cellWalk.isLabel() );
		
		// test a closet
		BoardCell cellUn = board.getCell(17, 9);
		Room roomUn = board.getRoom( cellUn ) ;
		assertTrue( roomUn != null );
		assertEquals( roomUn.getName(), "Unused" ) ;
		assertFalse( cellUn.isRoomCenter() );
		assertFalse( cellUn.isLabel() );
	}
}


