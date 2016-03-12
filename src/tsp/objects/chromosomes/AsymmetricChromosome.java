package tsp.objects.chromosomes;

import java.util.Arrays;

import tsp.objects.populations.PopulationType;

public class AsymmetricChromosome extends Chromosome
{
	//Construcctor
	public AsymmetricChromosome(int size, double[][] distanceIndex) 
	{
		super(size, distanceIndex);
		this.populationType = PopulationType.Asymmetric;
	}
	
	public AsymmetricChromosome(int[] destinations, double[][] distanceIndex)
	{
		super(destinations, distanceIndex);
		this.populationType = PopulationType.Asymmetric;
	}
	
	//Copy Constructor
	@Override
	public Chromosome copy()
	{
		Chromosome chromosome = new AsymmetricChromosome(this.destinations.length, this.distanceIndex);
		chromosome.fitnessScore = this.fitnessScore;
		chromosome.destinations = Arrays.copyOf(this.destinations, this.destinations.length);
		chromosome.currentSize = this.currentSize;
		return chromosome;
	}

	//Public Methods
	@Override
	public void calculateFitnessScore()
	{
		this.fitnessScore = 0;
		for(int i = 0; i < this.currentSize; i++)
		{
			int currentID = this.destinations[i];
			int previousID = (i == 0)? this.destinations[this.currentSize - 1] : this.destinations[i - 1];
			int nextID = (i == this.currentSize - 1)? this.destinations[0] : this.destinations[i + 1]; 
			this.fitnessScore += (this.distanceIndex[currentID][previousID] 
							  + this.distanceIndex[currentID][nextID]);
		}	
	}
	
	//Protected Methods
	// Used when updating an existing destination
	@Override
	protected void updateDistanceScore(int destinationIndex, int oldID)
	{
		try
		{
			int previousIndex = (destinationIndex > 0)? destinationIndex - 1 : this.currentSize - 1;
			int previousID = this.destinations[previousIndex];
			
			int currentID = this.destinations[destinationIndex];
			
			int nextIndex = (destinationIndex < this.currentSize - 1)? destinationIndex + 1 : 0;
			int nextID = this.destinations[nextIndex];
			
			this.fitnessScore -= (this.distanceIndex[previousID][oldID]
				  	+ this.distanceIndex[oldID][previousID]
				  	+ this.distanceIndex[oldID][nextID]
				  	+ this.distanceIndex[nextID][oldID]);
			
			this.fitnessScore += (this.distanceIndex[previousID][currentID]
				  	+ this.distanceIndex[currentID][previousID]
				  	+ this.distanceIndex[currentID][nextID]
				  	+ this.distanceIndex[nextID][currentID]);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	// Used when adding a new destination
	@Override
	protected void addDistanceScore(int currentIndex)
	{
		try
		{
			if(currentIndex > 1)
			{
				int previousID = this.destinations[currentIndex - 1];
				int currentID = this.destinations[currentIndex];
				int startID = this.destinations[0];
				
				this.fitnessScore -= (this.distanceIndex[startID][previousID]
									  + this.distanceIndex[previousID][startID]);
				
				
				this.fitnessScore += this.distanceIndex[startID][currentID]
							       + this.distanceIndex[currentID][startID]
								   + this.distanceIndex[currentID][previousID] 
								   + this.distanceIndex[previousID][currentID];
			}
			else if(currentIndex == 1)
			{
				this.fitnessScore += this.distanceIndex[this.destinations[0]][this.destinations[1]] 
								  +	 this.distanceIndex[this.destinations[1]][this.destinations[0]]; 
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}	
}