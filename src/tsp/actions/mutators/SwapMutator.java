package tsp.actions.mutators;

import java.util.Random;

import tsp.objects.Chromosome;

public class SwapMutator implements Mutator
{
	
	private float probability;
	private Random random;
	
	public SwapMutator(float probability)
	{
		this.probability = probability;
		this.random = new Random();
	}
	
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
			
			child.swapDestinations(p1, p2);
		}
		return child;
	}

}
