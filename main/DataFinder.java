package main;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONArray;
import org.json.JSONObject;

public class DataFinder {
	
	public static Scanner reader = new Scanner(System.in);
	
	private static APIHandlerFRC tba = new APIHandlerFRC(
			"http://www.thebluealliance.com/api/v3", 
			"?X-TBA-Auth-Key=bwidXGDgsZNrUbUIYG9zrLYfubC14liNqFwshbbVsrBRzAvMprB8MmLfyisKwDBJ");
	
	public static void main(String[] args) {

		int yearToScout = Integer.parseInt(getString("What year would you like to scout?", "2015", "2016", "2017", "2018"));
		String[] potentialStatistics;
		ArrayList<Team> selectedTeams = new ArrayList<Team>();
		ArrayList<String> selectedStatistics = new ArrayList<>();
		
		do {
			selectedTeams.clear();
			selectedStatistics.clear();
			
			//Gets information from the user and scouts selected teams
			if (getString("Do you want to prescout Teams or an Event?", "Teams", "Event").equals("Teams")) {
				for (String str : getMultipleStrings("What are the numbers of the Teams you wish to prescout?")) {
					try {selectedTeams.add(tba.getTeam("frc" + str));}
					catch(Exception e) {System.out.println("Team number " + str + " is invalid ");}
				}
				
				potentialStatistics = JSONObject.getNames((new JSONArray(tba.getInfo("/event/"+ yearToScout + 
						getString("What " + yearToScout + " event should be used to get statistics to be potentially queried?") +
						"/matches"))).getJSONObject(0).getJSONObject("score_breakdown").getJSONObject("red"));
				selectedStatistics = getMultipleStrings("Which statistics do you want to prescout?", potentialStatistics);
				
				for (Team team : selectedTeams) {
					tba.scoutTeamStatistics(team, yearToScout, selectedStatistics);
				}
				
			} else {
				String eventKey = getString("What is the key for the event you wish to prescout?");
				selectedTeams = tba.getEventTeams(eventKey, yearToScout);
				
				potentialStatistics = JSONObject.getNames((new JSONArray(tba.getInfo("/event/"+ yearToScout + eventKey + "/matches"))).
						getJSONObject(0).getJSONObject("score_breakdown").getJSONObject("red"));
				selectedStatistics = getMultipleStrings("What statistics do you want to prescout?", potentialStatistics);
				
				for (Team t : selectedTeams) {
					tba.scoutTeamStatistics(t, yearToScout, selectedStatistics);
				}
			}
			
			//Saves data to a selected location
			System.out.println("Attempting to save data...");
			
			try {
				FileWriter writer = new FileWriter(getFileSave("Comma-Separated-Values File", "csv"));
				
				writer.append("Number,Name,");
				for (String s : selectedStatistics) {
					writer.append(s + ",");
				}
				writer.append("\n");
				
				for (Team t : selectedTeams) {
					writer.append(((Integer)t.number).toString() + ',' + t.name + ',');
					
					for (Statistic s : t.statList.getStatistics()) {
						String statistic = "";
						try {
							statistic = ((Double)s.getAverage()).toString();
						} catch (Exception e) {
							try {
								statistic = ((Boolean)s.getModeBoolean()).toString();
							} catch (Exception ex) {
								ArrayList<String> modeStrings = s.getModeString();
								if (modeStrings.size() > 1) {
									for (String st : s.getModeString()) {
										statistic = statistic + "/" + st;
									}
								} else {
									statistic = modeStrings.get(0);
								}
								
								
							}
						}
						writer.append(statistic + ',');
					}
					writer.append("\n");
				}
				writer.close();
				System.out.println("Data was successfully saved!");
			} catch (Exception e) {
				System.out.println("Data was not successfully saved");
			}
		} while (getString("Would you like to prescout again?", "Yes", "No").equals("Yes"));
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
			
			System.out.print(msg + "\n" + "Respond with the following options and enter nothing when done: ");
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
					System.out.println("Please respond to the question with a given option.");
				} else if (!str.equals("")) {
					selectedOptions.add(str);
					System.out.println("Selected Options: " + selectedOptions.toString());
				}
				if (optionsList.size() == 0) {System.out.println("Please enter at least one statistic");}
			} while (!str.equals("") || selectedOptions.size() == 0);
		} else {str = reader.nextLine();}

		return selectedOptions;
	}
	
	public static ArrayList<String> getMultipleStrings(String msg) {
		System.out.println(msg + "\n" + "Enter nothing when done");
		String str = "";
		ArrayList<String> inputStrings = new ArrayList<>();
		
		do {
			str = reader.nextLine();
			if (!str.equals("") && !inputStrings.contains(str) && isInteger(str)) {
				inputStrings.add(str);
				System.out.println("Added Statistics: " + inputStrings.toString());
			} 
			if (inputStrings.size() == 0 ) {System.out.println("Please enter at least statistic");}
			if (!isInteger(str) && !str.equals("")) {System.out.println("Please enter a number");}
		} while (!str.equals("") || inputStrings.size() == 0);
		
		return inputStrings;
	}
	
	public static boolean isInteger(String s) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),10) < 0) return false;
	    }
	    return true;
	}
	
	public static File getFileSave(String fileExtensionDescription, String fileExtension) {
		boolean shouldTryAgain = false;
		JFileChooser chooser;
		String saveDirectory = "";
		do {
			shouldTryAgain = false;
		    chooser = new JFileChooser();
		    chooser.transferFocus();
		    FileFilter filter = new FileNameExtensionFilter(fileExtensionDescription, fileExtension);
		    chooser.setDialogTitle("Save CSV");
		    chooser.setAcceptAllFileFilterUsed(false);
		    chooser.setFileFilter(filter);
		    
		    if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) { 
		       saveDirectory = chooser.getSelectedFile().toString() + ".csv";
		    } else {
		    	if (getString("Would you like to select a save location?", "Yes", "No").equals("No")) shouldTryAgain = false;
		    	else {shouldTryAgain = true;}
		    }
		} while (shouldTryAgain);
		
		return new File(saveDirectory);
	}

}
