package tsp.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tsp.actions.crossers.OXCrosser;
import tsp.actions.mutators.SwapMutator;
import tsp.actions.selectors.RWSelector;
import tsp.objects.NonThreadedGA;
import tsp.objects.TSPObject;
import tsp.objects.chromosomes.Chromosome;
import tsp.objects.populations.Population;

public class MapQuestAPI 
{
	//Private Members
	final private String BaseURL = "http://www.mapquestapi.com/directions/v2/routematrix?";
	private String key;
	
	//Constructors
	public MapQuestAPI(String key)
	{
		this.key = key;
	}
	
	//Public Methods
	
	public TSPObject getTSPProblem(JSONArray addresses) throws JSONException
	{
		
		String locationsJSON = "{locations:[";
		
		int length = addresses.length();
		
		for(int i = 0; i < length; i++)
		{
			JSONObject addressObject = addresses.getJSONObject(i);
			
			String address = (addressObject.getString("address").replaceAll(" ", "%20")); //+ " " + 
							 //addressObject.getString("city") + " " + 
							 //addressObject.getString("state"))
			
			locationsJSON += "\"" + address + "\"" + ((i == length - 1)? "" : ",");
		}
		
		locationsJSON += "],options:{allToAll:true}}";
		
		String url = BaseURL + "key=" + this.key + "&json=" + locationsJSON;
		
		String result = this.makeAPIRequest(url);
		
		JSONObject distanceObject = new JSONObject(result);
		
		JSONArray distanceArray = distanceObject.getJSONArray("distance");
		
		JSONArray locationArray = distanceObject.getJSONArray("locations");
		
		double distanceIndex[][] = distanceArrayToDistanceIndex(distanceArray);
		double verticies[][] = locationArrayToVerticies(locationArray);
		
		return new TSPObject(distanceIndex, verticies);
	}
	
	public TSPObject getTSPProblem(String[] addresses) throws JSONException
	{
		
		String locationsJSON = "{locations:[";
		
		int length = addresses.length;
		
		for(int i = 0; i < length; i++)
		
		{
			String address = (addresses[i].replaceAll(" ", "%20")); //+ " " + 
							 //addressObject.getString("city") + " " + 
							 //addressObject.getString("state"))
			
			locationsJSON += "\"" + address + "\"" + ((i == length - 1)? "" : ",");
		}
		
		locationsJSON += "],options:{allToAll:true}}";
		
		String url = BaseURL + "key=" + this.key + "&json=" + locationsJSON;
		
		String result = this.makeAPIRequest(url);
		
		JSONObject distanceObject = new JSONObject(result);
		
		JSONArray distanceArray = distanceObject.getJSONArray("distance");
		
		JSONArray locationArray = distanceObject.getJSONArray("locations");
		
		double distanceIndex[][] = distanceArrayToDistanceIndex(distanceArray);
		double verticies[][] = locationArrayToVerticies(locationArray);
		
		return new TSPObject(distanceIndex, verticies);
	}
	
	//Private Methods
	
	private double[][] distanceArrayToDistanceIndex(JSONArray distanceArray) throws JSONException
	{
		int length = distanceArray.length();
		double[][] distanceIndex = new double[length][length];
		
		for(int i = 0; i < length; i++)
		{
			JSONArray distances = distanceArray.getJSONArray(i);
			for(int j = 0; j < length; j++)
			{
				distanceIndex[i][j] = distances.getDouble(j);
			}
		}
		return distanceIndex;
	}
	
	private double[][] locationArrayToVerticies(JSONArray locationArray) throws JSONException
	{
		int length = locationArray.length();
		double[][] verticies = new double[length][2];
		
		for(int i = 0; i < length; i++)
		{
			JSONObject latlng = locationArray.getJSONObject(i).getJSONObject("latLng");
			verticies[i][0] = latlng.getDouble("lat");
			verticies[i][1] = latlng.getDouble("lng");
		}
		
		return verticies;
	}
	
	private String makeAPIRequest(String URLString)
	{
		try
		{
			HttpURLConnection urlConnection;
			
			URL url = new URL(URLString);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			
			InputStream inputStream = urlConnection.getInputStream();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			
			StringBuffer buffer = new StringBuffer();
			
			String line;
			
			while((line = reader.readLine()) != null)
			{
				buffer.append(line + "\n");
			}
			
			String response = buffer.toString();
			
			return response;
			
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		 
		return null;
	}
	
	public static void main(String[] args)
	{
		String addresses[] = new String[]{"5100 Eldorado Pkwy McKinney, TX", 
										  "1925 N Central Expy McKinney, TX", 
										  "2414 W University Dr McKinney, TX", 
										  "6150 Eldorado Pkwy McKinney, TX",
										  "6851 Virginia Pkwy McKinney, TX",
										  "6405 Eldorado Pkwy McKinney, TX",
										  "107 N Kentucky St McKinney, TX",
										  "3250 Hudson Crossing McKinney, TX",
										  "1720 N Central Expy McKinney, TX",
										  "1521 N Custer Rd McKinney, TX",
										  "330 E Louisiana St McKinney, TX",
										  "107 S Church St McKinney, TX",
										  "7820 Eldorado Pkwy McKinney, TX",
										  "6100 Eldorado Pkwy McKinney, TX",
										  "1800 N Graves St McKinney, TX",
										  "2775 S Central Expy McKinney, TX",
										  "4610 Eldorado Pkwy McKinney, TX",
										  "311 E Louisiana St McKinney, TX",
										  "218 E Louisiana St McKinney, TX",
										  "115 N Kentucky St McKinney, TX"};
		
		MapQuestAPI api = new MapQuestAPI("RN6BLpAaEsvbUBaCgRVJBChczCTgS134");
		
		try {
			TSPObject tsp = api.getTSPProblem(addresses);
			
			Population population = null;
			try {
				population = DataFactory.createRandomSymmetricPopulation(tsp.getDistanceIndex(), null, 20, new OXCrosser(), 
						new SwapMutator(), new RWSelector(), (float) .7, (float) .1, 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			NonThreadedGA ga = new NonThreadedGA(population, 100);
			
			ga.run();
			
			double[][] verticies = tsp.getVerticies();
			
			Chromosome solution = ga.getMostOptimalChromosome();//this.uiController.getSolutions().get(0);
			
			System.out.println("Solution Distance: " + solution.getFitnessScore());
			
			JSONObject geolocations = new JSONObject();
			
			JSONArray solutionVerticies = new JSONArray();
			
			int length = solution.getSize();
			
			for(int i = 0; i < length; i++)
			{
				JSONObject vertex = new JSONObject();
				int destination = solution.getDestination(i);
				vertex.put("lat", verticies[destination][0]);
				vertex.put("lng", verticies[destination][1]);
				solutionVerticies.put(vertex);
			}
			
			geolocations.put("geolocations", solutionVerticies);
			
			System.out.println(geolocations.toString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
