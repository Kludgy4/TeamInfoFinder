package main;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

public class APIHandlerFRC extends APIHandler {
	
	private static int imgNum = 1, videoNum = 1;
	
	public APIHandlerFRC(String apiURL, String authKey) {
		super(apiURL, authKey);
	}
	
	public void getEventTeamMedia(ArrayList<Team> teams, int year, String saveLocation) {
		for (Team team: teams) {
			//Finds all team media info
			String information = getInfo("/team/"+ team.teamKey + "/media/" + year);
			JSONArray mediaInformation = new JSONArray(information);
			
			System.out.println(information);
			
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
	
	public ArrayList<Team> getEventTeams(String eventKey, int year) {
		String information = getInfo("/event/"+ year + eventKey+ "/teams");

		JSONArray teamInformation = new JSONArray(information);
		ArrayList<Team> teams = new ArrayList<>();
		for (int i = 0; i < teamInformation.length(); i++) {
		    String teamKey = teamInformation.getJSONObject(i).getString("key");
		    int number = teamInformation.getJSONObject(i).getInt("team_number");
		    String name = teamInformation.getJSONObject(i).getString("nickname");
		    int rookieYear = teamInformation.getJSONObject(i).getInt("rookie_year");
		    teams.add(new Team(teamKey, number, name, rookieYear));
		}
		
		Collections.sort(teams, new Comparator<Team>() {
	        @Override public int compare(Team t1, Team t2) {
	            return t1.number - t2.number; // Ascending
	        }
	    });
		return teams;		
	}
	
	public Team getTeam(String key) {

		JSONObject information = new JSONObject(getInfo("/team/" + key));
		
		//TODO Remove unneeded variables like those below
	    String teamKey = information.getString("key");
	    int number = information.getInt("team_number");
	    String name = information.getString("nickname");
	    int rookieYear = information.getInt("rookie_year");
		return new Team(teamKey, number, name, rookieYear);		
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
	
	public void getEventRankings(String eventKey, int year) {

		String information = getInfo("/event/"+ year + eventKey+ "/rankings");
			
		JSONArray teamInformation = (new JSONObject(information)).getJSONArray("rankings");
	
		for (int i = 0; i < teamInformation.length(); i++) {		    
			System.out.println(teamInformation.getJSONObject(i).getString("team_key") + " is " + 
		    					teamInformation.getJSONObject(i).getInt("rank") + " with RP " + 
		    					teamInformation.getJSONObject(i).getJSONArray("sort_orders").getFloat(0) + 
		    					"(" +  teamInformation.getJSONObject(i).getInt("matches_played") + 
		    					", " + teamInformation.getJSONObject(i).getJSONArray("extra_stats").getInt(0) + ")");
		}
		
	}

	public void scoutTeamStatistics(Team scoutTeam, int year, ArrayList<String> statisticsToScout) {
		
		String information = getInfo("/team/"+ scoutTeam.teamKey + "/matches/" + year);
		scoutTeam.statList.getStatistics().clear();
				
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
				System.out.println("		Match data not found for " + scoutTeam.name + " (" + scoutTeam.number + ") at event " 
						+ teamInformation.getJSONObject(i).getString("event_key") + " match " + teamInformation.getJSONObject(i).getInt("match_number"));
			}
		}
	}
	
	public void getEventPredictions(String eventKey, int year) {
		String information = getInfo("/event/"+ year + eventKey+ "/predictions");
		//System.out.println(information);
		
		for (int i = 1; i <= 130; i++) {
			JSONObject teamInformation = (new JSONObject(information).getJSONObject("match_predictions")
					.getJSONObject("qual").getJSONObject(year+eventKey.toLowerCase()+"_qm"+i));
			JSONObject redInfo = teamInformation.getJSONObject("red");
			JSONObject blueInfo = teamInformation.getJSONObject("blue");
			
			System.out.println("---------------------------------------------------");
			System.out.println("Qualification " + i + ": ");
			
			System.out.print("  Red: ");
			double redScore = redInfo.getDouble("score");
			System.out.println(redScore);
			System.out.println("   Red Endgame Points: " + redInfo.getDouble("endgame_points"));
			//System.out.println(redInfo);
			System.out.print("  Blue: ");
			double blueScore = blueInfo.getDouble("score");
			System.out.println(blueScore);
			System.out.println("   Blue Endgame Points: " + blueInfo.getDouble("endgame_points"));
			
			if (redScore > blueScore) System.out.println("Red should beat blue");
			else System.out.println("Blue should beat red");
		}
	}
	
	public void getEventMatchPredictions(String eventKey, int year, int matchNum, String matchType) {
		String information = getInfo("/event/"+ year + eventKey+ "/predictions");
		System.out.println(information);
		
		JSONObject teamInformation = (new JSONObject(information).getJSONObject("match_predictions")
				.getJSONObject("qual").getJSONObject(year+eventKey.toLowerCase()+"_matchType"+matchNum));
		JSONObject redInfo = teamInformation.getJSONObject("red");
		JSONObject blueInfo = teamInformation.getJSONObject("blue");
		
		System.out.println("---------------------------------------------------");
		System.out.println("Qualification " + matchNum + ": ");
		
		System.out.print("  Red: ");
		double redScore = formatDouble(redInfo.getDouble("score"));
		System.out.println(redScore);
		System.out.println("    Auto Points: " + formatDouble(redInfo.getDouble("auto_points")));
		System.out.println("    Endgame Points: " + formatDouble(redInfo.getDouble("endgame_points")));
		System.out.println("    RP: Auto Quest - " + formatDouble(redInfo.getDouble("prob_auto_quest")*100) + "%");
		System.out.println("        Boss - " + formatDouble(redInfo.getDouble("prob_face_boss")*100) + "%");
		System.out.print("  Blue: ");
		double blueScore = formatDouble(blueInfo.getDouble("score"));
		System.out.println(blueScore);
		System.out.println("    Auto Points: " + formatDouble(blueInfo.getDouble("auto_points")));
		System.out.println("    Endgame Points: " + formatDouble(blueInfo.getDouble("endgame_points")));
		System.out.println("    RP: Auto Quest - " + formatDouble(blueInfo.getDouble("prob_auto_quest")*100) + "%");
		System.out.println("        Boss - " + formatDouble(blueInfo.getDouble("prob_face_boss")*100) + "%");
		
		if (redScore > blueScore) System.out.println("Red should beat blue");
		else System.out.println("Blue should beat red");
	}
	
	public double formatDouble(double d) {
		return Double.parseDouble(String.format("%.4f", d));
	}
	
	public double formatDouble(String format, double d) {
		return Double.parseDouble(String.format(format, d));
	}
}
