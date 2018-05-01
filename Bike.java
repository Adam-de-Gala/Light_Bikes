import java.awt.Graphics;

public class Bike implements Space
{
	
	
	static Space[][] board = new Space[250][250];
	
	
	Coord pos;
	String color; 
	String wallColor; 
	
	boolean humanControled = false;
	
	int speed = 10; //10 is the standard;  
				    //100 is the max speed

	
	
	public Bike(Coord start,String bikeColor, String wallColor, Space[][] board)
	{
		pos = start; 
		this.board = board;
		this.color = bikeColor;
		this.wallColor = wallColor;
		System.out.println("Starting wall color is " + wallColor);
	}
	
	
	Coord move = new Coord(0,-1);
	public boolean Move()
	{
		Coord newPos = pos.addition(move);
		board[pos.x][pos.y] = new Wall(wallColor);
		
		if(color.equalsIgnoreCase("Orange"))
			System.out.println("Moving to " + newPos.toString());
		//Invalid move outside
		if(0 > newPos.x || board.length <= newPos.x)
			return false;
		if(0 > newPos.y || board[0].length <= newPos.y)
			return false;
		
		//Space Occupied by Wall; 
		if(board[newPos.x][newPos.y] != null)
		{
			return false;
		}
		else
		{
			pos = newPos;
			if(color.equalsIgnoreCase("Orange"))
				System.out.println("New Pos is " + newPos.toString());
			
			return true;
		}
	}
	
	//public boolean wallInfront
	
	public void ChangeDirection(String Change)
	{
		//System.out.println("Current Move is " + move.toString());
		if(Change.equalsIgnoreCase("North") && !move.equals(new Coord(0,1)))
			move = new Coord(0,-1);
		else if(Change.equalsIgnoreCase("East") && !move.equals(new Coord(-1,0)))
			move = new Coord(1,0);
		else if(Change.equalsIgnoreCase("South") && !move.equals(new Coord(0,-1)))
			move = new Coord(0,1);
		else if(Change.equalsIgnoreCase("West") && !move.equals(new Coord(1,0)))
			move = new Coord(-1,0);
		System.out.println("New Direction is " + move.toString());
	}
	
	
	
}
