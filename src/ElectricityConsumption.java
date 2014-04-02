import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class ElectricityConsumption {

	public static void getTopECCountries(LinkedList<String> countryCodes,
			HashMap<String, String> countryCodeToCountry) {	
		System.out.println("Please wait for a moment...");
		
		String[] topNations = new String[5];
		
		//create hashmap for highest nations
		HashMap<String, Double> trackingTopNations = new HashMap<String, Double>();
				
		for (String country : countryCodes) {
			float elecConsump = getElecConsump(country);
			double elecConsumpPerPop = 0.0;
			
			//if data was extracted for elecConsump
			if (elecConsump > 0) {
				int population = getPopulation(country);
				elecConsumpPerPop = elecConsump / ((float) population);				
				
				//if the leaderboard is not full, enter the country no matter what
				if (topNations[4] == null) {
					insertCountry(country, countryCodeToCountry, topNations, 
							trackingTopNations, elecConsumpPerPop);
				}
				else if (topNations[4] != null) {
					//check if the curNation should make the leaderboard
					if (elecConsumpPerPop > trackingTopNations.get(topNations[4])) {
						insertCountry(country, countryCodeToCountry, topNations, 
								trackingTopNations, elecConsumpPerPop);
					}
				}		
			}
		}
		//print out the results
		for (int i = 0; i < 5; i++) {
			System.out.println((i+1) + ": " + topNations[i] + " with " + trackingTopNations.get(topNations[i]) + " kWh per person");
		}
		
		//reset
		Reset.reset(countryCodes, countryCodeToCountry);
	}

	private static void insertCountry(String country, HashMap<String, String> countryCodeToCountry, 
			String[] topNations, HashMap<String, Double> trackingTopNations, double elecConsumpPerPop) {
		for (int i = 0; i < 5; i++) {
			if (topNations[i] == null) {
				topNations[i] = countryCodeToCountry.get(country);
				trackingTopNations.put(countryCodeToCountry.get(country), elecConsumpPerPop);
			}
			else if (trackingTopNations.get(topNations[i]) < elecConsumpPerPop) {
				//move the bottom of the list down one and kick off the bottom, then insert the curCountry
				int j = 4;
				for (j = 4; j > i; j--) {
					topNations[j] = topNations[j-1];
				}
				topNations[i] = countryCodeToCountry.get(country);
				trackingTopNations.put(countryCodeToCountry.get(country), elecConsumpPerPop);
				//we've inserted the country, break the loop
				break;
			}
		}		
	}

	private static float getElecConsump(String country) {
		String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
				+ country + ".html";
		try {
			Document countryPage = Jsoup.connect(curCountryURL).get();
			String pageHtml = countryPage.html();
		
			//get the section of the html that contains political party info and include enough
			//lines for 11 political parties (if the country has 11+)
			String template = "Electricity - consumption:</a>\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s\\s*(\\w*.?\\w*)\\s*(\\w*)";
			Pattern p = Pattern.compile(template);
			Matcher m = p.matcher(pageHtml);
			
			float multFactor = (float) 0.0;
			String multFactorTemp = "";
			if (m.find()) {
				String elecNumber = m.group(1);
				
				//case when number has no decimal 
				if (elecNumber.contains(" ")){
				   String tempElecNumber = elecNumber;
				   elecNumber = elecNumber.substring(0, elecNumber.indexOf(" "));
				   multFactorTemp = tempElecNumber.substring(tempElecNumber.lastIndexOf(" ") + 1);
				}	
				if (m.group(2).equals("thousand") || multFactorTemp.equals("thousand")) {
					multFactor = (float) 1000.0;
				}
				if (m.group(2).equals("million") || multFactorTemp.equals("million")) {
					multFactor = (float) 1000000.0;
				}
				else if (m.group(2).equals("billion") || multFactorTemp.equals("billion")) {
					multFactor = (float) 1000000000.0;
				}
				else if (m.group(2).equals("trillion") || multFactorTemp.equals("trillion")) {
					multFactor = (float) 1000000000000.0;
				}
				
				//case when there is a comma in the number
				if (elecNumber.contains(",")) {
					elecNumber = elecNumber.replace("," , "");
				}
				return Float.parseFloat(elecNumber)*multFactor;
			}			
			return -1;
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Should not be here");
		return (float) 0.00;
	}

	private static int getPopulation(String country) {
		// TODO Auto-generated method stub
		String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
				+ country + ".html";
		//atypical format for country ax: account for this edge case below
		if (country.equals("ax")) {
			return 15700;
		}
		try {
			Document countryPage = Jsoup.connect(curCountryURL).get();
			String pageHtml = countryPage.html();
		
			String template = "Population:</a>\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*(.*\\d*\\d*,\\d*)";
			Pattern p = Pattern.compile(template);
			Matcher m = p.matcher(pageHtml);
			
			if (m.find()) {
				//remove all commas
				return Integer.parseInt(m.group(1).replace("," , ""));
			}			
			return -1;
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Should not be here");
		return -1;
	}

}
//Population:</a>\s*.*\s*.*\s*.*\s*.*\s*.*\s*<div\sclass="category_data">\d*,\d*\s
//Electricity - consumption:</a>\s*.*\s*.*\s*.*\s*.*\s*.*\s*<div class="category_data">(\d*.?\d*)\s
