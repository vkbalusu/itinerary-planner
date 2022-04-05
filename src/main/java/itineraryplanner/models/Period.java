package itineraryplanner.models;


public class Period {
	private DayTime close;
	private DayTime open;
	
	public DayTime getClose() {
		return close;
	}
	public void setClose(DayTime close) {
		this.close = close;
	}
	public DayTime getOpen() {
		return open;
	}
	public void setOpen(DayTime open) {
		this.open = open;
	}
	
}
