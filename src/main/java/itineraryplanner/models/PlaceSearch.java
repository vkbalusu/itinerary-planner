package itineraryplanner.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class PlaceSearch {
	
	private String nextPageToken;
	
	private List<Place> results;
	
	private String status;
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

	public PlaceSearch() {
	}
	
	public PlaceSearch(List<Place> results) {
		this.results = results;
	}

	public List<Place> getResults() {
		return results;
	}

	public void setResults(List<Place> results) {
		this.results = results;
	}
	
	
	public String getNextPageToken() {
		return nextPageToken;
	}

	public void setNext_page_token(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}
}
