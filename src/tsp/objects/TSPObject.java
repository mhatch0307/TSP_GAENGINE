package tsp.objects;

public class TSPObject 
{
	private double distanceIndex[][];
	private double verticies[][];
	
	//Constructor
	public TSPObject(double distanceIndex[][], double verticies[][])
	{
		this.distanceIndex = distanceIndex;
		this.verticies = verticies;
	}
	
	//Getters
	public double[][] getDistanceIndex() { return this.distanceIndex; }
	public double[][] getVerticies() { return this.verticies; }
}
