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
		System.out.println("Welcome to the CIA Scraper! Please wait 3 seconds...");
		
		//print out an extra line for stylistic purposes
		System.out.println("");
		String ciaUrl = "https://www.cia.gov/library/publications/the-world-factbook/appendix/appendix-d.html";
		
		//create a list of all the country codes listed on the CIA website
		//initialize it to nothing
		LinkedList<String> countryCodes = new LinkedList<String>();
		
		//create a hashmap that links each country code to its country. also update the linked list of country codes
		HashMap<String, String> countryCodeToCountry = GetCountries.getCountries(ciaUrl, countryCodes, ciaUrl);
		
		//ask the user what he wants to do
		AskUser.askUserForInput(countryCodes, countryCodeToCountry);
	}
}