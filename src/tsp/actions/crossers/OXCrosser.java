package tsp.actions.crossers;

import java.util.Random;

import tsp.actions.DataFactory;
import tsp.actions.MyRandom;
import tsp.objects.chromosomes.Chromosome;

public class OXCrosser implements Crosser
{
	//Private Memebrs
	private Random random;
	
	//Constructors
	public OXCrosser() 
	{
		this.random = MyRandom.getInstance().random;
	}
	
	//Getters
	@Override
	public String getDescription() { return "OX"; }
	
	//Public Methods
	@Override
	/**
	 * OX Crossover
	 * @param parent1
	 * @param parent2
	 * @return Chromosome[] offspring
	 */
	public Chromosome[] cross(Chromosome parent1, Chromosome parent2) throws Exception
	{
		int size = parent1.getSize();
		
		int start = this.random.nextInt(size - (size / 4));
		int length = this.random.nextInt((size / 4) - (size / 8)) + (size / 8) + 1;
		
		Chromosome child1 = parent2.copy();
		Chromosome child2 = parent1.copy();
	
		this.swapSection(parent2, child1, start, length);
		this.swapSection(parent1, child2, start, length);
		
		Chromosome children[] = DataFactory.createNewChromosomeArray(parent1.populationType, 1);
		
		children[0] = child1;
		children[1] = child2;
		
		return children;
	}

	//Private Methods
	private void swapSection(Chromosome chromosome1, Chromosome chromosome2, int start, int length) throws Exception
	{
		int[] swapSection = chromosome1.getSubsection(start, length);
		chromosome2.removeElementsInSection(swapSection);
		chromosome2.slide(start, swapSection);
	}
	
}
