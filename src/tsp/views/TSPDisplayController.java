package tsp.views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tsp.objects.chromosomes.Chromosome;

public class TSPDisplayController
{
	@FXML
	private Label gaDescription;
	
	@FXML
	private Label generationNumber;
	
	@FXML
	private Label chromosome;
	
	@FXML
	private Label fitnessScore;
	
	//Constructors
	public TSPDisplayController()
	{
		
	}
	
	//Public Methods
	public void updateDisplay(Chromosome chromosome, int generationNumber, double fitnessScore)
	{
		this.gaDescription.setText(chromosome.getDescription());
		this.generationNumber.setText("Generation #: " + generationNumber);
		this.chromosome.setText("Chromosome: " + chromosome.toString());
		this.fitnessScore.setText("Fitness Score: " + fitnessScore);
	}
}
