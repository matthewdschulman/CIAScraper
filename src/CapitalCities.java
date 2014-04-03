import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class CapitalCities {

	public static void findDenseCapitals(LinkedList<String> countryCodes,
			HashMap<String, String> countryCodeToCountry, HashMap<String, String> countryToCode) {
		//create an adjacency Matrix to mark which capitals are within 10 degs of
		//latitude and longitude. mark '1' for within 10 degrees and '0' for 
		//not within 10 degrees. Initialize to '-1' for not yet visited
		System.out.println("Please wait for a minute...");
		System.out.println("The country/capital surronded by the most capitals within 10 degs of longitude and latitude is...");
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
			ArrayList<String> curCloseCountries = getCloseCountries(countryToLats, countryToLongs, country, countryCodes, countryCodeToCountry);
			countriesInRange.put(countryCodeToCountry.get(country), curCloseCountries);
		}
		
		HashMap<String, ArrayList> countryWithMostCapitals = new HashMap<String, ArrayList>();
		int largestNumberOfCloseCapitals = 0;
		//iterate through the hashmap of country -> closeCountriesToThatCountry to find the 
		//country with the most close countries (based of the capital cities)
		java.util.Iterator<Entry<String, ArrayList>> iter = countriesInRange.entrySet().iterator();
	    while (iter.hasNext()) {
	        Map.Entry pairs = (Map.Entry)iter.next();
	        ArrayList<String> curCloseCountries = (ArrayList<String>) pairs.getValue();
	        int curSizeOfCloseCountries = curCloseCountries.size();
	        //if the cur country has more close capitals, then make it the leader on the leaderboard
	        if (curSizeOfCloseCountries > largestNumberOfCloseCapitals) {
	        	largestNumberOfCloseCapitals = curSizeOfCloseCountries;
	        	countryWithMostCapitals.clear();
	        	countryWithMostCapitals.put((String) pairs.getKey(), (ArrayList<String>) pairs.getValue());
	        }
	        iter.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    //print out the one country with the most close capitals
	    java.util.Iterator<Entry<String, ArrayList>> iterWinner = countryWithMostCapitals.entrySet().iterator();
	    while (iterWinner.hasNext()) {
	        Map.Entry pairs = (Map.Entry)iterWinner.next();
	        System.out.println(pairs.getKey());
	        System.out.print("The latitude of " + pairs.getKey() + " is " + countryToLats.get(pairs.getKey()));
	        System.out.println(" and the longitude is " + countryToLongs.get(pairs.getKey()));
	        System.out.print("There are " + largestNumberOfCloseCapitals + " countries/capitals within ");
	        System.out.println("10 degrees of latitude and longitude. Here is the list:");
	        //iterate through all of the close countries
	        
	        for (String curCountry : ((ArrayList<String>) pairs.getValue())) {
	        	String curCapital = getCapital(countryToCode.get(curCountry));
	        	System.out.println("Country: " + curCountry + " | capital: " + curCapital + 
	        			" latitude: " + countryToLats.get(curCountry) + " | longitude: " + countryToLongs.get(curCountry));
	        }
	        
	        iterWinner.remove(); // avoids a ConcurrentModificationException
	    }		
		
	    Reset.reset(countryCodes, countryCodeToCountry, countryToCode);	
	}

	private static String getCapital(String country) {
		String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
				+ country + ".html";
		
		try {
			Document countryPage = Jsoup.connect(curCountryURL).get();
			String pageHtml = countryPage.html();
		
			//the southeastern hemisphere is defined by S latitudes and E longitudes
			String template = "Capital:</a>\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*(\\s*.*\\s*.*\\s*.*))";
			Pattern p = Pattern.compile(template);
			Matcher m = p.matcher(pageHtml);
			if (m.find()) {
				System.out.println(m.group(1));
			}
		} catch (Exception e) {
			System.out.println("Exception here");
		}
		return curCountryURL;
		
	}

	private static ArrayList<String> getCloseCountries(HashMap<String, Float> countryToLatitude,
			HashMap<String, Float> countryToLongitude, String country, LinkedList<String> countryCodes, HashMap<String, String> countryCodeToCountry) {
		ArrayList<String> closeCountries = new ArrayList<String>();
		//iterate through the countries, find those "close" to the capital
		for (String countryCode : countryCodes) {
			try {
				//check for global wraparound
				boolean updateCountryLat = false;
				boolean updateCountryCodeLat = false;
				boolean updateCountryLong = false;
				boolean updateCountryCodeLong = false;
				float tempLatCountry = countryToLatitude.get(countryCodeToCountry.get(country));
				float tempLatCountryCode = countryToLatitude.get(countryCodeToCountry.get(countryCode));
				float tempLongCountry = countryToLongitude.get(countryCodeToCountry.get(country));
				float tempLongCountryCode = countryToLongitude.get(countryCodeToCountry.get(country));
				if (Math.abs(countryToLatitude.get(countryCodeToCountry.get(country))) > 170 
						|| Math.abs(countryToLatitude.get(countryCodeToCountry.get(countryCode))) > 170) {
					//check for latitude global wraparound
					//case 1: country lat > 170
					if (countryToLatitude.get(countryCodeToCountry.get(country)) < -170) {
						tempLatCountry = (float) (360.0 + countryToLatitude.get(countryCodeToCountry.get(country)));
						updateCountryLat = true;
					}
					//case 2
					else if (countryToLatitude.get(countryCodeToCountry.get(countryCode)) < -170) {
						tempLatCountryCode = (float) (360.0 + countryToLatitude.get(countryCodeToCountry.get(countryCode)));
						updateCountryCodeLat = true;
					}	
					
					//check for longitude global wraparound
					if (countryToLongitude.get(countryCodeToCountry.get(country)) < -170) {
						tempLongCountry = (float) (360.0 + countryToLongitude.get(countryCodeToCountry.get(country)));
						updateCountryLong = true;
					}
					//case 2
					else if (countryToLongitude.get(countryCodeToCountry.get(countryCode)) < -170) {
						tempLongCountryCode = (float) (360.0 + countryToLongitude.get(countryCodeToCountry.get(country)));
						updateCountryCodeLong = true;
					}	
				}
				//normal case without global wraparound
				else {
					
					
					float latDifference = Math.abs(tempLatCountry - tempLatCountryCode);			
					float longDifference = Math.abs(tempLongCountry - tempLongCountryCode);
					if (latDifference < 10.0 && longDifference < 10.0) {
						//check that the current country isn't the same as the country of interest
						if (!countryCode.equals(country)) {
							closeCountries.add(countryCodeToCountry.get(countryCode));
						}
				    }
				}
			} catch (NullPointerException e) {}
			
		}
		return closeCountries;
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
			//Moldova has a weird format; cover the edge case
			if (country.equals("md")) {
				countryToLat.put(countryCodeToCountry.get(country), (float) 47.0);
				countryToLong.put(countryCodeToCountry.get(country), (float) (28.0 + (51.0/60.0)));
			}
			//Ukraine has a weird format; cover the edge case
			if (country.equals("md")) {
				countryToLat.put(countryCodeToCountry.get(country), (float) (50.0 + (26.0/60.0)));
				countryToLong.put(countryCodeToCountry.get(country), (float) (30.0 + (31.0/60.0)));
			}
			//Nauru doesn't list the coordinates for its capital...
			if (country.equals("nr")) {
				countryToLat.put(countryCodeToCountry.get(country), (float) (-1*(32.0/60.0)));
				countryToLong.put(countryCodeToCountry.get(country), (float) (166.0 + (55.0/60.0)));
			}
			try {
				Document countryPage = Jsoup.connect(curCountryURL).get();
				String pageHtml = countryPage.html();
			
				//the southeastern hemisphere is defined by S latitudes and E longitudes
				String template = "Capital:</a>\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*"
						+ "\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*>(\\d*\\s*\\d*\\s*\\w*,\\s*\\d*\\s*\\d*\\s*\\w*)\\s*<";
				Pattern p = Pattern.compile(template);
				Matcher m = p.matcher(pageHtml);	
				//Tokelau has no capital
				if (((!country.equals("md") && !country.equals("tl")) && (!country.equals("up") && !country.equals("nr"))) && m.find()) {
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

