package tsp.actions.crossers;

import java.util.Random;

import tsp.actions.DataFactory;
import tsp.actions.MyRandom;
import tsp.objects.chromosomes.Chromosome;

public class RingCrosser implements Crosser
{
	//Private Memebers
	private Random random;
	
	//Constructors
	public RingCrosser() 
	{
		this.random = MyRandom.getInstance().random;
	}
	
	//Getters
	@Override
	public String getDescription() { return "Ring"; }
	
	//Public Methods
	@Override
	public Chromosome[] cross(Chromosome parent1, Chromosome parent2) throws Exception 
	{		
		int p1Size = parent1.getSize();
		double[][] distanceIndex = parent1.getDistanceIndex();
		double[][] verticies = parent1.getDistanceIndex();
		
		Chromosome combinedChromosome = combine(parent1, parent2);
		
		int cutPoint1 = this.random.nextInt(p1Size);
		int cutPoint2 = cutPoint1 + p1Size;
		
		Chromosome child1 = DataFactory.createNewChromosome(parent1.populationType, combinedChromosome.getSubsection(cutPoint1, p1Size), distanceIndex, verticies);
		Chromosome child2 = DataFactory.createNewChromosome(parent1.populationType, combinedChromosome.getSubsectionReverse(cutPoint2, p1Size), distanceIndex, verticies);
		
		child1.swapDuplicates(child2);
		Chromosome children[] = DataFactory.createNewChromosomeArray(parent1.populationType, 2);
		
		child1.setDescription("Chromosome");
		child2.setDescription("Chromosome");
		
		child1.calculateFitnessScore();
		child2.calculateFitnessScore();
		
		children[0] = child1;
		children[1] = child2;
		
		return children;
	}

	//Private Methods
	private Chromosome combine(Chromosome parent1, Chromosome parent2) throws Exception
	{
		
		int p1Size = parent1.getSize();
		
		int combinedSize = p1Size + p1Size;
		
		double[][] distanceIndex = parent1.getDistanceIndex();
		double[][] verticies = parent1.getDistanceIndex();
		
		Chromosome combinedChromosome = DataFactory.createNewChromosome(parent1.populationType, combinedSize, distanceIndex, verticies);
		
		for(int i = 0; i < p1Size; i++)
			combinedChromosome.addDestination(parent1.getDestination(i));
		
		for(int i = 0; i < p1Size; i++)
			combinedChromosome.addDestination(parent2.getDestination(i));
		
		return combinedChromosome;
	}

}
