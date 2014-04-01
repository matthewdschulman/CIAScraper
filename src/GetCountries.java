import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class GetCountries {
	//for now, no countries are excluded
	public static LinkedList<String> getCountryCodes(String url) {
		LinkedList<String> countryCodes = new LinkedList<String>();
		try {			
			Document doc = Jsoup.connect(url).get();
			String html = doc.html();
			String[] htmlArr = html.split(" ");
			for (int i = 0; i < htmlArr.length; i++) {
				if (htmlArr[i].contains("../geos/")) {
					countryCodes.add(htmlArr[i].substring(15, 17));
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
		return countryCodes;
		
		
	}

	private static void removeJunk(LinkedList<String> countryCodes) {
		countryCodes.remove("xx");
		countryCodes.remove("oo");
		countryCodes.remove("um");
		countryCodes.remove("wi");
		countryCodes.remove("ee");		
	}

	public static HashMap<String, String> abbrevsToCountries(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
