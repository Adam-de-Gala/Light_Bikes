import java.util.ArrayList;

public class AI 
{
	Space[][] board;
	static Bike[] bikes;
	int tag; 
	int speed = 1;
	int intelligence = 0;
	
	//Stored Direction to move; 
	Coord[] directions = new Coord[4];
	public AI(Bike[] bikes, Space[][] board, int tag, int intelligence)
	{
		this.bikes = bikes;
		this.board = board;
		this.tag = tag;
		this.intelligence = intelligence;
		
		directions[0] = new Coord(0,-1); //North
		directions[1] = new Coord(1,0); //East
		directions[2] = new Coord(0,1); //South
		directions[3] = new Coord(-1,0); //West
	}
	
	
	public void updateBike(Bike[] bikes)
	{
		this.bikes = bikes;
	}
	
	public void MakeDescision()
	{
		 Bike changing = bikes[tag];
		 Coord orignalMove = changing.move;
		 
		 int rand = (int) (Math.random() * 20);
		 if(rand == 0)
		 {
			 System.out.println("Making Random Move");
			 makeRandomMove();
		 }
		 
		 
		 int newX = changing.pos.x + changing.move.x;
		 int newY = changing.pos.y + changing.move.y;
	 
		 if(newX < 0 || newY < 0 || newX >= board.length || newY >= board[0].length ||  board[newX][newY] != null)
		 {
			 System.out.println("Avoiding collision, currently at " + newX + " " + newY);
			 changing.move = orignalMove;
			 if(intelligence == 1)
				 avoidCollision();
			 if(intelligence == 2)
				 avoidCollisionSimple();
			 if(intelligence == 3)
				 avoidCollisionOpenSpaceSimple();
				 
		 }
	}
	
		//Calculates which direction gives the most open space (open squares)
		//based on the current mapping. Has no prediction of opponents movement.
		//or understanding of the usability of space. 
		public boolean avoidCollisionOpenSpaceSimple()
		{
			Bike changing = bikes[tag];

			int maxArea = 0;
			int direction = -1; 
			for(int i = 0; i< 4; i++)
			{
				int newX = changing.pos.x + directions[i].x;
			    int newY = changing.pos.y + directions[i].y;
				if(newX >= 0 && newY >= 0 && newX < board.length && newY < board[0].length &&  board[newX][newY] == null)
				{	
					//Means the immediate Square in that direction is free
					Space[][] copy = mockBoard();
					int tempArea = getOpenSpace(changing.pos, new Coord(newX, newY), copy);
					if(tempArea > maxArea)
					{
						maxArea = tempArea;
						direction = i;
					}
					
					
				}
			}
			if(direction == 0)
				bikes[tag].ChangeDirection("North");
			if(direction == 1)
				bikes[tag].ChangeDirection("East");
			if(direction == 2)
				bikes[tag].ChangeDirection("South");
			if(direction == 3)
				bikes[tag].ChangeDirection("West");
				
			if(direction == -1)
				return false;
			return true;
			//System.out.println("Changing move to " + rand) ;
		}
	



	//Just takes a linear line, to see which direction has the farthest distance
	//to the next Collision. Has no 2D awareness.  
	public boolean avoidCollisionSimple()
	{
		Bike changing = bikes[tag];

		int maxDistance = 0;
		int direction = -1; 
		for(int i = 0; i< 4; i++)
		{
			int newX = changing.pos.x + directions[i].x;
		    int newY = changing.pos.y + directions[i].y;
			if(newX >= 0 && newY >= 0 && newX < board.length && newY < board[0].length &&  board[newX][newY] == null)
			{	
				//Means the immediate Square in that direction is free
				//Measure how far the distance to the next collusion is. 
				int tempDistance = 0; 
				while(newX >= 0 && newY >= 0 && newX < board.length && newY < board[0].length &&  board[newX][newY] == null)
				{
					 newX += directions[i].x;
				     newY += directions[i].y;
				     tempDistance ++ ;
				}
				System.out.println("Direction " + i + " has a distance of " + tempDistance );
				if(tempDistance > maxDistance)
				{
					maxDistance = tempDistance;
					direction = i;
				}
				
				
			}
		}
		if(direction == 0)
			bikes[tag].ChangeDirection("North");
		if(direction == 1)
			bikes[tag].ChangeDirection("East");
		if(direction == 2)
			bikes[tag].ChangeDirection("South");
		if(direction == 3)
			bikes[tag].ChangeDirection("West");
			
		if(direction == -1)
			return false;
		return true;
		//System.out.println("Changing move to " + rand) ;
	}
	
	
	
	public boolean avoidCollision()
	{
		Bike changing = bikes[tag];

		int rand = (int) (Math.random() * 4);
		for(int i = rand; i<= rand+4; i++)
		{
			int modI = i % 4;
			int newX = changing.pos.x + directions[modI].x;
		    int newY = changing.pos.y + directions[modI].y;
		    if(newX >= 0 && newY >= 0 && newX < board.length && newY < board[0].length)
		    		System.out.println("Checking "+ newX + " " + newY + " Occupied " + ( board[newX][newY] == null));
			if(newX >= 0 && newY >= 0 && newX < board.length && newY < board[0].length &&  board[newX][newY] == null)
			{
				if(modI == 0)
					bikes[tag].ChangeDirection("North");
				if(modI == 1)
					bikes[tag].ChangeDirection("East");
				if(modI == 2)
					bikes[tag].ChangeDirection("South");
				if(modI == 3)
					bikes[tag].ChangeDirection("West");
				System.out.println("Changing direction to " + modI) ;
				return true;
			}
		}
		return false;
		//System.out.println("Changing move to " + rand) ;
	}
	
	
	public void makeRandomMove()
	{
		int rand = (int) (Math.random() * 4);
		
		if(rand == 0)
			bikes[tag].ChangeDirection("North");
		if(rand == 1)
			bikes[tag].ChangeDirection("East");
		if(rand == 2)
			bikes[tag].ChangeDirection("South");
		if(rand == 3)
			bikes[tag].ChangeDirection("West");
		System.out.println("Changing direction to " + rand) ;
		
	}

	private int getOpenSpace(Coord pos, Coord start, Space[][] mockBoard)
	{
		
		mockBoard[pos.x][pos.y] = new Wall("CLOSED");
		mockBoard[start.x][start.y] = new Wall("OPEN");
		
		//Iterate Fowards, Filling opening Spaces
		ArrayList<Coord> spaces = new ArrayList<Coord>();
		ArrayList<Coord> temp = new ArrayList<Coord>();
		spaces.add(start);
		
		while(spaces.size() != 0)
		{
			temp.clear();
			for(Coord e : spaces)
			{	
				//System.out.println("Caculating open space, point at" + e.toString());
				for(int i = 0; i< 4; i++)
				{
					
					int newX = e.x + directions[i].x;
				    int newY = e.y + directions[i].y;
				    //System.out.print("Caculating open space, looking at" + newX + " " + newY);
					if(newX >= 0 && newY >= 0 && newX < mockBoard.length && newY < mockBoard[0].length &&  mockBoard[newX][newY] == null)
					{	
						//System.out.print(" " + " it is open");
						//Means the immediate Square in that direction is free
						//Measure how far the distance to the next collusion is. 
						mockBoard[newX][newY] = new Wall("OPEN");
						temp.add(new Coord(newX, newY));
					}
					//System.out.println();
				}
			}
			spaces = (ArrayList<Coord>) temp.clone();
			System.out.println("Spaces Size at point " + pos.toString() + " for starting Point " +start.toString() + " is " + spaces.size());
		}
		
		
		
		
		int answer = 0;
		for(int i=0; i<mockBoard.length; i++)
		{
			for(int j=0; j<mockBoard[0].length; j++)
			{
				if(!(mockBoard[i][j] == null))
				{
					Space s = mockBoard[i][j];
					if(s instanceof Wall)
					{
						Wall w = (Wall) s;
						if(w.wallColor.equalsIgnoreCase("OPEN"))
							answer++;
					}
				}
			}
		}
		
		
		System.out.println("Total Open Space at point " + pos.toString() + " for starting point " + start.toString() + " is " + answer);
		return answer;
	}
	
	
	private Space[][] mockBoard() 
	{
		Space[][] mockBoard = new Space[board.length][board[0].length];
		for(int i=0; i<board.length; i++)
		{
			for(int j=0; j<board[0].length; j++)
			{
				if((board[i][j] != null))
				{
					mockBoard[i][j] = new Wall("Closed");
				}
			}
		}
		
		// TODO Auto-generated method stub
		return mockBoard;
	}
	
	
	
}
