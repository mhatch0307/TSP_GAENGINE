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

public class GoogleMapsAPI 
{
	//Private Members
	final private String BaseURL = "https://maps.googleapis.com/maps/api/";
	private String format;
	private String key;
	
	//Constructors
	public GoogleMapsAPI(String format, String key)
	{
		this.format = format;
		this.key = key;
	}
	
	//Public Methods
	public double[] getLatLng(String address, String city, String state)
	{
		address = address.replaceAll(" ", "+");
		city = city.replaceAll(" ", "+");
		
		String URLString = this.BaseURL + "geocode/"+this.format+"?" + "address="+address+",+"+city+",+"+state+"&key"+this.key;
		
		String latLngJSON = this.makeGoogleAPIRequest(URLString);
		
		//System.out.println(latLngJSON);
		
		try 
		{
			final JSONObject jsonObj = new JSONObject(latLngJSON);
			final JSONArray latlngData = jsonObj.getJSONArray("results");
			final int length = latlngData.length();
			
			double latlng[][] = new double[length][2];
			
			for(int i = 0; i < length; i++)
			{
				final JSONObject result = latlngData.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
				latlng[i][0] = result.getDouble("lat");
				latlng[i][1] = result.getDouble("lng");
			}
			
			return latlng[0];
			
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
	public double[][] getDistanceIndexLatLng(double[][] origins, double[][] destinations, String units)
	{
		
		String URLString = this.BaseURL + "distancematrix/" + this.format + "?" + "units="+units;
		
		//append origins
		String originsString = "&origins=";
		for(int i = 0; i < origins.length; i++)
		{
			originsString += origins[i][0] + "," + origins[i][1] + ((i != origins.length - 1)? "|" : "");
		}
		
		URLString += originsString;
		
		//append destinations
		String destinationsString = "&destinations=";
		
		for(int i = 0; i < destinations.length; i++)
		{
			destinationsString += destinations[i][0] + "%2C" + destinations[i][1] + ((i != destinations.length - 1)? "%7C" : "");
		}
		
		URLString += destinationsString;
	
		URLString += "&key="+this.key;
		
		//System.out.println(URLString);
		
		String destinationsJSON = this.makeGoogleAPIRequest(URLString);
		
		System.out.println(destinationsJSON);
		
		return null;
	}
	
	public double[][] getDistanceIndexAddress(String origins[], String destinations[], String units)
	{
		String URLString = this.BaseURL + "distancematrix/" + this.format + "?" + "units="+units;
		
		//append origins
		String originsString = "&origins=";
		for(int i = 0; i < origins.length; i++)
		{
			originsString += origins[i] + ((i != origins.length - 1)? "|" : "");
		}
		
		URLString += originsString;
		
		//append destinations
		String destinationsString = "&destinations=";
		for(int i = 0; i < destinations.length; i++)
		{
			destinationsString += destinations[i] + ((i != destinations.length - 1)? "%7C" : "");
		}
		
		URLString += destinationsString;
	
		URLString += "&key="+this.key;
		
		//System.out.println(URLString);
		
		String distanceJSONString = this.makeGoogleAPIRequest(URLString);
		
		double[][] distanceIndex = this.parseDistanceJSONString(distanceJSONString);
		
		return distanceIndex;
	}
	
	//Private Methods
	private double[][] parseDistanceJSONString(String distanceJSONString)
	{
		try
		{
			final JSONObject jsonObj = new JSONObject(distanceJSONString);
			final JSONArray distanceData = jsonObj.getJSONArray("rows");
			int length = distanceData.length();
			
			double distanceIndex[][] = new double[length][length];
			
			for(int i = 0; i < length; i++)
			{
				final JSONArray elementData = distanceData.getJSONObject(i).getJSONArray("elements");
				for(int j = 0; j < length; j++)
				{
					final JSONObject result = elementData.getJSONObject(j).getJSONObject("distance");
					distanceIndex[i][j] = result.getDouble("value");
				}
			}
			return distanceIndex;
		}
		catch (JSONException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String makeGoogleAPIRequest(String URLString)
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
			
			String latLngJSON = buffer.toString();
			
			return latLngJSON;
			
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		 
		return null;
	}
	
	public static void main(String args[])
	{
		GoogleMapsAPI googleMapsAPI = new GoogleMapsAPI("json", "AIzaSyAIIGgX0htxhCBnOy6xk_7ZCOAPz3ntoSk");
		
		double[][] origins = new double[2][2];
		origins[0] = googleMapsAPI.getLatLng("1515 Windrider Ct", "Fenton", "MO"); 
		origins[1] = googleMapsAPI.getLatLng("5119 Tree Valley Rd", "Chubbuck", "ID");
		
		double[][] destinations = new double[2][2];
		destinations[0] = googleMapsAPI.getLatLng("3250 Hudson Crossing", "McKinney", "TX");
		destinations[1] = googleMapsAPI.getLatLng("1412 Center St", "East Aurora", "NY");
	
		String[] stringOrigins = new String[] {"1515+Windrider+Ct+Fenton,+MO", "5119+Tree+Valley+Rd+Chubbuck,+ID"};
		String[] stringDestinations = new String[] {"3250+Hudson+Crossing+McKinney,+TX", "1412+Center+St+East+Aurora,+NY"};
		
		//System.out.println("lat: " + origins[0] + " lng: " + origins[1]);
		//googleMapsAPI.getDistanceIndexLatLng(origins, destinations, "imperial");
		
		double distanceIndex[][] = googleMapsAPI.getDistanceIndexAddress(stringOrigins, stringDestinations, "imperial");
		 
		for(int i = 0; i < distanceIndex.length; i++)
		{
			for(int j = 0; j < distanceIndex.length; j++)
			{
				System.out.println(distanceIndex[i][j]);
			}
		}
		
	}
}
