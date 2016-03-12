package tsp.actions.mutators;

import java.util.Random;

import tsp.objects.chromosomes.Chromosome;

public class ScrambleMutator implements Mutator
{
	//Private Members
	private float probability;
	private Random random;
	
	//Constructors
	public ScrambleMutator(float probability)
	{
		this.probability = probability;
		this.random = new Random();
	}
	
	//Getters
	@Override
	public String getDescription() { return "Scramble"; }
	
	//Public Methods
	@Override
	public Chromosome mutate(Chromosome parent) throws Exception 
	{
		Chromosome child = parent.copy();
		
		if(random.nextFloat() <= this.probability)
		{
			int size = parent.getSize();
			int p1 = random.nextInt(size);
			int p2 = 0;
			
			do
			{
				p2 = random.nextInt(size);
			} while(p2 == p1);
			
			//System.out.println(p1 + " " + p2);
			
			child.scramble(p1, p2);
		}
		return child;
	}
}
