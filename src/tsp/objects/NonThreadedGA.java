package tsp.objects;

import tsp.objects.chromosomes.Chromosome;
import tsp.objects.populations.Population;

public class NonThreadedGA 
{
	//Private Members
	protected Population population;
	protected Chromosome mostOptimalChromosome;
	protected int endCriteria;
	
	//Constructors
	public NonThreadedGA(Population population, int endCriteria)
	{
		this.population = population;
		this.endCriteria = endCriteria;
	}
	
	public Chromosome getMostOptimalChromosome() { return this.mostOptimalChromosome; }
	
	//Getters
	public Population getPopulation() { return this.population; }
	
	//Public Methods
	public void start()
	{
		this.mostOptimalChromosome = this.population.getMostOptimalMember();
		int numIterations = 0;
		
		while(numIterations < this.endCriteria)
		{
			try {
				this.population.generateOffSpring();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			Chromosome newMostOptimal = this.population.getMostOptimalMember();
			
			double newFitnessScore = newMostOptimal.getFitnessScore();
		
			if(newFitnessScore < this.mostOptimalChromosome.getFitnessScore())
			{
				this.mostOptimalChromosome = newMostOptimal;
				//numIterations = 0;
			}
			numIterations++;
		}
	}
}
