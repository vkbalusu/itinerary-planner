package itineraryplanner.models;

import java.util.List;

public class Itinerary {
	
	List<Route> days;
	
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
