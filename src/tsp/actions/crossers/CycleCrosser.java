package tsp.actions.crossers;

import java.util.Random;

import tsp.actions.DataFactory;
import tsp.objects.chromosomes.Chromosome;

public class CycleCrosser implements Crosser
{
	//Private Members
	private float probability; 
	private Random random;
	private Chromosome child1;
	private Chromosome child2;
	
	//Constructors
	public CycleCrosser(float probability) 
	{
		this.probability = probability;
		this.random = new Random();
	}

	//Getters
	@Override
	public String getDescription(){ return "Cycle"; }
	
	//Public Methods
	@Override
	public Chromosome[] cross(Chromosome parent1, Chromosome parent2) throws Exception 
	{
		//parent1.display();
		//parent2.display();
		
		if(this.random.nextFloat() <= this.probability)
		{
			int chromosomeSize = parent1.getSize();
			double[][] distanceIndex = parent1.getDistanceIndex();
			
			this.child1 = DataFactory.createNewChromosome(parent1.populationType, chromosomeSize, distanceIndex);
			this.child2 = DataFactory.createNewChromosome(parent1.populationType, chromosomeSize, distanceIndex);
			
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
		}
		else
		{
			this.child1 = parent1.copy();
			this.child2 = parent2.copy();
		}
		
		Chromosome children[] = DataFactory.createNewChromosomeArray(parent1.populationType, 2);
		
		children[0] = child1;
		children[1] = child2;
		
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
