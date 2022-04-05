package itineraryplanner.models;

public class Element {
	
	private TextValuePair distance;
	private TextValuePair duration;
	
	public TextValuePair getDistance() {
		return distance;
	}
	public void setDistance(TextValuePair distance) {
		this.distance = distance;
	}
	public TextValuePair getDuration() {
		return duration;
	}
	public void setDuration(TextValuePair duration) {
		this.duration = duration;
	}

}