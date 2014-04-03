import java.awt.List;
import java.io.*;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class CIAScraper {
	public static void main(String[] args) {
		System.out.println("Welcome to the CIA Scraper! Please wait 3 seconds...");
		
		//print out an extra line for stylistic purposes
		System.out.println("");
		String ciaUrl = "https://www.cia.gov/library/publications/the-world-factbook/appendix/appendix-d.html";
		String countryUrlTemplate = "https://www.cia.gov/library/publications/the-world-factbook/geos/countrytemplate_";
		
		//create a list of all the country codes listed on the CIA website
		//initialize it to nothing
		LinkedList<String> countryCodes = new LinkedList<String>();
		
		//create hashmaps that link each country code to its country in both directions. also update the linked list of country codes
		ArrayList<HashMap<String, String>> countryDictionaries = GetCountries.getCountries(ciaUrl, countryCodes, ciaUrl);
		HashMap<String, String> countryCodeToCountry = countryDictionaries.get(0);
		HashMap<String, String> countryToCode = countryDictionaries.get(1);
		
		//ask the user what he wants to do
		AskUser.askUserForInput(countryCodes, countryCodeToCountry, countryToCode, countryUrlTemplate);
	}
}