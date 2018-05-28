package main;

public class Webcast {
	String type; //Enum: [ youtube, twitch, ustream, iframe, html5, rtmp, livestream ]
	String channel;
	String file;
	
	public Webcast(String type, String channel, String file) {
		this.type = type;
		this.channel = channel;
		this.file = file;
	}
}
