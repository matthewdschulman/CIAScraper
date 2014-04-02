import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Hemispheres {
	//note: countries with capitals are classified by the coordinates of their capital city.
	public static void findCountries(String hemisphere, String numOfCountries, 
			LinkedList<String> countryCodes, HashMap<String, String> countryCodeToCountry) {
		if (!hemisphere.equals("SE")) {
			System.out.println("Haven't implemented for this homework assignment");			
		}
		System.out.println("Please wait a moment...the countries in the " + hemisphere + " hemisphere are...");
		for (String country : countryCodes) {
			String northSouth = northOrSouth(country);
			if (northSouth.equals("")) {
				northSouth = northOrSouthNoCapital(country);
			}
						
			String eastOrWest = eastOrWest(country);
			if (northSouth.equals("S") && eastOrWest.equals("E")) {
				System.out.println(countryCodeToCountry.get(country));
			}
		}			
	}
	
	private static String northOrSouth(String country) {
		String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
				+ country + ".html";
		try {
			Document countryPage = Jsoup.connect(curCountryURL).get();
			String pageHtml = countryPage.html();
		
			//the southeastern hemisphere is defined by S latitudes and E longitudes
			String template = "vertical-align:top;\">\\d*\\s\\d*\\s(\\w)";
			Pattern p = Pattern.compile(template);
			
			Matcher m = p.matcher(pageHtml);
			
			if (m.find()) {
				return m.group(1);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private static String northOrSouthNoCapital(String country) {
		String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
				+ country + ".html";	
		try {
			Document countryPage = Jsoup.connect(curCountryURL).get();
			String pageHtml = countryPage.html();
		
			//the southeastern hemisphere is defined by S latitudes and E longitudes
			String template = "\\d+\\s\\d+\\s(\\w),";
			Pattern p = Pattern.compile(template);
			
			Matcher m = p.matcher(pageHtml);
			
			if (m.find()) {				
				return m.group(1);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private static String eastOrWest(String country) {
		String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
				+ country + ".html";		
		try {
			Document countryPage = Jsoup.connect(curCountryURL).get();
			String pageHtml = countryPage.html();
		
			//the southeastern hemisphere is defined by S latitudes and E longitudes
			String template = ",\\s\\d*\\s\\d*\\s(\\w)";
			Pattern p = Pattern.compile(template);
			
			Matcher m = p.matcher(pageHtml);
			
			if (m.find()) {
				return m.group(1);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}
