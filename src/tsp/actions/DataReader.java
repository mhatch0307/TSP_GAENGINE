package tsp.actions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataReader 
{
	public static double[][] readTSPFile(String TSPFilePath) throws IOException
	{
		 BufferedReader reader = new BufferedReader(new FileReader(TSPFilePath));
		 
		 //Find TSP size
		
		 String line = reader.readLine();
		 
		 int size = Integer.parseInt(line);
		 
		 double[][] verticies = new double[size][2];
		 
		 for(int i = 0; i < size; i++)
		 {
			 line = reader.readLine();
			 int seperatorIndex = line.indexOf(',');
			 String xPos = line.substring(0, seperatorIndex);
			 String yPos = line.substring(seperatorIndex + 1, line.length());
			 verticies[i][0] = Double.parseDouble(xPos);
			 verticies[i][1] = Double.parseDouble(yPos);
		 }
		 
		 reader.close();
		 
		 return verticies;
	}
}
