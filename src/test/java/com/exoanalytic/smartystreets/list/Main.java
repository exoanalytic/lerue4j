package com.exoanalytic.smartystreets.list;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;


/**
 * A simple test driver.
 * @author walk_n_wind
 *
 */
public class Main {
	public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException, LiveAddressForListsException {
		String authId = promptForInfo("Enter your SmartyStreets auth id");
		String authToken = promptForInfo("Enter your SmartyStreets auth token");
		
		ListEndpoint endpoint = new ListEndpoint(authId, authToken);
		LiveAddressForLists submitter = new LiveAddressForLists(endpoint);
		System.out.println("Submitting list_sample.csv...");
		SubmissionResponse listResponse = submitter.submitList(new File(Main.class.getResource("list_sample.csv").toURI()));
		System.out.println("Submitted: " + listResponse);
		
		StatusResponse statusResponse = submitter.checkStatus(listResponse);
		while (!statusResponse.isComplete()) {
			System.out.println("Status: " + statusResponse);
			Thread.sleep(listResponse.polling_frequency_in_seconds * 1000);
			statusResponse = submitter.checkStatus(listResponse);
		}
		System.out.println("SmartyStreets has finished processing our list!");
		
		String yesOrNo = promptForInfo("Shall we download the result? (y/n)");
		if (yesOrNo.equals("n"))
			return;
		
		System.out.println("Downloading result to ./list_sample_result.zip...");
		submitter.downloadResult(listResponse, new File("./list_sample_result.zip"));
		System.out.println("Download complete!");
	}
	
	private static String promptForInfo(String prompt) throws IOException {
		System.out.print(prompt + ": ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		return br.readLine();
	}
}
