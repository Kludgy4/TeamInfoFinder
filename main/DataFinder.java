package main;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class DataFinder {
	
	public static ArrayList<Team> teams = new ArrayList<>();
	public static int year = 2018;
	
	public static void main(String[] args) {
		APIHandlerFRC tba = new APIHandlerFRC(
			"http://www.thebluealliance.com/api/v3", 
			"?X-TBA-Auth-Key=bwidXGDgsZNrUbUIYG9zrLYfubC14liNqFwshbbVsrBRzAvMprB8MmLfyisKwDBJ"
		);
		
		//Get teams at event
		teams = tba.getEventTeams("gal", year);
		
		//Does stuff with teams
		//tba.getEventTeamMedia(teams, year, "C:\\Users\\Matthew\\Pictures\\Test");
		
		Writer writer = null;
		try {writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\Matthew\\Documents\\Test\\TestData.txt"), "utf-8"));
			tba.scoutStatistic(teams, year, "totalPoints");
		} catch (IOException ex) {ex.printStackTrace();} finally {try {writer.close();} catch (Exception ex) {}}
		
		//tba.getInfo("/event/2018gal/teams/keys");
			
	}
	
	public static void print(String msg) {
		System.out.println(msg);
	}
	
	public static void print(int num) {
		System.out.println(num);
	}

}
