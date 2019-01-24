package main;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONException;

public class DataFinder {
	
	public static Scanner reader = new Scanner(System.in);
	public static int yearToScout;
	
	private static APIHandlerFRC tba = new APIHandlerFRC(
			"http://www.thebluealliance.com/api/v3", 
			"?X-TBA-Auth-Key=bwidXGDgsZNrUbUIYG9zrLYfubC14liNqFwshbbVsrBRzAvMprB8MmLfyisKwDBJ");
	
	public static void main(String[] args) throws InterruptedException {
		checkInternetConnection();
		yearToScout = Integer.parseInt(getString("What year would you like to scout?", "2015", "2016", "2017", "2018"));
		//getDataMain();
		tba.getEventSchedule("2018ausp");
	}
	
	public static void getDataMain() {
		String[] statisticsPotential;
		ArrayList<Team> teamsSelected = new ArrayList<Team>();
		ArrayList<String> statisticsSelected = new ArrayList<>();
		
		do {
			teamsSelected.clear();
			statisticsSelected.clear();
			
			//Gets information from the user and scouts selected teams
			if (getString("Do you want to prescout Teams or an Event?", "Teams", "Event").equals("Teams")) {
				for (String teamNum : getMultipleStrings("What are the numbers of the Teams you wish to prescout?")) {
					try {
						teamsSelected.add(tba.getTeam("frc" + teamNum));
					} catch(JSONException ex) {System.out.println("Team number " + teamNum + " is invalid ");}
				}
				
				do {try {
					String eventKeyPrescout = getString("What " + yearToScout + " event should be used to get statistics to be potentially queried?").toLowerCase();
					statisticsPotential = tba.getStatistics(yearToScout, eventKeyPrescout);
					break;
					} catch (JSONException e) {System.out.println("That event key is not valid. Please get the key from TBA");};
				} while(true);
				statisticsSelected = getMultipleStrings("Which statistics do you want to prescout?", statisticsPotential);
				
				tba.scoutStatisticsTeams(teamsSelected, yearToScout, statisticsSelected);
				
			} else {
				String eventKey;
				do {try {
						eventKey = getString("What is the key for the event you wish to prescout?").toLowerCase();
						teamsSelected = tba.getTeamsEvent(eventKey, yearToScout);
						break;
					} catch (JSONException e) {
						System.out.println("That event key is not valid. Please get the key from TBA");
				}} while(true);
				
				do {try {
						String eventKeyPrescout = getString("What " + yearToScout + " event should be used to get statistics to be potentially queried?").toLowerCase();
						statisticsPotential = tba.getStatistics(yearToScout, eventKeyPrescout);
						break;
					} catch (JSONException e) {
						System.out.println("That event key is not valid. Please get the key from TBA");
				}} while(true);
				
				statisticsSelected = getMultipleStrings("What statistics do you want to prescout?", statisticsPotential);
				
				tba.scoutStatisticsTeams(teamsSelected, yearToScout, statisticsSelected);
			}
			
			//Saves data to a selected location
			System.out.println("Attempting to save data - Please navigate to the opened save dialogue");
			try {
				saveStatistics(statisticsSelected, teamsSelected);
				System.out.println("Data was successfully saved!");
			} catch (Exception e) {System.out.println("Data was not successfully saved");}
		} while (getString("Would you like to prescout again?", "Yes", "No").equals("Yes"));
		reader.close();
	}
	
	/**
	 * Gets the next string entered by the user
	 * @param msg The message to be printed to tell the user what to enter
	 * @return The next string entered by the user
	 */
	public static String getString(String msg) {
		System.out.println(msg);
		return reader.nextLine();
	}
	
	/**
	 * Gets the next string entered by the user out of a list of options
	 * @param msg The message to be printed to tell the user what to enter
	 * @param options A list of string options that the user can choose from
	 * @return A string option chosen by the user
	 */
	public static String getString(String msg, String... options) {
		String stringInput;
		if (options.length != 0) {
			ArrayList<String> optionsList = new ArrayList<String>();
			optionsList.addAll(Arrays.asList(options));
			
			do {
				System.out.print(msg + " - " + "Respond with one of the following options: ");
				printOptions(optionsList, 4);
				stringInput = reader.nextLine();
				if (!inArray(stringInput, optionsList)) System.out.println("This input is an invalid response. Please respond to the question with a given option.");
			} while (!inArray(stringInput, optionsList));
			stringInput = stringInArray(stringInput, optionsList);
			System.out.println("Selected " + stringInput);
		} else {stringInput = reader.nextLine();}
		return stringInput;
	}
	
	/**
	 * Gets strings from the user until they enter an empty string
	 * @param msg The message to be printed to tell the user what to enter
	 * @param options A list of string options that the user can choose from
	 * @return An array of string options chosen by the user
	 */
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
				} else if (!inArray(stringInput, optionsList) && !stringInput.equals("") && !inArray(stringInput, selectedOptions)) {
					System.out.println("Please respond to the question with a given option.");
				} else if (!stringInput.equals("")) {
					for (String str : stringsInArray(stringInput, optionsList)) {
						if (!selectedOptions.contains(str)) {
							selectedOptions.add(str);
						}
					}
					System.out.println("Selected Options: " + selectedOptions.toString());
				}
				if (optionsList.size() == 0) {System.out.println("Please enter at least one statistic");}
			} while (!stringInput.equals("") || selectedOptions.size() == 0);
		} else {stringInput = reader.nextLine();}

		return selectedOptions;
	}
	
	/**
	 * Gets strings from the user until they enter an empty string
	 * @param msg The message to be printed to tell the user what to enter
	 * @return An array of strings entered by the user
	 */
	public static ArrayList<String> getMultipleStrings(String msg) {
		System.out.println(msg + "\n" + "Enter nothing when done");
		String stringInput = "";
		ArrayList<String> stringsInput = new ArrayList<>();
		
		do {
			stringInput = reader.nextLine();
			if (!stringInput.equals("") && !inArray(stringInput, stringsInput) && isInteger(stringInput)) {
				stringsInput.add(stringInput);
				System.out.println("Added Strings: " + stringsInput.toString());
			} 
			if (stringsInput.size() == 0 ) {System.out.println("Please enter at least statistic");}
			if (!isInteger(stringInput) && !stringInput.equals("")) {System.out.println("Please enter a number");}
		} while (!stringInput.equals("") || stringsInput.size() == 0);
		
		return stringsInput;
	}
	
	/**
	 * Prints a list of strings separated by commas
	 * @param optionsList The list of strings to be printed
	 * @param optionsPerLine The amount of strings to be printed on each line
	 */
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
	
	/**
	 * Checks whether or not an input string is an Integer
	 * @param str The string to check whether it is an integer
	 * @return A boolean represnting whether or not this string is an integer
	 */
	public static boolean isInteger(String str) {
	    if(str.isEmpty()) return false;
	    for(int i = 0; i < str.length(); i++) {
	        if(i == 0 && str.charAt(i) == '-') {
	            if(str.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(str.charAt(i),10) < 0) return false;
	    }
	    return true;
	}
	
	/**
	 * Gets the location of a file that the user enters via a prompt
	 * @param fileExtensionDescription A description of the file extension that the file will be saved using
	 * @param fileExtension The file extension that the file will be saved using
	 * @return A File object representative of the save location
	 */
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
	
	/**
	 * Checks if a case-insensitive sub-string of the strings in the array is the same as an input string
	 * @param str The string to check if a case-insensitive sub-string of it is in the array
	 * @param arr The array to check for sub-strings
	 * @return A boolean indicating whether or not the string is present in the array
	 */
	public static boolean inArray(String str, ArrayList<String> arr) {
		for (String s : arr) {
			try {
				if (s.substring(0, str.length()).toLowerCase().contains(str.toLowerCase())) return true;
			} catch (StringIndexOutOfBoundsException e) {}
		}
		return false;
	}
	
	/**
	 * Gets a string from a given array, which contains a sub-string equal to the given string
	 * @param str The string to check if it is contained in the array
	 * @param arr The array to check for sub-strings
	 * @return A string from the array representative of the given string
	 */
	public static String stringInArray(String str, ArrayList<String> arr) {
		for (String s : arr) {
			try {
				if (s.substring(0, str.length()).toLowerCase().contains(str.toLowerCase())) return s;
			} catch (StringIndexOutOfBoundsException e) {}
		}
		return "";
	}
	
	/**
	 * Gets strings from a given array, which contain a sub-string equal to the given string
	 * @param str The string to check if it is contained in the array
	 * @param arr The array to check for sub-strings
	 * @return An array of strings from the array representative of the given string
	 */
	public static ArrayList<String> stringsInArray(String str, ArrayList<String> arr) {
		ArrayList<String> contained = new ArrayList<>();
		for (String s : arr) {
			try {
				if (s.substring(0, str.length()).toLowerCase().contains(str.toLowerCase())) contained.add(s);
			} catch (StringIndexOutOfBoundsException e) {}
		}
		return contained;
	}
	
	/**
	 * Saves a list of statistics to a given file
	 * @param statistics A list of string statistics to be saved
	 * @param teams A list of teams with statistics stored in them that should be saved to a csv
	 * @throws Exception Thrown if the selected file does not exist
	 */
	public static void saveStatistics(ArrayList<String> statistics, ArrayList<Team> teams) throws Exception {
		FileWriter writer = new FileWriter(getFileSave("Comma-Separated-Values File", "csv"));
		
		writer.append("Number,Name,");
		for (String s : statistics) writer.append(s + ",");
		writer.append("\n");
		
		for (Team team : teams) {
			writer.append(team.number + ",");
			writer.append(team.name + ",");
			
			for (Statistic s : team.statList.statistics) {
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
	
	/**
	 * Stalls the program running until one is connected to the internet
	 * @throws InterruptedException Thrown if the program sleeping is somehow interrupted
	 */
	public static void checkInternetConnection() throws InterruptedException {
		int timeToWait1 = 2, timeToWait2 = 3, timeToWait3 = 5;
		while(!tba.checkConnection("/status")) {
			System.out.println("Please connect to the internet, retrying in " + timeToWait3 + " seconds");
			TimeUnit.SECONDS.sleep(timeToWait3);
			timeToWait1 = timeToWait2;
			timeToWait2 = timeToWait3;
			timeToWait3 = timeToWait1 + timeToWait2;
			System.out.println();
		}
	}

}
