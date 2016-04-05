package tsp.actions.mutators;

import tsp.objects.chromosomes.Chromosome;

public interface Mutator 
{
	//Getters
	public String getDescription();
	
	//Mutators
	public void mutate(Chromosome parent) throws Exception;
}

