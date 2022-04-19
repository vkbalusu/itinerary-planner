package itineraryplanner.exceptions;

public class ItineraryPlannerException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ItineraryPlannerException(String messgae) {
		super("Exeception in preparing itinerary. Status: " + messgae);
	}
}
