package tsp.objects;

import tsp.actions.TSPEngine;
import tsp.actions.crossers.Crosser;
import tsp.actions.mutators.Mutator;

public class GA extends Thread
{
	private Population population;
	
	// the difference between the a previous and current distnace score that must be reached to stop
	private float changeStopCriteria;
	// the number of consecutive iterations with changeStopCriteria reached
	private int numIterationsStopCriteria;
	// the maximum number of iterations for the algorithm
	private int maxIterations;
	//
	private Chromosome mostOptimalChromosome;
	
	
	public GA(Population population, float changeStopCriteria)
	{
		this.population = population;
		this.changeStopCriteria = changeStopCriteria;
		//this.numIterationsStopCriteria = numIterationsStopCriteria;
		//this.maxIterations = maxIterations;
	}
	
	//Getters
	public Population getPopulation() { return this.population; }
	
	public void run()
	{
		System.out.println("Running GA");
		this.mostOptimalChromosome = this.population.getMostOptimalMember();
		int numIterations = 0;
		//int numConsMinDiff = 0;
		
		while(numIterations < this.changeStopCriteria && this.population.getSize() > 2)
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

			//this.mostOptimalChromosome.display();
		}
		this.sendMostOptimalSolution();
		//System.out.println(numIterations);
	}
	
	private void sendMostOptimalSolution()
	{
		TSPEngine.getInstance().returnSolution(this.mostOptimalChromosome);
	}
	
}
