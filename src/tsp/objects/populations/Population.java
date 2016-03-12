package tsp.objects.populations;

import java.util.ArrayList;
import java.util.Arrays;

import tsp.actions.DataFactory;
import tsp.actions.crossers.Crosser;
import tsp.actions.mutators.Mutator;
import tsp.actions.selectors.Selector;
import tsp.objects.chromosomes.Chromosome;

public class Population 
{
	//Private Members
	private int populationType;
	private Chromosome[] chromosomes;
	private Crosser crosser;
	private Mutator mutator;
	private Selector selector;
	private int mostOptimalMemberIndex;
	private int secondMostOptimalMemberIndex;
	private int interfaceID;
	private int currentSize;
	
	//Constructors
	public Population(int maxSize, Crosser crosser, Mutator mutator, Selector selector, int populationType, int interfaceID)
	{
		this.currentSize = 0;
		this.crosser = crosser;
		this.mutator = mutator;
		this.selector = selector;
		this.mostOptimalMemberIndex = 0;
		this.secondMostOptimalMemberIndex = 0;
		this.interfaceID = interfaceID;
		this.populationType = populationType;
		try {
			this.chromosomes = DataFactory.createNewChromosomeArray(this.populationType, maxSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Getters 
	public int getMaxSize() { return chromosomes.length; }
	public int getSize() { return this.currentSize; }
	public boolean isEmpty() { return this.currentSize == 0; }
	public boolean isFull() { return this.currentSize == chromosomes.length; }
	public int getInterfaceID() { return this.interfaceID; }
	public String getCrosserDescription() { return this.crosser.getDescription(); }
	public String getMutatorDescription() { return this.mutator.getDescription(); }
	public String getSelectorDescription() { return this.selector.getDescription(); }
	public int getPopulationType() { return this.populationType; }
	
	public Chromosome getChromosome(int index) throws Exception
	{
		if(index >= currentSize || index < 0)
			throw new Exception ("The specified index does not exist in the current population.");
		return this.chromosomes[index];	
	}
	
	public Chromosome getMostOptimalMember()
	{
		return this.chromosomes[this.mostOptimalMemberIndex];
	}
	
	//Setters
	/**
	 * Sets the Chromsome at the specified index if the index specified is < 
	 * this.currentSize
	 * @param chromosome
	 * @param index
	 * @throws Exception 
	 */
	public void setChromosome(Chromosome chromosome, int index) throws Exception
	{
		if(index >= currentSize || index < 0)
			throw new Exception ("The specified index does not exist in the current population.");
		
		this.chromosomes[index] = chromosome;
	}
	
	/**
	 * Sets the chromosomes for the population
	 * @param chromosomes
	 */
	public void setChromosomes(Chromosome[] chromosomes)
	{
		this.currentSize = chromosomes.length;
		this.chromosomes = chromosomes;
		this.findMostOptimalChromosome();
	}
	
	//Publci Methods
	/**
	 * Adds a Chromosome to the population. If there is room in the population
	 * 
	 * @param Chromosome chromosome
	 * @throws Exception 
	 */
	public void addChromosome(Chromosome chromosome) throws Exception
	{
		if(this.currentSize == this.chromosomes.length)
			throw new Exception("The maximum population has been reached!");
		
		this.chromosomes[currentSize] = chromosome;
		if(chromosome.getFitnessScore() < this.chromosomes[this.mostOptimalMemberIndex].getFitnessScore())
		{
			this.secondMostOptimalMemberIndex = this.mostOptimalMemberIndex;
			this.mostOptimalMemberIndex = currentSize;
		}
		this.currentSize++;
		
	}
	
	/**
	 * Empties the Population by setting the current index to 0
	 */
	public void emptyPopulation()
	{
		this.currentSize = 0;
	}
	
	/**
	 * Generate the next stymmetric population
	 * @throws Exception 
	 */
	public void generateOffSpring() throws Exception
	{
		
		//System.out.println(this.chromosomes.length);
		
		Chromosome selectChromosomes[] = DataFactory.createNewChromosomeArray(this.populationType, this.chromosomes.length);
		System.arraycopy(this.chromosomes, 0, selectChromosomes, 0, this.chromosomes.length);
		this.selector.setChromosomes(new ArrayList<Chromosome>(Arrays.asList(this.chromosomes)));
		
		Chromosome[] newPopulation = DataFactory.createNewChromosomeArray(this.populationType, this.chromosomes.length);
		
		int chromosomeIndex = 0;
		newPopulation[chromosomeIndex++] = this.chromosomes[this.mostOptimalMemberIndex]; // always keep most optimal member
		
		if(this.currentSize % 2 == 0)
		{
			newPopulation[chromosomeIndex++] = this.chromosomes[this.secondMostOptimalMemberIndex]; // keep second most optimal
		}
		
		double mostOptimalFitnessScore = 999999999;
		
		while(chromosomeIndex < this.currentSize)
		{
			Chromosome parent1 = selector.select();
			Chromosome parent2 = selector.select();
			
			Chromosome children[] = this.crosser.cross(parent1, parent2);
			for(int j = 0; j < children.length && chromosomeIndex < this.currentSize; j++)
			{
				Chromosome child = this.mutator.mutate(children[j]);
				if(child.getFitnessScore() < mostOptimalFitnessScore)
				{
					mostOptimalFitnessScore = child.getFitnessScore();
					this.secondMostOptimalMemberIndex = this.mostOptimalMemberIndex;
					this.mostOptimalMemberIndex = chromosomeIndex;
				}
				newPopulation[chromosomeIndex++] = child;
			}
		}
		this.chromosomes = newPopulation;
	}
	
	//Private Methods
	/**
	 * Finds the most optimal chromosome
	 */
	private void findMostOptimalChromosome()
	{
		double mostOptimalFitnessScore = 999999999;
		for(int i = 0; i < this.currentSize; i++)
		{
			if(this.chromosomes[i].getFitnessScore() < mostOptimalFitnessScore)
			{
				this.secondMostOptimalMemberIndex = this.mostOptimalMemberIndex;
				this.mostOptimalMemberIndex = i;
			}
		}
	}
}
