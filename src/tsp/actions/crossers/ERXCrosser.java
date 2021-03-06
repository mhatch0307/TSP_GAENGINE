package tsp.actions.crossers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tsp.actions.DataFactory;
import tsp.actions.MyRandom;
import tsp.objects.chromosomes.Chromosome;

public class ERXCrosser implements Crosser
{
	//Private Members
	private Random random;
	private int neighborListSize; 
	private List<List<Integer>> neighborLists;
	private List<Integer> remainingDestinations;
	
	//Constructors
	public ERXCrosser() 
	{
		random = MyRandom.getInstance().random;
	}
	
	@Override
	public String getDescription() { return "ERX"; }	
	
	//Public Methods
	@Override
	public Chromosome[] cross(Chromosome parent1, Chromosome parent2) throws Exception 
	{
		Chromosome children[] = null;
		Chromosome child1 = parent1.copy();
		Chromosome child2 = parent2.copy();
		
		children = DataFactory.createNewChromosomeArray(parent1.populationType, 1);
		children[0] = this.runIteration(child1, child2);
		//children[1] = this.runIteration(child2, child1);
	
		return children;
	}
	
	private Chromosome runIteration(Chromosome parent1, Chromosome parent2) throws Exception 
	{
		Chromosome child;
		
		int chromosomeSize = parent1.getSize();
		double[][] distanceIndex = parent1.getDistanceIndex();	
		double[][] verticies = parent1.getVerticies();
		
		// prep tracking for remaining destinations
		this.remainingDestinations = new ArrayList<Integer>();
		for(int i = 0; i < chromosomeSize; i++)
			this.remainingDestinations.add(i);		
		
		this.generateNeighborList(parent1, parent2);
		
		child = DataFactory.createNewChromosome(parent1.populationType, chromosomeSize, distanceIndex, verticies);
		
		int destination = (random.nextInt(2) == 0)? parent1.getDestination(0) : parent2.getDestination(0);
		
		this.remainingDestinations.remove(this.remainingDestinations.indexOf(destination));
		child.addDestination(destination);
		this.removeFromNeighborList(destination);
	
		while(!child.isFull())
		{	
			List<Integer> neighbors = this.neighborLists.get(destination);
			if(neighbors.isEmpty())
			{
				int index = random.nextInt(this.remainingDestinations.size());
				destination = this.remainingDestinations.get(index);	
				this.remainingDestinations.remove(index);
			}
			else
			{
				destination = findFewestNeighborsIndex(neighbors);
				remainingDestinations.remove(remainingDestinations.indexOf(destination));
			}
			this.removeFromNeighborList(destination);
			child.addDestination(destination);
		}
		
		child.calculateFitnessScore();
		return child;
	}
	
	//Private Methods
	private void removeFromNeighborList(int destination)
	{
		for(int i = 0; i < this.neighborListSize; i++)
		{
			List<Integer> neighbors = this.neighborLists.get(i);
			
			if(neighbors != null && !neighbors.isEmpty())
			{
				int index = neighbors.indexOf(destination);
				
				if(index >= 0) neighbors.remove(index);
			}
		}
	}
	
	private int findFewestNeighborsIndex(List<Integer> neighbors)
	{
		int size = neighbors.size();
		int fewestNeighborsCount = 5;
		int destinationWithFewest = 0;
		
		for(int i = 0; i < size; i++)
		{
			int currentDestination = neighbors.get(i);
			int currentCount = this.neighborLists.get(currentDestination).size();
			if(currentCount < fewestNeighborsCount ||
					(currentCount == fewestNeighborsCount && this.random.nextInt(2) == 0))
			{
				destinationWithFewest = currentDestination;
				fewestNeighborsCount = currentCount;
			}
		}
		return destinationWithFewest;
	}
	
	private void generateNeighborList(Chromosome parent1, Chromosome parent2) throws Exception
	{
		this.neighborListSize = parent1.getSize();
		
		this.neighborLists = new ArrayList<List<Integer>>();
		
		for(int i = 0; i < this.neighborListSize; i++)
			this.neighborLists.add(new ArrayList<Integer>());
		
		// generate neighbor lists
		for(int i = 0; i < this.neighborListSize; i++)
		{
			int prevIndex = (i - 1 + this.neighborListSize) % this.neighborListSize;
			int nextIndex = (i + 1) % this.neighborListSize;
			
			int p1ID = parent1.getDestination(i);
			int p2ID = parent2.getDestination(i);
			
			int p1Prev = parent1.getDestination(prevIndex);
			int p1Next = parent1.getDestination(nextIndex);
			int p2Prev = parent2.getDestination(prevIndex);
			int p2Next = parent2.getDestination(nextIndex);
			
			List<Integer> p1Neighbors = this.neighborLists.get(p1ID);
			List<Integer> p2Neighbors = this.neighborLists.get(p2ID);
			
			if(p1Neighbors.indexOf(p1Next) == -1) p1Neighbors.add(p1Next);
			if(p1Neighbors.indexOf(p1Prev) == -1) p1Neighbors.add(p1Prev);
			if(p2Neighbors.indexOf(p2Next) == -1) p2Neighbors.add(p2Next);
			if(p2Neighbors.indexOf(p2Prev) == -1) p2Neighbors.add(p2Prev);
		}
	}
	
	
	@SuppressWarnings("unused")
	private void displayNeighborLists()
	{
		for (int i = 0; i < this.neighborListSize; i++)
		{
			System.out.print(i + ": ");
			List<Integer> neighbors = this.neighborLists.get(i);
			for(int j = 0; j < neighbors.size(); j++)
				System.out.print(neighbors.get(j) + " ");
			System.out.println();
		}
	}
}
