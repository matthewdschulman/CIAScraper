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
					//find the full country name
					String fullName = getFullName(htmlArr, i+1);
					codeToCountry.put(twoLetterCode, fullName);
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

	private static void removeJunk(HashMap<String, String> codeToCountry) {
		codeToCountry.remove("xx");
		codeToCountry.remove("oo");
		codeToCountry.remove("um");
		codeToCountry.remove("wi");
		codeToCountry.remove("ee");			
		codeToCountry.remove("xq");	
		codeToCountry.remove("zh");	
		codeToCountry.remove("io");	
		codeToCountry.remove("xo");	
		codeToCountry.remove("ip");	
		codeToCountry.remove("ck");
		codeToCountry.remove("dx");
		codeToCountry.remove("hm");
		codeToCountry.remove("pc");
		codeToCountry.remove("um");
		codeToCountry.remove("od");
		codeToCountry.remove("mq");
	}

	private static void removeJunk(LinkedList<String> countryCodes) {
		countryCodes.remove("xx");
		countryCodes.remove("oo");
		countryCodes.remove("um");
		countryCodes.remove("wi");
		countryCodes.remove("ee");
		countryCodes.remove("xq");
		countryCodes.remove("zh");
		countryCodes.remove("io");
		countryCodes.remove("xo");
		countryCodes.remove("ip");
		countryCodes.remove("ck");
		countryCodes.remove("dx");
		countryCodes.remove("hm");
		countryCodes.remove("pc");
		countryCodes.remove("um");
		countryCodes.remove("od");
		countryCodes.remove("mq");
	}
}
