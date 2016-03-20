package tsp.test;

import tsp.actions.DataFactory;
import tsp.actions.DataReader;
import tsp.actions.crossers.RingCrosser;
import tsp.actions.mutators.SwapMutator;
import tsp.actions.selectors.RWSelector;
import tsp.objects.populations.Population;

public class DataReaderTest 
{
	public static void main(String args[])
	{
		try 
		{
			/*String filePath = "/Users/matthew/Documents/TSP/TSPProblems/s1.tsp";
			double[][] verticies = DataReader.readTSPFile(filePath);
			
			double[][] distanceIndex = DataFactory.verticiesToDistanceIndex(verticies);
			
			@SuppressWarnings("unused")
			Population population = DataFactory.createRandomSymmetricPopulation(distanceIndex, 100, new RingCrosser((float) .8), new SwapMutator((float) .2), new RWSelector(), 1);*/
			
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
}
