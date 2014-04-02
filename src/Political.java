import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Political {

	public static void getPoliticalParties(String userRegion, LinkedList<String> countryCodes,
			HashMap<String, String> countryCodeToCountry) {
		if (!userRegion.equals("Asia")) {
			System.out.println("Haven't implemented for this homework assignment");			
		}
		System.out.println("Please wait a moment...the countries in "+ userRegion + " with"
				+ "more than 10 political parties are...");
		for (String country : countryCodes) {
			boolean moreThan10Parties = moreThan10Parties(country);
			boolean inAsia = checkInAsia(country);
			if (moreThan10Parties && inAsia) {
				System.out.println(countryCodeToCountry.get(country));
			}			
		}
		Reset.reset(countryCodes, countryCodeToCountry);		
	}

	private static boolean checkInAsia(String country) {
		String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
				+ country + ".html";
		try {
			Document countryPage = Jsoup.connect(curCountryURL).get();
			String pageHtml = countryPage.html();
		
			//the southeastern hemisphere is defined by S latitudes and E longitudes
			String template = "\"\\sclass=\"category_data\">(.*)<";
			Pattern p = Pattern.compile(template);
			
			Matcher m = p.matcher(pageHtml);
			
			if (m.find()) {
				return m.group(1).contains("Asia");
			}
			else {
				return false;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Should not be here");
		return false;
	}
	

	private static boolean moreThan10Parties(String country) {
		String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
				+ country + ".html";
		try {
			Document countryPage = Jsoup.connect(curCountryURL).get();
			String pageHtml = countryPage.html();
		
			//get the section of the html that contains political party info and include enough
			//lines for 11 political parties (if the country has 11+)
			String template = "Political parties and leaders:(</a>\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*"
					+ ".*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*"
					+ ".*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*"
					+ ".*\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*)";
			Pattern p = Pattern.compile(template);			
			Matcher m = p.matcher(pageHtml);
			
			if (m.find()) {
				//create a new template to identify a party				
				String partyTemplate = "class=\"category_data\"";
				Pattern pParty = Pattern.compile(partyTemplate);
				Matcher mParty = pParty.matcher(m.group(1));
				
				//count how many rows there are that contain a poltical party
				int count = 0;
				while (mParty.find()) {
					count++;
				}
				if (count > 10) {
					return true;
				}
				return false;
			}
			return false;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Should not be here");
		return false;
	}
}