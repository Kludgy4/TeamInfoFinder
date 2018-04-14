package main;

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
		tba.getEventTeamMedia(teams, year, "C:\\Users\\Matthew\\Pictures\\Test");
		
		//tba.getInfo("/event/2018gal/teams/keys");
			
	}
	
	public static void print(String msg) {
		System.out.println(msg);
	}
	
	public static void print(int num) {
		System.out.println(num);
	}

}
