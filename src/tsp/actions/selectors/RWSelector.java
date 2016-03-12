package tsp.actions.selectors;

import java.util.Random;

import tsp.objects.Chromosome;

// Selector that uses the Roulette-Wheel algorithm 
public class RWSelector extends Selector
{
	//Private Members
	private Random random;

	//Constructors
	public RWSelector() 
	{
		super();
		this.random = new Random();
	}

	//Getters
	@Override
	public String getDescription() { return "RW"; }
	
	//Public Methods
	@Override
	// Use Roulette-Wheel to select a chromosome
	public Chromosome select() 
	{
		
		//System.out.println(this.totalFitnessScore);
		double stop = this.random.nextInt((int) this.totalFitnessScore);
		
		double currentFitness = 0;
		
		int index = 0; //this.random.nextInt(this.numChromosomes);
		
		while(currentFitness <= stop)
		{
			currentFitness += this.chromosomes.get(index).getFitnessScore();
			index = (index + 1) % this.numChromosomes;
		}
		
		Chromosome selectedChromosome = this.chromosomes.get(index);
		
		this.numChromosomes--;
		this.totalFitnessScore -= selectedChromosome.getFitnessScore();
		this.chromosomes.remove(index);
		
		return selectedChromosome;
	}

}
