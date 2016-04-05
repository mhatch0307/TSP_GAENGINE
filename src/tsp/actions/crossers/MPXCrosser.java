package tsp.actions.crossers;

import java.util.Random;

import tsp.actions.DataFactory;
import tsp.actions.MyRandom;
import tsp.objects.chromosomes.Chromosome;

public class MPXCrosser implements Crosser
{
	//Private Members
	private Random random;
	
	//Constructors
	public	MPXCrosser() 
	{
		this.random = MyRandom.getInstance().random;
	}
	
	//Getters
	@Override
	public String getDescription() { return "MPX"; }
	
	//Public Methods
	@Override
	public Chromosome[] cross(Chromosome parent1, Chromosome parent2) throws Exception 
	{
		int size = parent1.getSize();
		int startIndex = this.random.nextInt(size - (size / 4));
		int length = this.random.nextInt((size / 4) - (size / 8)) + (size / 8) + 1;		
		int endIndex = startIndex + length - 1;
		
		int beggining = parent1.getDestination(startIndex);
		int end = parent1.getDestination(endIndex);
		
		int rIndexOne = parent2.indexOf(beggining);
		int rIndexTwo = parent2.indexOf(end);
		
		Chromosome child1 = parent2.copy();
		Chromosome child2 = parent2.copy();
	
		child1.invert(rIndexOne, rIndexTwo);
		child2.invert(rIndexTwo, rIndexOne);
	
		int[] swapSection = parent1.getSubsection(startIndex, length);
	
		child1.removeElementsInSection(swapSection);
		child2.removeElementsInSection(swapSection);
		
		int diff = length - 2;
		
		startIndex = (rIndexOne > rIndexTwo)? ((rIndexTwo > 0)? rIndexTwo - 1 : size - 1) : ((rIndexTwo >= diff)? rIndexTwo - diff : size + (rIndexTwo - diff - 1));
		
		child1.insertSubSection(startIndex, swapSection);
		
		startIndex = (rIndexTwo > rIndexOne)? ((rIndexOne > 0)? rIndexOne - 1 : size - 1) : ((rIndexOne >= diff)? rIndexOne - diff : size + (rIndexOne - diff -1));
		
		child2.insertSubSection(startIndex, swapSection);
		
		Chromosome children[] = DataFactory.createNewChromosomeArray(parent1.populationType, 2);
		
		children[0] = child1;
		children[1] = child2;
		
		return children;
	}

}
