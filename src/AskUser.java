import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;


public class AskUser {

	public static void askUserForInput(LinkedList<String> countryCodes, HashMap<String, String> countryCodeToCountry) {
		Scanner user_input = new Scanner(System.in);
		
		//determine user's interest
		System.out.println("What would you like to research? Please enter 'earthquakes' for earthquakes or...");
		System.out.println("'ep' for  elevation points or...");
		System.out.println("'hemispheres' for hemispheres");
		String researchTopic = user_input.nextLine();
		
		String hemisphere = "";
		String userRegion = "";
		if (researchTopic.equals("hemispheres")) {
			System.out.println("Which region of the world do you desire to research?");
			System.out.println("Please enter 'SE' for southeastern hemisphere or...");
			System.out.println("Please enter 'SW' for southwestern hemisphere or...");
			System.out.println("Please enter 'NE' for northeastern hemisphere or...");
			System.out.println("Please enter 'NW' for northwestern hemisphere or...");
			hemisphere = user_input.nextLine();
		}
		else {		
			//determine user's selected continent
			System.out.println("Which region would you like to consider?");
			System.out.println("Please enter 'SA' for South America or...");
			System.out.println("'SH' for southern hemisphere or...");
			System.out.println("'E' for Europe");
			userRegion = user_input.nextLine();	
		}
		
		//determine the scope of countries the user wants to consider
		System.out.println("How many countries do you want to consider from " + userRegion + hemisphere + "?");
		System.out.println("Please enter 'all' or...");
		System.out.println("'one' for most extreme country'");
		String numOfCountries = user_input.nextLine();
		
		if (researchTopic.equals("hemispheres")) {
			Hemispheres.findCountries(hemisphere, numOfCountries, countryCodes, countryCodeToCountry);
		}
		
		//depict which extreme the user wants
		String highOrLow = "";
		if (numOfCountries.equals("one")) {
			if (researchTopic.equals("ep")) {
				System.out.println("Do you want the country in " + userRegion + " with the "
						+ "highest or lowest elevation point");
				System.out.println("Please enter either 'highest' or 'lowest'");
				highOrLow = user_input.nextLine();
			}
			else if (researchTopic.equals("earthquakes")) {
				System.out.println("Do you want the country in " + userRegion + " with the "
						+ "most or least earthquakes?");
				System.out.println("Please enter either 'most' or 'least'");
				String mostOrLeast = user_input.nextLine();
				if (mostOrLeast.equals("most")) {
					highOrLow = "highest";
				}
				else {
					highOrLow = "lowest";
				}
			}
		}
		
		//update userRegion to match with the cia's style
		if (userRegion.equals("E")) {
			userRegion = "region_eur";
		}
		if (userRegion.equals("SA")) {
			userRegion = "region_soa";
		}
				
		//STILL HAVE TO MAKE THIS CODE MORE AGILE!
		if (researchTopic.equals("earthquakes")) {
			Earthquakes.findEarthquakes(countryCodes, countryCodeToCountry, userRegion);
		}
		
		if (researchTopic.equals("ep")) {
			ElevationPoint.findElevationPoint(userRegion, numOfCountries, highOrLow, countryCodes, countryCodeToCountry);
		}
			
	}

}
