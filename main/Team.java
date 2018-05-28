package main;

import java.net.URL;

public class Team {
	public String name, teamKey;
	public int number, rookieYear;
	public String city, country, stateProv;
	public String nickname, motto;
	public URL website;
		
	/**
	 * Basic Team constructor, only information required to be provided by a team
	 * @param teamKey The TBA provided team key in format 'frcXXXX', where XXXX is the team's number
	 * @param number The team number, the unique identifier used in the competition
	 * @param name The name of the team, typically unassociated with
	 * @param rookieYear The first year the the team officially registered with FIRST
	 */
	public Team(String teamKey, int number, String name, int rookieYear) {
		this.teamKey = teamKey;
		this.number = number;
		this.name = name;
		this.rookieYear = rookieYear;
	}

	/**
	 * Full Team constructor, all information possible to be found about a team
	 * @param teamKey The team key in format 'frcXXXX', where XXXX is the team's number
	 * @param number The team number, the unique identifier used in the competition
	 * @param name The name of the team, typically unassociated with
	 * @param rookieYear The first year the the team officially registered with FIRST
	 * @param nickname Team nickname provided by FIRST
	 * @param city City of team derived from parsing the team's registered address
	 * @param stateProv State of team derived from parsing the team's registered address
	 * @param country Country of team derived from parsing the team's registered address
	 * @param website Official website associated with the team
	 * @param motto Team’s motto as provided by FIRST
	 */
	public Team(String teamKey, int number, String name, int rookieYear, 
				String nickname, String city, String stateProv, String country, 
				URL website, String motto) {
		this(teamKey, number, name, rookieYear);
		
		this.nickname = nickname;
		this.city = city;
		this.stateProv = stateProv;
		this.country = country;
		this.website = website;
		this.motto = motto;
	}
}
