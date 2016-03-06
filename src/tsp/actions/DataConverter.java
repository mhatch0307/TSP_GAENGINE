package tsp.actions;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mxgraph.view.mxGraph;

import tsp.actions.crossers.Crosser;
import tsp.actions.crossers.RingCrosser;
import tsp.actions.mutators.Mutator;
import tsp.actions.mutators.SwapMutator;
import tsp.actions.selectors.RankSelector;
import tsp.actions.selectors.Selector;
import tsp.objects.Chromosome;
import tsp.objects.Destination;
import tsp.objects.Population;


public class DataConverter 
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
	
		if(vertexIndex != size - 1)
			throw new Exception ("Invalid File! Only " + (vertexIndex + 1) + " valid vertices found out of " + (size + 1));
		
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
		
		
		
		return null;
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
	
	public static Population createRandomPopulation(double[][] distanceIndex, int size, Crosser crosser, Mutator mutator, Selector selector) throws Exception
	{
		Population population = new Population(size, crosser, mutator, selector);
		
		for(int i = 0; i < size; i++)
		{
			population.addChromosome(DataConverter.createRandomChromosome(distanceIndex));
		}
		
		return population;
	}
	
	public static Chromosome createRandomChromosome(double[][] distanceIndex)
	{
		List<Integer> indicies = new ArrayList<Integer>();
		
		for(int i = 0; i < distanceIndex.length; i++)
			indicies.add(i);
		
		Chromosome chromosome = new Chromosome(distanceIndex.length, distanceIndex);
		
		int size = distanceIndex.length;
		
		Random random = new Random();
		
		while(size > 0)
		{
			int index = random.nextInt(size); 
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
	
}
