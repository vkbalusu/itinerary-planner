package edu.uci.ics.itineraryplanner.controllers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import edu.uci.ics.itineraryplanner.models.PlaceSearch;
import edu.uci.ics.itineraryplanner.models.Place;

@RestController
@RequestMapping("/itinerary-planner")
public class ItineraryPlannerController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	@GetMapping("/api/plan")
	public List<Place> getItineraryPlan(@RequestParam String placeName) {
		
		String pageToken = null;
		List<Place> places = new LinkedList<>();
		
		
		do {
			System.out.println(pageToken);
			
			PlaceSearch placeSearch;
			
			if(pageToken == null)
				placeSearch = restTemplate.getForObject("https://maps.googleapis.com/maps/api/place/textsearch/json?query={name}&key={key}", PlaceSearch.class, "tourist spots in " + placeName , "AIzaSyCo88sI5_mYgrS38YzOGeyNV4sxjJi0TRM");
			else {
				placeSearch = restTemplate.getForObject("https://maps.googleapis.com/maps/api/place/textsearch/json?pagetoken=" + pageToken + "&key=AIzaSyCo88sI5_mYgrS38YzOGeyNV4sxjJi0TRM", PlaceSearch.class);			
			}
			places.addAll(placeSearch.getResults());
			
			System.out.println(placeSearch.getStatus());
			
			pageToken = placeSearch.getNextPageToken();
			
		}while(pageToken != null);
		
		
		Iterator<Place> iterator = places.iterator();
		
		while(iterator.hasNext()) {
			Place place = iterator.next();
			if(place.getPermanently_closed() == true)
				iterator.remove();
		}
		
		return places;
	}
}
