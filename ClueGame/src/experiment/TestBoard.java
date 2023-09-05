/**
 * TestBoard Class
 * 
 * @author Anastasia Velyhko
 * @author Gordon Dina
 *
 */

package experiment;

import java.util.*;

public class TestBoard {
	private TestBoardCell[][] grid; //2D array of object TestBoardCell
	private Set<TestBoardCell> targets; //set of target cells
	private Set<TestBoardCell> visited; //set of visited cells
	final static int COLS = 4; //column size
	final static int ROWS = 4; //row size

	//constructor
	public TestBoard() {
		grid = new TestBoardCell[ROWS][COLS]; //set grid at size ROWS and COLS
		targets = new HashSet<TestBoardCell>(); //initialize targets
		visited = new HashSet<TestBoardCell>(); //initialize visited

		//for loop that creates grid at rows and cols
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				grid[i][j] = new TestBoardCell(i, j);
			}
		}

		//adds 4 adjacencies of rows and cols
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if ((i-1) >= 0) grid[i][j].addAdjacency(getCell(i-1, j)); //adjacency in x-1 direction
				if ((j-1) >= 0) grid[i][j].addAdjacency(getCell(i, j-1)); //adjacency in y-1 direction
				if ((i+1) < ROWS) grid[i][j].addAdjacency(getCell(i+1, j)); //adjacency in x+1 direction
				if ((j+1) < COLS) grid[i][j].addAdjacency(getCell(i, j+1)); //adjacency in y+1 direction
			}
		}
	}

	//calculates legal targets for a move from startCell of length pathlength
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		visited.add(startCell); //adds visited cell to visited set
		//for loop for each adjacency cell
		for (TestBoardCell adjCell: startCell.adjList) {
			//checks if adjacency cell has already been visited	
			if (visited.contains(adjCell) == false) {
				//checks if cell is occupied
				if (adjCell.getOccupied() == false) {
					if (pathlength == 1) targets.add(adjCell); //checks if length is 1 then add adj cell to targets set
					if (adjCell.getRoom() == true) targets.add(adjCell); //if is room add adj cell to targets
					else calcTargets(adjCell, pathlength - 1);  //else call adj cell recursively
					visited.remove(adjCell); //remove visited adj cell
				}
			}
		}
	}

	//returns the cell from the board at row, col
	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];
	}

	//gets the targets last created by calcTargets()
	public Set<TestBoardCell> getTargets() {
		return targets;
	}
}