import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class FindAnswers {

	public static void earthquakes(LinkedList<String> countryCodes, HashMap<String, String> countryCodeToCountry) {
		System.out.println("According to the CIA, the following countries in South America frequently have earthquakes:");
		for (String country : countryCodes) {
			String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
					+ country + ".html";
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
		Reset.reset(countryCodes, countryCodeToCountry);		
	}
	
	//note: countries that do not list elevation point on their pages will be ignored for query
	public static void elevationPoint(String userRegion, String numOfCountries, String highOrLow, 
			LinkedList<String> countryCodes, HashMap<String, String> countryCodeToCountry) {
		//in the case that we're not going for the country with the lowest elevation point
		if (highOrLow.equals("highest") || highOrLow.equals("")) {
			System.out.println("I have not yet implemented this loop for the homework");
		}
		
		System.out.println("Please wait one moment...according to the CIA, the country in " + userRegion + " "
				+ "with the lowest elevation is...");
		String lowestCountry = "";		
		//Mount Everest is less than 9000 meters
		float lowestElevationPoint = 9000;		
		for (String country : countryCodes) {
			String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
					+ country + ".html";
			try {
				Document countryPage = Jsoup.connect(curCountryURL).get();
				String pageHtml = countryPage.html();
				if (pageHtml.contains(userRegion)) {
					float curEP = getCurEP(pageHtml, highOrLow);
					if (curEP < lowestElevationPoint) {
						lowestCountry = countryCodeToCountry.get(country);
						lowestElevationPoint = curEP;
					}
					//handle edge case in which two countries have the same EP
					else if (curEP <= lowestElevationPoint) {
						lowestCountry += (" and " + countryCodeToCountry.get(country));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(lowestCountry + " both have an elevation low point of " + 
				lowestElevationPoint + " meters.");
		Reset.reset(countryCodes, countryCodeToCountry);	
	}

	private static float getCurEP(String pageHtml, String highOrLow) {
		String template = "(-|\\s)(\\s|\\d*)(.|\\s)(\\d*)(\\s)(m<)";
		Pattern p  = Pattern.compile(template);
		Matcher m = p.matcher(pageHtml);
		if (m.find()) {
			String elevation = (m.group(1)+m.group(2)+m.group(3)+m.group(4)).trim();
			return Float.parseFloat(elevation);
		}
		else {
			System.out.println("getCurEP failed");
			//ensure that this country is not considered
			if (highOrLow.equals("highest")) {
				return -1;
			}
			else {
				return 9000;
			}
		}
	}
}
