package main;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

public class APIHandlerFRC extends APIHandler {
	
	private static int imgNum = 1, videoNum = 1;
	
	/**
	 * Handles The Blue Alliance (TBA) API Calls
	 * @param apiURL
	 * @param authKey
	 */
	public APIHandlerFRC(String apiURL, String authKey) {
		super(apiURL, authKey);
	}
	
	/**
	 * Saves all team media logged on TBA to a given location
	 * @param teams The teams whose media will be downloaded
	 * @param year The year for which the media should be taken 
	 * @param saveLocation The location to which the media should be saved
	 */
	public void getMediaTeams(ArrayList<Team> teams, int year, String saveLocation) {
		for (Team team: teams) {
			//Finds all team media info
			String information = getInfo("/team/"+ team.teamKey + "/media/" + year);
			JSONArray mediaInformation = new JSONArray(information);
			
			imgNum = 1;
			videoNum = 1;
			for (int i = 0; i < mediaInformation.length(); i++) {
				switch(mediaInformation.getJSONObject(i).getString("type")) {
					case "avatar":
						//saveImage("data:image/png;base64," + mediaInformation.getJSONObject(i).getJSONObject("details").getString("base64Image"), "C:\\Users\\Matthew\\Pictures\\Test");
						break;
					case "instagram-image":
						saveImage(mediaInformation.getJSONObject(i).getJSONObject("details").getString("thumbnail_url"), 
								saveLocation,
								team.number + "_" + team.name.replace(" ", "") + "_" + imgNum + ".jpg");
						break;
					case "imgur":
						saveImage("https://i.imgur.com/" + mediaInformation.getJSONObject(i).getString("foreign_key") + ".png", 
								saveLocation,
								team.number + "_" + team.name.replace(" ", "") + "_" + imgNum + ".png");
						break;
					case "cdphotothread":
						saveImage("https://www.chiefdelphi.com/media/img/" + mediaInformation.getJSONObject(i).getJSONObject("details").getString("image_partial"),
								saveLocation,
								team.number + "_" + team.name.replace(" ", "") + "_" + imgNum + ".png");
						break;
					case "youtube":
						saveYoutube("https://www.youtube.com/watch?v="+mediaInformation.getJSONObject(i).getString("foreign_key"), 
								saveLocation,
								team.number + "_" + team.name.replace(" ", "") + "_" + videoNum + ".mp4");
						break;
					default:
						System.out.println("Unable to read media of type " + mediaInformation.getJSONObject(i).getString("type") + " ;(");
						break;
				}
			}
		}
	}
	
	/**
	 * Saves an image to a given path on a computer
	 * @param imageURL The online URL of the image
	 * @param destinationFolder The destination to save the file in
	 */
	public static void saveImage(String imageURL, String destinationFolder, String fileName) {
		try(InputStream in = new URL(imageURL).openStream()){
		    Files.copy(in, Paths.get(destinationFolder + "/" + fileName));
		    imgNum++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves a youtube video to a given path on a computer
	 * @param videoURL The URL of the youtube video
	 * @param destinationFolder The online URL of the video
	 * @param fileName The destination to save the file in
	 */
	public static void saveYoutube(String videoURL, String destinationFolder, String fileName) {
		try {
			System.out.println("Can't yet download video from " + videoURL);
            videoNum++;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
	
	/**
	 * Returns a list of teams who are competing at a given event
	 * @param eventKey The key for the event to be checked
	 * @param year The year for which this event should be checked
	 * @return A list of Team objects who are competing at the given event
	 */
	public ArrayList<Team> getTeamsEvent(String eventKey, int year) {
		JSONArray teamInformation = new JSONArray(getInfo("/event/"+ year + eventKey+ "/teams"));
		ArrayList<Team> teams = new ArrayList<>();
		
		System.out.println("Getting teams");
		for (int i = 0; i < teamInformation.length(); i++) teams.add(getTeam(teamInformation.getJSONObject(i).getString("key")));
		
		System.out.println("Sorting teams");
		Collections.sort(teams, new Comparator<Team>() {
	        @Override public int compare(Team t1, Team t2) {return t1.number - t2.number;}}); // Ascending
		return teams;
	}
	
	/**
	 * Gets a team object given their teamKey
	 * @param teamKey The key of the team
	 * @return Team A team object representative of the team associated with the given key
	 */
	public Team getTeam(String teamKey) {

		JSONObject information = new JSONObject(getInfo("/team/" + teamKey));
		
	    int number = information.getInt("team_number");
	    String name = information.getString("nickname");
	    int rookieYear = information.getInt("rookie_year");
		return new Team(teamKey, number, name, rookieYear);		
	}
	
	/**
	 * Gets an array of strings whose names are representative of gettable statistics
	 * @param yearToScout The year for which these statistics are gotten from
	 * @param eventKey The event that these statistics should be pulled from
	 * @return An array of strings whose names are representative of gettable statistics
	 */
	public String[] getStatistics(int yearToScout, String eventKey) {
		
		String[] statArray = JSONObject.getNames((new JSONArray(getInfo("/event/"+ yearToScout + eventKey + "/matches"))).
				getJSONObject(0).getJSONObject("score_breakdown").getJSONObject("red"));
		System.out.println("\n" + "Getting statistics");
		Arrays.sort(statArray);
		return statArray;
	}

	/**
	 * Scouts a teams statistics and adds them to that Team
	 * @param scoutTeam The team to be scouted
	 * @param year The year for which this team should be scouted
	 * @param statisticsToScout A list of strings representative of statistics that should be scouted
	 */
	public void scoutStatisticsTeam (Team scoutTeam, int year, ArrayList<String> statisticsToScout) {
		
		String information = getInfo("/team/"+ scoutTeam.teamKey + "/matches/" + year);
		scoutTeam.statList.statistics.clear();
				
		JSONArray teamInformation = new JSONArray(information);
		System.out.println("Processing stats for " + scoutTeam.name + " (" + scoutTeam.number + ")");
		
		for (int i = 0; i < teamInformation.length()-1; i++) {
			try {
				if (teamInformation.getJSONObject(i).getString("comp_level").equals("qm")) {	
					JSONArray blue = teamInformation.getJSONObject(i).getJSONObject("alliances").getJSONObject("blue").getJSONArray("team_keys");
					String alliance;
					if (blue.getString(0).equals(scoutTeam.teamKey) || blue.getString(1).equals(scoutTeam.teamKey) || blue.getString(2).equals(scoutTeam.teamKey)) {
						alliance = "blue";
					} else {alliance = "red";}
					JSONObject matchInfo = teamInformation.getJSONObject(i).getJSONObject("score_breakdown").getJSONObject(alliance);
					
					Statistic stat;
					for (String scoutStat : statisticsToScout) {
						try {
							stat = new Statistic(scoutStat, matchInfo.getInt(scoutStat));
						} catch (Exception e) {
							try {
								stat = new Statistic(scoutStat, matchInfo.getBoolean(scoutStat));
							} catch (Exception ex) {
								stat = new Statistic(scoutStat, matchInfo.getString(scoutStat));
							}
						}
						scoutTeam.statList.addStatistic(stat);
					}
				}
			} catch (Exception e) {
				System.out.println("	Some match data not found for " + scoutTeam.name + " (" + scoutTeam.number + ") at event " 
						+ teamInformation.getJSONObject(i).getString("event_key") + " match " + teamInformation.getJSONObject(i).getInt("match_number"));
			}
		}
	}
	
	/**
	 * Scouts a list of teams and adds their statistics to each Team object
	 * @param scoutTeams The list of teams to be scouted
	 * @param year The year for which this list of teams should be scouted
	 * @param statisticsToScout A list of strings representative of statistics that should be scouted
	 */
	public void scoutStatisticsTeams(ArrayList<Team> scoutTeams, int year, ArrayList<String> statisticsToScout) {
		for (Team t : scoutTeams) scoutStatisticsTeam(t, year, statisticsToScout);
	}
	
	
	public double formatDouble(double d) {
		return Double.parseDouble(String.format("%.4f", d));
	}
	
	public double formatDouble(String format, double d) {
		return Double.parseDouble(String.format(format, d));
	}
}
