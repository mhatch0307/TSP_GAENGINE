package tsp.actions.selectors;

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
	public int select()
	{
		double mostOptimalIndexScore = 999999;
		int mostOptimalChromosomeIndex = 0;
		for(int i = 0; i < this.numChromosomes; i++)
		{
			if(this.chromosomes.get(i).getFitnessScore() < mostOptimalIndexScore)
			{
				mostOptimalChromosomeIndex = i;
				mostOptimalIndexScore = this.chromosomes.get(i).getFitnessScore();
			}
		}
		
		return mostOptimalChromosomeIndex;
	}
	
}
