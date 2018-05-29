package main;

import java.util.ArrayList;

public class Event {
	String eventKey, eventCode, firstEventID, firstEventCode, parentEventKey;
	String name, shortName, eventTypeString, playoffTypeString;
	int eventType, playoffType;
	String startDate, endDate;
	int year, week;
	String country, stateProv, city, address, postalCode, gmapsPlaceID, gmapsURL, locationName, timeZone, website;
	double latitude, longitude;
	ArrayList<Webcast> webcasts = new ArrayList<>();
	ArrayList<String> divisions = new ArrayList<>();
	
	/**
	 * Basic event constructor, only contains information required to be provided by a team, and location information
	 * @param eventKey The TBA provided event key with the format xyxy[EVENT_CODE], where xyxy is the year
	 * @param name The official event name provided by FIRST or offseason event organisers
	 * @param eventCode The short code for the event as provided by FIRST
	 * @param eventType The event type as defined in the following website - https://github.com/the-blue-alliance/the-blue-alliance/blob/master/consts/event_type.py#L2 
	 * @param city The city the event is being held in
	 * @param stateProv The state or province that the event is being held in 
	 * @param country The country that the event is being held in
	 * @param startDate The start date in yyyy-mm-dd format
	 * @param endDate The end date in yyyy-mm-dd format
	 * @param year Year of the event
	 */
	public Event(String eventKey, String name, String eventCode, int eventType, 
			String city, String stateProv, String country, String startDate, String endDate, int year) {
		this.eventKey = eventKey;
		this.name = name;
		this.eventCode = eventCode;
		this.eventType = eventType;
		this.city = city;
		this.stateProv = stateProv;
		this.country = country;
		this.startDate = startDate;
		this.endDate = endDate;
		this.year = year;
	}
	
	/**
	 * Basic event constructor, only contains information required to be provided by a team, and location information
	 * @param eventKey The TBA provided event key with the format xyxy[EVENT_CODE], where xyxy is the year
	 * @param name The official event name provided by FIRST or offseason event organisers
	 * @param eventCode The short code for the event as provided by FIRST
	 * @param eventType The event type as defined in the following website - https://github.com/the-blue-alliance/the-blue-alliance/blob/master/consts/event_type.py#L2 
	 * @param city The city the event is being held in
	 * @param stateProv The state or province that the event is being held in 
	 * @param country The country that the event is being held in
	 * @param startDate The start date in yyyy-mm-dd format
	 * @param endDate The end date in yyyy-mm-dd format
	 * @param year Year of the event
	 */
	public Event(String eventKey, String name, String eventCode, int eventType, 
			String city, String stateProv, String country, String startDate, String endDate, int year, int week,
			String firstEventID, String firstEventCode, String parentEventKey,
			String shortName, String eventTypeString, String playoffTypeString, int playoffType,
			String address, String postalCode, String gmapsPlaceID, String gmapsURL, String locationName, String timeZone, String website,
			double longitude, double latitude, ArrayList<Webcast> webcasts, ArrayList<String> divisions) {
		
		this(eventKey, name, eventCode, eventType, city, stateProv, country, startDate, endDate, year);
		
		this.week = week;
		this.firstEventID = firstEventCode;
		this.firstEventCode = firstEventCode;
		this.parentEventKey = parentEventKey;
		this.shortName = shortName;
		this.eventTypeString = eventTypeString;
		this.playoffTypeString = playoffTypeString;
		this.playoffType = playoffType;
		this.address = address;
		this.postalCode = postalCode;
		this.gmapsPlaceID = gmapsPlaceID;
		this.gmapsURL = gmapsURL;
		this.locationName = locationName;
		this.timeZone = timeZone;
		this.website = website;
		this.longitude = longitude;
		this.latitude = latitude;
		this.webcasts = webcasts;
		this.divisions = divisions;
	}
}
