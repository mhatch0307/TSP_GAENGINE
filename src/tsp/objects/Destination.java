package tsp.objects;

public class Destination 
{
	protected int ID;
	protected String description;
	protected double distanceToNext;
	protected double distanceToPrevious;
	protected double xPos;
	protected double yPos;
	
	//CONSTRUCTORS
	
	public Destination(int ID, String description, double distanceToNext, double distanceToPrevious, double xPos, double yPos)
	{
		this.description = description;
		this.distanceToNext = distanceToNext;
		this.distanceToPrevious = distanceToPrevious;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public Destination(int ID)
	{
		this.ID = ID;
		this.distanceToNext = 0.0;
		this.distanceToPrevious = 0.0;
		this.xPos = 0;
		this.yPos = 0;
	}
	
	//SETTERS
	
	public void setID(int ID) { this.ID = ID; }
	public void setdescription(String description) { this.description = description; }
	public void setDistanceToNext(int distanceToNext) { this.distanceToNext = distanceToNext; }
	public void setDistanceToPrevious(int distanceToPrevious) { this.distanceToPrevious = distanceToPrevious; }
	public void setXPos(double xPos) { this.xPos = xPos; }
	public void setYPos(double yPos) { this.yPos = yPos; }
	
	//GETTERS
	
	public String getDescription() { return this.description; }
	public int getID() { return this.ID; }
	public double getDistanceToNext() { return this.distanceToNext; }
	public double getDistanceToPrevious() { return this.distanceToPrevious; }
	public double getXPos() { return this.xPos; }
	public double getYPos() { return this.yPos; }
	
	public Destination copy()
	{
		Destination destination = new Destination(this.ID);
		destination.distanceToNext = this.distanceToNext;
		destination.distanceToPrevious = this.distanceToPrevious;
		return destination;
	}
}
