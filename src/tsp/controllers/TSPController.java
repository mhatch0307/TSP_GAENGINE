package tsp.controllers;

import java.util.HashMap;
import java.util.Map;

import tsp.actions.DataFactory;
import tsp.actions.GoogleMapsAPI;
import tsp.actions.TSPEngine;
import tsp.actions.crossers.Crosser;
import tsp.actions.mutators.Mutator;
import tsp.actions.selectors.Selector;
import tsp.objects.GA;
import tsp.objects.chromosomes.Chromosome;
import tsp.objects.populations.Population;
import tsp.objects.populations.PopulationType;
import tsp.views.UserInterface;

public class TSPController 
{
	//Private Members
	private static TSPController instance;
	private Map<Integer, UserInterface> userInterfaces;
	private static int lastUserInterfaceID = 0;
	
	//Constructors
	public TSPController()
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
	
	public void addToQueue(double[][] distanceIndex, double[][] verticies, int size, int endCriteria, Crosser crosser, Mutator mutator, Selector selector, int populationType, int interfaceID)
	{
		Population population;
		try 
		{
			switch(populationType)
			{
				case PopulationType.Symmetric:
					population = DataFactory.createRandomSymmetricPopulation(distanceIndex, verticies, size, crosser, mutator, selector, interfaceID);
					break;
				case PopulationType.Asymmetric:
					population = DataFactory.createRandomAsymmetricPopulation(distanceIndex, verticies, size, crosser, mutator, selector, interfaceID);
					break;
				default:
					throw new Exception("Invalid Population Type!");
			}
			GA ga = new GA(population, endCriteria);
			TSPEngine.addToQueue(ga);
			this.userInterfaces.get(interfaceID).displayMessage("Population added successfully!");
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			String message = e.getMessage();
			this.userInterfaces.get(interfaceID).displayMessage("Unable to add Population." + message);
		}
	}
	
	public double[][] addressesToDistanceIndex(String[] addresses)
	{
		GoogleMapsAPI googleMapsAPI = new GoogleMapsAPI("json", "AIzaSyAIIGgX0htxhCBnOy6xk_7ZCOAPz3ntoSk");
		double distanceIndex[][] = googleMapsAPI.getDistanceIndexAddress(addresses, addresses, "imperial");
		return distanceIndex;
	}
}
