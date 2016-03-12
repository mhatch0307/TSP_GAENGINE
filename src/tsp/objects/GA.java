package tsp.objects;

import tsp.actions.TSPEngine;

public class GA extends Thread
{
	//Private Members
	private Population population;
	private Chromosome mostOptimalChromosome;
	private int endCriteria;
	
	//Constructors
	public GA(Population population, int endCriteria)
	{
		this.population = population;
		this.endCriteria = endCriteria;
	}
	
	//Getters
	public Population getPopulation() { return this.population; }
	
	//Public Methods
	public void run()
	{
		this.mostOptimalChromosome = this.population.getMostOptimalMember();
		int numIterations = 0;
		
		while(numIterations < this.endCriteria && this.population.getSize() > 2)
		{
			try {
				this.population.generateOffSpring();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Chromosome newMostOptimal = this.population.getMostOptimalMember();
			
			double newFitnessScore = newMostOptimal.getFitnessScore();
		
			if(newFitnessScore < this.mostOptimalChromosome.getFitnessScore())
				this.mostOptimalChromosome = newMostOptimal;
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
