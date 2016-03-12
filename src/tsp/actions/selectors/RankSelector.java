package tsp.actions.selectors;

import tsp.objects.chromosomes.Chromosome;

public class RankSelector extends Selector 
{
	
	//Constructors
	public RankSelector() 
	{
		super();
	}
	
	//Getters
	@Override
	public String getDescription() { return "Rank"; }
	
	//Public Methods
	public Chromosome select()
	{
		double mostOptimalIndexScore = 999999;
		Chromosome mostOptimalChromosome = null;
		int mostOptimalChromosomeIndex = 0;
		for(int i = 0; i < this.numChromosomes; i++)
		{
			if(this.chromosomes.get(i).getFitnessScore() < mostOptimalIndexScore)
			{
				mostOptimalChromosomeIndex = i;
				mostOptimalIndexScore = this.chromosomes.get(i).getFitnessScore();
			}
		}
		
		mostOptimalChromosome = this.chromosomes.get(mostOptimalChromosomeIndex);
		this.chromosomes.remove(mostOptimalChromosomeIndex);
		this.numChromosomes--;
		
		return mostOptimalChromosome;
	}
	
}
