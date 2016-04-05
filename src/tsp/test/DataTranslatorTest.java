package tsp.test;

import tsp.actions.DataFactory;
import tsp.actions.crossers.ERXCrosser;
import tsp.actions.mutators.SwapMutator;
import tsp.actions.selectors.RWSelector;
import tsp.objects.populations.Population;


@SuppressWarnings("unused")
public class DataTranslatorTest 
{
	public static void main(String[] args)
	{
		
		//String filePath = "/Users/matthew/Documents/TSP/TSPProblems/ulysses16.xml";
		//String filePath = "/Users/matthew/Documents/TSP/TSPProblems/ulysses22.xml";
		//String filePath = "/Users/matthew/Documents/TSP/TSPProblems/fnl4461.xml";
		String filePath =  "/Users/matthew/Documents/TSP/TSPProblems/a280.xml";
		//String filePath =  "/Users/matthew/Documents/TSP/TSPProblems/att532.xml";
		//String filePath =  "/Users/matthew/Documents/TSP/TSPProblems/berlin52.xml";
		//String filePath =  "/Users/matthew/Documents/TSP/TSPProblems/rat99.xml";
		
		
		double[][] distanceIndex;
		
		try 
		{
			distanceIndex = DataFactory.XMLToDistanceIndex(filePath);
			
			/*for (int i = 0; i < distanceIndex.length; i++)
			{
				System.out.println("vertex " + i);
				
				for (int j = 0; j < distanceIndex[i].length; j++)
				{
					System.out.println(distanceIndex[i][j]);
				}
				System.out.println("");
			}*/
			
			
			/*Chromosome chromosome1 = new Chromosome(10, distanceIndex);
			
			chromosome1.addDestination(8);
			chromosome1.addDestination(4);
			chromosome1.addDestination(7);
			chromosome1.addDestination(3);
			chromosome1.addDestination(6);
			chromosome1.addDestination(2);
			chromosome1.addDestination(5);
			chromosome1.addDestination(1);
			chromosome1.addDestination(9);
			chromosome1.addDestination(0);*/
			
			//Chromosome chromosome1 = DataConverter.createRandomChromosome(distanceIndex); 
			
			//chromosome1.display();
			
			
			/*Chromosome chromosome2  = new Chromosome(10, distanceIndex);
			chromosome2.addDestination(0);
			chromosome2.addDestination(1);
			chromosome2.addDestination(2);
			chromosome2.addDestination(3);
			chromosome2.addDestination(4);
			chromosome2.addDestination(5);
			chromosome2.addDestination(6);
			chromosome2.addDestination(7);
			chromosome2.addDestination(8);
			chromosome2.addDestination(9);*/
			
			//Chromosome chromosome2 = DataConverter.createRandomChromosome(distanceIndex); 
			
			//chromosome1.display();
			//chromosome2.display();
			
			//Crosser crosser = new RingCrosser((float) 1.0);
			
			//crosser.cross(chromosome1, chromosome2);
			
			//Mutator mutator = new ScrambleMutator((float) 1.0);
			
			//chromosome1.display();
			
			//Chromosome child = mutator.mutate(chromosome1);
			
			//child.display();
			
			/*Population population1 = DataFactory.createRandomSymmetricPopulation(distanceIndex, null, 100, new ERXCrosser((float) .8), new SwapMutator((float) .2), new RWSelector(), 1);
			
			double totalFitness = 0;
			
			for(int i = 0; i < population1.getSize(); i++)
			{
				double fitness = population1.getChromosome(i).getFitnessScore() - (population1.getMostOptimalMember().getFitnessScore() * .9);
				System.out.println(fitness);
				totalFitness += fitness;
			}
			
			System.out.println(totalFitness);
			System.out.println((int) totalFitness);*/
			
			/*Population population1 = DataFactory.createRandomSymmetricPopulation(distanceIndex, 100, new RingCrosser((float) .8), new SwapMutator((float) .2), new RWSelector(), 1);
			Population population2 = DataFactory.createRandomSymmetricPopulation(distanceIndex, 100, new ERXCrosser((float) .8), new SwapMutator((float) .2), new RWSelector(), 1);
			GA ga1 = new GA(population1, 1000);
			GA ga2 = new GA(population2, 1000);
			
			TSPEngine.addToQueue(ga1);
			TSPEngine.addToQueue(ga2);
			
			TSPEngine.getInstance().run();*/
			
			//GA ga1 = new GA(population, 100000);
			
			//ga1.start();
			
			//GA ga2 = new GA(population, 30000);
			
			//ga2.start();
			
			//solution.display();
			//System.out.println("Done!");
			
			/*Chromosome firstOptimal = population.getMostOptimalMember();
			
			firstOptimal.display();
			
			population.generateOffSpring();
			
			Chromosome secondOptimal = population.getMostOptimalMember();
			
			secondOptimal.display();*/
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
}
