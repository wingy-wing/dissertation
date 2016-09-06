package recommendationEngine;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Cast {
	
	private String castId;
	private String name;
	private LocalDate timeDateCreated;
	private String artwork;
	private String explanation;
	private double dest_lon;
	private double dest_lat;
	private LocalDate castDate;
	
	public Cast(){
		this.castId = "";
		this.name = "";
		this.artwork = "";
		this.explanation = "";
		this.dest_lat = 0.0;
		this.dest_lon = 0.0;
	}
	
	public Cast (String castId, 
			String name, 
			String timeDateCreated, 
			String artwork, 
			String explanation, 
			double dest_lat, 
			double dest_lon, 
			long day, 
			long month, 
			long year){
		this.castId = castId;
		this.name = name;
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.timeDateCreated = LocalDate.parse(timeDateCreated, dateFormat);
		this.artwork = artwork;
		this.explanation = explanation;
		this.dest_lat = dest_lat;
		this.dest_lon = dest_lon;
		this.castDate = LocalDate.of((int)year, (int)month, (int)day);
	}

	public String getCastId() {
		return castId;
	}
	
	public String getName() {
		return name;
	}

	public LocalDate getTimeDateCreated() {
		return timeDateCreated;
	}

	public String getArtwork() {
		return artwork;
	}
	
	public String getExplanation() {
		return explanation;
	}

	public double getDest_lon() {
		return dest_lon;
	}

	public double getDest_lat() {
		return dest_lat;
	}

	public LocalDate getCastDate() {
		return castDate;
	}	
	
}
