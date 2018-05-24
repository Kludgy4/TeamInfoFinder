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
	
	/*public static void handleSave(String output, GameRecord game) {
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);
        String defLoc = Defaults.getFilename() + "\\Match " + game.match + " Team " + game.team + ".csv";
        f.setSelectedFile(defLoc.length() > 0 ? new File(defLoc) : null);
        f.setFileFilter(new FileFilter() {
			public String getDescription() {
				return "Scouting interface record (.csv)";
			}
			
			public boolean accept(File f) {
				return f.isDirectory() || f.getPath().endsWith(".csv");
			}
		});
        f.setAcceptAllFileFilterUsed(false);
        int sel = f.showSaveDialog(ScoutingInterface.app);

		if(sel == JFileChooser.APPROVE_OPTION && f.getSelectedFile() != null) {
			String path = f.getSelectedFile().getPath();
			
			if(!path.endsWith(".csv")) path += ".csv";
			
			try{
				saveRecord(path, output);
			}
			catch(Exception e) {
				String buttons[] = {"Try Again"};
	            JOptionPane.showOptionDialog(ScoutingInterface.app, 
	                "Failed to save record.", "Scouting interface", 
	                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, 
	                buttons,buttons[0]);
	            
	            handleSave(output, game);
			}
		}
	}

	public static void saveRecord(String filename, String output) {
		try {
			Defaults.saveFileLoc(filename);
			PrintStream file = new PrintStream(filename);
			file.println(output);
			file.close();
			
			createSendThread(filename, output).start();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static Thread createSendThread(String filename, String output) {
		return new Thread(() -> {
			try {
				while(SendFile.sendFile(filename, filename.substring(filename.lastIndexOf("\\") + 1), ComSettings.sender.getBytes()) != 0);
			}
			catch(IOException | InterruptedException e) {
				createSendThread(filename, output);
			}
		});
	}*/
	
	public void getEventRankings(String eventKey, int year) {
		String information = getInfo("/event/"+ year + eventKey+ "/rankings");
			
		JSONArray teamInformation = (new JSONObject(information)).getJSONArray("rankings");
		
		for (int i = 0; i < teamInformation.length(); i++) {
			int rank = teamInformation.getJSONObject(i).getInt("rank");
			String teamKey = teamInformation.getJSONObject(i).getString("team_key");
			
			float RP = teamInformation.getJSONObject(i).getJSONArray("sort_orders").getFloat(0);
			int matchesPlayed = teamInformation.getJSONObject(i).getInt("matches_played");
			
		    int totalRP = teamInformation.getJSONObject(i).getJSONArray("extra_stats").getInt(0);
		    
		    System.out.println(teamKey + " is " + rank + " with RP " + RP + "(" + matchesPlayed + ", " + totalRP + ")");
		}
		
	}

	public ArrayList<Team> scoutStatistic(ArrayList<Team> teams, int year, String statistic) {
		
		ArrayList<Team> rankedTeams = null;
		rankedTeams = new ArrayList<>();
		
		double j = 0;
		for (Team team : teams) {
			String percentComplete = String.format("%.2f", (j / teams.size() *100));
			System.out.println(percentComplete + "% complete indexing " + statistic);
			double numericStatistic = 0;
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
					
					numericStatistic += matchInfo.getInt(statistic);
	
					totalMatches++;
				}
			}
			team.rank = numericStatistic/totalMatches;
			rankedTeams.add(team);
			j++;
		}
		Collections.sort(rankedTeams, new Comparator<Team>() {
	        @Override public int compare(Team t1, Team t2) {
	            return (int) ((t1.number - t2.number)*1000); // Ascending
	        }
	    });
		System.out.println("------------------------------------------------------------------------------------------------------------------");
		System.out.println(statistic.toUpperCase());
		int i = 1;
		for (Team t : rankedTeams) {
			t.rank = Double.parseDouble(String.format("%.3f", t.rank));
			//System.out.print(t.number + ",");
			//System.out.print(t.name + ",");
			System.out.println(t.rank);
			i++;
		}
		return rankedTeams;
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
}
