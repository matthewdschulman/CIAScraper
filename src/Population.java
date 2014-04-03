import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Population {
	public static int getPopulation(String country, String countryUrlTemplate) {
		// TODO Auto-generated method stub
		String curCountryURL = countryUrlTemplate + country + ".html";
		//atypical format for a few countries: account for these edge cases below
		if (country.equals("ax")) {
			return 15700;
		}
		if (country.equals("vt")) {
			return 839;
		}
		try {
			Document countryPage = Jsoup.connect(curCountryURL).get();
			String pageHtml = countryPage.html();
		
			String template = "Population:</a>\\s*.*\\s*.*\\s*.*\\s*.*\\s*.*\\s*(.*\\d*\\d*,\\d*)";
			Pattern p = Pattern.compile(template);
			Matcher m = p.matcher(pageHtml);
			
			if (m.find()) {
				//remove all commas
				return Integer.parseInt(m.group(1).replace("," , ""));
			}			
			return -1;
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Should not be here");
		return -1;
	}
}
