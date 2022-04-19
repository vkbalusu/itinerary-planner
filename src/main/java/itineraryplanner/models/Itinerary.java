package itineraryplanner.models;

import java.util.ArrayList;
import java.util.List;

public class Itinerary {
	
	List<Route> days;
	
	public Itinerary() {
		this.days = new ArrayList<>();
	}
	
	public Itinerary(List<Route> routes) {
		super();
		this.days = routes;
	}

	public List<Route> getDays() {
		return days;
	}

	public void setDays(List<Route> routes) {
		this.days = routes;
	}
	
}
