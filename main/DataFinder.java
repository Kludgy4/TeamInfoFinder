package main;

import java.util.ArrayList;

public class DataFinder {
	
	public static ArrayList<Team> teams = new ArrayList<>();
	
	public static void main(String[] args) {
		APIHandlerFRC tba = new APIHandlerFRC(
			"http://www.thebluealliance.com/api/v3", 
			"?X-TBA-Auth-Key=bwidXGDgsZNrUbUIYG9zrLYfubC14liNqFwshbbVsrBRzAvMprB8MmLfyisKwDBJ"
		);
		
		//Get teams at event
		teams = tba.getEventTeams("gal", 2018);
		
		//Does stuff with teams
		
		//tba.getInfo("/event/2018gal/teams/keys");
			
	}
	
	public static void print(String msg) {
		System.out.println(msg);
	}
	
	public static void print(int num) {
		System.out.println(num);
	}

}
