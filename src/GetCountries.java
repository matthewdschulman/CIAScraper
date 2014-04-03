import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class GetCountries {
	//exclude non countries including western sahara, EU, etc. 
	//create hash map that links country code to country name
	public static ArrayList<HashMap<String, String>> getCountries(String url, LinkedList<String> countryCodes, String ciaUrl) {
		HashMap<String, String> codeToCountry = new HashMap<String, String>();
		HashMap<String, String> countryToCode = new HashMap<String, String>();
		try {
			Document doc = Jsoup.connect(url).get();
			String html = doc.html();
			String[] htmlArr = html.split(" ");
			for (int i = 0; i < htmlArr.length; i++) {
				if (htmlArr[i].contains("../geos/")) {
					String twoLetterCode = htmlArr[i].substring(15, 17);
					countryCodes.add(twoLetterCode);
					//find the full country name
					String fullName = getFullName(htmlArr, i+1);
					codeToCountry.put(twoLetterCode, fullName);
					countryToCode.put(fullName, twoLetterCode);
				}
				if (htmlArr[i].contains("</select>")) {
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//remove non-countries from the list including EU, Western Sahara, etc.
		ArrayList<String> junk = new ArrayList<String>();
		junk.add("xx");
		junk.add("oo");
		junk.add("um");
		junk.add("wi");
		junk.add("ee");
		junk.add("xq");
		junk.add("zh");
		junk.add("io");
		junk.add("xo");
		junk.add("ip");
		junk.add("ck");
		junk.add("dx");
		junk.add("hm");
		junk.add("od");
		junk.add("mk");	
		junk.add("pc");	
		removeJunk(countryCodes, junk);
		removeJunk(codeToCountry, junk);
		
		//return the two dictionaries that go in both directions from code to country and vica versa
		ArrayList<HashMap<String, String>> dictionaries = new ArrayList<HashMap<String, String>>();
		dictionaries.add(codeToCountry);
		dictionaries.add(countryToCode);
		return dictionaries;		
	}

	private static String getFullName(String[] htmlArr, int i) {
		String fullName = "";
		boolean firstLoop = true;
		while (!htmlArr[i].equals("</option>")) {
			if (!firstLoop) {
				fullName += " ";
			}
			else {
				firstLoop = false;
			}
			fullName += htmlArr[i];
			i++;
		}
		return fullName;
	}

	private static void removeJunk(HashMap<String, String> codeToCountry, ArrayList<String> junk) {
		for (String curJunk : junk) {
			codeToCountry.remove(curJunk);
		}
	}

	private static void removeJunk(LinkedList<String> countryCodes, ArrayList<String> junk) {
		for (String curJunk : junk) {
			countryCodes.remove(curJunk);
		}
	}
}
