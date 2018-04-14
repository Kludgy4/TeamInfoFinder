package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class APIHandlerFRC extends APIHandler {
	
	public APIHandlerFRC(String apiURL, String authKey) {
		super(apiURL, authKey);
	}
	
	public ArrayList<Team> getEventTeams(String eventKey, int year) {
		String information;
		information = getInfo("/event/"+ year + eventKey+ "/teams");//.replaceAll("\\s+","");
		
		information = information.replace("[", "");
		information = information.replace("]", "");
		information = information.replace("{", "");
		
		
		String[] teamsRaw = information.split("},");
		for (String d : teamsRaw) {
			System.out.println(d);
		}
		ArrayList<String[]> teamRaw = new ArrayList<>();
		
		for (String team : teamsRaw) {
			System.out.println(team);
			teamRaw.add(team.split(","));
		}
		
		ArrayList<Team> teams = new ArrayList<>();
		
		for (String[] team : teamRaw) {
			teams.add(new Team(team[2], Integer.parseInt(team[1]), team[4], team[5], team[6]));
		}
		
		return teams;
		
	}
	
	public ArrayList<Integer> getEventTeamNumbers(String eventKey, int year) {
		ArrayList<Integer> teamNums = new ArrayList<>();
		ArrayList<String> teamNumsSt = new ArrayList<>();
		
		teamNumsSt.addAll(Arrays.asList(cleanMsg(getInfo("/event/"+ year + eventKey+ "/teams/keys")).split(",")));
		
		for(String s : teamNumsSt) teamNums.add(Integer.valueOf(s));
		Collections.sort(teamNums);
		return teamNums;
	}

	public static String cleanMsg(String msg) {
		msg = msg.replace("\"", "");
		msg = msg.replace("[", "");
		msg = msg.replace("]", "");
		msg = msg.replace("frc", "");
		msg = msg.replaceAll("\\s+","");
		return msg;
	}
}
