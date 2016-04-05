package tsp.actions;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.mxgraph.view.mxGraph;

import tsp.actions.crossers.Crosser;
import tsp.actions.mutators.Mutator;
import tsp.actions.selectors.Selector;
import tsp.objects.Destination;
import tsp.objects.chromosomes.AsymmetricChromosome;
import tsp.objects.chromosomes.Chromosome;
import tsp.objects.chromosomes.SymmetricChromosome;
import tsp.objects.populations.Population;
import tsp.objects.populations.PopulationType;

public class DataFactory 
{
	
	//Data Processing Functions
	/**
	 * 
	 * Creates and index of Vertex distances
	 * 
	 * @param String XMLFilePath
	 * @return double[][] distanceIndex -- contains the distances each vertex to each vertex
	 * @throws Exception
	 */
	public static double[][] XMLToDistanceIndex(String XMLFilePath) throws Exception
	{
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(XMLFilePath));
		
		// Find TSP size, distance precision
		
		int size = 0;
		
		int precision = 0;
		
		int ignoredDigits = 0;
		
		String line = reader.readLine();
		
		while(!line.contains("</vertex>") && line != null)
		{
			//System.out.println(line);
			
			if(line.contains("<doublePrecision>"))
				precision = Integer.parseInt(line.substring(line.indexOf('>') + 1, line.indexOf("</")));
			else if(line.contains("<ignoredDigits>"))
				ignoredDigits = Integer.parseInt(line.substring(line.indexOf('>') + 1, line.indexOf("</")));
			else if(line.contains("<edge"))
				size++;
				
			line = reader.readLine();	
		}
		
		if(precision == 0 || ignoredDigits == 0 || size == 0)
			throw new Exception("Class: DataProcessor Function:XMLToDistanceIndex Message: Not a valid xml file!");
		
		size++; // increment size by since the size is one more than number of edges
		
		reader = new BufferedReader(new FileReader(XMLFilePath));
		
		double[][] distanceIndex = new double[size][size];
		
		line = reader.readLine();
		
		int vertexIndex = -1;
		int edgeIndex = 0;
	
		try
		{
			while(line != null && vertexIndex < size)
			{
				if(line.contains("<vertex>"))
				{
					if(edgeIndex != size && vertexIndex > 0)
						throw new Exception ("Invalid File! Not enough edges found for vertex " + vertexIndex + ".");
					
					vertexIndex++;	
					edgeIndex = 0;
				}
				else if(line.contains("<edge"))
				{
					if(edgeIndex == vertexIndex)
					{
						distanceIndex[vertexIndex][edgeIndex++] = 0.00;
						continue;
					}
					String number = line.substring(line.indexOf("cost=") + 6, line.indexOf('>') -1);
					distanceIndex[vertexIndex][edgeIndex++] = Double.parseDouble(number);
				}
				line = reader.readLine();
			}
		}
		catch (Exception e)
		{
			throw new Exception("Invalid File! " + e.getMessage());
		}
	
		reader.close();
		
		if(vertexIndex != size - 1)
			throw new Exception ("Invalid File! Only " + (vertexIndex + 1) + " valid vertices found out of " + (size + 1));
		
		return distanceIndex;
	}
	
	public static double distanceBetween(double[] vertex1, double[] vertex2)
	{
		return Math.sqrt(Math.pow(vertex2[0] - vertex1[0], 2) + Math.pow(vertex2[1] - vertex1[1], 2));
	}
	
	public static double[][] verticiesToDistanceIndex(double[][] verticies)
	{
		double distanceIndex[][] = new double[verticies.length][verticies.length];
		
		for(int i = 0; i < verticies.length; i++)
		{
			for(int j = 0; j < verticies.length; j++)
			{
				distanceIndex[i][j] = DataFactory.distanceBetween(verticies[i], verticies[j]);
			}
		}
		
		return distanceIndex;
	}
	
	/*public static String PEToXML(Destination[][] destinations)
	{
		return null;
	}*/
	
	public static Destination[] GraphToPE(mxGraph graph)
	{
		return null;
	}
	
	public static mxGraph ChromosomeToGraph(Chromosome chromosome)
	{
		mxGraph graph = new mxGraph();
	
		return graph;
	}
	
	/*public static mxGraph XMLToGraph(String XML)
	{
		return null;
	}
	
	public static String GraphToXML(mxGraph graph)
	{
		return null;
	}*/
	
	// Helper Functions
	
	public static int getTSPSize(BufferedReader reader)
	{
		return 0;
	}
	
	public static Population createRandomSymmetricPopulation(double[][] distanceIndex, double[][] verticies, int size, 
			Crosser crosser, Mutator mutator, Selector selector, float crossProbability, float mutateProbability, int interfaceID) throws Exception
	{
		Population population = new Population(size, crosser, mutator, selector, PopulationType.Symmetric, 
				crossProbability, mutateProbability, interfaceID);
		
		for(int i = 0; i < size; i++)
		{
			population.addChromosome(DataFactory.createRandomChromosome(PopulationType.Symmetric, distanceIndex, verticies));
		}
		
		return population;
	}
	
	public static Population createRandomAsymmetricPopulation(double[][] distanceIndex, double[][] verticies, int size, 
			Crosser crosser, Mutator mutator, Selector selector, float crossProbability, float mutateProbability, int interfaceID) throws Exception
	{
		Population population = new Population(size, crosser, mutator, selector, PopulationType.Asymmetric, 
				crossProbability, mutateProbability, interfaceID);
		
		for(int i = 0; i < size; i++)
		{
			population.addChromosome(DataFactory.createRandomChromosome(PopulationType.Asymmetric, distanceIndex, verticies));
		}
		
		return population;
	}
	
	public static Chromosome createRandomChromosome(int populationType, double[][] distanceIndex, double[][] verticies) throws Exception
	{
		List<Integer> indicies = new ArrayList<Integer>();
		
		for(int i = 0; i < distanceIndex.length; i++)
			indicies.add(i);
		
		Chromosome chromosome;
		
		switch(populationType)
		{
			case PopulationType.Symmetric:
				chromosome = new SymmetricChromosome(distanceIndex.length, distanceIndex, verticies);
				break;
			case PopulationType.Asymmetric:
				chromosome = new AsymmetricChromosome(distanceIndex.length, distanceIndex, verticies);
				break;
			default:
				throw new Exception("Invalid population type!");
		}
	
		int size = distanceIndex.length;
		
		while(size > 0)
		{
			int index = MyRandom.getInstance().random.nextInt(size); 
			int destinationID = indicies.remove(index);
			
			try {
				chromosome.addDestination(destinationID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			size--;
			
		}

		chromosome.calculateFitnessScore();
		
		return chromosome;
	}
	
	public static Chromosome[] createNewChromosomeArray(int populationType, int size) throws Exception
	{
		switch(populationType)
		{
			case PopulationType.Symmetric:
				return new SymmetricChromosome[size];
			case PopulationType.Asymmetric:
				return new AsymmetricChromosome[size];
			default:
				throw new Exception("Invalid population type!");
		}
	}
	
	public static Chromosome createNewChromosome(int populationType, int size, double[][] distanceIndex, double[][] verticies) throws Exception
	{
		switch(populationType)
		{
			case PopulationType.Symmetric:
				return new SymmetricChromosome(size, distanceIndex, verticies);
			case PopulationType.Asymmetric:
				return new AsymmetricChromosome(size, distanceIndex, verticies);
			default:
				throw new Exception("Invalid population type!");
		}
	}
	
	public static Chromosome createNewChromosome(int populationType, int[] destinations, double[][] distanceIndex, double[][] verticies) throws Exception
	{
		switch(populationType)
		{
			case PopulationType.Symmetric:
				return new SymmetricChromosome(destinations, distanceIndex, verticies);
			case PopulationType.Asymmetric:
				return new AsymmetricChromosome(destinations, distanceIndex, verticies);
			default:
				throw new Exception("Invalid population type!");
		}
	}
}
