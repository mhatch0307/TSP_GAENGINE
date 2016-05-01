package tsp.objects.populations;

import java.util.Random;

import tsp.actions.DataFactory;
import tsp.actions.MyRandom;
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
	private int worstOptimalMemberIndex;
	private int interfaceID;
	private int currentSize;
	private float crossProbability;
	private float mutateProbability;
	private Random random;
	
	//Constructors
	public Population(int maxSize, Crosser crosser, Mutator mutator, Selector selector, int populationType, 
			float crossProbability, float mutateProbability, int interfaceID)
	{
		this.currentSize = 0;
		this.crosser = crosser;
		this.mutator = mutator;
		this.selector = selector;
		this.mostOptimalMemberIndex = 0;
		this.worstOptimalMemberIndex = 0;
		this.interfaceID = interfaceID;
		this.populationType = populationType;
		this.crossProbability = crossProbability;
		this.mutateProbability = mutateProbability;
		this.random = MyRandom.getInstance().random;
		
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
			this.mostOptimalMemberIndex = currentSize;
		}
		else if(chromosome.getFitnessScore() > this.chromosomes[this.worstOptimalMemberIndex].getFitnessScore())
		{
			this.worstOptimalMemberIndex = currentSize;
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
	 * Evolve to the next generation
	 * @throws Exception 
	 */
	public void evolve() throws Exception
	{
		Chromosome selectChromosomes[] = DataFactory.createNewChromosomeArray(this.populationType, this.chromosomes.length);
		System.arraycopy(this.chromosomes, 0, selectChromosomes, 0, this.chromosomes.length);

		this.selector.setChromosomes(this.chromosomes, this.chromosomes[this.mostOptimalMemberIndex].getFitnessScore());
		
		Chromosome[] newPopulation = DataFactory.createNewChromosomeArray(this.populationType, this.chromosomes.length);
		
		int chromosomeIndex = 0;
		newPopulation[chromosomeIndex++] = this.chromosomes[this.mostOptimalMemberIndex]; // always keep most optimal member
		this.mostOptimalMemberIndex = 0;
		
		double mostOptimalFitnessScore = this.chromosomes[this.mostOptimalMemberIndex].getFitnessScore();
		double worstOptimalFitnessScore = this.chromosomes[this.worstOptimalMemberIndex].getFitnessScore();
		
		while(chromosomeIndex < this.currentSize)
		{
			int p1Index = 0;
			int p2Index = 0;
			
			Chromosome parent1 = null;
			Chromosome parent2 = null;
			
			while(p1Index == p2Index)
			{
				p1Index = this.selector.select();
				p2Index = this.selector.select();
			}
			parent1 = this.chromosomes[p1Index].copy();
			parent2 = this.chromosomes[p2Index].copy();
			
			Chromosome[] children;
			
			if(this.random.nextFloat() <= this.crossProbability)
			{
				children = this.crosser.cross(parent1, parent2);
			}
			else if(parent1.getFitnessScore() < parent2.getFitnessScore())
			{
				children = new Chromosome[]{parent1};
			}
			else
			{
				children = new Chromosome[]{parent2};
			}
			
			for(int j = 0; j < children.length && chromosomeIndex < this.currentSize; j++)
			{
				
				if(this.random.nextFloat() <= this.mutateProbability)
					this.mutator.mutate(children[j]);
				
				if(children[j].getFitnessScore() < mostOptimalFitnessScore)
				{
					mostOptimalFitnessScore = children[j].getFitnessScore();
					this.mostOptimalMemberIndex = chromosomeIndex;
					newPopulation[chromosomeIndex++] = children[j];
				}
				else if(children[j].getFitnessScore() > worstOptimalFitnessScore)
				{
					this.worstOptimalMemberIndex = chromosomeIndex;
					worstOptimalFitnessScore = children[j].getFitnessScore();
				}
				else
					newPopulation[chromosomeIndex++] = children[j];
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
		double worstOptimalFitnessScore = 0;
		for(int i = 0; i < this.currentSize; i++)
		{
			if(this.chromosomes[i].getFitnessScore() < mostOptimalFitnessScore)
			{
				this.mostOptimalMemberIndex = i;
				mostOptimalFitnessScore = chromosomes[i].getFitnessScore();
			}
			else if(this.chromosomes[i].getFitnessScore() > worstOptimalFitnessScore)
			{
				this.worstOptimalMemberIndex = i;
				worstOptimalFitnessScore = this.chromosomes[i].getFitnessScore();
			}
		}
	}
}
