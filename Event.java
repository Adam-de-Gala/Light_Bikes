
public class Event implements Comparable<Event> {

	
	int tag; 
	int timeStamp = 0;
	String desc = "";
	
	int increaseTimeStamp = 0; //A possible remedy for realtive timemoving. 
	public Event(int tag, String desc, int timeStamp)
	{
		this.tag = tag;
		this.desc = desc;
		this.timeStamp = timeStamp;
	}
	
	



	@Override
	public int compareTo(Event o) 
	{
		if(o == null)
			return 1;
		
		if(o.timeStamp < this.timeStamp)
			return 1;
		if(o.timeStamp > this.timeStamp)
			return -1;
	
		// TODO Auto-generated method stub
		return 0;
	}

}
