/**
 * BoardTestsExp class tests adjacency and targets
 * 
 * @author Anastasia Velyhko
 * @author Gordon Dina
 *
 */

package tests;

import java.util.Set;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import experiment.*;

public class BoardTestsExp {
	TestBoard board;

	@BeforeEach 
	//method to set up your BoardExp.
	public void setUp() {
		board = new TestBoard();
	}

	/*
	 * Test adjacencies for different locations
	 * Test center, edges, and corners
	 */

	@Test
	public void testAdjacency() {
		TestBoardCell topLeftCorner = board.getCell(0, 0);
		Set<TestBoardCell> testTLC = topLeftCorner.getAdjList();

		
		Assert.assertTrue(testTLC.contains(board.getCell(1, 0)));
		Assert.assertTrue(testTLC.contains(board.getCell(0, 1)));
		Assert.assertEquals(2, testTLC.size());
		
		TestBoardCell bottomRightCorner = board.getCell(3, 3);
		Set<TestBoardCell> testBRC = bottomRightCorner.getAdjList();

		Assert.assertTrue(testBRC.contains(board.getCell(2, 3)));
		Assert.assertTrue(testBRC.contains(board.getCell(3, 2)));
		Assert.assertEquals(2, testBRC.size());

		TestBoardCell rightEdge = board.getCell(1, 3);
		Set<TestBoardCell> testRE = rightEdge.getAdjList();

		Assert.assertTrue(testRE.contains(board.getCell(0, 3)));
		Assert.assertTrue(testRE.contains(board.getCell(1, 2)));
		Assert.assertTrue(testRE.contains(board.getCell(2, 3)));
		Assert.assertEquals(3, testRE.size());

		TestBoardCell leftEdge = board.getCell(2,0);
		Set<TestBoardCell> testLE = leftEdge.getAdjList();

		Assert.assertTrue(testLE.contains(board.getCell(1, 0)));
		Assert.assertTrue(testLE.contains(board.getCell(3, 0)));
		Assert.assertTrue(testLE.contains(board.getCell(2, 1)));
		Assert.assertEquals(3, testLE.size());

		TestBoardCell middleGrid = board.getCell(2,2);
		Set<TestBoardCell> testMG = middleGrid.getAdjList();

		Assert.assertTrue(testMG.contains(board.getCell(1, 2)));
		Assert.assertTrue(testMG.contains(board.getCell(2, 1)));
		Assert.assertTrue(testMG.contains(board.getCell(2, 3)));
		Assert.assertTrue(testMG.contains(board.getCell(3, 2)));
		Assert.assertEquals(4, testMG.size()); 
	}

	/*
	 * Test targets with several rolls and start locations
	 */
	@Test
	public void testTargetsNormal() {
		TestBoardCell cell = board.getCell(0,0);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> target = board.getTargets();
		Assert.assertEquals(6, target.size());
		Assert.assertTrue(target.contains(board.getCell(3, 0)));
		Assert.assertTrue(target.contains(board.getCell(2, 1)));
		Assert.assertTrue(target.contains(board.getCell(0, 1)));
		Assert.assertTrue(target.contains(board.getCell(1, 2)));
		Assert.assertTrue(target.contains(board.getCell(0, 3)));
		Assert.assertTrue(target.contains(board.getCell(1, 0)));
	}

	/*
	 * Test a complex situation where there may be a cell that represents a room and another that is occupied by an opponent
	 */
	@Test
	public void testTargetsMixed() {
		//set up occupied cells
		board.getCell(0, 2).setOccupied(true);
		board.getCell(1, 2).setRoom(true);
		TestBoardCell cell = board.getCell(0, 3);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(3, 3)));		
	}
}
