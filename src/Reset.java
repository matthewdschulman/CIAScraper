import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;


public class Reset {
	public static void reset(LinkedList<String> countryCodes, HashMap<String, String> countryCodeToCountry, HashMap<String, String> countryToCode) {
		//determine if user desires to do a new search
		System.out.println("Would you like to do another query? Please enter 'yes' or 'no'");
		Scanner user_input = new Scanner(System.in);
		String newSearch = user_input.nextLine();
		if (newSearch.equals("no") || newSearch.equals("No")) {
			System.out.println("Goodbye!");
			System.exit(0);
		}
		
		//else, ask user for new search
		AskUser.askUserForInput(countryCodes, countryCodeToCountry, countryToCode);
		
	}
}
