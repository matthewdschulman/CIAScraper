import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class FindAnswers {

	public static void earthquakes(LinkedList<String> countryCodes) {
		System.out.println("According to the CIA, the following countries in South America frequently have earthquakes:");
		for (String country : countryCodes) {
			String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
					+ country + ".html";
			try {
				Document countryPage = Jsoup.connect(curCountryURL).get();
				String pageHtml = countryPage.html();
				if (pageHtml.contains("earthquake") && pageHtml.contains("region_soa")) {
					System.out.println(getCountry(country));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Reset.reset(countryCodes);		
	}
	
	public static void lowestElevationPoint(LinkedList<String> countryCodes) {
		System.out.println("According to the CIA, the country in Europe with the lowest elevation is...");
		String lowestCountry = "";
		//Mount Everest is less than 9000 meters
		int lowestElevationPoint = 9000;
		
		for (String country : countryCodes) {
			String curCountryURL = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_"
					+ country + ".html";
			try {
				Document countryPage = Jsoup.connect(curCountryURL).get();
				String pageHtml = countryPage.html();
				if (pageHtml.contains("region_eur")) {
					int curEP = getCurEP(pageHtml);
					if (curEP < lowestElevationPoint) {
						lowestCountry = getCountry(country);
						lowestElevationPoint = curEP;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(lowestCountry);
		Reset.reset(countryCodes);	
	}

	private static int getCurEP(String pageHtml) {
		// TODO Auto-generated method stub
		return 0;
	}

	//input: country code, output: full country name
	private static String getCountry(String country) {
		// TODO Auto-generated method stub
		return null;
	}


}
