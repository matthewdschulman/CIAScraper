import java.util.HashMap;
import java.util.LinkedList;


public class Religion {

	public static void getDominantReligionCountries(LinkedList<String> countryCodes,
			HashMap<String, String> countryCodeToCountry) {
		System.out.println("Please wait one moment...");
		
		//create hashmaps for countries with dominant religions in the two sets
		HashMap<String, Double> greaterThan80 = new HashMap<String, Double>();
		HashMap<String, Double> lessThan50 = new HashMap<String, Double>();
		
		for (String country : countryCodes) {
			double percentage = getPercentage(country);
			if (percentage < 50.0) {
				lessThan50.put(countryCodeToCountry.get(country), percentage);
			}
			else if (percentage > 80.0) {
				greaterThan80.put(countryCodeToCountry.get(country), percentage);
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
		return 90;
	}

	private static void printCountries(HashMap<String, Double> lessThan50) {
		for (String country : lessThan50.keySet()){

            String value = lessThan50.get(country).toString();  
            System.out.println(country + ": " + value + "%");
        } 
	}
}
