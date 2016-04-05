package tsp.actions.mutators;

import java.util.Random;

import tsp.actions.MyRandom;
import tsp.objects.chromosomes.Chromosome;

public class SwapMutator implements Mutator
{
	//Private Members
	private Random random;
	
	//Constructors
	public SwapMutator()
	{
		this.random = MyRandom.getInstance().random;
	}
	
	//Getters
	@Override
	public String getDescription() { return "Swap"; }
	
	//Public Methods
	@Override
	public void mutate(Chromosome chromosome) throws Exception 
	{
		int size = chromosome.getSize();
		int p1 = random.nextInt(size);
		int p2 = 0;
		do
		{
			p2 = random.nextInt(size);
		} while(p2 == p1);
		
		chromosome.swapDestinations(p1, p2);
	}
}
