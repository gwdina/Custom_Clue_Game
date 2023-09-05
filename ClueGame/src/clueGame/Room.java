package clueGame;

/**
 * Room Class
 * 
 * @author Anastasia Velyhko
 * @author Gordon Dina
 *
 */

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	
	//name setter and getter
	public String getName() { return name; } 
	public void setName(String name) { this.name = name; } 
	
	//label setter and getter
	public BoardCell getLabelCell() { return labelCell; } 
	public void setLabelCell(BoardCell labelCell) { this.labelCell = labelCell; } 
	
	//center setter and getter
	public BoardCell getCenterCell() { return centerCell; } 
	public void setCenterCell(BoardCell centerCell) { this.centerCell = centerCell; }
}
