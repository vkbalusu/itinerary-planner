package itineraryplanner.models;

import java.util.List;

public class TouristSpot {
	private String startTime;
	private String endTime;
	private String name;
	private String address;
	private float rating;
	private long user_ratings_total;
	private List<String> types;
	 
	public TouristSpot(String startTime, String endTime, String name, String address, float rating,
			long user_ratings_total, List<String> types) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.name = name;
		this.address = address;
		this.rating = rating;
		this.user_ratings_total = user_ratings_total;
		this.types = types;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public long getUser_ratings_total() {
		return user_ratings_total;
	}
	public void setUser_ratings_total(long user_ratings_total) {
		this.user_ratings_total = user_ratings_total;
	}
	public List<String> getTypes() {
		return types;
	}
	public void setTypes(List<String> types) {
		this.types = types;
	}
}

