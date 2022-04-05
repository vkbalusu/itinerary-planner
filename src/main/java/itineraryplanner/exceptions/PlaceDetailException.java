package itineraryplanner.exceptions;

public class PlaceDetailException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlaceDetailException(String message) {
		super("Exeception in Place Details API call. Status: " + message);
	}
}
