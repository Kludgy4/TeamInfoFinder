package main;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;

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
}
