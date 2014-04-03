import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class CapitalCities {

	public static void findDenseCapitals(LinkedList<String> countryCodes,
			HashMap<String, String> countryCodeToCountry) {
		//create an adjacency Matrix to mark which capitals are within 10 degs of
		//latitude and longitude. mark '1' for within 10 degrees and '0' for 
		//not within 10 degrees. Initialize to '-1' for not yet visited
		int[][] adjacencyMatrix = new int[countryCodes.size()+1][countryCodes.size()+1];
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			for (int j = 0; j < adjacencyMatrix[0].length; j++) {
				adjacencyMatrix[i][j] = -1;
			}
		}
		
		//create a new system for longitude and latitude. N and E values count
		//as positive, and S and W values count as negative values.
		//get the values for each country. also, get rid of mintues and turn
		//values into decimals.
		ArrayList<HashMap<String, Float>> latsAndLongMatchings = getLatsAndLongs(countryCodes, countryCodeToCountry);
		HashMap<String, Float> countryToLats = latsAndLongMatchings.get(0);
		HashMap<String, Float> countryToLongs = latsAndLongMatchings.get(1);
		
		//create a hashmap that describes which countries are within range
		HashMap<String, ArrayList> countriesInRange = new HashMap<String, ArrayList>();
		
		for (String country : countryCodes) {
			ArrayList<String> curCloseCountries = getCloseCountries(countryToLats, countryToLongs, country);
			countriesInRange.put(countryCodeToCountry.get(country), curCloseCountries);
		}
		
		//then find country with most...
		
		
		Reset.reset(countryCodes, countryCodeToCountry);	
	}

	private static ArrayList<String> getCloseCountries(
			HashMap<String, Float> countryToLatitude,
			HashMap<String, Float> countryToLongitude, String country) {
		// TODO Auto-generated method stub
		return null;
	}

	private static ArrayList<HashMap<String, Float>> getLatsAndLongs(LinkedList<String> countryCodes, HashMap<String, String> countryCodeToCountry) {
		//the first element of the list is a hashmap that contains the country-lat mappings.
		//the second element of the list is a hashmap that contains the county-long mappings.
		ArrayList<HashMap<String, Float>> latsAndLongs = new ArrayList<HashMap<String, Float>>();
		
		//first, we need the two hashmaps that will be inserted into the ArrayList
		HashMap<String, Float> countryToLat = new HashMap<String, Float>();
		HashMap<String, Float> countryToLong = new HashMap<String, Float>();
		
		//get the latitudes and longitudes for each country
		for (String country : countryCodes) {
			String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
					+ country + ".html";
			try {
				Document countryPage = Jsoup.connect(curCountryURL).get();
				String pageHtml = countryPage.html();
			
				//the southeastern hemisphere is defined by S latitudes and E longitudes
				String template = "Capital:</a>\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*"
						+ "\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*>(\\d*\\s*\\d*\\s*\\w*,\\s*\\d*\\s*\\d*\\s*\\w*)\\s*<";
				Pattern p = Pattern.compile(template);
				Matcher m = p.matcher(pageHtml);		
				if (m.find()) {
					String coordinates = m.group(1);
					String[] coordinatesParts = coordinates.split(" ");
					int negOrPosLat = 0;
					if (coordinatesParts[2].contains("N")) {
						negOrPosLat = 1;
					}
					else if (coordinatesParts[2].contains("S")) {
						negOrPosLat = -1;
					}
					float firstHalfOfLatitude = Float.parseFloat(coordinatesParts[0]);
					float secondHalfOfLatitude = (float) (Float.parseFloat(coordinatesParts[1])/60.0);
					float latitude = (firstHalfOfLatitude + secondHalfOfLatitude)*negOrPosLat;
					//put the country's latitude in the mapping
					countryToLat.put(countryCodeToCountry.get(country), latitude);
					
					int negOrPosLong = 0;
					if (coordinatesParts[5].contains("E")) {
						negOrPosLong = 1;
					}
					if (coordinatesParts[5].contains("W")) {
						negOrPosLong = -1;
					}
					float firstHalfOfLongitude = Float.parseFloat(coordinatesParts[3]);
					float secondHalfOfLongitude = (float) (Float.parseFloat(coordinatesParts[4])/60.0);
					float longitude = (firstHalfOfLongitude + secondHalfOfLongitude)*negOrPosLong;
					countryToLong.put(countryCodeToCountry.get(country), longitude);	
					
					System.out.println(countryCodeToCountry.get(country) + "...lat: " + latitude + " ...long: " + longitude);
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		latsAndLongs.add(countryToLat);
		latsAndLongs.add(countryToLong);
		return latsAndLongs;
	}
}

