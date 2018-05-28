package main;

public class Robot {
	int year;
	String robotName;
	String robotKey, teamKey;
	
	/**
	 * Standard robot constructor, all parameters required to be supplied by teams
	 * @param year The year this robot competed
	 * @param robotName The name of the robot as provided by the team
	 * @param robotKey The TBA provided key specific to that robot used to identify on TBA
	 * @param teamKey The team key in format 'frcXXXX', where XXXX is the team's number
	 */
	public Robot(int year, String robotName, String robotKey, String teamKey) {
		this.year = year;
		this.robotName = robotName;
		this.robotKey = robotKey;
		this.teamKey = teamKey;
	}
}
