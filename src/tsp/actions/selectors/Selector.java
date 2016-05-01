package tsp.actions.selectors;

import tsp.objects.chromosomes.Chromosome;

public abstract class Selector 
{
	//Private Members
	protected Chromosome chromosomes[];
	protected double totalFitnessScore;
	protected int numChromosomes;
	protected double mostOptimalScore;
	
	//Constructors
	public Selector()
	{
		
	}
	
	//Getters
	public int getNumChromosomes() { return this.numChromosomes; }
	public abstract String getDescription();
	
	//Setters
	public void setChromosomes(Chromosome chromosomes[], double mostOptimalScore) 
	{ 
		this.chromosomes = chromosomes; 
		this.numChromosomes = chromosomes.length;
		this.mostOptimalScore = mostOptimalScore * .9;
		this.sumFitnessValues();
	}

	//Public Methods
	public abstract int select();
	
	public void sumFitnessValues()
	{
		this.totalFitnessScore = 0;
		for(int i = 0; i < this.chromosomes.length; i++)
		{
			this.totalFitnessScore += (chromosomes[i].getFitnessScore() - mostOptimalScore);
		}
	}
}
