package com.hacktech.wherenext;

public class PlaceEvent {
	private String summary;
	private String url;
	private long time;
	
	public PlaceEvent(){}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
}
