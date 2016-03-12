package tsp.actions.crossers;

import tsp.objects.chromosomes.Chromosome;

public interface Crosser
{
	//Getters
	public String getDescription();
	
	//Public Methods
	public Chromosome[] cross(Chromosome parent1, Chromosome parent2) throws Exception;
}
