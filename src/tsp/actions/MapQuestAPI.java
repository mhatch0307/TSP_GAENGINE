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

import tsp.objects.TSPObject;

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
			
			String address = (addressObject.getString("address").replaceAll(" ", "%20"));
			
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
			//System.out.println(e.getMessage());
			e.printStackTrace();
		}
		 
		return null;
	}
}
