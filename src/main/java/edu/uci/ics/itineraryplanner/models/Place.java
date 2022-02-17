package edu.uci.ics.itineraryplanner.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class Place {

	private String place_id;
	private String name;
	private String formatted_address;
	private Geometry geometry;
	private long rating;
	private long user_ratings_total;
	private OpeningHours opening_hours;
	private String business_status;
	private boolean permanently_closed;
	private List<String> types;
	
	
	public boolean getPermanently_closed() {
		return permanently_closed;
	}
	public void setPermanently_closed(boolean permanently_closed) {
		this.permanently_closed = permanently_closed;
	}
	public long getUser_ratings_total() {
		return user_ratings_total;
	}
	public void setUser_ratings_total(long user_ratings_total) {
		this.user_ratings_total = user_ratings_total;
	}
	public String getBusiness_status() {
		return business_status;
	}
	public void setBusiness_status(String business_status) {
		this.business_status = business_status;
	}
	public String getPlace_id() {
		return place_id;
	}
	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}
	public String getFormatted_address() {
		return formatted_address;
	}
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}
	public long getRating() {
		return rating;
	}
	public void setRating(long rating) {
		this.rating = rating;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public OpeningHours getOpening_hours() {
		return opening_hours;
	}

	public void setOpening_hours(OpeningHours opening_hours) {
		this.opening_hours = opening_hours;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}
	
}
