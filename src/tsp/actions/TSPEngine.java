package tsp.actions;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import tsp.controllers.TSPController;
import tsp.objects.GA;
import tsp.objects.chromosomes.Chromosome;

public class TSPEngine extends Thread
{
	//Private Memebrs
	private int maxThreads = 5;
	private Queue<GA> GAQueue;
	private boolean run;
	private int numRunning;
	private static TSPEngine instance = null;
	
	//Contructors
	protected TSPEngine()
	{ 
		this.maxThreads = 5;
		this.GAQueue = new LinkedBlockingQueue<GA>();
		this.run = false;
		this.numRunning = 0;
	}
	
	//Getters
	public static TSPEngine getInstance()
	{
		if(instance == null){
			instance = new TSPEngine();
		}
		return instance;
	}
	
	public boolean isRunning() { return TSPEngine.getInstance().run; }
	
	//Setters
	public void addToQueue(GA ga)
	{
		GAQueue.offer(ga);
	}
	
	//Public Methods
	public void returnSolution(Chromosome solution, int userInterfaceID)
	{
		TSPController.getInstance().sendSolutionToUserInterface(solution, userInterfaceID);
		this.numRunning--;
	}

	public void stopEngine()
	{
		this.run = false;
		System.out.println("Engine Stopped\n");
	}
	
	public void startEngine()
	{
		if(!this.isAlive()) this.start();
		this.run = true;
		System.out.println("Engine Started\n");
	}
	
	public void run()
	{
		this.run = true;
		
		while(this.run)
		{
			if(this.numRunning < this.maxThreads && !this.GAQueue.isEmpty())
			{
				GA ga = this.GAQueue.remove();
				ga.start();
				this.numRunning++;
			}
		}
	}
	
}
