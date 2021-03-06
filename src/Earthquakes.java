import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Earthquakes {

	public static void findEarthquakes(LinkedList<String> countryCodes, HashMap<String, String> countryCodeToCountry, String userRegion, HashMap<String, String> countryToCode, String countryUrlTemplate) {
		if (!userRegion.equals("region_soa")) {
			System.out.println("Haven't implemented regions other than south america for this homework");
			Reset.reset(countryCodes, countryCodeToCountry, countryToCode, countryUrlTemplate);
		}
		System.out.println("According to the CIA, the following countries in South America frequently have earthquakes:");
		for (String country : countryCodes) {
			String curCountryURL = countryUrlTemplate + country + ".html";
			try {
				Document countryPage = Jsoup.connect(curCountryURL).get();
				String pageHtml = countryPage.html();
				if (pageHtml.contains("earthquake") && pageHtml.contains("region_soa")) {
					System.out.println(countryCodeToCountry.get(country));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Reset.reset(countryCodes, countryCodeToCountry, countryToCode, countryUrlTemplate);			
	}	
}
