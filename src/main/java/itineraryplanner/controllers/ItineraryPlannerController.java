package itineraryplanner.controllers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import itineraryplanner.models.TouristSpot;
import itineraryplanner.models.VisitingTime;
import itineraryplanner.models.Route;

import itineraryplanner.models.DistanceDurationPair;
import itineraryplanner.models.DistanceMatrix;
import itineraryplanner.models.Element;
import itineraryplanner.models.Interval;
import itineraryplanner.models.Itinerary;
import itineraryplanner.models.ItineraryPlan;
import itineraryplanner.models.Period;

import itineraryplanner.models.PlaceDetail;
import itineraryplanner.exceptions.DistanceMatrixException;
import itineraryplanner.exceptions.ItineraryPlannerException;
import itineraryplanner.exceptions.PlaceDetailException;
import itineraryplanner.exceptions.PlaceSearchException;
import itineraryplanner.models.Place;
import itineraryplanner.models.PlaceSearch;
import itineraryplanner.models.Row;
import itineraryplanner.utils.Constants;





@RestController
@RequestMapping("/itinerary-planner")
public class ItineraryPlannerController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	@GetMapping("/api/plan")
	public ItineraryPlan getItineraryPlan(
			
			@RequestParam String placeName, 
			
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
			
			) 
	{
		int numberOfDays = (int) ChronoUnit.DAYS.between(startDate,endDate) + 1;
		String pageToken = null;
		List<Place> places = new LinkedList<>();
		PlaceSearch placeSearch;
		Place place;
		PlaceDetail placeDetail;
		ItineraryPlan itineraryPlan = new ItineraryPlan();
		try{
			do {
				if(pageToken == null)
					placeSearch = restTemplate.getForObject(Constants.TEXT_SEARCH_URL, PlaceSearch.class, Constants.SEARCH_PHRASE + placeName , Constants.API_KEY);
				else {
					placeSearch = restTemplate.getForObject(Constants.TEXT_SEARCH_NEXT_PAGE_URL, PlaceSearch.class, pageToken, Constants.API_KEY);			
				}
				if(placeSearch != null && ! placeSearch.getStatus().equals(Constants.OK))
					throw new PlaceSearchException(placeSearch.getStatus());
				for(Place spot : placeSearch.getResults()) {
					if(spot.getPermanently_closed() != true && spot.getPlace_id() != null)
						places.add(spot);
				}
				pageToken = placeSearch.getNextPageToken();
				TimeUnit.SECONDS.sleep(2);
			}while(pageToken != null);
			
			Collections.sort(places, Collections.reverseOrder());
			places = places.subList(0, Math.min(Math.min(40, 10 * numberOfDays), places.size()));
			
			Iterator<Place> placesIterator = places.iterator();
			List<List<Interval>> timings;
			Interval interval;
			int day;
			
			while(placesIterator.hasNext()) {
				place = placesIterator.next();
				placeDetail = restTemplate.getForObject(Constants.PLACE_DETAILS_API, PlaceDetail.class, place.getPlace_id(), Constants.API_KEY);
				if(placeDetail != null && ! placeDetail.getStatus().equals(Constants.OK))
					throw new PlaceDetailException(placeDetail.getStatus());
				if(placeDetail.getResult().getOpening_hours() != null) {
					timings = new ArrayList<>(Collections.nCopies(7, null));
					for(Period period : placeDetail.getResult().getOpening_hours().getPeriods()) {
						day = period.getOpen().getDay();
						if(period.getClose() == null) 
							interval = new Interval(day, period.getOpen().getTime(), 0000);
						else
							interval = new Interval(day, period.getOpen().getTime(), period.getClose().getTime());
						List<Interval> intervals = timings.get(day);
						if(intervals == null)
							intervals = new ArrayList<Interval>();
						intervals.add(interval);
						timings.set(day, intervals);
					}
					place.setTimings(timings);
				}else {
					placesIterator.remove();
				}
			}
	
			int numbersOfRows, numberofColumns; DistanceMatrix distanceMatrix;
			DistanceDurationPair[][] matrix = new DistanceDurationPair[places.size()][places.size()];
			for(int i = 0; i < places.size(); i = i + 10){
				String origins = new String(); numbersOfRows = Math.min(places.size(), i + 10);
		        for(int t = i; t < numbersOfRows; t++){
		        	if(t == numbersOfRows - 1)
		        		origins = origins + "place_id:" + places.get(t).getPlace_id();
		        	else
		        		origins = origins + "place_id:" + places.get(t).getPlace_id() + "|";
		        }
		        for(int j = 0; j < places.size(); j = j + 10){
		        	String destinations = new String(); numberofColumns = Math.min(places.size(), j + 10);
		            for(int t = j; t < numberofColumns; t++){
		            	if(t == numberofColumns - 1)
		            		destinations = destinations + "place_id:" + places.get(t).getPlace_id();
		            	else 
		            		destinations = destinations + "place_id:" + places.get(t).getPlace_id() + "|";;
		            }
		            distanceMatrix = restTemplate.getForObject(Constants.DISTANCE_MATRIX_API, DistanceMatrix.class, origins, destinations, Constants.API_KEY);
		            if(distanceMatrix != null && ! distanceMatrix.getStatus().equals(Constants.OK))
						throw new DistanceMatrixException(distanceMatrix.getStatus());
		            int rnum = i;
		            for(Row row : distanceMatrix.getRows()) {
					    int cnum = j;
						for(Element element : row.getElements()) {
							DistanceDurationPair pair = new DistanceDurationPair(element.getDistance().getValue(), element.getDuration().getValue());
							matrix[rnum][cnum] = pair;
							cnum++;
						}
						rnum++;
					}
		        }
		    }
		
			ArrayList<ArrayList<Integer>> routePermuations = new ArrayList<>();
		    routePermute(routePermuations, new ArrayList<>(), places.size(), 4);
		    
		    day = 4; //preparing itinerary for Thursday. Should ideally consider day of the week for the selected dates
		    List<Interval> placeTimings;
		    int start, open, close, i, j, oneDay = 24 * 60;
			double ratingScore = 0, distanceScore = 0, maxRatingScore = -1, maxDistanceScore = -1;
			List<Route> routes = new ArrayList<>();
			
			for(ArrayList<Integer> route : routePermuations) {
				ratingScore = 0; distanceScore = 0;
				ArrayList<VisitingTime> travelTimes = new ArrayList<>();
				place = places.get(route.get(0));
				placeTimings = place.getTimings().get(day); // first place among 3
				if(placeTimings == null) {
					continue; // invalid if place has no open timings on given day of the week
				}
				start = getMinutes(Math.max(900, placeTimings.get(0).getOpeningTime())); // max(earliest opening hours, 9AM);
				travelTimes.add(new VisitingTime(getHours(start), getHours((start + 120) % oneDay)));
				start = (start + 120) % oneDay;
				
				ratingScore += (place.getRating() * place.getUser_ratings_total());
				
				for(i = 1; i < route.size(); i++) {
					start  = (start + (int) (matrix[route.get(i - 1)][route.get(i)].getDuration() / 60)) % oneDay; // travel to next place
					place = places.get(route.get(i));
					ratingScore += (place.getRating() * place.getUser_ratings_total());
					distanceScore += matrix[route.get(i - 1)][route.get(i)].getDistance();
					placeTimings = place.getTimings().get(day);
					if(placeTimings == null) {
						break; // invalid if place has no open timings on given day of the week
					}
					for(j = 0; j < placeTimings.size(); j++) {
						interval = placeTimings.get(j);
						open  = getMinutes(interval.getOpeningTime());
						close = getMinutes(interval.getClosingTime());
						if(start >= open && (start + 120) % oneDay <= close) {
							travelTimes.add(new VisitingTime(getHours(start), getHours((start + 120) % oneDay)));
							break;
						}	
					}
					if(j == placeTimings.size()) {
						break; // invalid if place is not open at required time
					}
					start = (start + 120) % oneDay;
				}
				
				if(i == route.size()) {
					ratingScore /= route.size();
					maxRatingScore = Math.max(maxRatingScore, ratingScore);
					maxDistanceScore = Math.max(maxDistanceScore, distanceScore);
					ArrayList<TouristSpot> p = new ArrayList<>(); Place temp;
					for(int s  = 0; s < route.size(); s++) {
						temp = places.get(route.get(s));
						p.add(new TouristSpot(travelTimes.get(s).getStart(), travelTimes.get(s).getEnd(), temp.getName(), temp.getFormatted_address(), temp.getRating(), temp.getUser_ratings_total(), temp.getTypes()));
					}
					routes.add(new Route(p, ratingScore, distanceScore));
				}
			}
			
			double profitScore;
			for(Route route : routes) {
				profitScore = 2 * (route.getRatingScore()/ maxRatingScore) - (route.getDistanceScore() / maxDistanceScore);
				route.setProfitScore(profitScore);
			}
			if(routes.size() < numberOfDays) {
				throw new ItineraryPlannerException(Constants.FEWER_ROUTES);
			}
			Collections.sort(routes);
			Itinerary itinerary = new Itinerary();
			HashMap<String, Integer> visited = new HashMap<>();
			for(Route route : routes) {
				boolean skipRoute = false;
				for(i = 0; i < 4; i++) {
					if(visited.containsKey(route.getPlaces().get(i).getName())) {
						skipRoute = true;
						break;
					}
				}
				//select routes with previously unvisited places.
				if(skipRoute == true) continue;
				for(i = 0; i < 4; i++) {
					visited.put(route.getPlaces().get(i).getName(), 1);
				}
				route.setDate(startDate);
				startDate = startDate.plusDays(1);
				itinerary.getDays().add(route);
				if(itinerary.getDays().size() == numberOfDays)
					break;
			}
			if(itinerary.getDays().size() < numberOfDays)
				throw new ItineraryPlannerException(Constants.FEWER_ROUTES);
			itineraryPlan.setNumberofDays(numberOfDays);
			itineraryPlan.setItinerary(itinerary);
			itineraryPlan.setStatus(Constants.OK);
		}catch(Exception exception){
			itineraryPlan.setStatus(Constants.ERROR);
			itineraryPlan.setErrorMessage(exception.getMessage());
		}
		return itineraryPlan;
	}
	
	private void routePermute(ArrayList<ArrayList<Integer>> list, ArrayList<Integer> tempList, int placesCount, int permutationSize) {
		if (tempList.size() == permutationSize) {
			list.add(new ArrayList<>(tempList));
		} else {
			for (int i = 0; i < placesCount; ++i) {
				if (tempList.contains(i))
					continue;
				tempList.add(i);
				routePermute(list, tempList, placesCount, permutationSize);
				tempList.remove(tempList.size() - 1);
			}
		}
	}
	
	private String getHours(int time) {
		String s =  String.valueOf(time / 60) + ":";
		if(time % 60 < 10) s = s + "0" + String.valueOf(time % 60);
		else s = s + String.valueOf(time % 60);
		return s;
			
	}

	private int getMinutes(int time) {
		return ((time / 100) * (60) + (time % 100)) % (24 * 60);
	}
}
