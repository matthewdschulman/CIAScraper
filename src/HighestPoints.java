import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class HighestPoints {

	public static void getHighCountries(LinkedList<String> countryCodes,
			HashMap<String, String> countryCodeToCountry,
			HashMap<String, String> countryToCode, String countryUrlTemplate) {
		System.out.println("Please wait one moment...according to the CIA, the countries in the NW hemisphere " +
				 "with the highest mountains are...");	
		for (String country : countryCodes) {
			String northSouth = Hemispheres.getDirection(country, "vertical-align:top;\">\\d*\\s\\d*\\s(\\w)", countryUrlTemplate);
			if (northSouth.equals("")) {
				northSouth = Hemispheres.getDirection(country, "\\d+\\s\\d+\\s(\\w),", countryUrlTemplate);
			}
						
			String eastOrWest = Hemispheres.getDirection(country, ",\\s\\d*\\s\\d*\\s(\\w)", countryUrlTemplate);
			if (northSouth.equals("N") && eastOrWest.equals("W")) {
				float highestPoint = getCurEP(country, "highest\\spoint:(\\s*.*)", countryUrlTemplate);
				if (highestPoint > 3000.0) {
					System.out.println(countryCodeToCountry.get(country) + ": " + highestPoint + " meters");
				}
			}
		}	
		System.out.println("You should go mountain climbing in these countries!");
		Reset.reset(countryCodes, countryCodeToCountry, countryToCode, countryUrlTemplate);		
	}

	private static float getCurEP(String country, String template, String countryUrlTemplate) {
		String curCountryURL = countryUrlTemplate + country + ".html";
		try {
			Document countryPage = Jsoup.connect(curCountryURL).get();
			String pageHtml = countryPage.html();
			Pattern p  = Pattern.compile(template);
			Matcher m = p.matcher(pageHtml);
			if (m.find()) {
				String elevation = (m.group(1));
				String[] elevationParts = elevation.split(" ");
				elevation = elevationParts[21].replace(",", "").trim();
				return Float.parseFloat(elevation);
			}
		} catch (Exception e) {}
		return 0;
	}
}
