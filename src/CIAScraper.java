import java.awt.List;
import java.io.*;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class CIAScraper {
	public static void main(String[] args) {
		System.out.println("Welcome to the CIA Scraper!");
		
		//create a list of all the country codes listed on the CIA website
		LinkedList<String> countryCodes = GetCountries.getCountryCodes("https://www.cia.gov/library/publications/the-world-factbook/appendix/appendix-d.html");
		
		//create a hashmap that links each country code to its country
		HashMap<String, String> countryCodeToCountry = GetCountries.abbrevsToCountries("https://www.cia.gov/library/publications/the-world-factbook/appendix/appendix-d.html");
		
		//ask the user what he wants to do
		AskUser.askUserForInput(countryCodes);
	}
}