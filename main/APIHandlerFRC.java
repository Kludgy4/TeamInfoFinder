package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

public class APIHandlerFRC extends APIHandler {
	
	public APIHandlerFRC(String apiURL, String authKey) {
		super(apiURL, authKey);
	}
	
	
	public ArrayList<String> getTeamsMedia(ArrayList<Team> teams, int year) {
		for (Team team: teams) {
			String information = getInfo("/team/"+ team.teamKey + "/media/" + year);
			System.out.println(information);
		}
		return null;
	}
	
	public ArrayList<Team> getEventTeams(String eventKey, int year) {
		String information = getInfo("/event/"+ year + eventKey+ "/teams/simple");

		JSONArray teamInformation = new JSONArray(information);
		ArrayList<Team> teams = new ArrayList<>();
		
		for (int i = 0; i < teamInformation.length(); i++) {
		    String name = teamInformation.getJSONObject(i).getString("nickname");
		    String teamKey = teamInformation.getJSONObject(i).getString("key");
		    int number = teamInformation.getJSONObject(i).getInt("team_number");
		    String city = teamInformation.getJSONObject(i).getString("city");
		    String country = teamInformation.getJSONObject(i).getString("country");
		    teams.add(new Team(name, teamKey, number, city, country));
		}
		
		Collections.sort(teams, new Comparator<Team>() {
	        @Override public int compare(Team t1, Team t2) {
	            return t1.number - t2.number; // Ascending
	        }
	    });
		System.out.println(teams.size() + " teams found at " + eventKey + "!");
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
