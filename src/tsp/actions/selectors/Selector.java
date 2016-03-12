package tsp.actions.selectors;

import java.util.List;

import tsp.objects.chromosomes.Chromosome;

public abstract class Selector 
{
	//Private Members
	protected List<Chromosome> chromosomes;
	protected double totalFitnessScore;
	protected int numChromosomes;
	
	//Constructors
	public Selector()
	{
		this.totalFitnessScore = 0;
	}
	
	//Getters
	public int getNumChromosomes() { return this.numChromosomes; }
	public abstract String getDescription();
	
	//Setters
	public void setChromosomes(List<Chromosome> chromosomes) 
	{ 
		this.chromosomes = chromosomes; 
		this.numChromosomes = chromosomes.size();
		this.sumFitnessValues();
	}

	//Public Methods
	public abstract Chromosome select();
	
	public void sumFitnessValues()
	{
		for(Chromosome chromosome : chromosomes)
		{
			this.totalFitnessScore += chromosome.getFitnessScore();
		}
	}
	
	// removes the element in the specified index from this.chromosomes
	public void remove(int index)
	{
		this.numChromosomes--;
		this.totalFitnessScore -= this.chromosomes.get(index).getFitnessScore();
		this.chromosomes.remove(index);
	}
}
