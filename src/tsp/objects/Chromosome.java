package tsp.objects;

import java.util.Arrays;
import java.util.Random;

public abstract class Chromosome 
{
	
	//Private Members
	private String description;
	
	//Protected Members
	protected int[] destinations;
	protected int currentSize;
	protected double[][] distanceIndex;
	protected double fitnessScore;
	
	//Constructors
	public Chromosome(int size, double[][] distanceIndex)
	{
		this.destinations = new int[size];
		this.currentSize = 0;
		this.distanceIndex = distanceIndex;
		this.fitnessScore = 0;
		this.description = "Chromosome";
	}
	
	public Chromosome(int[] destinations, double[][] distanceIndex)
	{
		this.destinations = Arrays.copyOf(destinations, destinations.length);
		this.currentSize = destinations.length;
		this.distanceIndex = distanceIndex;
		this.calculateFitnessScore();
	}
	
	//Copy Constrcutor
	public abstract Chromosome copy();
	
	//Getters
	public int getMaxSize() { return destinations.length; }
	public int getSize() { return this.currentSize; }
	public boolean isEmpty() { return this.currentSize == 0; }
	public boolean isFull() { return this.currentSize == destinations.length; }
	public double getFitnessScore() { return this.fitnessScore; }
	public double[][] getDistanceIndex() { return this.distanceIndex; }
	public String getDescription() { return this.description; }
	
	public int getDestination(int index) throws Exception
	{
		if(index >= currentSize || index < 0)
			throw new Exception ("The specified index does not exist in the current population. Current Size: " + this.currentSize);
		
		return this.destinations[index];	
	}
	
	public int[] getSubsection(int start, int length) throws Exception
	{	
		//int end = (start + length) % this.currentSize;
		
		if(/*start > end || end > this.currentSize ||*/ start < 0)
			throw new Exception ("Invalid subsction!");

		int[] subSection = new int[length];
		
		for (int i = 0; i < length; i++)
		{
			subSection[i] = this.destinations[(i + start) % this.currentSize];
		}
		
		return subSection;
	}
	
	// gets the subsection in the reverse order
	public int[] getSubsectionReverse(int start, int length) throws Exception
	{	
		//int end = (start + length) % this.currentSize;
		
		if(/*start > end || end > this.currentSize ||*/ start < 0)
			throw new Exception ("Invalid subsction!");

		int[] subSection = new int[length];
		
		int endIndex = length - 1;
		
		for (int i = 0; i < length; i++)
		{
			subSection[endIndex - i] = this.destinations[(i + start) % this.currentSize];
		}
		
		return subSection;
	}
	
	public int indexOf(int destination)
	{
		for (int i = 0; i < this.currentSize; i++)
		{
			if(this.destinations[i] == destination)
				return i;
		}
		return -1;
	}
	
	public int getOutterDistanceBetween(int index1, int index2)
	{
		if(index1 >= this.currentSize || index2 >= this.currentSize)
			return -1;
		
		if(index1 == index2)
			return this.currentSize - 1;
		else if(index1 < index2)
			return index1 + (this.currentSize - 1 - index2);
		else
			return index2 + (this.currentSize - 1 - index1);
	}
	
	//Setters
	public void setDescription(String description) { this.description = description; }
	
	/**
	 * Sets the destination at the specified index if the index specified is < 
	 * this.currentSize
	 * @param Destination
	 * @param index
	 * @throws Exception 
	 */
	public void setDestinationUpdateFitness(int destination, int index) throws Exception
	{
		if(index >= currentSize || index < 0)
			throw new Exception ("The specified index does not exist in the current population.");
		int old = this.destinations[index];
		this.destinations[index] = destination;
		this.updateDistanceScore(index, old);
	}
	
	/**
	 * Sets the destination at the specified index if the index specified is < 
	 * this.currentSize
	 * @param Destination
	 * @param index
	 * @throws Exception 
	 */
	public void setDestination(int destination, int index) throws Exception
	{
		if(index >= this.destinations.length || index < 0)
			throw new Exception ("The specified index its outside of the maximum bound.");
		this.destinations[index] = destination;
		if(this.currentSize <= index)
			this.currentSize = index + 1;
	}
	
	//Public Methods
	public abstract void calculateFitnessScore();

	/**
	 * Adds a Destination to the population if there is room in the chromosome
	 * 
	 * @param Destination destination
	 * @throws Exception 
	 */
	public void addDestination(int destination) throws Exception
	{
		try
		{
			if(this.currentSize == this.destinations.length)
				throw new Exception("The maximum population has been reached!");
			this.destinations[currentSize] = destination;
			this.addDistanceScore(currentSize++);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Swaps the Destination at index1 with the Destination at index2
	 * 
	 * @param int index1
	 * @param int index2
	 * @throws Exception 
	 */
	public void swapDestinations(int index1, int index2) throws Exception
	{
		try
		{
			int temp = this.destinations[index1];
			this.destinations[index1] = this.destinations[index2];
			this.updateDistanceScore(index1, temp);
			int temp2 = this.destinations[index2];
			this.destinations[index2] = temp;
			this.updateDistanceScore(index2, temp2);
		}
		catch(Exception e)
		{
			throw new Exception ("Invalid indicies specified.");
		}
	}
	
	public void removeElementsInSection(int[] section)
	{
		for(int i = 0; i < section.length; i++)
		{
			for (int j = 0; j < this.currentSize; j++)
			{
				if (section[i] == this.destinations[j])
				{
					this.destinations[j] = -1;
					break;
				}
			}
		}
	}
	
	public void slide(int start, int[] swapSection)
	{
		int newIndex = (start - swapSection.length + this.currentSize) % this.currentSize;
		int[] newDestinations = new int[this.currentSize];
		int spotsFilled = 0;
		int destIndex = start;
		
		for(int i = 0; i < swapSection.length; i++, newIndex = (newIndex + 1) % this.currentSize, destIndex = (destIndex + 1) % this.currentSize, spotsFilled +=2)
		{
			newDestinations[start + i] = swapSection[i];
			while(this.destinations[destIndex] == -1)
				destIndex = (destIndex + 1) % this.currentSize;
			newDestinations[newIndex] = this.destinations[destIndex];
		}
		
		newIndex = (newIndex + swapSection.length) % this.currentSize;
		
		for( ; spotsFilled < this.currentSize; destIndex = (destIndex + 1) % this.currentSize, newIndex = (newIndex + 1) % this.currentSize, spotsFilled++)
		{
			while(this.destinations[destIndex] == -1)
				destIndex = (destIndex + 1) % this.currentSize;
			newDestinations[newIndex] = this.destinations[destIndex];
		}
		
		this.destinations = newDestinations;
		this.calculateFitnessScore();
	}
	
	public void insertSubSection(int startIndex, int[] subSection)
	{
		int[] newDestinations = new int[this.currentSize];
		
		int index = 0;
		int currentIndex = startIndex;
		while (index < subSection.length)
		{
			newDestinations[currentIndex] = subSection[index++];
			currentIndex = (currentIndex + 1) % this.currentSize;
		}
		
		int spotsFilled = subSection.length;
		
		for(int i = (startIndex + 2) % this.currentSize; spotsFilled < this.currentSize; 
				spotsFilled++, i = (i + 1) % this.currentSize, currentIndex = (currentIndex + 1) % this.currentSize)
		{
			while(this.destinations[i] == -1)
				i = (i + 1) % this.currentSize;
			newDestinations[currentIndex] = this.destinations[i];
		}
	
		this.destinations = newDestinations;
		this.calculateFitnessScore();
	}
	
	/**
	 * Inverts between index1 and index 2 by bringing index1 to index2
	 * index1 - int first index
	 * index2 - int second index
	 * @throws Exception 
	 */
	public void invert(int index1, int index2) throws Exception
	{
		if(index1 >= this.currentSize || index2 >= this.currentSize)
			throw new Exception("Indexes are out of bounds!");
		if(index1 == index2)
			throw new Exception("Indicies cannot equal each other!");
		
		int outterDistance = this.getOutterDistanceBetween(index1, index2);
		
		int stoppingPoint = outterDistance / 2;
		
		if(index1 > index2)
		{
			for (int adder = outterDistance, i = 0; adder > stoppingPoint; i++, adder--)
			{
				int swapIndex1 = (index1 + adder) % this.currentSize;
				int swapIndex2 = (index1 + i) % this.currentSize;
				this.swapDestinations(swapIndex1, swapIndex2);
			}
		}
		else
		{
			for (int adder = outterDistance, i = 0; adder > stoppingPoint; i++, adder--)
			{
				int swapIndex1 = (this.currentSize + (index1 -i)) % this.currentSize;
				int swapIndex2 = (this.currentSize + (index1 - adder)) % this.currentSize;
				
				this.swapDestinations(swapIndex1, swapIndex2);
			}
		}
		
	}
	
	public void swapDuplicates(Chromosome chromosome) throws Exception
	{
		int[] destinationCount = new int[this.currentSize];
		int chromo2Index = 0;
		int[] chromo2DestinationCount = new int[this.currentSize];
		for(int i = 0; i < this.currentSize; i++)
		{
			destinationCount[this.destinations[i]] += 1;
			if(destinationCount[this.destinations[i]] == 2)
			{
				for(int j = chromo2Index; j < this.currentSize; j++, chromo2Index++)
				{
					chromo2DestinationCount[chromosome.destinations[j]] += 1;
					if(chromo2DestinationCount[chromosome.destinations[j]] == 2)
					{
						int temp = this.destinations[i];
						this.setDestination(chromosome.destinations[j], i);
						chromosome.setDestination(temp, j);
						break;
					}
				}
			}
		}
	}
	
	public void scramble(int index1, int index2) throws Exception
	{
		if(index1 < 0 || index1 >= this.currentSize || index2 < 0 || index2 >= this.currentSize)
			throw new Exception("Indicies are out of bounds!");
		
		if(index1 > index2) // swap
		{
			int temp = index1;
			index1 = index2;
			index2 = temp;
		}
		
		int numInSubsection = (index2 - index1) + 1;
		Random random = new Random();
		for(int i = index1; i <= index2; i++)
		{
			int swapWith = random.nextInt(numInSubsection) + index1;
			this.swapDestinations(i, swapWith);
		}
		
	}
	
	public void dispaly()
	{
		System.out.println(this.description);
		for(int i = 0; i < this.currentSize; i++)
		{
			System.out.print(this.destinations[i] + " ");
		}
		System.out.println("\nDistance Score: " + this.fitnessScore + "\n");
		//this.calculateFitnessScore();
		//System.out.println(this.fitnessScore);
	}
	
	//Protected Methods
	// Used when updating an existing destination
	protected abstract void updateDistanceScore(int destinationIndex, int oldID);
	
	// Used when adding a new destination
	protected abstract void addDistanceScore(int currentIndex);
}
