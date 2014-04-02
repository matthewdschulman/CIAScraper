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
			String northSouth = getDirection(country, "vertical-align:top;\">\\d*\\s\\d*\\s(\\w)");
			if (northSouth.equals("")) {
				northSouth = getDirection(country, "\\d+\\s\\d+\\s(\\w),");
			}
						
			String eastOrWest = getDirection(country, ",\\s\\d*\\s\\d*\\s(\\w)");
			if (northSouth.equals("S") && eastOrWest.equals("E")) {
				System.out.println(countryCodeToCountry.get(country));
			}
		}	
		Reset.reset(countryCodes, countryCodeToCountry);	
	}
	
	private static String getDirection(String country, String regex) {
		String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
				+ country + ".html";
		try {
			Document countryPage = Jsoup.connect(curCountryURL).get();
			String pageHtml = countryPage.html();
		
			//the southeastern hemisphere is defined by S latitudes and E longitudes
			String template = regex;
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
