package itineraryplanner.exceptions;

public class PlaceSearchException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  PlaceSearchException(String message) {
		super("Exeception in Place Search API call. Status: " + message);
	}
}
