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
		
		Chromosome winner = null;
		Chromosome loser = null;
		
		if(parent1.getFitnessScore() > parent2.getFitnessScore())
		{
			winner = parent2;
			loser = parent1;
		}
		else
		{
			winner = parent1;
			loser = parent2;
		}
	
		this.swapSection(loser, winner, start, length);
		this.swapSection(winner, loser, start, length);
		Chromosome children[] = DataFactory.createNewChromosomeArray(parent1.populationType, 2);
		children[0] = winner;
		children[1] = loser;
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
