package tsp.actions;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import tsp.controllers.TSPController;
import tsp.objects.GA;
import tsp.objects.chromosomes.Chromosome;

public class TSPEngine extends Thread
{
	//Private Memebrs
	private static int maxThreads = 5;
	private static TSPEngine instance = null;
	private static Queue<GA> GAQueue = new LinkedBlockingQueue<GA>();
	private static boolean run = false;
	private static int numRunning = 0;
	
	//Contructors
	protected TSPEngine() { }
	
	//Getters
	public static TSPEngine getInstance()
	{
		if(instance == null){
			instance = new TSPEngine();
		}
		return instance;
	}
	
	public static boolean isRunning() { return TSPEngine.run; }
	
	//Setters
	public static void addToQueue(GA ga)
	{
		GAQueue.offer(ga);
	}
	
	//Public Methods
	public void returnSolution(Chromosome solution, int userInterfaceID)
	{
		TSPController.getInstance().sendSolutionToUserInterface(solution, userInterfaceID);
		TSPEngine.numRunning--;
	}

	public void stopEngine()
	{
		TSPEngine.run = false;
		//System.out.println("Engine Stopped\n");
	}
	
	public void startEngine()
	{
		if(!this.isAlive()) this.start();
		TSPEngine.run = true;
		//System.out.println("Engine Started\n");
	}
	
	public void run()
	{
		TSPEngine.run = true;
		
		while(TSPEngine.run)
		{
			if(TSPEngine.numRunning < TSPEngine.maxThreads && !TSPEngine.GAQueue.isEmpty())
			{
				GA ga = TSPEngine.GAQueue.remove();
				ga.start();
				TSPEngine.numRunning++;
			}
		}
	}
	
}
