package tsp.actions.selectors;

import java.util.List;

import tsp.objects.chromosomes.Chromosome;

public abstract class Selector 
{
	//Private Members
	protected List<Chromosome> chromosomes;
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
	public void setChromosomes(List<Chromosome> chromosomes, double mostOptimalScore) 
	{ 
		this.chromosomes = chromosomes; 
		this.numChromosomes = chromosomes.size();
		this.mostOptimalScore = mostOptimalScore * .9;
		this.sumFitnessValues();
	}

	//Public Methods
	public abstract int select();
	
	public void sumFitnessValues()
	{
		this.totalFitnessScore = 0;
		for(Chromosome chromosome : chromosomes)
		{
			this.totalFitnessScore += (chromosome.getFitnessScore() - mostOptimalScore);
		}
	}
	
	// removes the element in the specified index from this.chromosomes
	public void remove(int index)
	{
		//this.numChromosomes--;
		//this.totalFitnessScore -= this.chromosomes.get(index).getFitnessScore();
		//this.chromosomes.remove(index);
	}
}
