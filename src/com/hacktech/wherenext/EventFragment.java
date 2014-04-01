package com.hacktech.wherenext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;

public class EventFragment extends ListFragment {
private Bundle args;
private ArrayList<PlaceEvent> events;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		args = getArguments();
		
		new HTTPPostTask().execute();
		
		// list all events
		setListAdapter(new EventsListAdapter(this.getActivity().getBaseContext(), events));
	}

	private class HTTPPostTask extends AsyncTask<String, Void, Void>{
		private static final String URL = "http://untravel.azurewebsites.net/untravel.php";
		
		@Override
		protected Void doInBackground(String... arg0) {
			HttpClient httpClient = new DefaultHttpClient();
		    HttpPost httpPost = new HttpPost(URL);
		    HttpResponse response;
		    
			ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
			// allow server to know what we're asking for
			data.add(new BasicNameValuePair("eventreq", "true"));
	        data.add(new BasicNameValuePair("location", args.getString("location")));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(data));
				response = httpClient.execute(httpPost);
				if(response != null){
		        	HttpEntity entity = response.getEntity();
		        	InputStream input = null;
		        	String result = null;
		        	JSONObject json = null;
		        	
		        	input = entity.getContent();
		            // json is UTF-8 by default
		            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 8);
		            StringBuilder sb = new StringBuilder();

		            String line = null;
		            while ((line = reader.readLine()) != null)
		                sb.append(line + "\n");
		            
		            result = sb.toString();
		            try{
		            	// parse JSON
		            	json = new JSONObject(result);
		            	JSONObject resultEvents = json.getJSONObject("result");
		            	if(resultEvents.has("events")){
		            		JSONArray allEvents = resultEvents.getJSONArray("events");
		            		PlaceEvent pe = new PlaceEvent();
		            		JSONObject oneEvent  = null;
		            		for(int i = 0; i < allEvents.length(); i++){
		            			oneEvent = allEvents.getJSONObject(i);
		            			if(oneEvent.has("start_time"))
		            				pe.setTime(oneEvent.getLong("start_time"));
		            			if(oneEvent.has("summary"))
		            				pe.setSummary(oneEvent.getString("summary"));
		            			if(oneEvent.has("url"))
		            				pe.setUrl(oneEvent.getString("url"));
		            		}
		            		// add to list
		            		events.add(pe);
		            	}
		            }catch(JSONException e){
		            	e.printStackTrace();
		            }
		        }
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;	        
		}
		
	}
	
}
