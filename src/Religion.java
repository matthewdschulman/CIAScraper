import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Religion {

	public static void getDominantReligionCountries(LinkedList<String> countryCodes,
			HashMap<String, String> countryCodeToCountry) {
		System.out.println("Please wait one moment...");
				
		//create hashmaps for countries with dominant religions in the two sets
		HashMap<String, Double> greaterThan80 = new HashMap<String, Double>();
		HashMap<String, Double> lessThan50 = new HashMap<String, Double>();
		HashMap<String, String> countryToReligion = new HashMap<String, String>();
		
		for (String country : countryCodes) {
			LinkedHashMap<String, Float> religionToPercent = getPercentage(country);
			//percentage > 0 if a percentage of the dominant religion was found
			Entry<String, Float> mapping = religionToPercent.entrySet().iterator().next();
			String religion = mapping.getKey();
			float percentage = mapping.getValue();
			if (percentage > 0.0) {
				countryToReligion.put(countryCodeToCountry.get(country), religion);
				if (percentage < 50.0) {
					lessThan50.put(countryCodeToCountry.get(country), (double) percentage);
				}
				else if (percentage > 80.0) {
					greaterThan80.put(countryCodeToCountry.get(country), (double) percentage);
				}
			}
		}	
		
		//print the greaterThan80 countries
		System.out.println("The countries with dominant religions that account "
				+ "for more than 80% of the population are...");
		printCountries(greaterThan80, countryToReligion);
		System.out.println("The countries with dominant religions that account "
				+ "for less than 50% of the population are...");
		printCountries(lessThan50, countryToReligion);
		Reset.reset(countryCodes, countryCodeToCountry);	
	}

	private static LinkedHashMap<String, Float> getPercentage(String country) {
		String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
				+ country + ".html";
		LinkedHashMap<String, Float> religionToPercent = new LinkedHashMap<String, Float>();
		try {
			Document countryPage = Jsoup.connect(curCountryURL).get();
			String pageHtml = countryPage.html();
			
			//make the regex pattern matching easier
			pageHtml = pageHtml.replace("(", "");
			pageHtml = pageHtml.replace(")", "");
		
			try {
				String template = "Religions:</a>\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*(\\s*.*)";
				
				Pattern p = Pattern.compile(template);			
				Matcher m = p.matcher(pageHtml);
				
				
				if (!m.find()) {
					religionToPercent.put("no religion found", (float) -1.0);
					return religionToPercent;
			    }
				else {
					String religionList = m.group(1);
					String[] religions = religionList.split("%");
					String firstReligion = religions[0];
					String[] religionArgs = firstReligion.split(" ");
					
					//get the percentage
					float majorityPercentage = Float.parseFloat(religionArgs[religionArgs.length-1]);
					
					//get the name of the religion
					String religion = "";
					for (int i = 1; i < religionArgs.length - 1; i++) {
						//delimit the words in the name of the religion
						if (i > 1) {
							religion += " ";
						}
						religion += religionArgs[i];
					}
					religionToPercent.put(religion, majorityPercentage);
					
					return religionToPercent;
				}
			} catch (Exception e) {
				religionToPercent.put("no religion found", (float) -1.0);
				return religionToPercent;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		religionToPercent.put("no religion found", (float) -1.0);
		return religionToPercent;
	}

	private static void printCountries(HashMap<String, Double> countrySet, HashMap<String, String> countryToReligion) {
		for (String country : countrySet.keySet()){
            String value = countrySet.get(country).toString();  
            System.out.println(country + ": " + value + "% of citizens practice " + countryToReligion.get(country).trim());
        }
	}
}
