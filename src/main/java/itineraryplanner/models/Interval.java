package itineraryplanner.models;


public class Interval {
	
	private int day;
	private int openingTime;
	private int closingTime;
	
	public Interval(int day, int open, int close) {
		super();
		this.day = day;
		this.openingTime = open;
		this.closingTime = close;
	}
	public int getOpeningTime() {
		return openingTime;
	}
	public void setOpeningTime(int open) {
		this.openingTime = open;
	}
	public int getClosingTime() {
		return closingTime;
	}
	public void setClosingTime(int close) {
		this.closingTime = close;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	
}

