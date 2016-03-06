package tsp.controllers;

import java.io.InputStreamReader;
import java.util.Scanner;

import tsp.actions.DataConverter;
import tsp.actions.TSPEngine;
import tsp.actions.crossers.Crosser;
import tsp.actions.crossers.CycleCrosser;
import tsp.actions.crossers.ERXCrosser;
import tsp.actions.crossers.MPXCrosser;
import tsp.actions.crossers.OXCrosser;
import tsp.actions.crossers.RingCrosser;
import tsp.actions.mutators.Mutator;
import tsp.actions.mutators.ScrambleMutator;
import tsp.actions.mutators.SwapMutator;
import tsp.actions.selectors.RWSelector;
import tsp.actions.selectors.RankSelector;
import tsp.actions.selectors.Selector;
import tsp.objects.Population;

public class CLI 
{
	//Private member variables
	protected static float crossProbability;
	protected static float mutateProbability;
	protected static Scanner scan;
	private static CLI instance;
	
	// Constructor
	protected CLI(){
		
	}
	
	public static CLI getInstance()
	{
		if(instance == null){
			instance = new CLI();
		}
		return instance;
	}
	
	//Displays
	
	public static int prompt()
	{
		CLI.displayCLIOptions();
		int option = scan.nextInt();
		CLI.executeCommand(option);
		return option;
	}
	
	private static void displayCLIOptions()
	{
		System.out.println("Start TSP Engine: 1\n"
						 + "Stop TSP Engine: 2\n"
						 + "Add Population to Queue: 3\n"
						 + "Set End Criteria: 4\n"
						 + "Exit: 5\n");
	}
	
	private static void displayCrosserOptions()
	{
		System.out.println("OXCrosser: 1\n"
						 + "MPXCrosser: 2\n"
						 + "ERXCrosser: 3\n"
						 + "RingCrosser: 4\n"
						 + "CycleCrosser: 5\n");
	}
	
	private static void displayMutatorOptions()
	{
		System.out.println("Swap Mutator: 1\n"
						 + "Scramble Mutator: 2\n");
	}
	
	private static void displaySelectorOptions()
	{
		System.out.println("RW Selector: 1\n"
						 + "Rank Selector: 2\n");
	}
	
	//Actions
	
	private static void setEndCriteria()
	{
		System.out.println("End Criteria: ");
		TSPEngine.setEndCriteria(CLI.scan.nextInt());
	}
	
	public static void executeCommand(int option)
	{
		switch(option)
		{
			case 1:
				CLI.startTSPEngine();
				break;
			case 2:
				CLI.stopTSPEngine();
				break;
			case 3:
				CLI.addPopulationToQueue();
				break;
			case 4:
				CLI.setEndCriteria();
				break;
			default:
				CLI.exit();
		}

	}
	
	private static void startTSPEngine()
	{
		TSPEngine.run();
	}
	
	private static void stopTSPEngine()
	{
		TSPEngine.stop();
	}
	
	private static void addPopulationToQueue()
	{
		try {
			System.out.println("File: ");
			String filePath = CLI.scan.next();
			
			double[][] distanceIndex = DataConverter.XMLToDistanceIndex(filePath);
			
			CLI.displayCrosserOptions();
			Crosser crosser = CLI.getCrosser(CLI.scan.nextInt());
			
			CLI.displayMutatorOptions();
			Mutator mutator = CLI.getMutator(CLI.scan.nextInt());
			
			CLI.displaySelectorOptions();
			
			Selector selector = CLI.getSelector(CLI.scan.nextInt());
			
			System.out.print("Population Size: \n");
			int size = CLI.scan.nextInt();
			
			Population population = DataConverter.createRandomPopulation(distanceIndex, size, crosser, mutator, selector);
			
			TSPEngine.addToQueue(population);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static Selector getSelector(int option)
	{
		switch(option)
		{
			case 1:
				return new RWSelector();
			case 2:
				return new RankSelector();
			default:
				return new RWSelector();
		}
	}
	
	private static Mutator getMutator(int option)
	{
		switch(option)
		{
			case 1:
				return new SwapMutator(CLI.mutateProbability);
			case 2:
				return new ScrambleMutator(CLI.mutateProbability);
			default:
				return new SwapMutator(CLI.mutateProbability);
		}
	}
	
	private static Crosser getCrosser(int option)
	{
		switch(option)
		{
			case 1: 
				return new OXCrosser(CLI.crossProbability);
			case 2:
				return new MPXCrosser(CLI.crossProbability);
			case 3:
				return new ERXCrosser(CLI.crossProbability);
			case 4:
				return new RingCrosser(CLI.crossProbability);
			case 5:
				return new CycleCrosser(CLI.crossProbability);
			default:
				return new OXCrosser(CLI.crossProbability);
		}
	}
	
	private static void exit()
	{
		TSPEngine.stop();
	}
	
	public static void main(String[] args)
	{
		InputStreamReader cin = new InputStreamReader(System.in);
		Scanner scan = new Scanner(cin);
		//CLI cli = CLI.getInstance();
		CLI.scan = scan;
		CLI.crossProbability = (float) 0.9;
		CLI.mutateProbability = (float) 0.2;
		int option = 0;
		do
		{
			option = CLI.prompt();
		} while(option < 5 && option > 0);
		scan.close();
	}
	
}
