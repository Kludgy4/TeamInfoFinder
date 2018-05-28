package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DataFinder {
	
	public static Scanner reader = new Scanner(System.in);
	
	public static void main(String[] args) {
		APIHandlerFRC tba = new APIHandlerFRC(
			"http://www.thebluealliance.com/api/v3", 
			"?X-TBA-Auth-Key=bwidXGDgsZNrUbUIYG9zrLYfubC14liNqFwshbbVsrBRzAvMprB8MmLfyisKwDBJ"
		);
		
		do {
			if (getString("Do you want to prescout a Team or an Event?", "Team", "Event").equals("Team")) {
				getString("What is the number of the Team you wish to prescout?");
			} else {
				
			}
		} while (getString("Would you like to prescout more?", "Yes", "No").equals("Yes"));
			
	}
	
	public static String getString(String msg, String... options) {
		
		String str;
		
		if (options.length != 0) {
			ArrayList<String> optionsList = new ArrayList<String>();
			optionsList.addAll(Arrays.asList(options));
			
			boolean notValid;
			do {
				System.out.print(msg + " - " + "Respond with one of the following options: ");
				for (String o : optionsList) {
					if (optionsList.indexOf(o) != optionsList.size()-1) {
						System.out.print(o + ", ");
					} else {
						System.out.println(o);
					}
					
				}
				str = reader.nextLine();
				notValid = !optionsList.contains(str);
				if (notValid) System.out.println("This input is an invalid response. Please respond to the question with a given option.");
			} while (notValid);
		} else {
			str = reader.nextLine();
		}

		return str;
	}

}
