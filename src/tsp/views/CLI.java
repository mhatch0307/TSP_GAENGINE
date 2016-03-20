package tsp.views;

import java.util.ArrayList;
import java.util.Scanner;

import tsp.actions.DataFactory;
import tsp.actions.DataReader;
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
import tsp.controllers.TSPController;
import tsp.objects.DemoGA;
import tsp.objects.GA;
import tsp.objects.chromosomes.Chromosome;
import tsp.objects.populations.Population;
import tsp.objects.populations.PopulationType;

public class CLI extends UserInterface
{
	//Private Members
	private float crossProbability;
	private float mutateProbability;
	private Scanner scan;
	private TSPController tspController;
	
	//Constructors
	protected CLI(float crossProbability, float mutateProbability)
	{
		this.crossProbability = crossProbability;
		this.mutateProbability = mutateProbability;
		this.scan = new Scanner(System.in);
		this.tspController = TSPController.getInstance();
		this.tspController.addUserInterface(this);
		this.solutions = new ArrayList<Chromosome>();
	}
	
	//Public Methods
	public void prompt(boolean displayOptions)
	{
		if(displayOptions) this.displayCLIOptions();
		int option = scan.nextInt();
		this.executeCommand(option);
	}

	private void exit()
	{
		this.tspController.removeUserInterface(this.interfaceID);
	}
	
	@Override
	public void displayMessage(String message)
	{
		System.out.println("\n" + message);
	}

	@Override
	public void refresh() 
	{
		prompt(true);
	}
	
	public void executeCommand(int option)
	{
		switch(option)
		{
			case 1:
				this.tspController.startTSPEngine();
				this.prompt(false);
				break;
			case 2:
				this.tspController.stopTSPEngine();
				this.prompt(false);
				break;
			case 3:
				this.addPopulationToQueue(PopulationType.Symmetric);
				this.prompt(true);
				break;
			case 4:
				this.addPopulationToQueue(PopulationType.Asymmetric);
				this.prompt(true);
				break;
			case 5:
				this.viewSolutions();
				this.prompt(true);
			case 6:
				this.demoGA(PopulationType.Symmetric);
				this.prompt(true);
			case 7:
				this.demoGA(PopulationType.Asymmetric);
				this.prompt(true);
			default:
				this.exit();
		}

	}
	
	//Private Methods
	private void displayCLIOptions()
	{
		System.out.print("Start TSP Engine: 1\n"
						 + "Stop TSP Engine: 2\n"
						 + "Add Symmetric Population to Queue: 3\n"
						 + "Add Asymmetric Population to Queue: 4\n"
						 + "View Solutions: 5\n"
						 + "Demo GA: 6\n"
						 + "Exit: 7\n"
						 + "Enter Selection > ");
	}
	
	private void displayCrosserOptions()
	{
		System.out.print("OXCrosser: 1\n"
						 + "MPXCrosser: 2\n"
						 + "ERXCrosser: 3\n"
						 + "RingCrosser: 4\n"
						 + "CycleCrosser: 5\n"
						 + "Enter Selection > ");
	}
	
	private void displayMutatorOptions()
	{
		System.out.print("Swap Mutator: 1\n"
						 + "Scramble Mutator: 2\n"
						 + "Enter Selection > ");
	}
	
	private void displaySelectorOptions()
	{
		System.out.print("RW Selector: 1\n"
						 + "Rank Selector: 2\n"
						 + "Enter Selection > ");
	}
	
	private void viewSolutions()
	{
		for(Chromosome solution : this.solutions)
		{
			solution.display();
		}
		
	}
	
	private void addPopulationToQueue(int populationType)
	{
		try
		{
			System.out.print("File > ");
			String filePath = this.scan.next();
			
			String ext = filePath.substring(filePath.indexOf('.') + 1, filePath.length());
			
			double[][] distanceIndex;
			double[][] verticies = null;
			
			if(ext.equals("xml"))
			{
				distanceIndex = DataFactory.XMLToDistanceIndex(filePath);
			}
			else if(ext.equals("tsp"))
			{
				verticies = DataReader.readTSPFile(filePath);
				distanceIndex = DataFactory.verticiesToDistanceIndex(verticies);
			}
			else
			{
				throw new Exception("Invalid file type!");
			}
			
			this.displayCrosserOptions();
			Crosser crosser = this.getCrosser(this.scan.nextInt());
			
			this.displayMutatorOptions();
			Mutator mutator = this.getMutator(this.scan.nextInt());
			
			this.displaySelectorOptions();
			
			Selector selector = this.getSelector(this.scan.nextInt());
			
			System.out.print("Population Size > ");
			int size = this.scan.nextInt();
			
			System.out.print("End Criteria > ");
			int endCriteria = this.scan.nextInt();
			
			this.tspController.addToQueue(distanceIndex, verticies, size, endCriteria, crosser, mutator, selector, populationType, interfaceID);
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void demoGA(int populationType)
	{
		try
		{
			System.out.print("File > ");
			String filePath = this.scan.next();
			
			String ext = filePath.substring(filePath.indexOf('.') + 1, filePath.length());
			
			double[][] distanceIndex;
			double[][] verticies = null;
			
			if(ext.equals("xml"))
			{
				distanceIndex = DataFactory.XMLToDistanceIndex(filePath);
			}
			else if(ext.equals("tsp"))
			{
				verticies = DataReader.readTSPFile(filePath);
				distanceIndex = DataFactory.verticiesToDistanceIndex(verticies);
			}
			else
			{
				throw new Exception("Invalid file type!");
			}
			
			this.displayCrosserOptions();
			Crosser crosser = this.getCrosser(this.scan.nextInt());
			
			this.displayMutatorOptions();
			Mutator mutator = this.getMutator(this.scan.nextInt());
			
			this.displaySelectorOptions();
			
			Selector selector = this.getSelector(this.scan.nextInt());
			
			System.out.print("Population Size > ");
			int size = this.scan.nextInt();
			
			System.out.print("End Criteria > ");
			int endCriteria = this.scan.nextInt();
			
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
				GA ga = new DemoGA(population, endCriteria);
				ga.start();
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//this.tspController.addToQueue(distanceIndex, verticies, size, endCriteria, crosser, mutator, selector, populationType, interfaceID);
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Selector getSelector(int option)
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
	
	private Mutator getMutator(int option)
	{
		switch(option)
		{
			case 1:
				return new SwapMutator(this.mutateProbability);
			case 2:
				return new ScrambleMutator(this.mutateProbability);
			default:
				return new SwapMutator(this.mutateProbability);
		}
	}
	
	private Crosser getCrosser(int option)
	{
		switch(option)
		{
			case 1: 
				return new OXCrosser(this.crossProbability);
			case 2:
				return new MPXCrosser(this.crossProbability);
			case 3:
				return new ERXCrosser(this.crossProbability);
			case 4:
				return new RingCrosser(this.crossProbability);
			case 5:
				return new CycleCrosser(this.crossProbability);
			default:
				return new OXCrosser(this.crossProbability);
		}
	}
	
	public static void main(String[] args)
	{
		CLI cli = new CLI((float) .7, (float) .1);
		cli.prompt(true);
	}
}
