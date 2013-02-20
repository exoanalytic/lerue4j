package com.exoanalytic.smartystreets.list;

/**
 * Represents a status response from SmartStreets
 * @author jmink
 *
 */
public class StatusResponse {
	public String current_step;
	public Double step_progress;
	
	public boolean isComplete() {
		return current_step.equals("Succeeded");
	}
	
	public String toString() {
		return "current_step: " + current_step + ", step_progress: " + step_progress;
	}
}
