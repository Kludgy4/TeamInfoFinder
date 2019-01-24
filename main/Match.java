package main;

import java.util.ArrayList;

public class Match {
	String matchKey, eventKey;
	String compLevel;
	String setNumber, matchNumber;
	ArrayList<Alliance> alliances = new ArrayList<>();
	String winningAlliance;
	int time, actualTime, predictedTime, postResultTime;
	
	/**
	 * Basic Match constructor, only contains information required to be provided by a TBA match
	 * @param matchKey The TBA provided match key with the format yyyy[EVENT_CODE]_[COMP_LEVEL]m[MATCH_NUMBER] where yyyy is the year, and EVENT_CODE is the event code of the event, COMP_LEVEL is (qm, ef, qf, sf, f), and MATCH_NUMBER is the match number in the competition level
	 * @param compLevel The competition level of the match (qm, ef, qf, sf, f)
	 * @param setNumber The set number in a series of matches where more than one match is required
	 * @param matchNumber The match number of the match in the competition level
	 * @param eventKey The TBA provided event key with the format xyxy[EVENT_CODE], where xyxy is the year
	 */
	public Match(String matchKey, String compLevel, String setNumber, 
			String matchNumber, String eventKey) {
		this.matchKey = matchKey;
		this.compLevel = compLevel;
		this.setNumber = setNumber;
		this.matchNumber = matchNumber;
		this.eventKey = eventKey;
	}
	
	/**
	 * Full Match constructor, all information possible to be found about a match
	 * @param matchKey The TBA provided match key with the format yyyy[EVENT_CODE]_[COMP_LEVEL]m[MATCH_NUMBER] where yyyy is the year, and EVENT_CODE is the event code of the event, COMP_LEVEL is (qm, ef, qf, sf, f), and MATCH_NUMBER is the match number in the competition level
	 * @param compLevel The competition level of the match (qm, ef, qf, sf, f)
	 * @param setNumber The set number in a series of matches where more than one match is required
	 * @param matchNumber The match number of the match in the competition level
	 * @param alliances A list of alliances (red, blue), and the teams on the alliances
	 * @param winningAlliance The colour of the winning alliance
	 * @param eventKey The TBA provided event key with the format xyxy[EVENT_CODE], where xyxy is the year
	 * @param time UNIX timestamp of the scheduled match time from the published schedule
	 * @param actualTime UNIX timestamp of the actual match start time
	 * @param predictedTime UNIX timestamp of the TBA predicted match time
	 * @param postResultTime UNIX timestamp when the match result was posted
	 */
	public Match(String matchKey, String compLevel, String setNumber, 
			String matchNumber, ArrayList<Alliance> alliances, String winningAlliance, String eventKey,
			int time, int actualTime, int predictedTime, int postResultTime) {
		this.matchKey = matchKey;
		this.compLevel = compLevel;
		this.setNumber = setNumber;
		this.matchNumber = matchNumber;
		this.alliances = alliances;
		this.winningAlliance = winningAlliance;
		this.eventKey = eventKey;
		this.time = time;
		this.actualTime = actualTime;
		this.predictedTime = predictedTime;
		this.postResultTime = postResultTime;
	}
}
