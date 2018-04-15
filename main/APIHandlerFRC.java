package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

public class APIHandlerFRC extends APIHandler {
	
	private static int imgNum = 1;
	private static int videoNum = 1;
	
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

	public ArrayList<Team> scoutStatistics(ArrayList<Team> teams, int year, String... statistics) {
		
		ArrayList<Team> rankedTeams = null;
		rankedTeams = new ArrayList<>();
		
		ArrayList<Double> stats = new ArrayList<>();
		stats.add(0, (double) 0);
		double j = 0;
		for (Team team : teams) {
			System.out.println(formatDouble("%.2f", j / teams.size() *100) + "% complete indexing " + team.name);
			
			double totalMatches = 0;
			String information = getInfo("/team/"+ team.teamKey + "/matches/" + year);
			JSONArray teamInformation = new JSONArray(information);
			for (int i = 0; i < teamInformation.length(); i++) {
				if (teamInformation.getJSONObject(i).getString("comp_level").equals("qm")) {	
					JSONArray blue = teamInformation.getJSONObject(i).getJSONObject("alliances").getJSONObject("blue").getJSONArray("team_keys");
					String alliance;
					if (blue.getString(0).equals(team.teamKey) || blue.getString(1).equals(team.teamKey) || blue.getString(2).equals(team.teamKey)) {
						alliance = "blue";
					} else {alliance = "red";}
					
					JSONObject matchInfo = teamInformation.getJSONObject(i).getJSONObject("score_breakdown").getJSONObject(alliance);
					System.out.println("stats size" + statistics);
					for (int k = 0; k < statistics.length; k++) {
						System.out.println(k);
						stats.add(k, matchInfo.getDouble(statistics[k]));
					}
					totalMatches++;
				}
			}
			int size = stats.size();
			for (int i = 0; i < size; i++) {
				stats.add(i, stats.get(i)/totalMatches);
				System.out.println(stats.size());
			}
			team.numericalStatistics = new ArrayList<>();
			team.numericalStatistics.addAll(stats);
			rankedTeams.add(team);
			System.out.print(team.number + ", " + team.name + ",");
			for (double d : team.numericalStatistics) {
				System.out.print(formatDouble("%.3f", d) + ",");
			}
			System.out.print("\n");
			stats.clear();
			j++;
		}
		/*Collections.sort(rankedTeams, new Comparator<Team>() {
	        @Override public int compare(Team t1, Team t2) {
	            return (int) ((t2.rank - t1.rank)*1000); // Ascending
	        }
	    });*/
		System.out.println("------------------------------------------------------------------------------------------------------------------");
		ArrayList<String> li = new ArrayList<String>();
		System.out.print("TeamName");
		for (String s : statistics) {
			System.out.print(","+s);
		}
		for (Team t : rankedTeams) {
			System.out.print(t.number + ", " + t.name + ",");
			for (double d : t.numericalStatistics) {
				System.out.print(formatDouble("%.3f", d) + ",");
			}
			System.out.print("\n");
		}
		/*System.out.println(statistic.toUpperCase());
		int i = 1;
		for (Team t : rankedTeams) {
			t.rank = formatDouble( t.rank);
			System.out.println(i + ": " + t.name + " - " + t.rank);
			i++;
		}*/
		return rankedTeams;
	}
	
	public double formatDouble(String format, double d) {
		return Double.parseDouble(String.format(format, d));
	}
}
