package tsp.actions.ga;

import tsp.actions.TSPEngine;
import tsp.objects.chromosomes.Chromosome;
import tsp.objects.populations.Population;

public class GA extends Thread
{
	//Private Members
	protected Population population;
	protected Chromosome mostOptimalChromosome;
	protected int endCriteria;
	
	//Constructors
	public GA(Population population, int endCriteria)
	{
		this.population = population;
		this.endCriteria = endCriteria;
	}
	
	//Getters
	public Population getPopulation() { return this.population; }
	public Chromosome getMostOptimalSolution() { return this.mostOptimalChromosome; }
	
	//Public Methods
	public void run()
	{
		this.mostOptimalChromosome = this.population.getMostOptimalMember();
		int numIterations = 0;
		
		while(numIterations < this.endCriteria)
		{
			try {
				this.population.evolve();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Chromosome newMostOptimal = this.population.getMostOptimalMember();
			
			double newFitnessScore = newMostOptimal.getFitnessScore();
		
			if(newFitnessScore < this.mostOptimalChromosome.getFitnessScore())
			{
				this.mostOptimalChromosome = newMostOptimal;
				numIterations = 0;
			}
			else
				numIterations++;
		}
		this.sendMostOptimalSolution();
		//System.out.println(numIterations);
	}
	
	//Private Methods
	private void sendMostOptimalSolution()
	{
		this.mostOptimalChromosome.setDescription("Crosser: " + this.population.getCrosserDescription()
												+ "\nMutator: " + this.population.getMutatorDescription() 
												+ "\nSelector: " + this.population.getSelectorDescription()
												+ "\nPopulation Size: " + this.population.getSize() 
												+ "\nEnd Criteria: " + this.endCriteria);
		TSPEngine.getInstance().returnSolution(this.mostOptimalChromosome, this.population.getInterfaceID());
	}
}
