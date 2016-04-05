package tsp.actions.crossers;

import tsp.actions.DataFactory;
import tsp.objects.chromosomes.Chromosome;

public class CycleCrosser implements Crosser
{
	//Private Members
	private Chromosome child1;
	private Chromosome child2;
	
	//Constructors
	public CycleCrosser() 
	{

	}

	//Getters
	@Override
	public String getDescription(){ return "Cycle"; }
	
	//Public Methods
	@Override
	public Chromosome[] cross(Chromosome parent1, Chromosome parent2) throws Exception 
	{
		int chromosomeSize = parent1.getSize();
		double[][] distanceIndex = parent1.getDistanceIndex();
		double[][] verticies = parent1.getVerticies();
		
		this.child1 = DataFactory.createNewChromosome(parent1.populationType, chromosomeSize, distanceIndex, verticies);
		this.child2 = DataFactory.createNewChromosome(parent1.populationType, chromosomeSize, distanceIndex, verticies);
		
		int[] cycles = new int[chromosomeSize];
		
		int destinationsUsed = 0;
		int cycleCount = 0;
		int startIndex = 0;
		
		while(destinationsUsed < chromosomeSize)
		{				
			while(cycles[startIndex] > 0) // find the first index not in a cycle
				startIndex++;
			
			// find a cycle
			cycleCount++;
			int startValue = parent1.getDestination(startIndex);
			int currentIndex = startIndex;
			int currentValue = parent2.getDestination(currentIndex);
			cycles[currentIndex] = cycleCount;
			
			updateDestinations(cycleCount, currentIndex,
					startValue, 
					currentValue);
			destinationsUsed++;
			
			while(currentValue != startValue)
			{		
				currentIndex = parent1.indexOf(currentValue);
				cycles[currentIndex] = cycleCount;
				currentValue = parent2.getDestination(currentIndex);
				
				updateDestinations(cycleCount, currentIndex,
									parent1.getDestination(currentIndex), 
									currentValue);
				
				destinationsUsed++;
			}
		}
		
		child1.calculateFitnessScore();
		child2.calculateFitnessScore();
		
		Chromosome children[] = DataFactory.createNewChromosomeArray(parent1.populationType, 1);
		
		if(child1.getFitnessScore() > child2.getFitnessScore())
			children[0] = child2;
		else
			children[0] = child1;
		
		return children;		
	}
	
	//Private Methods
	private void updateDestinations(int cycleCount, int index, int parent1Destination, int parent2Destination) throws Exception
	{
		if(cycleCount % 2 == 1)
		{
			this.child1.setDestination(parent1Destination, index);
			this.child2.setDestination(parent2Destination, index);
		}
		else
		{
			this.child1.setDestination(parent2Destination, index);
			this.child2.setDestination(parent1Destination, index);
		}
	}
}
