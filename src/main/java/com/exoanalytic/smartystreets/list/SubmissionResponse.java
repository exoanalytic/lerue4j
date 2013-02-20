package com.exoanalytic.smartystreets.list;

/**
 * Represents a response from SmartStreets for the submission of a list
 * @author jmink
 *
 */
public class SubmissionResponse {
	public String list_id;
	public Integer polling_frequency_in_seconds;
	
	public String toString() {
		return "list_id: " + list_id + ", polling_frequency_in_seconds: " + polling_frequency_in_seconds;
	}
}
