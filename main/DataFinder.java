package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class DataFinder {
	
	public static Scanner reader = new Scanner(System.in);
	
	public static void main(String[] args) {
		APIHandlerFRC tba = new APIHandlerFRC(
			"http://www.thebluealliance.com/api/v3", 
			"?X-TBA-Auth-Key=bwidXGDgsZNrUbUIYG9zrLYfubC14liNqFwshbbVsrBRzAvMprB8MmLfyisKwDBJ");
		
		int yearToScout = Integer.parseInt(getString("What year would you like to scout?", "2015", "2016", "2017", "2018"));
		
		//TODO Make eventkey verification method
		String exampleEvent = getString("What " + yearToScout + " event should be used for example statistics?");
		
		String[] potentialStats = JSONObject.getNames((new JSONArray(tba.getInfo("/event/"+ yearToScout + exampleEvent + "/matches"))).
				getJSONObject(0).getJSONObject("score_breakdown").getJSONObject("red"));
		
		do {
			if (getString("Do you want to prescout a Team or an Event?", "Team", "Event").equals("Team")) {
				getString("What is the number of the Team you wish to prescout?");
				getMultipleStrings("What statistics do you want to prescout?", potentialStats);
				//Go through every qualification match they played this season and average their score for selected stats
				//Prints data pulling status while doing above
				//Ask for where to save the data as a CSV
				//Save the data as a CSV in the given location
			} else {
				String eventKey = getString("What is the key for the event you wish to prescout?");
				ArrayList<Team> eventTeams = tba.getEventTeams(eventKey, yearToScout);
				ArrayList<String> statistics = getMultipleStrings("What statistics do you want to prescout?", potentialStats);
				
				for (Team t : eventTeams) {
					tba.scoutTeamStatistics(t, yearToScout, statistics);
				}
				
				//Go through every qualification match every team in this comp played this season and average their 
				//score for selected stats
				//Prints data pulling status while doing above
				//Ask for where to save the data as a CSV
				//Save the data as a CSV in the given location
			}
		} while (getString("Would you like to prescout more?", "Yes", "No").equals("Yes"));
		reader.close();
	}
	
	public static String getString(String msg, String... options) {
		String str;
		if (options.length != 0) {
			ArrayList<String> optionsList = new ArrayList<String>();
			optionsList.addAll(Arrays.asList(options));
			do {
				System.out.print(msg + " - " + "Respond with one of the following options: ");
				int iterator = 4;
				for (String o : optionsList) {
					if (optionsList.indexOf(o) != optionsList.size()-1 && iterator % 4 == 0) {
						System.out.println("");
						System.out.print(o + ", ");
					} else if (optionsList.indexOf(o) != optionsList.size()-1) {
						System.out.print(o + ", ");
					} else {
						System.out.println(o);
					}
					iterator++;
				}
				str = reader.nextLine();
				if (!optionsList.contains(str)) System.out.println("This input is an invalid response. Please respond to the question with a given option.");
			} while (!optionsList.contains(str));
		} else {str = reader.nextLine();}

		return str;
	}
	
	public static String getString(String msg) {
		System.out.println(msg);
		return reader.nextLine();
	}
	
	public static ArrayList<String> getMultipleStrings(String msg, String... options) {
		String str;
		ArrayList<String> selectedOptions = new ArrayList<>();
		
		if (options.length != 0) {
			ArrayList<String> optionsList = new ArrayList<String>();
			optionsList.addAll(Arrays.asList(options));
			
			System.out.print(msg + " - " + "Respond with one of the following options: ");
			int iterator = 4;
			for (String o : optionsList) {
				if (optionsList.indexOf(o) != optionsList.size()-1 && iterator % 4 == 0) {
					System.out.println("");
					System.out.print(o + ", ");
				} else if (optionsList.indexOf(o) != optionsList.size()-1) {
					System.out.print(o + ", ");
				} else {
					System.out.println(o);
				}
				iterator++;
			}
			
			do {
				str = reader.nextLine();
				if (!optionsList.contains(str) && !str.equals("") && !selectedOptions.contains(str)) {
					System.out.println("This input is an invalid response. Please respond to the question with a given option.");
				} else if (!str.equals("")) {
					selectedOptions.add(str);
					System.out.println("Added Statistics: " + selectedOptions.toString());
				}
			} while (!str.equals(""));
		} else {str = reader.nextLine();}

		return selectedOptions;
	}

}
