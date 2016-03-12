package tsp.controllers;

import java.util.HashMap;
import java.util.Map;

import tsp.actions.DataConverter;
import tsp.actions.TSPEngine;
import tsp.actions.crossers.Crosser;
import tsp.actions.mutators.Mutator;
import tsp.actions.selectors.Selector;
import tsp.objects.Chromosome;
import tsp.objects.GA;
import tsp.objects.Population;
import tsp.views.UserInterface;

public class TSPController 
{
	//Private Members
	private static TSPController instance;
	private Map<Integer, UserInterface> userInterfaces;
	private static int lastUserInterfaceID = 0;
	
	//Constructors
	protected TSPController()
	{
		this.userInterfaces = new HashMap<Integer, UserInterface>();
	}
	
	//Public Methods
	public static TSPController getInstance()
	{
		if(TSPController.instance == null)
			TSPController.instance = new TSPController();
		return TSPController.instance;
	}
	
	public void startTSPEngine()
	{
		if(!TSPEngine.isRunning())
			TSPEngine.getInstance().startEngine();
	}

	public void stopTSPEngine()
	{
		if(TSPEngine.isRunning())
		{
			TSPEngine.getInstance().stopEngine();
			//this.sendDisplayMessageToViews("TSP Engine stopped successfully.");
		}
	}
	
	public void addUserInterface(UserInterface userInterface)
	{
		userInterface.setInterfaceID(++TSPController.lastUserInterfaceID);
		this.userInterfaces.put(TSPController.lastUserInterfaceID, userInterface);
	}
	
	public void removeUserInterface(int index)
	{
		this.userInterfaces.remove(index);
	}
	
	public void sendSolutionToUserInterface(Chromosome solution, int userInterfaceID)
	{
		UserInterface userInterface = this.userInterfaces.get(userInterfaceID);
		userInterface.addSolution(solution);
		userInterface.displayMessage("Optimal solution has been found!");
	}
	
	public void addToQueue(double[][] distanceIndex, int size, int endCriteria, Crosser crosser, Mutator mutator, Selector selector, int interfaceID)
	{
		Population population;
		try {
			population = DataConverter.createRandomPopulation(distanceIndex, size, crosser, mutator, selector, interfaceID);
			GA ga = new GA(population, endCriteria);
			TSPEngine.addToQueue(ga);
			this.userInterfaces.get(interfaceID).displayMessage("Population added successfully!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String message = e.getMessage();
			this.userInterfaces.get(interfaceID).displayMessage("Unable to add Population." + message);
		}
	}
}
