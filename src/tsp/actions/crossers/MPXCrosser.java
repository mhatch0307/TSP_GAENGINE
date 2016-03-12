package tsp.actions.crossers;

import java.util.Random;

import tsp.objects.Chromosome;

public class MPXCrosser implements Crosser
{
	//Private Members
	private float probability; 
	private Random random;
	
	//Constructors
	public	MPXCrosser(float probability) 
	{
		this.probability = probability;
		this.random = new Random();
	}
	
	//Getters
	@Override
	public String getDescription() { return "MPX"; }
	
	//Public Methods
	@Override
	public Chromosome[] cross(Chromosome parent1, Chromosome parent2) throws Exception 
	{
		Chromosome child1;
		Chromosome child2;
		
		if(this.random.nextFloat() <= this.probability)
		{
			int size = parent1.getSize();
			int startIndex = this.random.nextInt(size - (size / 4));
			int length = this.random.nextInt((size / 4) - (size / 8)) + (size / 8) + 1;		
			int endIndex = startIndex + length - 1;
			
			/*Destination*/int beggining = parent1.getDestination(startIndex);
			/*Destination*/int end = parent1.getDestination(endIndex);
			
			int rIndexOne = parent2.indexOf(beggining);
			int rIndexTwo = parent2.indexOf(end);
			
			child1 = parent2.copy();
			child2 = parent2.copy();
		
			child1.invert(rIndexOne, rIndexTwo);
			child2.invert(rIndexTwo, rIndexOne);
		
			/*Destination*/int[] swapSection = parent1.getSubsection(startIndex, length);
		
			child1.removeElementsInSection(swapSection);
			child2.removeElementsInSection(swapSection);
			
			int diff = length - 2;
			
			startIndex = (rIndexOne > rIndexTwo)? ((rIndexTwo > 0)? rIndexTwo - 1 : size - 1) : ((rIndexTwo >= diff)? rIndexTwo - diff : size + (rIndexTwo - diff - 1));
			
			child1.insertSubSection(startIndex, swapSection);
			
			//child1.display();
			
			startIndex = (rIndexTwo > rIndexOne)? ((rIndexOne > 0)? rIndexOne - 1 : size - 1) : ((rIndexOne >= diff)? rIndexOne - diff : size + (rIndexOne - diff -1));
			
			child2.insertSubSection(startIndex, swapSection);
			
			//child2.display();
		}
		else
		{
			child1 = parent1.copy();
			child2 = parent2.copy();
		}
		return new Chromosome[] {child1, child2};
	}

}
