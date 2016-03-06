package tsp.actions;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import tsp.objects.Chromosome;
import tsp.objects.GA;
import tsp.objects.Population;

public class TSPEngine 
{
	private static int maxThreads = 5;
	
	private static TSPEngine instance = null;
	
	private static Queue<Population> populationQueue = new LinkedBlockingQueue<Population>();
	
	private static boolean stop = false;
	
	private static int numRunning = 0;
	
	private static int endCriteria = 0;
	
	protected TSPEngine(){
	
	}
	
	public static TSPEngine getInstance()
	{
		if(instance == null){
			instance = new TSPEngine();
		}
		return instance;
	}
	
	public static void addToQueue(Population population)
	{
		populationQueue.offer(population);
	}
	
	public static void setEndCriteria(int endCriteria)
	{
		TSPEngine.endCriteria = endCriteria;
	}
	
	public void returnSolution(Chromosome solution)
	{
		solution.display();
		TSPEngine.numRunning--;
	}

	public static void stop()
	{
		System.out.println(TSPEngine.stop);
		TSPEngine.stop = true;
	}
	
	public static void run()
	{
		TSPEngine.stop = false;
		
		System.out.println("Running Engine");
		
		while(!TSPEngine.stop)
		{
			if(TSPEngine.numRunning < TSPEngine.maxThreads && !TSPEngine.populationQueue.isEmpty())
			{
				GA ga = new GA(TSPEngine.populationQueue.remove(), TSPEngine.endCriteria);
				ga.start();
				TSPEngine.numRunning++;
				System.out.println("Num Running: " + numRunning);
			}
		}
	}
	
}
