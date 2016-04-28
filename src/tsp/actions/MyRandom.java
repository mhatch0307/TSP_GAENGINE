package tsp.actions;

import java.util.Random;

public class MyRandom {

	public Random random;
	private static MyRandom instance;
	
	public MyRandom()
	{
		this.random = new Random();
		//this.random.setSeed(System.nanoTime());
	}
	
	public static MyRandom getInstance()
	{
		if(MyRandom.instance == null)
		{
			MyRandom.instance = new MyRandom();
		}
		return instance;
	}
	
}
