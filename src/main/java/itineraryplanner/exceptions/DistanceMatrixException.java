package itineraryplanner.exceptions;

public class DistanceMatrixException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DistanceMatrixException(String messgae) {
		super("Exeception in Distance Matrix API call. Status: " + messgae);
	}
}
