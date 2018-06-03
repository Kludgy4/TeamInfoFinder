package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DataFinder {
	
	public static Scanner reader = new Scanner(System.in);
	
	private static APIHandlerFRC tba = new APIHandlerFRC(
			"http://www.thebluealliance.com/api/v3", 
			"?X-TBA-Auth-Key=bwidXGDgsZNrUbUIYG9zrLYfubC14liNqFwshbbVsrBRzAvMprB8MmLfyisKwDBJ");
	
	public static void main(String[] args) {

		int yearToScout = Integer.parseInt(getString("What year would you like to scout?", "2015", "2016", "2017", "2018"));
		String[] statisticsPotential;
		ArrayList<Team> teamsSelected = new ArrayList<Team>();
		ArrayList<String> statisticsSelected = new ArrayList<>();
		
		do {
			teamsSelected.clear();
			statisticsSelected.clear();
			
			//Gets information from the user and scouts selected teams
			if (getString("Do you want to prescout Teams or an Event?", "Teams", "Event").equals("Teams")) {
				for (String teamNum : getMultipleStrings("What are the numbers of the Teams you wish to prescout?")) {
					try {teamsSelected.add(tba.getTeam("frc" + teamNum));}
					catch(Exception e) {System.out.println("Team number " + teamNum + " is invalid ");}
				}
				
				statisticsPotential = tba.getStatistics(yearToScout, getString("What " + yearToScout + " event should be used to get statistics to be potentially queried?"));
				statisticsSelected = getMultipleStrings("Which statistics do you want to prescout?", statisticsPotential);
				
				tba.scoutStatisticsTeams(teamsSelected, yearToScout, statisticsSelected);
			} else {
				//TODO Get event object instead
				String eventKey = getString("What is the key for the event you wish to prescout?");
				teamsSelected = tba.getTeamsEvent(eventKey, yearToScout);
				
				statisticsPotential = tba.getStatistics(yearToScout, eventKey);
				statisticsSelected = getMultipleStrings("What statistics do you want to prescout?", statisticsPotential);
				
				tba.scoutStatisticsTeams(teamsSelected, yearToScout, statisticsSelected);
			}
			
			//Saves data to a selected location
			System.out.println("Attempting to save data...");
			try {
				saveStatistics(statisticsSelected, teamsSelected);
				System.out.println("Data was successfully saved!");
			} catch (Exception e) {
				System.out.println("Data was not successfully saved");
			}
		} while (getString("Would you like to prescout again?", "Yes", "No").equals("Yes"));
		reader.close();
	}
	
	public static String getString(String msg) {
		System.out.println(msg);
		return reader.nextLine();
	}
	
	public static String getString(String msg, String... options) {
		String stringInput;
		if (options.length != 0) {
			ArrayList<String> optionsList = new ArrayList<String>();
			optionsList.addAll(Arrays.asList(options));
			
			do {
				System.out.print(msg + " - " + "Respond with one of the following options: ");
				printOptions(optionsList, 4);
				stringInput = reader.nextLine();
				if (!optionsList.contains(stringInput)) System.out.println("This input is an invalid response. Please respond to the question with a given option.");
			} while (!optionsList.contains(stringInput));
			
		} else {stringInput = reader.nextLine();}
		return stringInput;
	}
	
	public static ArrayList<String> getMultipleStrings(String msg, String... options) {
		String stringInput;
		ArrayList<String> selectedOptions = new ArrayList<>();
		
		if (options.length != 0) {
			ArrayList<String> optionsList = new ArrayList<String>();
			optionsList.addAll(Arrays.asList(options));
			
			System.out.print(msg + "\n" + "Respond with the following options and enter nothing when done: ");
			printOptions(optionsList, 4);
			
			do {
				stringInput = reader.nextLine();
				if (stringInput.equals("all")) {
					selectedOptions.addAll(optionsList);
					System.out.println("Selected Options: " + selectedOptions.toString());
					break;
				} else if (!optionsList.contains(stringInput) && !stringInput.equals("") && !selectedOptions.contains(stringInput)) {
					System.out.println("Please respond to the question with a given option.");
				} else if (!stringInput.equals("")) {
					selectedOptions.add(stringInput);
					System.out.println("Selected Options: " + selectedOptions.toString());
				}
				if (optionsList.size() == 0) {System.out.println("Please enter at least one statistic");}
			} while (!stringInput.equals("") || selectedOptions.size() == 0);
		} else {stringInput = reader.nextLine();}

		return selectedOptions;
	}
	
	public static ArrayList<String> getMultipleStrings(String msg) {
		System.out.println(msg + "\n" + "Enter nothing when done");
		String stringInput = "";
		ArrayList<String> stringsInput = new ArrayList<>();
		
		do {
			stringInput = reader.nextLine();
			if (!stringInput.equals("") && !stringsInput.contains(stringInput) && isInteger(stringInput)) {
				stringsInput.add(stringInput);
				System.out.println("Added Strings: " + stringsInput.toString());
			} 
			if (stringsInput.size() == 0 ) {System.out.println("Please enter at least statistic");}
			if (!isInteger(stringInput) && !stringInput.equals("")) {System.out.println("Please enter a number");}
		} while (!stringInput.equals("") || stringsInput.size() == 0);
		
		return stringsInput;
	}
	
	public static void printOptions(ArrayList<String> optionsList, int optionsPerLine) {
		int iterator = optionsPerLine;
		for (String option : optionsList) {
			if (optionsList.indexOf(option) != optionsList.size()-1 && iterator % optionsPerLine == 0) {
				System.out.println("");
				System.out.print(option + ", ");
			} else if (optionsList.indexOf(option) != optionsList.size()-1) {
				System.out.print(option + ", ");
			} else {
				System.out.println(option);
			}
			iterator++;
		}
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
	
	public static void saveStatistics(ArrayList<String> statistics, ArrayList<Team> teams) throws Exception {
		FileWriter writer = new FileWriter(getFileSave("Comma-Separated-Values File", "csv"));
		
		writer.append("Number,Name,");
		for (String s : statistics) writer.append(s + ",");
		writer.append("\n");
		
		for (Team team : teams) {
			writer.append(team.number + ",");
			writer.append(team.name + ",");
			
			for (Statistic s : team.statList.getStatistics()) {
				String statistic = "";
				try {statistic = ((Double)s.getAverage()).toString();} 
				catch (Exception e) {
					try {statistic = ((Boolean)s.getModeBoolean()).toString();} 
					catch (Exception ex) {
						ArrayList<String> modeStrings = s.getModeString();
						if (modeStrings.size() > 1) for (String st : s.getModeString()) statistic = statistic + "/" + st;
						else statistic = modeStrings.get(0);
					}
				}
				writer.append(statistic + ',');
			}
			writer.append("\n");
		}
		writer.flush();
		writer.close();
	}

}
