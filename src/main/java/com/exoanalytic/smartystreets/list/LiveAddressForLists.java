package com.exoanalytic.smartystreets.list;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

/**
 * An interface to the automated Live Address For Lists API at SmartyStreets.
 * @see http://smartystreets.com/kb/liveaddress-lists/list-upload-api
 * @author jmink
 *
 */
public class LiveAddressForLists {
	private ListEndpoint endpoint;

	public LiveAddressForLists(ListEndpoint endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * Submits a list of addresses to be cleaned
	 * @param file contains the addresses to be cleaned
	 * @return a response object used subsequently to check status and retrieve results
	 * @throws LiveAddressForListsException 
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public SubmissionResponse submitList(File file) throws LiveAddressForListsException {
		HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost httppost = new HttpPost(endpoint.constructListSubmissionUrl(file.getName()));

            // Referenced HttpPost example here:
            // http://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientChunkEncodedPost.java
            InputStreamEntity reqEntity = new InputStreamEntity(
                    new FileInputStream(file), -1);
            reqEntity.setContentType("binary/octet-stream");
            reqEntity.setChunked(true);

            httppost.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            String jsonResponse = convertStreamToString(resEntity.getContent());
            SubmissionResponse listResponse = interpretSubmissionResponse(jsonResponse);
            
            EntityUtils.consume(resEntity);
            return listResponse;
        } catch (FileNotFoundException e) {
			throw new LiveAddressForListsException(e);
		} catch (ClientProtocolException e) {
			throw new LiveAddressForListsException(e);
		} catch (IOException e) {
			throw new LiveAddressForListsException(e);
		} catch (LiveAddressForListsException e) {
			throw new LiveAddressForListsException(e);
		} catch (URISyntaxException e) {
			throw new LiveAddressForListsException(e);
		} finally {
            httpclient.getConnectionManager().shutdown();
        }
	}
	
	/**
	 * Checks the status of a list already submitted
	 * @param listResponse the response from the original list submission
	 * @return the status response
	 * @throws LiveAddressForListsException 
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public StatusResponse checkStatus(SubmissionResponse listResponse) throws LiveAddressForListsException {
		HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpget = new HttpGet(endpoint.constructListStatusUrl(listResponse.list_id));

            HttpResponse response = httpclient.execute(httpget);
            HttpEntity resEntity = response.getEntity();
            String jsonResponse = convertStreamToString(resEntity.getContent());
            StatusResponse statusResponse = interpretStatusResponse(jsonResponse);
            
            EntityUtils.consume(resEntity);
            return statusResponse;
        } catch (URISyntaxException e) {
        	throw new LiveAddressForListsException(e);
		} catch (IOException e) {
			throw new LiveAddressForListsException(e);
		} finally {
            httpclient.getConnectionManager().shutdown();
        }
	}
	
	public void downloadResult(SubmissionResponse listResponse, File targetFile) throws ClientProtocolException, IOException, URISyntaxException {
		HttpClient httpclient = new DefaultHttpClient();
		InputStream contentStream = null;
        try {
            HttpGet httpget = new HttpGet(endpoint.constructDownloadUrl(listResponse.list_id));

            HttpResponse response = httpclient.execute(httpget);
            HttpEntity resEntity = response.getEntity();
            contentStream = resEntity.getContent();
            dumpToFile(contentStream, targetFile);
            
            EntityUtils.consume(resEntity);
        } finally {
        	if (contentStream != null)
        		contentStream.close();
            httpclient.getConnectionManager().shutdown();
        }
	}
	
	SubmissionResponse interpretSubmissionResponse(String jsonResponse) throws LiveAddressForListsException {
		SubmissionResponse sr = new Gson().fromJson(jsonResponse, SubmissionResponse.class);
		if (sr != null)
			return sr;
		else throw new LiveAddressForListsException("Could not interpret submission response: " + jsonResponse);
	}
	
	StatusResponse interpretStatusResponse(String jsonResponse) throws LiveAddressForListsException {
		StatusResponse sr = new Gson().fromJson(jsonResponse, StatusResponse.class); 
		if (sr != null)
			return sr;
		else throw new LiveAddressForListsException("Could not interpret status response: " + jsonResponse);
	}
	
	/**
	 * Thanks to Pavel Repin (http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string).
	 */
	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public static void dumpToFile(java.io.InputStream is, File file) throws IOException {
		byte[] buffer = new byte[2048];
		FileOutputStream fos = new FileOutputStream(file);
		try {
			for (int length; (length = is.read(buffer)) > 0;) {
                fos.write(buffer, 0, length);
            }
		} finally {
			fos.close();
		}
	}
}
