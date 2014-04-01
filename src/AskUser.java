import java.util.LinkedList;
import java.util.Scanner;


public class AskUser {

	public static void askUserForInput(LinkedList<String> countryCodes) {
		Scanner user_input = new Scanner(System.in);
		
		//determine user's interest
		System.out.println("What would you like to research? Please enter 'earthquakes' for earthquakes");
		String researchTopic = user_input.nextLine();
		
		//determine user's selected continent
		System.out.println("Which region would you like to consider? Please enter SA for South America");
		String userRegion = user_input.nextLine();
		
		//determine the scope of countries the user wants to consider
		System.out.println("How many countries do you want to consider from " + userRegion + "? Please enter 'all' or 'the most extreme country'");
		String numOfCountries = user_input.nextLine();
		
		if (researchTopic.equals("earthquakes")) {
			FindAnswers.earthquakes(countryCodes);
		}
		
		
	}

}
