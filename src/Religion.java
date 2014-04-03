import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
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
		
		for (String country : countryCodes) {
			double percentage = getPercentage(country);
			//percentage > 0 if a percentage of the dominant religion was found
			if (percentage > 0.0) {
				if (percentage < 50.0) {
					lessThan50.put(countryCodeToCountry.get(country), percentage);
				}
				else if (percentage > 80.0) {
					greaterThan80.put(countryCodeToCountry.get(country), percentage);
				}
			}
		}	
		
		//print the greaterThan80 countries
		System.out.println("The countries with dominant religions that account "
				+ "for more than 80% of the population are...");
		printCountries(greaterThan80);
		System.out.println("The countries with dominant religions that account "
				+ "for less than 50% of the population are...");
		printCountries(lessThan50);
		Reset.reset(countryCodes, countryCodeToCountry);	
	}

	private static double getPercentage(String country) {
		String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
				+ country + ".html";
		System.out.println(country);
		try {
			Document countryPage = Jsoup.connect(curCountryURL).get();
			String pageHtml = countryPage.html();
			
			//make the regex pattern matching easier
			pageHtml = pageHtml.replace("(", "");
			pageHtml = pageHtml.replace(")", "");
		
			try {
				String template = "Religions:</a>\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*data\">\\s*((\\w*\\s*)*\\d*.?\\d*)%";
				
				Pattern p = Pattern.compile(template);			
				Matcher m = p.matcher(pageHtml);
				
				
				if (!m.find()) {
					System.out.println("no religion found");
					return -1.0;
			    }
				else {
					System.out.println(m.group(1));
					return 0; // Double.parseDouble(m.group(1));
				}
			} catch (Exception e) {
				System.out.println("no religion found");
				return -1.0;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Should not be here");
		return -1.0;
	}

	private static void printCountries(HashMap<String, Double> lessThan50) {
		for (String country : lessThan50.keySet()){

            String value = lessThan50.get(country).toString();  
            System.out.println(country + ": " + value + "%");
        }
	}
}
