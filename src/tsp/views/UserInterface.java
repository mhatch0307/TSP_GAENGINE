package tsp.views;

import java.util.List;

import tsp.objects.chromosomes.Chromosome;

public abstract class UserInterface extends Thread
{
	//Public Members
	public abstract void prompt(boolean displayOptions);
	public abstract void displayMessage(String message);
	public abstract void refresh();

	//Protected Members
	protected int interfaceID;
	protected List<Chromosome> solutions;
	
	//Getters
	List<Chromosome> getSolution() { return this.solutions; }
	
	//Setters
	public void setInterfaceID(int interfaceID) { this.interfaceID = interfaceID;}
	
	//Public Methods
	public void addSolution(Chromosome solution) { this.solutions.add(solution); }
}
