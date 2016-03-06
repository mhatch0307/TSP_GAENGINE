package tsp.actions.mutators;

import tsp.objects.Chromosome;

public interface Mutator 
{
	public Chromosome mutate(Chromosome parent) throws Exception;
}

