package itineraryplanner.utils;

public class Constants {
	public static final String INVALID_REQ = "INVALID REQUEST";
	public static final String REQUEST_DENIED = "REQUEST_DENIED";
	public static final String OK = "OK";
	public static final String ERROR = "ERROR";
	public static final String TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query={name}&key={key}";
	public static final String TEXT_SEARCH_NEXT_PAGE_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?pagetoken={name}&key={key}";
	public static final String PLACE_DETAILS_API = "https://maps.googleapis.com/maps/api/place/details/json?fields=opening_hours&place_id={placeid}&key={key}";
	public static final String DISTANCE_MATRIX_API = "https://maps.googleapis.com/maps/api/distancematrix/json?origins={origins}&destinations={destinations}&key={key}";
	public static final String SEARCH_PHRASE = "tourist spots in ";
	public static final String API_KEY = "AIzaSyAPxVYwbbCIBRfrozpMNkAfP7e_HZUOQX0";
}
