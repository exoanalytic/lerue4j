package com.exoanalytic.smartystreets.list;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

/**
 * Represents a URL for the SmartyStreets Lists service
 * @author jmink
 *
 */
public class ListEndpoint {
	private String authToken;
	private String authId;
	
	/**
	 * @param authId your API Auth ID from Smarty Streets
	 * @param authToken your Auth Token from Smarty Streets
	 * @throws URISyntaxException
	 */
	public ListEndpoint(String authId, String authToken) throws URISyntaxException {
		this.authId = authId;
		this.authToken = authToken;
	}
	
	private URIBuilder constructUrl(URI uri) {
		URIBuilder builder = new URIBuilder(uri);

		builder.addParameter("auth-id", authId);
		builder.addParameter("auth-token", authToken);
		
		return builder;
	}
	
	public URI constructListSubmissionUrl(String resultFilename) throws URISyntaxException {
		return constructUrl(new URI("https://api.smartystreets.com/lists/"))
				.addParameter("filename", resultFilename)
				.build();
	}
	
	public URI constructListStatusUrl(String listId) throws URISyntaxException {
		
		return constructUrl(new URI("https://api.smartystreets.com/lists/" + listId)).build();
	}
	
	public URI constructDownloadUrl(String listId) throws URISyntaxException {		
		return constructUrl(new URI("https://api.smartystreets.com/lists/" + listId + "/download")).build();
	}
}
