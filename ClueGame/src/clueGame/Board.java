package clueGame;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

/**
 * Board Class
 * 
 * @author Anastasia Velyhko
 * @author Gordon Dina
 *
 */

public class Board extends JPanel{
	private BoardCell[][] grid; //2D array of object TestBoardCell
	private int numColumns; //column size
	private int numRows; //row size
	private String layoutConfigFile; //board layout
	private String setupConfigFile; //board setup
	private Map<Character, Room> roomMap = new HashMap<Character, Room>(); //map for board rooms
	private List<String[]> layoutFile; //array list stores layout cells 
	private String[] setupFile; //array stores setup lines
	private Set<BoardCell> targets; //set of target cells
	private Set<BoardCell> visited; //set of visited cells
	private ArrayList<Card> roomDeck = new ArrayList<Card>(); // array list stores room cards
	private ArrayList<Card> personDeck = new ArrayList<Card>(); // array list stores person cards
	private ArrayList<Card> weaponDeck = new ArrayList<Card>(); // array list stores weapon cards
	private ArrayList<Card> totalDeck = new ArrayList<Card>(); // array list stores remainding cards
	private ArrayList<Player> player = new ArrayList<Player>(); // array list stores players, human and computer
	private Solution answer;
	private Player playerTurn;
	private boolean turnFinished = false;
	private Card roomCard;
	private GameControlPanel controlPanel;
	private GameCardPanel cardPanel;

	/*
	 * variable and methods used for singleton pattern
	 */
	private static Board theInstance = new Board();
	Board() { super(); } // constructor is private to ensure only one can be created

	public static Board getInstance() { 
		if (theInstance == null) theInstance = new Board();
		return theInstance; 
	} // this method returns the only Board

	/*
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize() { 
		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
		roomMap = new HashMap<Character, Room>(); 
		player = new ArrayList<Player>();

		//load setup and layout
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (BadConfigFormatException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		setAdj();
		setUpSolution();
		deal();
		addMouseListener(new MovePlayerListener());
	}

	private void setAdj() {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {

				char initial;
				char secretPassage = grid[row][col].getSecretPassage();
				boolean doorway = grid[row][col].isDoorway();
				int up = row-1;
				int down = row+1;
				int left = col-1;
				int right = col+1;

				if (secretPassage != 0) {
					initial = grid[row][col].getInitial();
					BoardCell currentRoom = roomMap.get(initial).getCenterCell();
					currentRoom.addAdj(roomMap.get(secretPassage).getCenterCell());
				}

				//sets adj for both the doorway and the center of the room its pointing to 
				if (doorway) {
					BoardCell roomCell;
					switch(grid[row][col].getDoorDirection()) {
					case UP:
						initial = grid[up][col].getInitial();
						roomCell = roomMap.get(initial).getCenterCell();
						roomCell.addAdj(grid[row][col]);
						grid[row][col].addAdj(roomCell);
						break;
					case DOWN:
						initial = grid[down][col].getInitial();
						roomCell = roomMap.get(initial).getCenterCell();
						roomCell.addAdj(grid[row][col]);
						grid[row][col].addAdj(roomCell);
						break;
					case LEFT:
						initial = grid[row][left].getInitial();
						roomCell = roomMap.get(initial).getCenterCell();
						roomCell.addAdj(grid[row][col]);
						grid[row][col].addAdj(roomCell);
						break;
					case RIGHT: 
						initial = grid[row][right].getInitial();
						roomCell = roomMap.get(initial).getCenterCell();
						roomCell.addAdj(grid[row][col]);
						grid[row][col].addAdj(roomCell);
						break;
					default:
						break;
					}
				}


				//adjacency in x-1 direction
				if (up >= 0) {
					initial = grid[up][col].getInitial();
					if (initial == 'W') {
						BoardCell cell = getCell(up, col);
						grid[row][col].addAdj(cell);
					}
				} 

				//adjacency in y-1 direction
				if (left >= 0) {
					initial = grid[row][left].getInitial();
					if (initial == 'W') {
						BoardCell cell = getCell(row, left);
						grid[row][col].addAdj(cell);
					}
				} 

				//adjacency in x+1 direction
				if (down < numRows) {
					initial = grid[down][col].getInitial();
					if (initial == 'W') {
						BoardCell cell = getCell(down, col);
						grid[row][col].addAdj(cell);
					}
				} 

				//adjacency in y+1 direction
				if (right < numColumns) {
					initial = grid[row][right].getInitial();
					if (initial == 'W') {
						BoardCell cell = getCell(row, right);
						grid[row][col].addAdj(cell);
					}
				} 
			}
		}
	}

	//sets board setup and layout
	public void setConfigFiles(String layout, String setup) {
		this.layoutConfigFile = layout;
		this.setupConfigFile = setup;
	}

	//loads board setup 
	public void loadSetupConfig() throws Exception {
		FileReader reader = new FileReader(setupConfigFile); //reads file
		Scanner in = new Scanner(reader);

		while(in.hasNextLine()) {
			readSetupConfig(in);
		}
		in.close(); //close file
	}

	//reads board setup
	private void readSetupConfig(Scanner in) throws BadConfigFormatException, Exception { 
		String[] setupLines = in.nextLine().split(", "); //adds split line to array

		//check if line is a comment or line in empty
		if (!(setupLines[0].contains("//") || setupLines[0].isEmpty())) {
			//If an entry in either file does not have the proper format.
			if (setupLines[0].equals("Room") || setupLines[0].equals("Space")) {
				Room room = new Room();
				room.setName(setupLines[1]); //sets room name
				char initial = setupLines[2].charAt(0);
				roomMap.put(initial, room); //adds room name and initial to map
				if (setupLines[0].equals("Room")) {
					Card roomCard = new Card(setupLines[1], CardType.ROOM);
					roomDeck.add(roomCard); // adds card to roomDeck
				}
			}
			else if (setupLines[0].equals("Person")) {
				Card personCard = new Card(setupLines[1], CardType.PERSON);
				personDeck.add(personCard); // adds card to personDeck
				if (setupLines.length < 6) {
					ComputerPlayer computer = new ComputerPlayer(setupLines[1], setupLines[2], Integer.parseInt(setupLines[3]), Integer.parseInt(setupLines[4]));
					player.add(computer); // adds computer player to player list
				}
				else {
					Player human = new HumanPlayer(setupLines[1], setupLines[2], Integer.parseInt(setupLines[3]), Integer.parseInt(setupLines[4]));
					player.add(human); // adds human player to player list
				}
			}
			else if (setupLines[0].equals("Weapon")) {
				Card weaponCard = new Card(setupLines[1], CardType.WEAPON);
				weaponDeck.add(weaponCard); // adds card to weaponDeck
			}
			else throw new BadConfigFormatException("Error: Setup file doesn't have proper format");
		}
	}

	//loads board layout 
	public void loadLayoutConfig() throws Exception {
		FileReader reader = new FileReader(layoutConfigFile);
		Scanner in = new Scanner(reader);
		String[] nextCell = null; //stores layout lines
		layoutFile = new ArrayList<String[]>(); //holds file lines

		//reads each line and adds to array
		while(in.hasNextLine()) {
			nextCell = in.nextLine().split(","); //adds split cell to array
			layoutFile.add(nextCell); //stores each cell in array list
		}
		in.close(); //close file
		setGridSize();
		setGridCell();
	}

	private void setGridCell() throws Exception {
		DoorDirection doorDirection;
		//identifies if cell is center, label, door (which way), or secret passage
		//adds each cell to grid
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {   			 
				grid[row][col] = new BoardCell(row,col); //sets board sells
				char ch = layoutFile.get(row)[col].charAt(0);
				int numOfChar = layoutFile.get(row)[col].length();
				grid[row][col].setInitial(ch); //set cell initial

				if ((grid[row][col].getInitial() != 'W') || (grid[row][col].getInitial() !='X')) grid[row][col].setRoom(true);
				if (grid[row][col].getInitial() == 'W') grid[row][col].setWalkway(true); //check if walkway
				if (grid[row][col].getInitial() =='X') grid[row][col].setUnused(true); //check if unused

				doorDirection = DoorDirection.NONE;
				grid[row][col].setDoorDirection(doorDirection); //set all cells to initial no door

				//If the board layout refers to a room that is not in your setup file.
				if (!roomMap.containsKey(ch)) 
					throw new BadConfigFormatException("Error: Layout refers to room that is not in setup file");

				//check if cell char contains a sign
				if (numOfChar == 2) {
					//if cell char contains *, set to room center
					char initial = grid[row][col].getInitial();
					char identifier = layoutFile.get(row)[col].charAt(1);

					switch(identifier) {
					case '*': //if cell char contains *, set to center
						grid[row][col].setRoomCenter(); //set cell to center
						roomMap.get(initial).setCenterCell(grid[row][col]); 
						break;
					case '#': //if cell char contains #, set to label
						grid[row][col].setLabel(); //set cell to label
						roomMap.get(initial).setLabelCell(grid[row][col]);
						break;
					case '^': //if cell char contains ^, set door direction
						doorDirection = DoorDirection.UP;
						grid[row][col].setDoorDirection(doorDirection);
						grid[row][col].isDoorway();
						break;
					case '>': //if cell char contains >, set door direction
						doorDirection = DoorDirection.RIGHT;
						grid[row][col].setDoorDirection(doorDirection);
						grid[row][col].isDoorway();
						break;
					case '<': //if cell char contains <, set door direction
						doorDirection = DoorDirection.LEFT;
						grid[row][col].setDoorDirection(doorDirection);
						grid[row][col].isDoorway();
						break;
					case 'v': //if cell char contains v, set door direction
						doorDirection = DoorDirection.DOWN;
						grid[row][col].setDoorDirection(doorDirection);
						grid[row][col].isDoorway();
						break;
					default: //if cell char contains another char, set secret passage
						grid[row][col].setSecretPassage(identifier);
						break;
					}
				}
			}
		}
	}

	private void setGridSize() throws Exception {
		numRows = layoutFile.size(); //set row size
		numColumns = layoutFile.get(0).length; //set column size
		grid = new BoardCell[numRows][numColumns]; //set grid size

		//If the board layout file does not have the same number of columns in every row.
		for(int k=0; k < layoutFile.size(); k++) {
			if (layoutFile.get(k).length != numColumns) 
				throw new BadConfigFormatException("Error: Layout file does not have the same number of columns in every row");
		}
	}

	//calculates legal targets for a move from startCell of length pathlength
	public void calcTargets(BoardCell startCell, int pathlength) {
		visited.clear(); //empty visited set
		targets.clear(); //empty targets set
		visited.add(startCell); //add startCell to visited
		findAllTargets(startCell, pathlength); 
	}

	private void findAllTargets(BoardCell thisCell, int numSteps) {
		if (playerTurn.isDragged()) targets.add(thisCell);
		for (BoardCell adjCell: thisCell.adjList) {
			boolean isRoom = !(adjCell.getWalkway() || adjCell.getUnused());
			//checks if adjacency cell has been visited, or is occupied but not room
			if (visited.contains(adjCell) || (adjCell.getOccupied() && !isRoom)) continue; //continue onto next cell
			//if is room add adj cell to targets
			if (isRoom) {
				targets.add(adjCell);
				continue; //continue onto next cell
			}
			visited.add(adjCell); //adds visited cell to visited set
			//check if cell is occupied
			if (numSteps == 1) targets.add(adjCell); //checks if length is 1 then add adj cell to targets set
			else findAllTargets(adjCell, numSteps - 1);  //else call adj cell recursively
			visited.remove(adjCell); //remove visited adj cell
		}
		playerTurn.setDragged(false);
	}


	//grabs three random cards from each deck and puts it into solution, and removes it so it's not given to players, and makes the deck used to deal to players
	public void setUpSolution() {
		Random randRoom = new Random();
		int randomIndexRoom = randRoom.nextInt(roomDeck.size());
		Card randomRoomCard = roomDeck.get(randomIndexRoom);
		roomDeck.remove(randomIndexRoom);

		Random randPerson = new Random();
		int randomIndexPerson = randPerson.nextInt(personDeck.size());
		Card randomPersonCard = personDeck.get(randomIndexPerson);
		personDeck.remove(randomIndexPerson);

		Random randWeapon = new Random();
		int randomIndexWeapon = randWeapon.nextInt(weaponDeck.size());
		Card randomWeaponCard = weaponDeck.get(randomIndexWeapon);
		weaponDeck.remove(randomIndexWeapon);
		answer = new Solution(randomRoomCard, randomPersonCard, randomWeaponCard); //Deal cards to the Answer

		//adds all cards to total deck to be given to players
		for (Card i: roomDeck) totalDeck.add(i);
		for (Card i: personDeck) totalDeck.add(i);
		for (Card i: weaponDeck) totalDeck.add(i);

		//adds back the soln cards so that the players can make suggestions/accusations
		roomDeck.add(randomRoomCard);
		personDeck.add(randomPersonCard);
		weaponDeck.add(randomWeaponCard);

		Collections.shuffle(roomDeck);
		Collections.shuffle(personDeck);
		Collections.shuffle(weaponDeck);

		Collections.shuffle(totalDeck);//Create complete deck of cards (weapons, people and rooms)
	}

	//deals 3 random cards to each player from totalDeck and removes them to avoid dups
	public void deal() {
		for (int i = 0; i < player.size(); i++) {
			//first card
			Random randFirstCard = new Random();
			int randomIndexFirstCard = randFirstCard.nextInt(totalDeck.size());
			Card randomFirstCard = totalDeck.get(randomIndexFirstCard);
			totalDeck.remove(randomIndexFirstCard);

			player.get(i).updateHand(randomFirstCard);


			//second card
			Random randSecondCard = new Random();
			int randomIndexSecondCard = randSecondCard.nextInt(totalDeck.size());
			Card randomSecondCard = totalDeck.get(randomIndexSecondCard);
			totalDeck.remove(randomIndexSecondCard);

			player.get(i).updateHand(randomSecondCard);

			//third card
			Random randThirdCard = new Random();
			int randomIndexThirdCard = randThirdCard.nextInt(totalDeck.size());
			Card randomThirdCard = totalDeck.get(randomIndexThirdCard);
			totalDeck.remove(randomIndexThirdCard);

			player.get(i).updateHand(randomThirdCard);

		}
	}

	// if player makes an accusation and is correct then they will win, if not they will kick from the game
	public boolean checkAccusation(Card room, Card person, Card weapon) {
		boolean correctRoom = room == answer.getRoom();
		boolean correctPerson = person == answer.getPerson();
		boolean correctWeapon = weapon == answer.getWeapon();
		if (correctRoom && correctPerson && correctWeapon) return true;
		return false; // if the accusation is wrong
	}

	//goes through each player to see if they can dispute the suggestion, if they can return the card 
	//Card room, Card person, Card weapon
	public Card handleSuggestion(Player character, Solution suggestion) {
		int indexPLayer = player.indexOf(character);
		while(true) {
			indexPLayer++;
			if (indexPLayer == player.size()) indexPLayer = 0;
			if (indexPLayer == player.indexOf(character)) return null; //reaches back to the player
			Card room = suggestion.getRoom();
			Card person = suggestion.getPerson();
			Card weapon = suggestion.getWeapon();
			if (player.get(indexPLayer).disproveSuggestion(room, person, weapon) != null) 
				return player.get(indexPLayer).disproveSuggestion(room, person, weapon);
		}
	}

	//paint board
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//find size of cells
		int width = getWidth() / numColumns;
		int height = getHeight() / numRows;

		//draw each cell
		drawCell(g, width, height);
		//draw each room name at label cell
		drawRoom(g, width, height);
		//draws target for human player
		drawTargets(g, width, height);
		repaint(); // adds targets to the board
		//draw each player
		drawPlayer(g, width, height);
	}

	private void drawPlayer(Graphics g, int width, int height) {
		for (int i = 0; i < player.size(); i++) {
			player.get(i).drawPlayer(g, width-2, height-1);
		}
	}

	private void drawTargets(Graphics g, int width, int height) {
		for (BoardCell moves: targets) {
			g.setColor(Color.black); 
			g.drawRect(moves.getX(), moves.getY(), width-2, height-1);
			g.setColor(Color.black);
			g.fillRect(moves.getX(), moves.getY(), width-2, height-1);
		}
	}

	private void drawRoom(Graphics g, int width, int height) {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) { 
				if (getCell(row, col).isLabel()) grid[row][col].drawName(g, width-2, height-1);
			}
		}
	}

	private void drawCell(Graphics g, int width, int height) {
		int y = 1;
		for (int row = 0; row < numRows; row++) {
			int x = 1;
			for (int col = 0; col < numColumns; col++) { 
				grid[row][col].setX(x);
				grid[row][col].setY(y);
				grid[row][col].setWidth(width-2);
				grid[row][col].setHeight(height);
				grid[row][col].drawCell(g);
				x += width-2;
			}
			y += height-1;
		}
	}

	public class MovePlayerListener implements MouseListener {
		//check if it is human player's turn

		//  Empty definitions for unused event methods.
		public void mousePressed (MouseEvent event) {}
		public void mouseReleased (MouseEvent event) {}
		public void mouseEntered (MouseEvent event) {}
		public void mouseExited (MouseEvent event) {}
		public void mouseClicked (MouseEvent event) {
			if(turnFinished == false) {
				// only for the human player
				if (getPlayerTurn() == player.get(0)) {
					BoardCell targetCell = null;
					for (BoardCell target : targets) {
						//player clicks on valid target
						if (target.containsClick(event.getX(), event.getY())) {
							targetCell = target; //player has chosen a tile
							break;
						}
					}

					// if player has chosen valid tile then they will move to it
					if (targetCell != null) {
						int row = player.get(0).getRow();
						int col = player.get(0).getCol();
						grid[row][col].setOccupied(false);

						player.get(0).updateRow(targetCell.getRow());
						player.get(0).updateCol(targetCell.getCol());

						if (targetCell.getWalkway() || targetCell.getUnused()) {
							grid[row][col].setOccupied(true);
						}

						repaint();
						//if the target is a room then the player could do a suggestion
						suggest(targetCell);
						turnFinished = true;
						targets.clear(); //gets rid of targets so they don't get repainted
						repaint();
					}

					//display message if target is not clicked
					else JOptionPane.showMessageDialog(null, "Not a target!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				//display message if target is not the human players turn
				else JOptionPane.showMessageDialog(null, "Not your turn!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		private void suggest(BoardCell targetCell) {
			if (!(targetCell.getWalkway() || targetCell.getUnused())) {
				for (int i = 0; i < roomDeck.size(); i++) {
					if (roomDeck.get(i).getCardName() == getRoom(targetCell).getName()) {
						roomCard = roomDeck.get(i);
						Suggestion suggestion = new Suggestion();
						suggestion.setControlPanel(controlPanel);
						suggestion.setCardPanel(cardPanel);
						suggestion.setVisible(true);
						break;
					}
				}
			}
		}
	}


	public Set<BoardCell> getAdjList(int row, int col) { return grid[row][col].grabAdjList(); } 
	public Set<BoardCell> getTargets() { return targets; } //returns the targets last created by calcTargets()

	public BoardCell getCell(int row, int col) { return grid[row][col]; } //returns the cell from the board at row, col

	public int getNumRows() { return numRows; } //returns board row size
	public int getNumColumns() { return numColumns; } //returns board column size

	public Room getRoom(char room) { return roomMap.get(room); } //returns room at char
	public Room getRoom(BoardCell cell) { return roomMap.get(cell.getInitial()); } //return room at cell 

	public Card getCard(String name, CardType cardType) { 
		if (cardType == CardType.ROOM) {
			for (int i = 0; i < roomDeck.size(); i++) {
				if (roomDeck.get(i).getCardName().equals(name)) return roomDeck.get(i);
			}
		}
		else if (cardType == CardType.PERSON) {
			for (int i = 0; i < personDeck.size(); i++) {
				if (personDeck.get(i).getCardName().equals(name)) return personDeck.get(i);
			}
		}
		else if (cardType == CardType.WEAPON) {
			for (int i = 0; i < weaponDeck.size(); i++) {
				if (weaponDeck.get(i).getCardName().equals(name)) return weaponDeck.get(i);
			}
		}
		return null; 
	} //return card

	public ArrayList<Player> getPlayer() { return player; } //return player list
	public Solution getSolution() { return answer; } //return answer

	public void setAnswer(Solution answer) { this.answer = answer; } //set answer
	public void setPlayer(ArrayList<Player> player) { this.player = player; } //set player list

	public void setPlayerTurn(Player turnPlayer) { this.playerTurn = turnPlayer; }
	public Player getPlayerTurn() { return playerTurn; }

	public boolean isTurnFinished() { return turnFinished; }

	public void setTurnFinished(boolean turnFinished) { this.turnFinished = turnFinished; }

	public ArrayList<Card> getRoomDeck() { return roomDeck; }
	public ArrayList<Card> getPersonDeck() { return personDeck; }
	public ArrayList<Card> getWeaponDeck() { return weaponDeck; }

	public Card getRoomCard() { return roomCard; }

	public void setControlPanel(GameControlPanel controlPanel) { this.controlPanel = controlPanel; }
	public void setCardPanel(GameCardPanel cardPanel) { this.cardPanel = cardPanel; }
}
