package tsp.actions.crossers;

import java.util.Random;

import tsp.objects.Chromosome;

public class RingCrosser implements Crosser
{

	private float probability; 
	private Random random;
	private Chromosome child1;
	private Chromosome child2;
	
	// Constructors
	
	public RingCrosser(float probability) 
	{
		this.probability = probability;
		this.random = new Random();
	}
	
	@Override
	public Chromosome[] cross(Chromosome parent1, Chromosome parent2) throws Exception 
	{		
		//parent1.display();
		//parent2.display();
		Chromosome child1;
		Chromosome child2;
		
		if(this.random.nextFloat() <= this.probability)
		{
			int p1Size = parent1.getSize();
			double[][] distanceIndex = parent1.getDistanceIndex();
			
			Chromosome combinedChromosome = combine(parent1, parent2);
			
			int cutPoint1 = this.random.nextInt(p1Size);
			int cutPoint2 = cutPoint1 + p1Size;
			
			child1 = new Chromosome(combinedChromosome.getSubsection(cutPoint1, p1Size), distanceIndex);
			child2 = new Chromosome(combinedChromosome.getSubsectionReverse(cutPoint2, p1Size), distanceIndex);
			
			//combinedChromosome.display();
		
			//System.out.println(cutPoint1 + " " + cutPoint2);
			
			//child1.display();
			//child2.display();
			
			child1.swapDuplicates(child2);
		}
		
		child1 = parent1.copy();
		child2 = parent2.copy();
		
		//child1.display();
		//child2.display();
		
		return new Chromosome[] {child1, child2};
	}
	
	private Chromosome combine(Chromosome parent1, Chromosome parent2) throws Exception
	{
		
		int p1Size = parent1.getSize();
		
		int combinedSize = p1Size + p1Size;
		
		double[][] distanceIndex = parent1.getDistanceIndex();
		
		Chromosome combinedChromosome = new Chromosome(combinedSize, distanceIndex);
		
		for(int i = 0; i < p1Size; i++)
			combinedChromosome.addDestination(parent1.getDestination(i));
		
		for(int i = 0; i < p1Size; i++)
			combinedChromosome.addDestination(parent2.getDestination(i));
		
		return combinedChromosome;
	}

}
