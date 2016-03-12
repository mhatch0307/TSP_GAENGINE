package tsp.actions.crossers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tsp.objects.Chromosome;
import tsp.objects.SymmetricChromosome;

public class ERXCrosser implements Crosser
{
	//Private Members
	private float probability; 
	private Random random;
	private int neighborListSize; 
	private List<List<Integer>> neighborLists;
	private List<Integer> remainingDestinations;
	
	//Constructors
	public ERXCrosser(float probability) 
	{
		random = new Random();
	}
	
	@Override
	public String getDescription() { return "ERX"; }	
	
	//Public Methods
	@Override
	public Chromosome[] cross(Chromosome parent1, Chromosome parent2) throws Exception 
	{
		
		
		return new Chromosome[] {this.runIteration(parent1, parent2), this.runIteration(parent1, parent2)};
	}
	
	private Chromosome runIteration(Chromosome parent1, Chromosome parent2) throws Exception 
	{
		//parent1.display();
		//parent2.display();
		Chromosome child;
		
		if(this.random.nextFloat() <= this.probability)
		{
			int chromosomeSize = parent1.getSize();
			double[][] distanceIndex = parent1.getDistanceIndex();	
			
			// prep tracking for remaining destinations
			this.remainingDestinations = new ArrayList<Integer>();
			for(int i = 0; i < chromosomeSize; i++)
				this.remainingDestinations.add(i);		
			
			this.generateNeighborList(parent1, parent2);
			
			//this.displayNeighborLists();
			
			child = new SymmetricChromosome(chromosomeSize, distanceIndex);
			
			int destination = (random.nextInt(2) == 0)? parent1.getDestination(0) : parent2.getDestination(0);
			
			this.remainingDestinations.remove(this.remainingDestinations.indexOf(destination));
			child.addDestination(destination);
			removeFromNeighborList(destination);
			
			//System.out.println("First Destination: " + destination);
			//child.display();
			//this.displayNeighborLists();
		
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
				//System.out.println("Destination: " + destination);
				removeFromNeighborList(destination);
				child.addDestination(destination);
				//child.display();
				//this.displayNeighborLists();
			}
			
			child.calculateFitnessScore();
			//child.display();
		}
		else
		{
			child = parent1.copy();
		}
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
