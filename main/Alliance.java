package main;

import java.util.ArrayList;

public class Alliance {
	AllianceColour colour;
	ArrayList<Team> teams, surrogateTeams, disqualifiedTeams;
	int score;
	
	/**
	 * Basic Alliance constructor, only contains essential information
	 * @param colour The colour of the alliance
	 * @param teams TBA provided team keys for teams on this alliance
	 */
	public Alliance(AllianceColour colour, ArrayList<Team> teams) {
		this.colour = colour;
		this.teams = teams;
	}
	
	/**
	 * Full Alliance constructor, all information possible to be found about an alliance
	 * @param colour The colour of the alliance
	 * @param teams TBA provided team keys for teams on this alliance
	 * @param surrogateTeams TBA provided team keys for teams playing as surrogates on this alliance
	 * @param disqualifiedTeams TBA provided team keys for disqualified teams on this alliance
	 * @param score The score for this alliance in this match
	 */
	public Alliance(AllianceColour colour, ArrayList<Team> teams, ArrayList<Team> surrogateTeams, ArrayList<Team> disqualifiedTeams, int score) {
		this.colour = colour;
		this.teams = teams;
		this.surrogateTeams = surrogateTeams;
		this.disqualifiedTeams = disqualifiedTeams;
		this.score = score;
	}
}
