package tsp.actions.mutators;

import java.util.Random;

import tsp.actions.MyRandom;
import tsp.objects.chromosomes.Chromosome;

public class ScrambleMutator implements Mutator
{
	//Private Members
	private Random random;
	
	//Constructors
	public ScrambleMutator()
	{
		this.random = MyRandom.getInstance().random;
	}
	
	//Getters
	@Override
	public String getDescription() { return "Scramble"; }
	
	//Public Methods
	@Override
	public void mutate(Chromosome parent) throws Exception 
	{
		int size = parent.getSize();
		int p1 = random.nextInt(size);
		int p2 = 0;
		
		do
		{
			p2 = random.nextInt(size);
		} while(p2 == p1);

		parent.scramble(p1, p2);
	}
}
