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
	public static HashMap<String, String> getCountries(String url, LinkedList<String> countryCodes, String ciaUrl) {
		HashMap<String, String> codeToCountry = new HashMap<String, String>();
		try {
			Document doc = Jsoup.connect(url).get();
			String html = doc.html();
			String[] htmlArr = html.split(" ");
			for (int i = 0; i < htmlArr.length; i++) {
				if (htmlArr[i].contains("../geos/")) {
					String twoLetterCode = htmlArr[i].substring(15, 17);
					countryCodes.add(twoLetterCode);
					//htmlArr[i+1] is the full country name
					codeToCountry.put(twoLetterCode, htmlArr[i+1]);
					System.out.println(twoLetterCode + " " + htmlArr[i+1]);
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
		removeJunk(countryCodes);
		removeJunk(codeToCountry);
		return codeToCountry;
		
		
	}

	private static void removeJunk(HashMap<String, String> codeToCountry) {
		codeToCountry.remove("xx");
		codeToCountry.remove("oo");
		codeToCountry.remove("um");
		codeToCountry.remove("wi");
		codeToCountry.remove("ee");				
	}

	private static void removeJunk(LinkedList<String> countryCodes) {
		countryCodes.remove("xx");
		countryCodes.remove("oo");
		countryCodes.remove("um");
		countryCodes.remove("wi");
		countryCodes.remove("ee");		
	}
}
