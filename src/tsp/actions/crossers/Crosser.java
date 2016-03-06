package tsp.actions.crossers;

import tsp.objects.Chromosome;

public interface Crosser
{
	public Chromosome[] cross(Chromosome parent1, Chromosome parent2) throws Exception;
}
