package tsp.actions.crossers;

import java.util.Random;

import tsp.objects.Chromosome;
import tsp.objects.Destination;

public class OXCrosser implements Crosser
{

	private float probability; 
	private Random random;
	
	// Constructors
	
	public OXCrosser(float probability) 
	{
		this.probability = probability;
		this.random = new Random();
	}
	
	@Override
	/**
	 * OX Crossover
	 * @param parent1
	 * @param parent2
	 * @return Chromosome[] offspring
	 */
	public Chromosome[] cross(Chromosome parent1, Chromosome parent2) throws Exception
	{
		int size = parent1.getSize();
		
		int start = this.random.nextInt(size - (size / 4));
		int length = this.random.nextInt((size / 4) - (size / 8)) + (size / 8) + 1;
		
		//start = 3;
		//length = 3;
		
		Chromosome child1 = parent2.copy();
		Chromosome child2 = parent1.copy();
		
		//System.out.print("Child 1 Before: "); child1.display();
		//System.out.print("Child 2 Before: "); child2.display(); 
		
		if(this.random.nextFloat() <= this.probability)
		{
			this.swapSection(parent1, child1, start, length);
			this.swapSection(parent2, child2, start, length);
		}
		//System.out.print("Child 1 After: "); child1.display();
		//System.out.print("Child 2 After: "); child2.display();
		
		return new Chromosome[] {child1, child2};
	}

	private void swapSection(Chromosome chromosome1, Chromosome chromosome2, int start, int length) throws Exception
	{
		int[] swapSection = chromosome1.getSubsection(start, length);
		
		chromosome2.removeElementsInSection(swapSection);
		
		chromosome2.slide(start, swapSection);
	}
	
}
