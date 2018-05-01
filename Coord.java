
public class Coord
{
	public Coord(int i, int j) 
	{
		// TODO Auto-generated constructor stub
		x = i;
		y = j;
	}
	int x; 
	int y;
	
	public Coord addition(Coord c)
	{
		return new Coord(c.x + x, c.y + y);
	}
	
	public String toString()
	{
		return "(" + x+ "," + y + ")";
	}
	public boolean equals(Coord c)
	{
		return (x == c.x) && (y == c.y);
	}
}
