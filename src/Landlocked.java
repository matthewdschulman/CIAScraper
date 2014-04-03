import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Landlocked {

	public static void findSingleLandlocked(LinkedList<String> countryCodes,
			HashMap<String, String> countryCodeToCountry) {
		System.out.println("Please wait one moment...");
		System.out.println("The landlocked countries surronded by only country are...");
		
			try {
				for (String country : countryCodes) {
					
					String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
							+ country + ".html";
					Document countryPage = Jsoup.connect(curCountryURL).get();
					String pageHtml = countryPage.html();			
				
						//check if the current country is landlocked
						boolean isLandlocked = checkIfLandlocked(pageHtml);
						if (isLandlocked) {
							//check if the country only has one border country
							boolean onlyOneBorderCountry = checkNeighbors(pageHtml);
							//if is landlocked and only has one border country, print country
							if (onlyOneBorderCountry) {
								System.out.println(countryCodeToCountry.get(country));
							}
						}
				}
				
				Reset.reset(countryCodes, countryCodeToCountry);	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	private static boolean checkNeighbors(String pageHtml) {
		String template = "border\\scountries:\\s*.*>(\\w*\\s.*\\skm)";
		Pattern p = Pattern.compile(template);
		Matcher m = p.matcher(pageHtml);
		
		if (m.find()) {
			String borderCountries = m.group(1);			
			//check if there is more than 1 border country
			int initialWordSize = borderCountries.length();
			borderCountries = borderCountries.replace("km", "");
			int postRemovalWordSize = borderCountries.length();
			if ((initialWordSize - postRemovalWordSize) > 2) {
				return false;
			}
			return true;
		}			
		return false;
	}

	private static boolean checkIfLandlocked(String pageHtml) {
		if (pageHtml.contains("0 km (landlocked)")) {
			return true;
		}
		return false;
	}

}