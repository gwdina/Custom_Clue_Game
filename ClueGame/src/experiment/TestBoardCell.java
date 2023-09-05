/**
 * TestBoardCell Class
 * 
 * @author Anastasia Velyhko
 * @author Gordon Dina
 *
 */

package experiment;

import java.util.*;

public class TestBoardCell {
	private int row, col; //row and col variables
	private Boolean isRoom = false; //isRoom boolean initialized to false
	private Boolean isOccupied = false; //isOccupied initialized to false
	Set<TestBoardCell> adjList = new HashSet<TestBoardCell>(); //initialized adjList set
	
	//constructor
	public TestBoardCell() { adjList = new HashSet<TestBoardCell>(); }
	
	//a constructor that has passed into it the row and column for that cell
	public TestBoardCell(int row, int col) {
		this.row = row;
		this.col = col;
	}

	//a setter to add a cell to this cells adjacency list
	public void addAdjacency(TestBoardCell cell) {
		this.adjList.add(cell);
	}

	//returns the adjacency list for the cell
	public Set<TestBoardCell> getAdjList() {
		return adjList;
	}

	//a setter for indicating a cell is part of a room 
	public void setRoom(boolean bool) {
		this.isRoom = bool;
	}
	
	//a getter for indicating a cell is part of a room 
	public boolean getRoom() {
		return this.isRoom;
	}

	//a setter for indicating a cell is occupied by another player
	public void setOccupied(boolean bool) {
		this.isOccupied = bool;
	}
	
	//a getter for indicating a cell is occupied by another player
	public boolean getOccupied() {
		return this.isOccupied;
	}
}