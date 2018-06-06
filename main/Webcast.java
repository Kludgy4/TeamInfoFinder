package main;

public class Webcast {
	String type; //Enum: [ youtube, twitch, ustream, iframe, html5, rtmp, livestream ]
	String channel;
	String file;
	
	/**
	 * A basic constructor for the webcast object
	 * @param type The type of webcast //TODO ENUM
	 * @param channel The channel of the webcast
	 * @param file The file type of the webcast stream
	 */
	public Webcast(String type, String channel, String file) {
		this.type = type;
		this.channel = channel;
		this.file = file;
	}
}
