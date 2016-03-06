package tsp.actions.crossers;

import java.util.Random;

import tsp.objects.Chromosome;

public class XCrosser implements Crosser
{
	private float probability; 
	private Random random;
	
	// Constructors
	
	public	XCrosser(float probability) 
	{
		this.probability = probability;
		this.random = new Random();
	}
	
	//Getters
	public float getProbability() { return this.probability; }
	
	//Setters
	public void setProbability(float probability) { this.probability = probability; }

	@Override
	public Chromosome[] cross(Chromosome parent1, Chromosome parent2) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}
}
