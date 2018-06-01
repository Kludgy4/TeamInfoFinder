package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

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
		String statPullEvent;
		String[] potentialStats;
		
		ArrayList<Team> teams = new ArrayList<Team>();
		ArrayList<String> statistics = new ArrayList<>();
		
		do {
			teams.clear();
			statistics.clear();
			if (getString("Do you want to prescout Teams or an Event?", "Teams", "Event").equals("Teams")) {
				statPullEvent = getString("What " + yearToScout + " event should be used for example statistics?");
				
				potentialStats = JSONObject.getNames((new JSONArray(tba.getInfo("/event/"+ yearToScout + statPullEvent + "/matches"))).
						getJSONObject(0).getJSONObject("score_breakdown").getJSONObject("red"));
				
				teams.add(tba.getTeam("frc" + getString("What is the number of the Team you wish to prescout?")));
				
				statistics = getMultipleStrings("What statistics do you want to prescout?", potentialStats);
				
				tba.scoutTeamStatistics(teams.get(0), yearToScout, statistics);
				
			} else {
				String eventKey = getString("What is the key for the event you wish to prescout?");
				teams = tba.getEventTeams(eventKey, yearToScout);
				potentialStats = JSONObject.getNames((new JSONArray(tba.getInfo("/event/"+ yearToScout + eventKey + "/matches"))).
						getJSONObject(0).getJSONObject("score_breakdown").getJSONObject("red"));
				
				statistics = getMultipleStrings("What statistics do you want to prescout?", potentialStats);
				
				for (Team t : teams) {
					tba.scoutTeamStatistics(t, yearToScout, statistics);
				}
			}
			System.out.println("Saving Information...");
			/*JFileChooser chooser = new JFileChooser();
			JTextField filename = new JTextField(), dir = new JTextField();
			 //chooser.set
			
			if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
		        filename.setText(chooser.getSelectedFile().getName());
		        dir.setText(chooser.getCurrentDirectory().toString());
		       
		    }
			*/
			try {
				//FileWriter writer = new FileWriter(dir.getText() + filename.getText());
				FileWriter writer = new FileWriter("C:\\Users\\Matthew\\Desktop\\Test.csv");
				
				writer.append(" ");
				writer.append(",");
				writer.append(" ");
				writer.append(",");
				for (String s : statistics) {
					writer.append(s);
					writer.append(',');
				}
				writer.append("\n");
				
				for (Team t : teams) {
					writer.append(((Integer)t.number).toString());
					writer.append(',');
					writer.append(t.name);
					writer.append(',');
					for (Statistic s : t.statList.getStatistics()) {
						writer.append(((Double)s.getAverage()).toString());
						writer.append(',');
					}
					writer.append("\n");
				}
				
				writer.flush();
				writer.close();
			} catch (IOException e) {e.printStackTrace();}
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
