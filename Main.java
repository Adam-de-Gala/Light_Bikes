import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

public class Main extends JFrame
{	
	
	final static int SIZE = 750;
	public static void main(String arg[]) 
	{
		//TODO Auto-generated method stub
	    MyJFrame f = new MyJFrame();
	    f.setTitle("Light Bikes ");
	    f.setSize(SIZE, SIZE+50);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setVisible(true);
	    f.addKeyListener(f);
	   
	    
	    f.setUpGame();
	    f.repaint();
	    while(f.playGame())
		{
			f.repaint();
		}
	    f.repaint();
		
	}
	
	static class MyJFrame extends JFrame implements KeyListener  
	{
		
		
			 static Space[][] board = new Space[75][75];
			 private Image offScreenImageDrawed = null;
		     private Graphics offScreenGraphicsDrawed = null;  
			
			
			
			//Parameter Values
			int SquareSize = SIZE / board.length;
			static Bike[] bikes = new Bike[3];
			static AI[] computer = new AI[3];
			boolean keyPressed = false;
			PriorityQueue<Event> eventSimulator = new PriorityQueue<Event>();
			
			public void setUpGame()
			{
				//Creation of Bikes
				for(int i=0; i<bikes.length;i++)
				{
					if(i==0)
					{
						bikes[i] = new Bike(new Coord(3, 72), "Blue", "Light Blue", board);
						bikes[i].humanControled = false;
						bikes[i].move = new Coord(0,-1);
						eventSimulator.add(new Event(i, "Move", 0));
						bikes[i].speed = 30;
						
						//Level 1 intelligence
						computer[i] = new AI(bikes, board, i,1);
						computer[i].speed = 31;
						eventSimulator.add(new Event(i, "Think", 0));
					}
					if(i==1)
					{
						bikes[i] = new Bike(new Coord(45, 3), "Red", "Orange", board);
						bikes[i].humanControled = false;
						bikes[i].move = new Coord(0,1);
						eventSimulator.add(new Event(i, "Move", 0));
						bikes[i].speed = 30;
						
						//Level 1 intelligence
						computer[i] = new AI(bikes, board, i,2);
						computer[i].speed = 31;
						eventSimulator.add(new Event(i, "Think", 0));
						
					}
					if(i==2)
					{
						bikes[i] = new Bike(new Coord(72, 74), "Orange", "Grey", board);
						bikes[i].humanControled = false;
						eventSimulator.add(new Event(i, "Move", 0));
						
						//Level 2 intelligence
						computer[i] = new AI(bikes, board, i,3);
						computer[i].speed = 31;
						eventSimulator.add(new Event(i, "Think", 0));
						
						bikes[i].speed = 30;
					}
				}
				
			}
			
			
			public boolean playGame()
			{

					if(!eventSimulator.isEmpty())
					{
						Event e = eventSimulator.poll();
						
						//System.out.println(e.tag);
						Bike b = bikes[e.tag];
						if(e.desc.equalsIgnoreCase("Move"))
						{
							//System.out.println("MOVED. Tag " + e.tag );
							boolean moved = b.Move();
							if(moved)
							{
								eventSimulator.add(new Event(e.tag, e.desc, e.timeStamp + (101- b.speed)));
								if(b.color.equalsIgnoreCase("Orange"))
									System.out.println("Move Event added, new TimeStamp = " +  (e.timeStamp + (101- b.speed)));
							}
							else
							{
								bikes[e.tag] = null;
					
								Event placeHolder = new Event(0, "Place Holder", e.timeStamp + (101- b.speed));
								placeHolder.increaseTimeStamp =  (101- b.speed);
								eventSimulator.add(placeHolder);
							}
							
						}
						if(e.desc.equalsIgnoreCase("Think"))
						{
							//System.out.println("Pulled event think ");
							if(bikes[e.tag] != null)
							{
								computer[e.tag].MakeDescision();
								//computer[e.tag].updateBike(bikes);
								
								e.timeStamp += (101 - computer[e.tag].speed);
								eventSimulator.add(e);
								
								if(b.color.equalsIgnoreCase("Orange"))
									System.out.println("Think Event added , new TimeStamp = " +  e.timeStamp);
	
								
								
								//System.out.println("Added event think ");
							}
							else
							{
								Event placeHolder = new Event(0, "Place Holder", e.timeStamp +  (101 - computer[e.tag].speed));
								placeHolder.increaseTimeStamp =  (101- computer[e.tag].speed);
								eventSimulator.add(placeHolder);
							}
							
						}
						if (e.desc.equalsIgnoreCase("Place Holder"))
						{
							e.timeStamp += e.increaseTimeStamp;
							eventSimulator.add(e);
						}

					}
					else
						return false;
					
					try
					{
						//System.out.println("Size is " + (bikes.length + 1 - eventSimulator.size()));
						TimeUnit.MILLISECONDS.sleep(5);
					} 
					catch (InterruptedException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				return true;			
			}
			
			
			//public 
			public void paint(Graphics g) 
		    {				
				//super.paint(g);
				
				final Dimension d = getSize();
	            if (offScreenImageDrawed == null) {   
	                // Double-buffer: clear the offscreen image.                
	                offScreenImageDrawed = createImage(d.width, d.height);   
	            }          
	            offScreenGraphicsDrawed = offScreenImageDrawed.getGraphics();      
	            offScreenGraphicsDrawed.setColor(Color.white);
	            offScreenGraphicsDrawed.fillRect(0, 0, d.width, d.height) ;                           
	            /////////////////////
	            // Paint Offscreen //
	            /////////////////////
	            drawBoard(offScreenImageDrawed.getGraphics(), bikes);
	            g.drawImage(offScreenImageDrawed, 0, 0, null);
	            
		    }
			
			//For some unknown reason, the board is drawn out of screen 
			public final int OFFSET = 50; // Migos! 
			private void drawBoard(final Graphics g, Bike[] bikes)
			{
				
				//Now Draw Board
				for(int i=0; i<board.length;i++)
				{
					for(int j=0; j<board[0].length;j++)
					{
						
						g.setColor(Color.gray);
						g.drawRect(i*SquareSize, j*SquareSize + OFFSET, i*SquareSize + SquareSize , j*SquareSize + SquareSize + OFFSET);

						if(!(board[i][j] == null))
						{
							Space s = board[i][j];
							g.setColor(getColor(s.color));
							if(s instanceof Wall)
							{
								Wall w = (Wall) s;
								g.setColor(getColor(w.wallColor));
							}
							//System.out.println("None null " + board[i][j].color);
							
							g.fillRect(i*SquareSize, j*SquareSize + OFFSET, SquareSize+1, SquareSize+1);
						}	
					}
				}
				
				//System.out.println("*** PAINTING ***");
				for(Bike bike : bikes)
				{
					if(bike != null)
					{
						//System.out.print(bike.color + " " + bike.pos.toString() + " ");
						g.setColor(getColor(bike.color));
						g.fillRect(bike.pos.x*SquareSize, bike.pos.y*SquareSize + OFFSET, SquareSize, SquareSize);
						
					}
					
				}
//				System.out.println();
//				System.out.println("*** PAINTING ***");
			}
			
			
			private Color getColor(String color)
			{
				//System.out.println(color);
				if(color.equalsIgnoreCase("OPEN"))
					return Color.BLACK;
				if(color.equalsIgnoreCase("Grey"))
					return Color.GRAY;
				if(color.equalsIgnoreCase("Blue"))
					return Color.BLUE;
				if(color.equalsIgnoreCase("Light Blue"))
					return Color.CYAN;
				if(color.equalsIgnoreCase("Red"))
					return Color.RED;
				if(color.equalsIgnoreCase("Orange"))
					return Color.ORANGE;
				return Color.GRAY;
			}

			@Override
			public void keyTyped(KeyEvent e) 
			{
				keyPressed = true;
				// TODO Auto-generated method stub
			    int key = e.getKeyCode();
			    String direction = null;
			    
			    if (key == KeyEvent.VK_LEFT) 
			    {
			    		direction = "West";
			    }

			    if (key == KeyEvent.VK_RIGHT) 
			    {
			    		direction = "East";
			    }

			    if (key == KeyEvent.VK_UP) 
			    {
			    		direction = "North";
			    }

			    if (key == KeyEvent.VK_DOWN) 
			    {
			    		direction = "South";
			    }
			    
			    if(direction != null)
			    {
				    for(int i=0; i<bikes.length;i++)
				    {
				    		if(bikes[i] != null && bikes[i].humanControled)
				    			bikes[i].ChangeDirection(direction);
				    		//System.out.println("Changed Direction");
				    }
			    }
			    
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e)
			{
				// TODO Auto-generated method stub
			    int key = e.getKeyCode();
			    String direction = null;
			    
			    if (key == KeyEvent.VK_LEFT) 
			    {
			    		direction = "West";
			    }
		
			    if (key == KeyEvent.VK_RIGHT) 
			    {
			    		direction = "East";
			    }
		
			    if (key == KeyEvent.VK_UP) 
			    {
			    		direction = "North";
			    }
		
			    if (key == KeyEvent.VK_DOWN) 
			    {
			    		direction = "South";
			    }
			    
			    if(direction != null)
			    {
				    for(int i=0; i<bikes.length;i++)
				    {
				    		if(bikes[i] != null && bikes[i].humanControled)
				    		{
				    			bikes[i].ChangeDirection(direction);
				    			//System.out.println("Changed Direction");
				    		}
				    }
			    }
			   
			
		}
			
		}
	
	
	
	
}
