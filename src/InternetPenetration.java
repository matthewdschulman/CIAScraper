import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class InternetPenetration {

	public static void findInternetPenetration(LinkedList<String> countryCodes,
			HashMap<String, String> countryCodeToCountry, HashMap<String, String> countryToCode, String countryUrlTemplate) {
		System.out.println("Please wait one moment...");
		
		try {
			//create template for leaderboard and loserboard
			String[] topNations = new String[10];
			HashMap<String, Double> trackingNations = new HashMap<String, Double>();
			
			for (String country : countryCodes) {
				
				String curCountryURL = countryUrlTemplate + country + ".html";
				Document countryPage = Jsoup.connect(curCountryURL).get();
				String pageHtml = countryPage.html();			
			
				//get the current country's population
				int population = Population.getPopulation(country, countryUrlTemplate);
				float internetUsers = getInternetUsers(pageHtml);
				double internetPenetration = (double)internetUsers / (double)population;
				//if the population was correctly received
				if (population != -1) {
					//if the leaderboard is not full, enter the country no matter what
					if (topNations[9] == null) {
						insertCountry(country, countryCodeToCountry, topNations, 
								trackingNations, internetPenetration);
					}
					else if (topNations[9] != null) {
						//check if the curNation should make the leaderboard
						if (internetPenetration > trackingNations.get(topNations[9])) {
							insertCountry(country, countryCodeToCountry, topNations, 
									trackingNations, internetPenetration);
						}
					}	
				}
			}
			
			//print out the results
			System.out.println("The countries with the highest internet penetration are...");
			for (int i = 0; i < 10; i++) {
				System.out.println((i+1) + ": " + topNations[i] + ": " + trackingNations.get(topNations[i])*100 + "% of citizens use the internet");
			}
			//reset
			Reset.reset(countryCodes, countryCodeToCountry, countryToCode, countryUrlTemplate);		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}

	private static void insertCountry(String country,
			HashMap<String, String> countryCodeToCountry, String[] topNations,
			HashMap<String, Double> trackingNations, double internetPenetration) {
		for (int i = 0; i < 10; i++) {
			if (topNations[i] == null) {
				topNations[i] = countryCodeToCountry.get(country);
				trackingNations.put(countryCodeToCountry.get(country), internetPenetration);
			}
			else if (trackingNations.get(topNations[i]) < internetPenetration) {
				//move the bottom of the list down one and kick off the bottom, then insert the curCountry
				int j = 9;
				for (j = 9; j > i; j--) {
					topNations[j] = topNations[j-1];
				}
				topNations[i] = countryCodeToCountry.get(country);
				trackingNations.put(countryCodeToCountry.get(country), internetPenetration);
				//we've inserted the country, break the loop
				break;
			}
		}		
		
	}

	private static float getInternetUsers(String pageHtml) {		
		try {
			String template = "Internet\\susers:</a>\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*(\\s*.*\\s*).*";
		
			Pattern p = Pattern.compile(template);
			Matcher m = p.matcher(pageHtml);		
			if (m.find()) {
				String users = m.group(1).trim();
				//make format easier to work with
				users = users.replace(",","");
				String[] arguments = users.split(" ");
				
				//check if internet users are listed as NA
				if (arguments[0].equals("NA")) {
					return 0;
				}
				
				//check for multiplicative factor
				float multFactor = (float) 1.0;
				if (arguments[1].contains("illion")) {
					if (arguments[1].equals("million")) {
						multFactor = (float) 1000000.0;
					}
					else if (arguments[1].equals("billion")) {
						multFactor = (float) 1000000000.0;
					}
					else if (arguments[1].equals("trillion")) {
						multFactor = (float) 1000000000000.0;
					}
				}
				return (float)Float.parseFloat(arguments[0])*multFactor;
			}
		} catch (Exception e) {
			return 0;
		}	
		return 0;
			
	}

}
