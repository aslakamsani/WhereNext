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

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoFragment extends Fragment {
	private Bundle args;
	private TextView tv;
	private Place place;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		args = getArguments();
	}

	public View onCreateView(LayoutInflater inf, ViewGroup vg, Bundle savedInstanceState){
		View v = inf.inflate(R.layout.info_fragment_layout, vg, false);
		tv = (TextView)(v.findViewById(R.id.text));

		new HTTPPostTask().execute();
		return tv;
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
			data.add(new BasicNameValuePair("inforeq", "true"));
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
				    try {
				    	// parse JSON
						json = new JSONObject(result);
						
			            JSONArray venues = json.getJSONObject("response").getJSONArray("venues");
						JSONObject area = null, location = null, cat = null;
						String address = "";
						for(int i = 0; i < venues.length(); i++){
							area = venues.getJSONObject(i);
							location = area.getJSONObject("location");
							
							// add info to Place structure
							place = new Place();
							if(area.has("name"))
								place.setName(area.getString("name"));
							
							address = "";
							if(location.has("address"))
								address += location.getString("address");
							if(location.has("city"))
								address += ", " + location.getString("city");
							if(location.has("state"))
								address += ", " + location.getString("state");
							place.setAddress(address);
							
							if(area.has("contact") && area.getJSONObject("contact").has("formattedPhone"))
								place.setPhone(area.getJSONObject("contact").getString("formattedPhone"));
							if(area.has("url"))
								place.setUrl(area.getString("url"));
							if(area.has("rating"))
								place.setRating(area.getDouble("rating"));
						}
					} catch (JSONException e) {
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
		
		protected View onPostExecute(){
			// add all the info found
			tv.append(place.getName()+"\n\n");
			tv.append(place.getAddress()+"\n\n");
			tv.append(place.getPhone()+"\n");
			tv.append("Rating: " + place.getRating() + "\5\n\n");
			tv.append(place.getUrl());
			
			//more info to add later
			return tv;
		}
		
	}
	
}
