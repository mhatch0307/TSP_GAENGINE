package tsp.views;

import javax.swing.JFrame;

import tsp.objects.chromosomes.Chromosome;

public class ChromosomeGraph extends JFrame
{
	
	//Private Members
	private static final long serialVersionUID = 1L;
	private Chromosome chromosome;
	
	//Constrcutors
	public ChromosomeGraph(Chromosome chromosome)
	{
		this.chromosome = chromosome;
	}
	
	//Getters
	public Chromosome getChromosome() { return this.chromosome; }
	
	//Setters
	
	//Public Methods
	
	//Private Methods
	
}


