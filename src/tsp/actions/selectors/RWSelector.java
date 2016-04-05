package tsp.actions.selectors;

import java.util.Random;

import tsp.actions.MyRandom;

// Selector that uses the Roulette-Wheel algorithm 
public class RWSelector extends Selector
{
	//Private Members
	private Random random;

	//Constructors
	public RWSelector() 
	{
		super();
		this.random = MyRandom.getInstance().random;
	}

	//Getters
	@Override
	public String getDescription() { return "RW"; }
	
	//Public Methods
	@Override
	// Use Roulette-Wheel to select a chromosome
	public int select() 
	{
		double stop = this.random.nextInt((int) this.totalFitnessScore);
		
		double currentFitness = 0;
		
		int size = this.chromosomes.size();
		
		for(int i = 0; i < size; i++)
		{
			currentFitness += (this.totalFitnessScore - (this.chromosomes.get(i).getFitnessScore() - this.mostOptimalScore));
			if(currentFitness >= stop)
				return i;
		}
		
		return 0;
	}

}
