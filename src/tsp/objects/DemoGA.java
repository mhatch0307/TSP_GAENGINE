package tsp.objects;

import tsp.objects.chromosomes.Chromosome;
import tsp.objects.populations.Population;

public class DemoGA extends GA
{

	public DemoGA(Population population, int endCriteria) 
	{
		super(population, endCriteria);
	}

	public void run()
	{
		this.mostOptimalChromosome = this.population.getMostOptimalMember();
		int numIterations = 0;
		double previousFitnessScore = 0;
		int previousNumDots = 0;
		double startingFitnessScore = this.mostOptimalChromosome.getFitnessScore();
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
		
			//this.mostOptimalChromosome.display(); 
			
			if(newFitnessScore < this.mostOptimalChromosome.getFitnessScore())
			{
				this.mostOptimalChromosome = newMostOptimal;
			}
			else
				numIterations++;
			
			double percentageOfOriginal = (newFitnessScore / startingFitnessScore) * 100;
			int numDots = (int) (percentageOfOriginal);
			//System.out.println(numDots);
			
			if(numDots != previousNumDots)
			{
				for(int i = 0; i < numDots; i++)
				{
					System.out.print('.');
				}
				System.out.printf(" %.2f", percentageOfOriginal);
				System.out.print("% ");
				
				newMostOptimal.display();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			previousNumDots = numDots;
			previousFitnessScore = newFitnessScore;
		}
		//this.sendMostOptimalSolution();
		//System.out.println(numIterations);
		this.mostOptimalChromosome.display();
		System.out.println("Done!");
	}
	
}
