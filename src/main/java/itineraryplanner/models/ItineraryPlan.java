package itineraryplanner.models;


public class ItineraryPlan {
	
	int numberofDays;
	Itinerary itinerary;
	String status;
	String errorMessage;
	
	public int getNumberofDays() {
		return numberofDays;
	}
	public void setNumberofDays(int numberofDays) {
		this.numberofDays = numberofDays;
	}
	public Itinerary getItinerary() {
		return itinerary;
	}
	public void setItinerary(Itinerary itinerary) {
		this.itinerary = itinerary;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
