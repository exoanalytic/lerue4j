package com.exoanalytic.smartystreets.list;

import junit.framework.Assert;

import org.junit.Test;

public class ListSubmitterTest {
	@Test
	public void testIntepretResponse() throws LiveAddressForListsException {
		final String sampleResponse =	// Taken from http://smartystreets.com/kb/liveaddress-lists/list-upload-api
		"{\n"
		+ "	\"list_id\": \"4ba8807e-139b-45e1-a5c7-1e30b5dd5c9a\",\n"
		+ "	\"polling_frequency_in_seconds\": 30\n"
		+ "}";
		
		LiveAddressForLists submitter = new LiveAddressForLists(null);
		SubmissionResponse response = submitter.interpretSubmissionResponse(sampleResponse);
		Assert.assertEquals("4ba8807e-139b-45e1-a5c7-1e30b5dd5c9a", response.list_id);
		Assert.assertEquals(30, response.polling_frequency_in_seconds.intValue());
	}
	
	@Test
	public void testInterpretStatusResponse() throws LiveAddressForListsException {
		final String sampleResponse =	// Taken from http://smartystreets.com/kb/liveaddress-lists/list-upload-api
		"{\n"
		+ "	\"current_step\": \"Processing\",\n"
		+ "	\"step_progress\": .54\n"
		+ "}";
		
		LiveAddressForLists submitter = new LiveAddressForLists(null);
		StatusResponse response = submitter.interpretStatusResponse(sampleResponse);
		Assert.assertEquals("Processing", response.current_step);
		Assert.assertEquals(0.54, response.step_progress.doubleValue());
	}
}
